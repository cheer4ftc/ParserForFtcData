public class TeamTournamentRanking extends TeamInfo implements Comparable<TeamTournamentRanking> {

    int[] rank;
    Elim elim;

    TeamTournamentRanking() {
        rank = new int[RankType.values().length];
        elim = new Elim();
    }

    void copy(TeamTournamentRanking in) {
        super.copy(in);
        for (RankType r : RankType.values()) {
            rank[r.ordinal()] = in.rank[r.ordinal()];
        }
        elim = in.elim;
    }

    void updateRanking(MatchResult mr, int all) {

        if (mr.type == 1) { // qual matches
            int winner = 0;
            boolean tie = false;

//            System.err.println("Updating Q match ranking");

            // update Q matches played
            rank[RankType.MATCHES_PLAYED.ordinal()]++;

            // update QP
            if (mr.alliance[0].score() < mr.alliance[1].score()) {
                winner = 1;
            } else if (mr.alliance[0].score() > mr.alliance[1].score()) {
                winner = 0;
            } else {
                tie = true;
            }
            if (tie) {
                rank[RankType.QP.ordinal()]++;
            } else if (all == winner) {
                rank[RankType.QP.ordinal()] += 2;
            }

            // update RP (may need to guess if don't have penalty points??)
            if (mr instanceof MatchResultDetails) {
                MatchResultDetails mrd = (MatchResultDetails) mr;
                if (mrd.alliance[0].score() < mrd.alliance[1].score()) {
                    rank[RankType.RP.ordinal()] += mrd.alliance[0].score() - mrd.allianceDetails[0].score(AllianceResultDetails.ScoreType.PEN);
                } else if (mrd.alliance[0].score() > mrd.alliance[1].score()) {
                    rank[RankType.RP.ordinal()] += mrd.alliance[1].score() - mrd.allianceDetails[1].score(AllianceResultDetails.ScoreType.PEN);
                } else {
                    rank[RankType.RP.ordinal()] += min(mrd.alliance[0].score() - mrd.allianceDetails[0].score(AllianceResultDetails.ScoreType.PEN),
                            mrd.alliance[1].score() - mrd.allianceDetails[1].score(AllianceResultDetails.ScoreType.PEN));
                }
            } else { // don't know penalties, so guess 0 penalties
                rank[RankType.RP.ordinal()] += min(mr.alliance[0].score(), mr.alliance[1].score);
            }

            // update highest
            if (mr.alliance[all].score() > rank[RankType.HIGHEST.ordinal()]) {
                rank[RankType.HIGHEST.ordinal()] = mr.alliance[all].score();
            }
        } else if ((mr.type == 3) || (mr.type == 4)) { // elim

            // determine position in alliance
            for (int i = 0; i < 3; i++)
                if (mr.alliance[all].team[i].number == number) {
                    elim.setPosition(i);
                }

            // determine alliance result. keep overwriting. final match played gives final status!
            if (mr.type == 3) {
                elim.allianceType = Elim.Result.SEMIFINALIST;
            } else {
                int winner = 0;
                boolean tie = false;

                if (mr.alliance[0].score() < mr.alliance[1].score()) {
                    winner = 1;
                } else if (mr.alliance[0].score() > mr.alliance[1].score()) {
                    winner = 0;
                } else {
                    tie = true;
                }

                if (!tie) { // ignore ties
                    if (all == winner) {
                        elim.allianceType = Elim.Result.WINNING;
                    } else {
                        elim.allianceType = Elim.Result.FINALIST;
                    }
                }
            }
        }

    }

    @Override
    public int compareTo(TeamTournamentRanking tts) {
        int cval;

        cval = rank[RankType.QP.ordinal()] - tts.rank[RankType.QP.ordinal()];
        if (cval != 0) {
            return -cval;
        }

        cval = rank[RankType.RP.ordinal()] - tts.rank[RankType.RP.ordinal()];
        if (cval != 0) {
            return -cval;
        }

        cval = rank[RankType.HIGHEST.ordinal()] - tts.rank[RankType.HIGHEST.ordinal()];
        if (cval != 0) {
            return -cval;
        }

        cval = number - tts.number;
        if (cval != 0) {
            return cval;
        }

        return 0;
    }

    private int min(int a, int b) {
        if (a > b) return b;
        return a;
    }

    public static String header(Format.File outputFileFormat, String tournamentName) {
        String out = "";
        if (outputFileFormat == Format.File.HTML) {
            out = "<HTML><head><style>@media print{  table { page-break-after:auto }  tr    { page-break-inside:avoid; page-break-after:auto }  "
                    + "td    { page-break-inside:avoid; page-break-after:auto }  thead { display:table-header-group }"
                    + "  tfoot { display:table-footer-group }}</style>"
                    + "<TITLE>Team Rankings</TITLE></head><CENTER><H2>"
                    + tournamentName
                    + "<BR>Team Rankings</H2></CENTER><HR>"
                    + "<DIV ALIGN=CENTER><TABLE BORDER=2 CELLPADDING=5 CELLSPACING=1 WIDTH=75%>"
                    + "<TR ALIGN=CENTER><TH>Rank</TH><TH>Team #</TH><TH>Team Name</TH><TH>QP</TH><TH>RP</TH><TH>Highest</TH><TH>Matches</TH></TR>";
        }
        if (outputFileFormat == Format.File.CSV) {
            out = "TournamentCode,Num,TeamName,R,QP,RP,High,MP,Elim,";
        }

        return out;
    }

    public String bodyLine(Format.File outputFileFormat, String tournamentCode) {
        String outS = "";
        if (outputFileFormat == Format.File.HTML) {
            outS += "<TR  ALIGN=CENTER><TD>" + rank[RankType.RANK.ordinal()] + "</TD>";
            outS += "<TD>" + number + "</TD>";
            outS += "<TD>" + name + "</TD>";

            outS += "<TD>" + rank[RankType.QP.ordinal()] + "</TD>";
            outS += "<TD>" + rank[RankType.RP.ordinal()] + "</TD>";
            outS += "<TD>" + rank[RankType.HIGHEST.ordinal()] + "</TD>";
            outS += "<TD>" + rank[RankType.MATCHES_PLAYED.ordinal()] + "</TD>";
        }
        if (outputFileFormat == Format.File.CSV) {
            outS += tournamentCode + ",";

            outS += number + ",";
            outS += name + ",";

            outS += rank[RankType.RANK.ordinal()] + ",";
            outS += rank[RankType.QP.ordinal()] + ",";
            outS += rank[RankType.RP.ordinal()] + ",";
            outS += rank[RankType.HIGHEST.ordinal()] + ",";
            outS += rank[RankType.MATCHES_PLAYED.ordinal()] + ",";

            outS += elim.to2Char() + ",";
        }
        return outS;
    }

}
