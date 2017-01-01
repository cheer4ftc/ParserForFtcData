/**
 * Created by Bill on 12/22/2016.
 */
public class Format {
    public enum Data {
        RANKINGS, RESULTS, RESULTS_DETAILS, RESULTS_ALL, STATS_RESULTS, STATS_DETAILS, EVENT_NAMES
    }

    public enum File {
        MATCHESTXT, CSV, HTML, RESULTSTXT, DETAILSTXT
    }

    public enum Season {
        s1213RIUP(1213,"Ring It Up!","1213riup"),
        s1314BLKP(1314,"Block Party","1314blkp"),
        s1415CASC(1415,"Cascade Effect","1415casc"),
        s1516RESQ(1516,"RES-Q","1516resq"),
        s1617VELV(1617,"Velocity Vortex","1617velv");

        private final int yearCode;
        private final String fullName;
        private final String code;
        private Season(final int i, final String n, final String c) {yearCode=i; fullName=n; code=c;}
        public int yearCode() {return yearCode;}
        public String fullName() {return fullName;}
        public String code() {return code;}
    }

    public enum Results {
        ORIGINAL, ONE_PER_LINE
    }
}
