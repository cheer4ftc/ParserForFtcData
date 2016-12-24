public class ParserForMatch1516resq extends ParserForMatchesDotText {
	public static MatchResult1516resq parseMatch(String[] col) {
		MatchResult1516resq match = new MatchResult1516resq();
		
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
		}
		for (int j=0; j<2; j++) {
			for (int k=0; k<3; k++) {
				if (Integer.valueOf(col[i++])==0) {
					match.alliance[j].team[k].surrogate=false;
				} else {
					match.alliance[j].team[k].surrogate=true;					
				}
			}
		}
		match.played = Integer.valueOf(col[i++]);
//		1,0,0,1,0,5,4,4,0,0,3,2,0,1,0,0,0,0,5,0,0,0,4,0,3,0,0,0,4,1,1,1,0,0,0,0,
		for (int j=0; j<2; j++) {
			match.alliance1516[j].cAutoRobotLoc[0] = Integer.valueOf(col[i++]);
			match.alliance1516[j].cAutoRobotLoc[1] = Integer.valueOf(col[i++]);
			match.alliance1516[j].autoBeacons = Integer.valueOf(col[i++]);
			match.alliance1516[j].autoClimbers = Integer.valueOf(col[i++]);
			
			match.alliance1516[j].cTeleRobotLoc[0] = Integer.valueOf(col[i++]);
			match.alliance1516[j].cTeleRobotLoc[1] = Integer.valueOf(col[i++]);
			for (int k=0; k<4; k++) {
				match.alliance1516[j].teleGoal[k] = Integer.valueOf(col[i++]);
			}
			match.alliance1516[j].teleClimbers = Integer.valueOf(col[i++]);
			match.alliance1516[j].teleZipliners = Integer.valueOf(col[i++]);
			
			match.alliance1516[j].endAllClear = Integer.valueOf(col[i++]);
			match.alliance1516[j].endPullup = Integer.valueOf(col[i++]);
			
			match.alliance1516[j].penMinorIncurred = Integer.valueOf(col[i++]);
			match.alliance1516[j].penMajorIncurred = Integer.valueOf(col[i++]);
			match.alliance1516[j].penMinor = Integer.valueOf(col[i++]);
			match.alliance1516[j].penMajor = Integer.valueOf(col[i++]);
		}
		
		return match;
	}
}
