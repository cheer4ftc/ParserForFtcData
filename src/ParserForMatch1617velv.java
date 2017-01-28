public class ParserForMatch1617velv extends ParserForMatchesDotText {
	public static MatchResult1617velv parseMatch(String[] col) {
		MatchResult1617velv match = new MatchResult1617velv();
		match.rawString = col;
		
		// common parsing here
		parseTeams(col,match);
		
		// season specific parsing here
		int i=9;
		for (int j=0; j<2; j++) {
			for (int k=0; k<3; k++) {
				int tmp=Integer.valueOf(col[i++]);
				if (tmp==0) {
					match.alliance[j].team[k].noShow=false;
					match.alliance[j].team[k].dq=false;
				} else if (tmp==1) {
					match.alliance[j].team[k].noShow=true;
					match.alliance[j].team[k].dq=false;					
				} else {
					match.alliance[j].team[k].noShow=false;
					match.alliance[j].team[k].dq=true;
				}					
			}
			for (int k=0; k<3; k++) {
				match.alliance[j].team[k].yellowCard=Boolean.valueOf(col[i++]);
			}
		}
		for (int j=0; j<2; j++) {
			for (int k=0; k<3; k++) {
				if (Integer.valueOf(col[i++]) != 0) {
					match.alliance[j].team[k].surrogate = true;
				} else {
					match.alliance[j].team[k].surrogate = false;
				}
			}
		}
		match.played = Integer.valueOf(col[i++]);
		for (int j=0; j<2; j++) {
			match.alliance1617[j].autoBeacons = Integer.valueOf(col[i++]);
			match.alliance1617[j].cAutoCap = Boolean.valueOf(col[i++]);
			match.alliance1617[j].autoCenterParticles = Integer.valueOf(col[i++]);
			match.alliance1617[j].autoCornerParticles = Integer.valueOf(col[i++]);
			match.alliance1617[j].cAutoRobotLoc[0] = Integer.valueOf(col[i++]);
			match.alliance1617[j].cAutoRobotLoc[1] = Integer.valueOf(col[i++]);
			match.alliance1617[j].teleBeacons = Integer.valueOf(col[i++]);
			match.alliance1617[j].teleCenterParticles = Integer.valueOf(col[i++]);
			match.alliance1617[j].teleCornerParticles = Integer.valueOf(col[i++]);
			match.alliance1617[j].cEndCap = Integer.valueOf(col[i++]);
			match.alliance1617[j].penMinorIncurred = Integer.valueOf(col[i++]);
			match.alliance1617[j].penMajorIncurred = Integer.valueOf(col[i++]);
			match.alliance1617[j].penMinor = Integer.valueOf(col[i++]);
			match.alliance1617[j].penMajor = Integer.valueOf(col[i++]);

		}
		
		return match;
	}
}
