/**
 * Created by Bill on 12/20/2016.
 */
public class TeamTournamentStatsResults extends TeamTournamentRanking {

    public enum StatType {OPR, CPR, DIF, OPRM, CPRM, DIFM}

    public static String TypeDisplayString[] = {"OPR", "OPRm", "CPRm", "Difm"};

    double winPercent=0;
    double stat[][]; //2d for expansion with details later... bad code form. :(

    public TeamTournamentStatsResults() {
        super();
        stat=new double[StatType.values().length][1];
    }
    public TeamTournamentStatsResults(TeamTournamentRanking tr) {
        super.copy(tr);
    }

    void copy(TeamTournamentStatsResults in) {
        super.copy(in);
        winPercent = in.winPercent;
        for (StatType s : StatType.values()) {
            stat[s.ordinal()][0] = in.stat[s.ordinal()][0];
        }
    }


    public static String header(Format.File f, String tournamentName) {
        return TeamTournamentRanking.header(Format.File.CSV,tournamentName)+"WP,OPR,OPRm,";
    }

    public String bodyLineStatResult(Format.File outputFileFormat, String tournamentCode) {
        String outS="";
        outS+=super.bodyLine(outputFileFormat,tournamentCode);

        if (outputFileFormat == Format.File.CSV) {
            outS += String.format("%.2f",winPercent/100) + ",";
            outS += String.format("%.1f",stat[StatType.OPR.ordinal()][0]) + ",";
            outS += String.format("%.1f",stat[StatType.OPRM.ordinal()][0]) + ",";
        }
        return outS;
    }

}
