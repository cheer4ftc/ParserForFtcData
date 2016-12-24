public class AllianceResult1617velv extends AllianceResultDetails {
	int autoBeacons, sAutoBeacons;
	boolean cAutoCap;
	int sAutoCap;
	int autoCenterParticles, sAutoCenterParticles;
	int autoCornerParticles, sAutoCornerParticles;
	int[] cAutoRobotLoc=new int[2];
	int[] sAutoRobotLoc=new int[2];

	int teleBeacons, sTeleBeacons;
	int teleCenterParticles, sTeleCenterParticles;
	int teleCornerParticles, sTeleCornerParticles;

	int cEndCap, sEndCap;

	int penMinor, sPenMinor;
	int penMajor, sPenMajor;
	int penMinorIncurred, sPenMinorIncurred;
	int penMajorIncurred, sPenMajorIncurred;


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
				sAutoBeacons=30*autoBeacons;
				if (cAutoCap) {
					sAutoCap=5;
				} else {
					sAutoCap=0;
				}
				sAutoCenterParticles=15*autoCenterParticles;
				sAutoCornerParticles=5*autoCornerParticles;
				for (int i=0; i<2; i++) {
					if ((cAutoRobotLoc[i]==1)||(cAutoRobotLoc[i]==3)) {
						sAutoRobotLoc[i]=5;
					} else if ((cAutoRobotLoc[i]==2)||(cAutoRobotLoc[i]==4)) {
						sAutoRobotLoc[i]=10;
					} else {
						sAutoRobotLoc[i]=0;
					}
				}
				score[ScoreType.AUTO.ordinal()]=sAutoBeacons+sAutoCap;
				score[ScoreType.AUTO.ordinal()]+=sAutoCenterParticles+sAutoCornerParticles;
				score[ScoreType.AUTO.ordinal()]+=sAutoRobotLoc[0]+sAutoRobotLoc[1];
				return score[ScoreType.AUTO.ordinal()];

			case AUTOB:
				return 0;

			case TELE:
				sTeleBeacons=10*teleBeacons;
				sTeleCenterParticles=5*teleCenterParticles;
				sTeleCornerParticles=teleCornerParticles;
				score[ScoreType.TELE.ordinal()]=sTeleBeacons+sTeleCenterParticles+sTeleCornerParticles;
				return score[ScoreType.TELE.ordinal()];

			case END:
				sEndCap=10*cEndCap;
				if (cEndCap==3)
					sEndCap=40;
				score[ScoreType.END.ordinal()]=sEndCap;
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
