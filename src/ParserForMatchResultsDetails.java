import java.io.BufferedReader;
import java.io.IOException;

public class ParserForMatchResultsDetails extends ParserForMatchResults {

	public static MatchResultDetails parseMatch(String line, BufferedReader br) throws IOException {
		MatchResultDetails match = new MatchResultDetails();
		String[] matchInCol = line.split("[ \t]+");

		if (matchInCol.length<2)
			return null;

		match.division=1;
		switch (line.substring(0,1)) {
			case "Q":
				match.type=1;
				match.number=Integer.valueOf(matchInCol[0].substring(2));
				break;
			case "P":
				match.type=0;
				match.number=Integer.valueOf(matchInCol[0].substring(2));
				break;
			case "F":
				match.type=4;
				match.number=Integer.valueOf(matchInCol[0].substring(2));
				break;
			case "S":
				match.type=3;
				match.number=10*Integer.valueOf(matchInCol[0].substring(3,4))+Integer.valueOf(matchInCol[0].substring(5,6)) ;
				break;
			default:
				return null;
		}
		match.played=1;

		String[] matchResult = matchInCol[1].split("-");
		match.alliance[0].score = Integer.valueOf(matchResult[0]);
		match.alliance[1].score = Integer.valueOf(matchResult[1]);

		int colPtr=3;
		match.alliance[0].team[0].setNumberAndSurrogate(matchInCol[colPtr++]);
		match.alliance[0].team[1].setNumberAndSurrogate(matchInCol[colPtr++]);

		match.alliance[1].team[0].setNumberAndSurrogate(matchInCol[colPtr++]);
		match.alliance[1].team[1].setNumberAndSurrogate(matchInCol[colPtr++]);

		if (match.type==3 || match.type==4){
			if (Integer.valueOf(matchInCol[colPtr])!=match.alliance[0].score) {
				// 3 team alliances, so shift team numbers and read two more
				match.alliance[0].team[2].number = match.alliance[1].team[0].number;
				match.alliance[1].team[0].number = match.alliance[1].team[1].number;

				match.alliance[1].team[1].number = Integer.valueOf(matchInCol[colPtr++]);
				match.alliance[1].team[2].number = Integer.valueOf(matchInCol[colPtr++]);
			}
		}
		for (int i=0; i<2; i++) {
			match.allianceDetails[i].setScore(AllianceResultDetails.ScoreType.TOTAL, Integer.valueOf(matchInCol[colPtr++]));
			match.allianceDetails[i].setScore(AllianceResultDetails.ScoreType.AUTO, Integer.valueOf(matchInCol[colPtr++]));
			match.allianceDetails[i].setScore(AllianceResultDetails.ScoreType.AUTOB, Integer.valueOf(matchInCol[colPtr++]));
			match.allianceDetails[i].setScore(AllianceResultDetails.ScoreType.TELE, Integer.valueOf(matchInCol[colPtr++]));
			match.allianceDetails[i].setScore(AllianceResultDetails.ScoreType.END, Integer.valueOf(matchInCol[colPtr++]));
			match.allianceDetails[i].setScore(AllianceResultDetails.ScoreType.PEN, Integer.valueOf(matchInCol[colPtr]));
			match.allianceDetails[1-i].setScore(AllianceResultDetails.ScoreType.PEN_INCURRED, Integer.valueOf(matchInCol[colPtr++]));
		}
		return match;
	}

}

