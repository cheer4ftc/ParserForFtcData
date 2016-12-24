import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

/**
 * Created by Bill on 12/20/2016.
 */
public class TournamentRankings {

    public static void addTeamNames(List<TeamTournamentRanking> teamRanking, String filenameOfTeams) throws IOException {
        // get team names
        FileInputStream ftn = null;
        ftn = new FileInputStream(filenameOfTeams);
        BufferedReader brt = new BufferedReader(new InputStreamReader(ftn));
        String inTeamStr;
        brt.readLine(); // read and ignore header row
        while ((inTeamStr = brt.readLine()) != null) {
            String[] teamInCol = inTeamStr.split("[,]");
            int num = Integer.valueOf(teamInCol[2]);
            String name = teamInCol[1];
            for (TeamTournamentRanking aTeamT : teamRanking) {
                if (aTeamT.number == num) {
                    aTeamT.name = name;
                }
            }
        }
        brt.close();
    }

    public static void computeRankings(List<MatchResult> matchList, List<TeamTournamentRanking> teamRanking) {
        //       public static void computeRankings(List<MatchResultDetails> matchList, List<TeamTournamentRanking> teamRanking) {
        for (MatchResult currMatch : matchList) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 3; k++) {
                    if (currMatch.alliance[j].team[k].number>0) {
                        int l = 0;
                        while ((l < teamRanking.size())
                                && ((teamRanking.get(l).number != currMatch.alliance[j].team[k].number))) {
                            l++;
                        }
                        if (l == teamRanking.size()) {
                            teamRanking.add(new TeamTournamentRanking());
                            teamRanking.get(l).number = currMatch.alliance[j].team[k].number;
                        }
                        if (!currMatch.alliance[j].team[k].surrogate)
                            teamRanking.get(l).updateRanking(currMatch, j);
                    }
                }
            }
        }
        Collections.sort(teamRanking);
        for (int t = 0; t < teamRanking.size(); t++) {
            teamRanking.get(t).rank[RankType.RANK.ordinal()] = t + 1;
        }
    }


}
