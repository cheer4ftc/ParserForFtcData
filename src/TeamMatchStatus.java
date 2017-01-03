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
        if (str.length()<1) {
            surrogate = false;
            number = 0;
            return;
        }
        if (str.substring(str.length() - 1, str.length()).equals("*")) {
            surrogate = true;
            try {
                number = Integer.valueOf(str.substring(0, str.length() - 1));
            } catch (Exception e) {
                System.err.println("Surrogate error:"+str);
                number=0;
            }
        } else {
            surrogate = false;
            number = Integer.valueOf(str);
            if (number<0)
                number=0;
        }
    }
}
