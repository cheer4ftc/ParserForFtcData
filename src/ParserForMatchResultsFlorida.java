import java.io.IOException;

public class ParserForMatchResultsFlorida {

	public static MatchResult parseMatch(String[] matchInCol) throws IOException {
		MatchResult match = new MatchResult();

		if (matchInCol.length<2)
			return null;

		int colPtr=0;

		match.division=1;
		match.type=1;
		match.number=Integer.valueOf(matchInCol[colPtr++]);
		match.played=1;

		match.alliance[0].team[0].setNumberAndSurrogate(matchInCol[colPtr++]);
		match.alliance[0].team[1].setNumberAndSurrogate(matchInCol[colPtr++]);

		match.alliance[1].team[0].setNumberAndSurrogate(matchInCol[colPtr++]);
		match.alliance[1].team[1].setNumberAndSurrogate(matchInCol[colPtr++]);


		match.alliance[0].score = Integer.valueOf(matchInCol[colPtr++]);
		match.alliance[1].score = Integer.valueOf(matchInCol[colPtr++]);

		return match;
	}
}

