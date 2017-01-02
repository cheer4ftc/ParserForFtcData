/**
 * Created by Bill on 12/20/2016.
 */
public class TeamTournamentStatsDetails extends TeamTournamentStatsResults {

    public TeamTournamentStatsDetails() {
        super();

        stat = new double[StatType.values().length][AllianceResultDetails.ScoreType.values().length];

    }

    public TeamTournamentStatsResults teamTouranamentStatsResults() {
        TeamTournamentStatsResults tr = new TeamTournamentStatsResults();

        tr.copy(this);

        return tr;
    }

    public TeamTournamentStatsDetails(TeamTournamentRanking tr) {
        super();

        super.copy(tr);

        stat = new double[StatType.values().length][AllianceResultDetails.ScoreType.values().length];
    }

    public TeamTournamentStatsDetails(TeamTournamentStatsResults tr) {
        super();

        super.copy(tr);
        stat = new double[StatType.values().length][AllianceResultDetails.ScoreType.values().length];

        for (StatType s : StatType.values()) {
            stat[s.ordinal()][AllianceResultDetails.ScoreType.TOTAL.ordinal()] = tr.stat[s.ordinal()][0];
            stat[s.ordinal()][AllianceResultDetails.ScoreType.TOTAL_WITH_NEGATIVE_PEN.ordinal()] = tr.stat[s.ordinal()][0];
            stat[s.ordinal()][AllianceResultDetails.ScoreType.TELE.ordinal()] = tr.stat[s.ordinal()][0];
        }
    }

    public TeamTournamentStatsResults teamTournamentStatsResults() {
        TeamTournamentStatsResults tr = new TeamTournamentStatsResults();
        tr.copy(this);
        return tr;
    }

    public static String header(Format.File f, String tournamentName) {
        return TeamTournamentStatsResults.header(f, tournamentName) + "OPR-,OPRm-,OPRA,OPRmA,OPRB,OPRmB,OPRT,OPRmT,OPRE,OPRmE,OPRP,OPRmP,OPRp,OPRmp,";
    }

    public String bodyLine(Format.File outputFileFormat, String tournamentCode) {
        String outS = "";
        outS += super.bodyLineStatResult(outputFileFormat, tournamentCode);

        if (outputFileFormat == Format.File.CSV) {
            for (int i = 1; i < AllianceResultDetails.ScoreType.values().length; i++) {
                outS += String.format("%.1f", stat[StatType.OPR.ordinal()][i]) + ",";
                outS += String.format("%.1f", stat[StatType.OPRM.ordinal()][i]) + ",";
            }
        }
        return outS;
    }

}
