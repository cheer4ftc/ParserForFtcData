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

    public void setNumberAndSurrogate(String str) {
        if (str.substring(str.length() - 1, str.length()).equals("*")) {
            surrogate = true;
            number = Integer.valueOf(str.substring(0, str.length() - 1));
        } else {
            surrogate = false;
            number = Integer.valueOf(str);
        }
    }
}
