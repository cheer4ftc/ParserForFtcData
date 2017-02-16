public class MatchResult {
    int division;
    int type;
    int number;
    int played;
    AllianceResult alliance[];

    MatchResult() {
        alliance = new AllianceResult[2];

        alliance[0] = new AllianceResult();
        alliance[1] = new AllianceResult();
    }

    public void copy(MatchResult mri) {
        division = mri.division;
        type=mri.type;
        number=mri.number;
        played=mri.played;
        alliance[0].copy(mri.alliance[0]);
        alliance[1].copy(mri.alliance[1]);
    }

    public String title() {
        String title;
        if (type == 1) {
            title = "Q-" + number;
        } else if (type == 3) {
            title = "SF-" + number / 10 + "-" + (number - 10 * ((int) (number / 10)));
        } else if (type == 4) {
            title = "F-" + number;
        } else if (type == 0) {
            title = "P-" + number;
        } else {
            title = "ERROR";
        }
        return title;
    }

    public String resultString() {
        String result;
        result = alliance[0].score() + "-" + alliance[1].score() + " ";
        if (alliance[0].score() > alliance[1].score()) {
            result += "R";
        } else if (alliance[0].score() < alliance[1].score()) {
            result += "B";
        } else {
            result += "T";
        }
        return result;
    }

    public static String header(Format.File outputFileFormat, String tournamentName) {
        String out = "";
        if (outputFileFormat == Format.File.HTML) {
            out = "<HTML><head><style>@media print{  table { page-break-after:auto }  tr    { page-break-inside:avoid; page-break-after:auto }  "
                    + "td    { page-break-inside:avoid; page-break-after:auto }  thead { display:table-header-group }"
                    + "  tfoot { display:table-footer-group }}</style>"
                    + "<TITLE>Match Results</TITLE></head><CENTER><H2>"
                    + tournamentName
                    + "<BR>Match Results</H2></CENTER><HR>"
                    + "<DIV ALIGN=CENTER><TABLE BORDER=2 CELLPADDING=5 CELLSPACING=1 WIDTH=75%>\n" +
                    "<TR ALIGN=CENTER><TH>Match</TH><TH>Result</TH><TH>Red</TH><TH>Blue</TH></TR>\n";
        }
        if (outputFileFormat == Format.File.CSV) {
            out = "TournamentMatchCode,Match,Result,Red1,Red2,Red3,Blue1,Blue2,Blue3,RTot,BTot,R1Sur,R2Sur,B1Sur,B2Sur,";
        }
        return out;
    }

    public static String bodyLine(MatchResult match, Format.File outputFileFormat, String code) {
        String out = "";
        if (outputFileFormat == Format.File.HTML) {
            out = "";
            int teamsPerAlliance = 2;
            if (match.alliance[0].team[2].number > 0) {
                teamsPerAlliance = 3;
            }
            out += "<TR ALIGN=CENTER><TD ROWSPAN=" + teamsPerAlliance + ">" + match.title() + "</TD>\n";
            out += "<TD ROWSPAN=" + teamsPerAlliance + " BGCOLOR=\"";
            if (match.alliance[0].score > match.alliance[1].score) {
                out += "#FF4444";
            } else if (match.alliance[0].score < match.alliance[1].score) {
                out += "#44AAFF";
            } else {
                out += "#CCCCCC";
            }
            out += "\">" + match.resultString() + "</TD>\n";
            out += "  <TD>" + match.alliance[0].team[0].number + "</TD>\n";
            out += "  <TD>" + match.alliance[1].team[0].number + "</TD></TR>\n";

            for (int i = 1; i < 3; i++) {
                if (match.alliance[0].team[i].number > 0) {
                    out += "<TR ALIGN=CENTER><TD>" + match.alliance[0].team[i].number + "</TD>\n";
                    out += "<TD>" + match.alliance[1].team[i].number + "</TD></TR>\n";
                }
            }
        }
        if (outputFileFormat == Format.File.CSV) {
            out = code;
            out += "-" + match.title() + ",";
            out += match.title() + "," + match.resultString() + ",";
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 3; j++) {
                    out += match.alliance[i].team[j].number + ",";
                }
            }
            for (int i = 0; i < 2; i++) {
                out += match.alliance[i].score() + ",";
            }
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    out += match.alliance[i].team[j].surrogate + ",";
                }
            }
            out += "\n";
        }

        return out;
    }


}
