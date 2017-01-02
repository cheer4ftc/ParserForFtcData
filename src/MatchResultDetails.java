public class MatchResultDetails extends MatchResult {
	AllianceResultDetails[] allianceDetails;
	
	MatchResultDetails() {
		super();

		allianceDetails = new AllianceResultDetails[2];

		allianceDetails[0]=new AllianceResultDetails();
		alliance[0]=allianceDetails[0];
		
		allianceDetails[1]=new AllianceResultDetails();
		alliance[1]=allianceDetails[1];
	}

	MatchResultDetails(MatchResult mr) {
		super();

		allianceDetails = new AllianceResultDetails[2];

		allianceDetails[0]=new AllianceResultDetails(mr.alliance[0]);
		alliance[0]=allianceDetails[0];
		alliance[0].score = mr.alliance[0].score;
		allianceDetails[0].setScore(AllianceResultDetails.ScoreType.TELE,mr.alliance[0].score);

		allianceDetails[1]=new AllianceResultDetails(mr.alliance[1]);
		alliance[1]=allianceDetails[1];
		alliance[1].score = mr.alliance[1].score;
		allianceDetails[1].setScore(AllianceResultDetails.ScoreType.TELE,mr.alliance[1].score);

		this.copy(mr);

	}

	public static String header(Format.File outputFileFormat, String tournamentName) {
		String out = "";
		if (outputFileFormat == Format.File.HTML) {
			out = "<HTML><head><style>@media print{  table { page-break-after:auto }  tr    { page-break-inside:avoid; page-break-after:auto }  "
					+ "td    { page-break-inside:avoid; page-break-after:auto }  thead { display:table-header-group }"
					+ "  tfoot { display:table-footer-group }}</style>"
					+ "<TITLE>Match Results (w/ Details)</TITLE></head><CENTER><H2>"
					+ tournamentName
					+ "<BR>Match Results (w/ Details)</H2></CENTER><HR>Column Abbreviations:<ul><li>Tot - Total Match Score</li>"
					+ "<li>Auto - Autonomous Score</li><li>AutoB - Autonomous Bonus Score</li><li>Tele - Tele-Operated Score</li>"
					+ "<li>EndG - End Game Score</li><li>Pen - Penalty Points</li><p> </p>"
					+ "<DIV ALIGN=CENTER><TABLE BORDER=2 CELLPADDING=5 CELLSPACING=1 WIDTH=75%>"
					+ "<TR ALIGN=CENTER><TH>&nbsp;</th><TH>&nbsp;</th><TH>&nbsp;</th><TH >&nbsp;</th><TH COLSPAN=\"6\">Red Scores</TH><TH COLSPAN=\"6\">Blue Scores</TH><TR ALIGN=CENTER>"
					+ "<TH >Match</TH><TH >Result</TH><TH >Red Teams</TH><TH >Blue Teams</TH><TH >Tot</TH><TH >Auto</TH><TH >AutoB</TH><TH >Tele</TH>"
					+ "<TH >EndG</TH><TH >Pen</TH><TH >Tot</TH><TH >Auto</TH><TH >AutoB</TH><TH >Tele</TH><TH >EndG</TH><TH >Pen</TH></TR>\n";
		}
		if (outputFileFormat == Format.File.CSV) {
			out = "TournamentMatchCode,Match,Result,Red0,Red1,Red2,Blue0,Blue1,Blue2,RTot,RAuto,RAutoB,RTele,REnd,RPen,BTot,BAuto,BAutoB,BTele,BEnd,BPen,";
		}
		return out;
	}

	public static String bodyLine(MatchResultDetails match, Format.File outputFileFormat, String code) {
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
				out += "  <TD ALIGN=RIGHT SDVAL=\"" + match.allianceDetails[i].score(AllianceResultDetails.ScoreType.AUTO)
						+ "\" SDNUM=\"1033;\">" + match.allianceDetails[i].score(AllianceResultDetails.ScoreType.AUTO)
						+ "</TD>\n";
				out += "  <TD ALIGN=RIGHT SDVAL=\"" + match.allianceDetails[i].score(AllianceResultDetails.ScoreType.AUTOB)
						+ "\" SDNUM=\"1033;\">" + match.allianceDetails[i].score(AllianceResultDetails.ScoreType.AUTOB)
						+ "</TD>\n";
				out += "  <TD ALIGN=RIGHT SDVAL=\"" + match.allianceDetails[i].score(AllianceResultDetails.ScoreType.TELE)
						+ "\" SDNUM=\"1033;\">" + match.allianceDetails[i].score(AllianceResultDetails.ScoreType.TELE)
						+ "</TD>\n";
				out += "  <TD ALIGN=RIGHT SDVAL=\"" + match.allianceDetails[i].score(AllianceResultDetails.ScoreType.END)
						+ "\" SDNUM=\"1033;\">" + match.allianceDetails[i].score(AllianceResultDetails.ScoreType.END)
						+ "</TD>\n";
				out += "  <TD ALIGN=RIGHT SDVAL=\"" + match.allianceDetails[i].score(AllianceResultDetails.ScoreType.PEN)
						+ "\" SDNUM=\"1033;\">" + match.allianceDetails[i].score(AllianceResultDetails.ScoreType.PEN)
						+ "</TD>\n";
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
				for (AllianceResultDetails.ScoreType t : AllianceResultDetails.ScoreType.values()) {
					if ((t != AllianceResultDetails.ScoreType.TOTAL_WITH_NEGATIVE_PEN) && (t != AllianceResultDetails.ScoreType.PEN_INCURRED))
						out += match.allianceDetails[i].score(t) + ",";
				}
			}
			out += "\n";
		}

		return out;
	}
}
