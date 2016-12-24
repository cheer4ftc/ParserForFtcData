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
	
}
