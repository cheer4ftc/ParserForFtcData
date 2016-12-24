public class ParserForMatchesDotText {
	
	public static void parseTeams(String[] in, MatchResult match) {
		int i=0;

		match.division = Integer.valueOf(in[i++]);
		match.type = Integer.valueOf(in[i++]);
		match.number = Integer.valueOf(in[i++]);
		for (int j=0; j<2; j++) {
			for (int k=0; k<3; k++) {
				match.alliance[j].team[k].number=Integer.valueOf(in[i++]);
			}
		}
	}
}
