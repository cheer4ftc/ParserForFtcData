public class AllianceResult1516resq extends AllianceResultDetails {
    int[] cAutoRobotLoc = new int[2];
    int[] sAutoRobotLoc = new int[2];

    int autoBeacons, sAutoBeacons;
    int autoClimbers, sAutoClimbers;

    int[] cTeleRobotLoc = new int[2];
    int[] sTeleRobotLoc = new int[2];

    int[] teleGoal = new int[4];
    int[] sTeleGoal = new int[4];

    int teleClimbers, sTeleClimbers;

    int teleZipliners, sTeleZipliners;

    int endAllClear, sEndAllClear;

    int endPullup, sEndPullup;

    int penMinor, sPenMinor;
    int penMajor, sPenMajor;
    int penMinorIncurred, sPenMinorIncurred;
    int penMajorIncurred, sPenMajorIncurred;

    private int[] scorePerParkingZone = {0, 5, 5, 5, 10, 20, 40};
    private int[] scorePerGoal = {1, 15, 5, 10}; // crazy order from scoring system!!

    @Override
    public int score(ScoreType t) {
        switch (t) {
            case TOTAL:
                score[ScoreType.TOTAL.ordinal()] = score(ScoreType.AUTO)
                        + score(ScoreType.AUTOB)
                        + score(ScoreType.TELE)
                        + score(ScoreType.END)
                        + score(ScoreType.PEN);
                return score[ScoreType.TOTAL.ordinal()];

            case TOTAL_WITH_NEGATIVE_PEN:
                score[ScoreType.TOTAL_WITH_NEGATIVE_PEN.ordinal()] = score(ScoreType.AUTO)
                        + score(ScoreType.AUTOB)
                        + score(ScoreType.TELE)
                        + score(ScoreType.END)
                        - score(ScoreType.PEN_INCURRED);
                return score[ScoreType.TOTAL_WITH_NEGATIVE_PEN.ordinal()];

            case AUTO:
                for (int i = 0; i < 2; i++) {
                    sAutoRobotLoc[i] = scorePerParkingZone[cAutoRobotLoc[i]];
                }

                sAutoBeacons = 20 * autoBeacons;
                sAutoClimbers = 10 * autoClimbers;
                score[ScoreType.AUTO.ordinal()] = sAutoRobotLoc[0] + sAutoRobotLoc[1] + sAutoBeacons + sAutoClimbers;

                return score[ScoreType.AUTO.ordinal()];

            case AUTOB:
                return 0;

            case TELE:
                score[ScoreType.TELE.ordinal()] = 0;
                for (int i = 0; i < 4; i++) {
                    sTeleGoal[i] = teleGoal[i] * scorePerGoal[i];
                    score[ScoreType.TELE.ordinal()] += sTeleGoal[i];
                }

                for (int i = 0; i < 2; i++) {
                    sTeleRobotLoc[i] = scorePerParkingZone[cTeleRobotLoc[i]];
                    score[ScoreType.TELE.ordinal()] += sTeleRobotLoc[i];
                }

                sTeleClimbers = 10 * teleClimbers;
                score[ScoreType.TELE.ordinal()] += sTeleClimbers;

                sTeleZipliners = 20 * teleZipliners;
                score[ScoreType.TELE.ordinal()] += sTeleZipliners;

                return score[ScoreType.TELE.ordinal()];

            case END:
                score[ScoreType.END.ordinal()] = 0;
                sEndAllClear = 20 * endAllClear;
                score[ScoreType.END.ordinal()] += sEndAllClear;

                sEndPullup = 80 * endPullup;
                score[ScoreType.END.ordinal()] += sEndPullup;

                return score[ScoreType.END.ordinal()];

            case PEN:
                sPenMinor = 10 * penMinor;
                sPenMajor = 40 * penMajor;
                score[ScoreType.PEN.ordinal()] = sPenMinor + sPenMajor;
                return score[ScoreType.PEN.ordinal()];

            case PEN_INCURRED:
                sPenMinorIncurred = 10 * penMinorIncurred;
                sPenMajorIncurred = 40 * penMajorIncurred;
                score[ScoreType.PEN_INCURRED.ordinal()] = sPenMinorIncurred + sPenMajorIncurred;
                return score[ScoreType.PEN_INCURRED.ordinal()];

            default:
                return 0;
        }
    }
}
