public class AllianceResult {
	protected int score=0;
	public TeamMatchStatus team[];
	
	AllianceResult() {
		team = new TeamMatchStatus[3];
		for (int j=0; j<3; j++) {
			team[j]=new TeamMatchStatus();
		}
	}

	public void copy(AllianceResult ari) {
		score=ari.score;
		team[0].copy(ari.team[0]);
		team[1].copy(ari.team[1]);
		team[2].copy(ari.team[2]);
	}
	
	public int score() {
		return score;
	}
	public void setScore(int s) {
		score = s;
	}
	public String numbersString() {
		String out="";
		for (int i=0; i<3; i++) {
			if (team[i].number>0) {
				out+=Integer.toString(team[i].number);
				if (team[i].surrogate) {
					out+="*";
				}
				out+=" ";
			}
		}
		return out;
	}
}
