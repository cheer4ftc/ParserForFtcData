/**
 * Created by Bill on 12/20/2016.
 */
public class TeamInfo {
    int number;
    String name;

    TeamInfo() {
        number=-1;
        name="";
    }
    public void copy(TeamInfo in) {
        number = in.number;
        name = in.name;
    }
}
