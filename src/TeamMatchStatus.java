public class TeamMatchStatus extends TeamInfo {
	boolean noShow;
	boolean dq;
	boolean yellowCard;
	boolean surrogate;

	public void copy(TeamMatchStatus in) {
		super.copy(in);
		noShow = in.noShow;
		dq = in.dq;
		yellowCard = in.yellowCard;
		surrogate = in.surrogate;
	}
}
