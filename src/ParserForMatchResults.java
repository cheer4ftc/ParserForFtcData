import java.io.BufferedReader;
import java.io.IOException;

public class ParserForMatchResults {

	public static MatchResult parseMatch(String line, BufferedReader br) throws IOException {
		MatchResult match = new MatchResult();
		String[] matchInCol = line.split("[ \t]");

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

		if (matchInCol.length==5) { // two lines per match
			match.alliance[0].team[0].setNumberAndSurrogate(matchInCol[3]);
			match.alliance[1].team[0].setNumberAndSurrogate(matchInCol[4]);

			String line2 = br.readLine();
			matchInCol = line2.split("[ \t]");
			match.alliance[0].team[1].setNumberAndSurrogate(matchInCol[0]);
			match.alliance[1].team[1].setNumberAndSurrogate(matchInCol[1]);

			if (match.type == 3 || match.type == 4) {
				String line3 = br.readLine();
				matchInCol = line3.split("[ \t]");
				if (matchInCol.length == 2) {
					match.alliance[0].team[2].number = Integer.valueOf(matchInCol[0]);
					match.alliance[1].team[2].number = Integer.valueOf(matchInCol[1]);
				}
			}
		} else { // one line per match
			match.alliance[0].team[0].setNumberAndSurrogate(matchInCol[3]);
			match.alliance[0].team[1].setNumberAndSurrogate(matchInCol[4]);

			match.alliance[1].team[0].setNumberAndSurrogate(matchInCol[5]);
			match.alliance[1].team[1].setNumberAndSurrogate(matchInCol[6]);

			if (match.type == 3 || match.type == 4) { // 3 per alliance
				match.alliance[0].team[2].number=match.alliance[1].team[0].number;
				match.alliance[1].team[0].number=match.alliance[1].team[1].number;

				match.alliance[1].team[1].number = Integer.valueOf(matchInCol[7]);
				match.alliance[1].team[2].number = Integer.valueOf(matchInCol[8]);
			}
		}
		return match;
	}
}

