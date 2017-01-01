public class MatchResult {
	int division;
	int type;
	int number;
	int played;
	AllianceResult alliance[];
	
	MatchResult() {
		alliance = new AllianceResult[2];

		alliance[0]=new AllianceResult();
		alliance[1]=new AllianceResult();
	}
	
	public String title() {
		String title;
		if (type==1) {
			title="Q-"+number;
		} else if (type==3) {
			title="SF-"+number/10+"-"+(number - 10*((int)(number/10)));
		} else if (type==4) {
			title="F-"+number;
		} else if (type==0) {
			title="P-"+number;
		} else {
			title="ERROR";
		}
		return title;
	}

	public String resultString() {
		String result;
		result = alliance[0].score()+"-"+alliance[1].score()+" ";
		if (alliance[0].score()>alliance[1].score()) {
			result += "R";
		} else if (alliance[0].score()<alliance[1].score()) {
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
					+ "<BR>Match Results</H2></CENTER><HR>\n";
		}
		if (outputFileFormat == Format.File.CSV) {
			out = "TournamentMatchCode,Match,Result,Red0,Red1,Red2,Blue0,Blue1,Blue2,RTot,BTot,";
		}
		return out;
	}

	public static String bodyLine(MatchResult match, Format.File outputFileFormat, String code) {
		String out = "";
		if (outputFileFormat == Format.File.HTML) {
			out = "<TR>\n";
			out += "  <TD HEIGHT=17 ALIGN=LEFT>" + match.title() + "</TD>\n";
			out += "  <TD ALIGN=LEFT>" + match.resultString() + "</TD>\n";
			out += "  <TD ALIGN=LEFT>" + match.alliance[0].numbersString() + "</TD>\n";
			out += "  <TD ALIGN=LEFT>" + match.alliance[1].numbersString() + "</TD>\n";

			for (int i = 0; i < 2; i++) {
				out += "  <TD ALIGN=RIGHT SDVAL=\"" + match.alliance[i].score()
						+ "\" SDNUM=\"1033;\">" + match.alliance[i].score() + "</TD>\n";
			}

			out += "</TR>\n";
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
			out += "\n";
		}

		return out;
	}


}
