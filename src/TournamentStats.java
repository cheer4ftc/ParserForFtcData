import Jama.Matrix;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bill on 12/20/2016.
 */
public class TournamentStats extends TournamentRankings {
    static boolean useTrueMean = true;

    public static void computeRankingsBeforeStats(List<MatchResult> matchList, List<TeamTournamentStatsDetails> teamStatList, String fileOfTeams) throws IOException {
        List<TeamTournamentRanking> teamRankingList = new ArrayList<>();
        List<MatchResult> matchResultList = new ArrayList<>();

        computeRankings(matchList, teamRankingList);
        addTeamNames(teamRankingList, fileOfTeams);

        for (TeamTournamentRanking tri : teamRankingList) {
            teamStatList.add(new TeamTournamentStatsDetails(tri));
        }
    }

    public static void computeStatsResults(List<MatchResult> matchList, List<TeamTournamentStatsResults> teamStatList, double MmseFactor) {
        List<MatchResultDetails> matchListDetails = new ArrayList<>();
        for (MatchResult m : matchList) {
            matchListDetails.add(new MatchResultDetails(m));
        }

        List<TeamTournamentStatsDetails> teamStatListDetails = new ArrayList<>();
        for (TeamTournamentStatsResults t : teamStatList) {
            teamStatListDetails.add(new TeamTournamentStatsDetails(t));
        }
        computeStatsDetails(matchListDetails, teamStatListDetails, MmseFactor);

        teamStatList = new ArrayList<>();
        for (TeamTournamentStatsDetails t : teamStatListDetails) {
            teamStatList.add(t.teamTournamentStatsResults());
        }

    }
    public static void computeStatsDetails(List<MatchResultDetails> matchList, List<TeamTournamentStatsDetails> teamStatList, double MmseFactor) {

        for (TeamTournamentStatsDetails t : teamStatList) {
            /////////////////////////////////////////
            // win percentage is normal percentage of wins of matches played.
            // if no matches played, return 50% (best guess)
            // Also, add eps for each match if win percent is >=50%
            //   and subtract eps for each match if win percent is <50%
            // This will make teams that are 5-0 have a higher win percentage than 4-0 teams,
            //   and similarly make teams that are 0-5 have a lower win percentage than 0-4 teams.
            if (t.rank[RankType.MATCHES_PLAYED.ordinal()] > 0) {
                t.winPercent = (float) t.rank[RankType.QP.ordinal()] / (float) t.rank[RankType.MATCHES_PLAYED.ordinal()]
                        * (float) 100.0 / (float) (2.0) + 0.0005;
                if (t.winPercent >= 50.0) {
                    t.winPercent += 0.00005 * t.rank[RankType.MATCHES_PLAYED.ordinal()]; // reward more wins
                } else {
                    t.winPercent -= 0.00005 * t.rank[RankType.MATCHES_PLAYED.ordinal()]; // penalize more losses
                }
            } else {
                t.winPercent = 50; // best guess if no matches played
            }
        }

        ArrayList<Integer> atn = new ArrayList<>();

        int numTeams = teamStatList.size();
        for (TeamTournamentStatsDetails aTeamStatList : teamStatList) {
            atn.add(aTeamStatList.number);
        }

        // only count SCORED QUALIFYING matches
        int numMatches = 0;
        for (MatchResultDetails aMatchList : matchList) {
            if ((aMatchList.allianceDetails[0].score() >= 0) &&
                    (aMatchList.type == 1)) { // Q match
                numMatches++;
            }
        }

        // OPRm, CPRm, and Dif

        Matrix Ar = new Matrix(numMatches, numTeams);
        Matrix Ab = new Matrix(numMatches, numTeams);
        Matrix Mr = new Matrix(numMatches, 1);
        Matrix Mb = new Matrix(numMatches, 1);

        Matrix Ao = new Matrix(2 * numMatches, numTeams);
        Matrix Aw = new Matrix(numMatches, numTeams);
        Matrix Mo = new Matrix(2 * numMatches, 1);
        Matrix Mw = new Matrix(numMatches, 1);

        Matrix Oprm = new Matrix(numTeams, 1);
        Matrix Cprm = new Matrix(numTeams, 1);

        int meanOffenseScoreTotal[] = new int[AllianceResultDetails.ScoreType.values().length];
        for (int i = 0; i < meanOffenseScoreTotal.length; i++) {
            meanOffenseScoreTotal[i] = 0;
        }

        if (numMatches > 0) {

            int matchesPerTeam[] = new int[numTeams];

            int iM = 0;
            for (MatchResultDetails m : matchList) {
                if ((m.allianceDetails[0].score(AllianceResultDetails.ScoreType.TOTAL) >= 0) &&
                        (m.type == 1)) {

                    Ar.set(iM, atn.indexOf(m.alliance[0].team[0].number), 1.0);
                    Ar.set(iM, atn.indexOf(m.alliance[0].team[1].number), 1.0);
                    Ab.set(iM, atn.indexOf(m.alliance[1].team[0].number), 1.0);
                    Ab.set(iM, atn.indexOf(m.alliance[1].team[1].number), 1.0);
                    iM++;

                    matchesPerTeam[atn.indexOf(m.alliance[0].team[0].number)]++;
                    matchesPerTeam[atn.indexOf(m.alliance[0].team[1].number)]++;
                    matchesPerTeam[atn.indexOf(m.alliance[1].team[0].number)]++;
                    matchesPerTeam[atn.indexOf(m.alliance[1].team[1].number)]++;

                }
            }

            Ao.setMatrix(0, numMatches - 1, 0, numTeams - 1, Ar);
            Ao.setMatrix(numMatches, 2 * numMatches - 1, 0, numTeams - 1, Ab);
            Aw = Ar.minus(Ab);

            // normal OPR Matrix AoTAoInv = Ao.transpose().times(Ao).inverse();
            // normal CPR Matrix AwTAwInv = Aw.transpose().times(Aw).inverse();


            // OPR MMSE
            Matrix AoTAoInv = Ao.transpose().times(Ao).plus(Matrix.identity(numTeams, numTeams).times(MmseFactor)).inverse();


            // CPR MMSE
            Matrix AwTAwInv;
            try {
                AwTAwInv= Aw.transpose().times(Aw).plus(Matrix.identity(numTeams, numTeams).times(2 * MmseFactor)).inverse();
            } catch (Exception e) {
                AwTAwInv= Aw.transpose().times(Aw).plus(Matrix.identity(numTeams, numTeams).times(2 * (MmseFactor+0.01))).inverse();
            }

            ////////////////////////
            // OPR:
            //
            // Compute A topr = B
            //   where A = rows with two 1s representing which teams were in each alliance
            //       A has 2 rows per match.
            //   and B is the offensive score minus penalties for that alliance.
            // Then, least squares topr solution is, solve A' A topr = A' b
            //   A' A is positive semidef, so use Cholesky decomposition to solve it.

            for (AllianceResultDetails.ScoreType type : AllianceResultDetails.ScoreType.values()) {

                double offensePerTeam[] = new double[numTeams];
                double marginPerTeam[] = new double[numTeams];

                iM = 0;
                for (MatchResultDetails m : matchList) {
                    if ((m.allianceDetails[0].score(AllianceResultDetails.ScoreType.TOTAL) >= 0) &&
                            (m.type == 1)) {

                        Mr.set(iM, 0, m.allianceDetails[0].score(type));
                        Mb.set(iM, 0, m.allianceDetails[1].score(type));

                        offensePerTeam[atn.indexOf(m.alliance[0].team[0].number)] += Mr.get(iM, 0);
                        offensePerTeam[atn.indexOf(m.alliance[0].team[1].number)] += Mr.get(iM, 0);

                        offensePerTeam[atn.indexOf(m.alliance[1].team[0].number)] += Mb.get(iM, 0);
                        offensePerTeam[atn.indexOf(m.alliance[1].team[1].number)] += Mb.get(iM, 0);

                        marginPerTeam[atn.indexOf(m.alliance[0].team[0].number)] += Mr.get(iM, 0) - Mb.get(iM, 0);
                        marginPerTeam[atn.indexOf(m.alliance[0].team[1].number)] += Mr.get(iM, 0) - Mb.get(iM, 0);

                        marginPerTeam[atn.indexOf(m.alliance[1].team[0].number)] -= Mr.get(iM, 0) - Mb.get(iM, 0);
                        marginPerTeam[atn.indexOf(m.alliance[1].team[1].number)] -= Mr.get(iM, 0) - Mb.get(iM, 0);

                        meanOffenseScoreTotal[type.ordinal()] += Mr.get(iM, 0);
                        meanOffenseScoreTotal[type.ordinal()] += Mb.get(iM, 0);

                        iM++;

                    }
                }

                meanOffenseScoreTotal[type.ordinal()] /= 2 * numMatches * 2; // per team, 2 for red/blue, 2 for 2 teams per alliance
                for (int i = 0; i < numMatches; i++) {
                    Mr.set(i, 0, Mr.get(i, 0) - 2 * meanOffenseScoreTotal[type.ordinal()]);
                    Mb.set(i, 0, Mb.get(i, 0) - 2 * meanOffenseScoreTotal[type.ordinal()]);
                }

                for (int i = 0; i < numTeams; i++) {
                    marginPerTeam[i] /= 2;
                }

                Mo.setMatrix(0, numMatches - 1, 0, 0, Mr);
                Mo.setMatrix(numMatches, 2 * numMatches - 1, 0, 0, Mb);
                Mw = Mr.minus(Mb);

                ////////////////////////
                // OPR:
                //
                // Compute A topr = B
                //   where A = rows with two 1s representing which teams were in each alliance
                //       A has 2 rows per match.
                //   and B is the offensive score minus penalties for that alliance.
                // Then, least squares topr solution is, solve A' A topr = A' b

                Oprm = AoTAoInv.times(Ao.transpose().times(Mo));
                Cprm = AwTAwInv.times(Aw.transpose().times(Mw));
                // put in to normalize like traditional OPR
                if (useTrueMean) {
                    for (int i = 0; i < numTeams; i++) {
                        //                   Log.i("O "+String.valueOf(AtAinvA1.times(A1.transpose()).get(0,i)),String.valueOf(BoprA.get(i,0)));
                        Oprm.set(i, 0, Oprm.get(i, 0) + meanOffenseScoreTotal[type.ordinal()]);
                        Cprm.set(i, 0, Cprm.get(i, 0) + meanOffenseScoreTotal[type.ordinal()]);
                    }
                }


                for (int i = 0; i < numTeams; i++) {
                    if (MmseFactor == 0) {
                        teamStatList.get(i).stat[TeamTournamentStatsResults.StatType.OPR.ordinal()][type.ordinal()] = Oprm.get(i, 0);
                        teamStatList.get(i).stat[TeamTournamentStatsResults.StatType.CPR.ordinal()][type.ordinal()] = Cprm.get(i, 0);
                        teamStatList.get(i).stat[TeamTournamentStatsResults.StatType.DIF.ordinal()][type.ordinal()] = Cprm.get(i, 0) - Oprm.get(i, 0);
                    } else {
                        teamStatList.get(i).stat[TeamTournamentStatsResults.StatType.OPRM.ordinal()][type.ordinal()] = Oprm.get(i, 0);
                        teamStatList.get(i).stat[TeamTournamentStatsResults.StatType.CPRM.ordinal()][type.ordinal()] = Cprm.get(i, 0);
                        teamStatList.get(i).stat[TeamTournamentStatsResults.StatType.DIFM.ordinal()][type.ordinal()] = Cprm.get(i, 0) - Oprm.get(i, 0);
                    }
                }

                if (type == AllianceResultDetails.ScoreType.TOTAL) {
                    iM = 0;
                    double Ex2 = 0;

                    for (MatchResultDetails m : matchList) {
                        if ((m.allianceDetails[0].score(AllianceResultDetails.ScoreType.TOTAL) >= 0) &&
                                (m.type == 1)) {

                            Ex2 += m.allianceDetails[0].score[type.ordinal()] * m.allianceDetails[0].score[type.ordinal()];
                            Ex2 += m.allianceDetails[1].score[type.ordinal()] * m.allianceDetails[1].score[type.ordinal()];

                            iM++;

                        }
                    }

                    double offenseVariance = Ex2 / (double) (2 * iM - 1)
                            - meanOffenseScoreTotal[type.ordinal()] * meanOffenseScoreTotal[type.ordinal()] + 1;
                }

            }

        }

    }


}
