
public class AllianceResultDetails extends AllianceResult implements Comparable<AllianceResultDetails> {

    public enum ScoreType {
        TOTAL, TOTAL_WITH_NEGATIVE_PEN, AUTO, AUTOB, TELE, END, PEN, PEN_INCURRED
    }

    protected int[] score = new int[ScoreType.values().length]; // size of ScoreType

    AllianceResultDetails() {
        super();
    }
    AllianceResultDetails(AllianceResult ar) {
        super();
        for (int j=0; j<3; j++) {
            team[j]=ar.team[j];
        }
        score[ScoreType.TOTAL.ordinal()]=ar.score;
        score[ScoreType.TOTAL_WITH_NEGATIVE_PEN.ordinal()]=ar.score;
        score[ScoreType.TELE.ordinal()]=ar.score;
    }

    @Override
    public int compareTo(AllianceResultDetails ard) {
        return team[0].number - ard.team[0].number;
    }

    public int score(ScoreType t) {
        if (t == ScoreType.TOTAL) {
            score[ScoreType.TOTAL.ordinal()] = score[ScoreType.AUTO.ordinal()]
                    + score[ScoreType.AUTOB.ordinal()]
                    + score[ScoreType.TELE.ordinal()]
                    + score[ScoreType.END.ordinal()]
                    + score[ScoreType.PEN.ordinal()];
        }
        if (t == ScoreType.TOTAL_WITH_NEGATIVE_PEN) {
            score[ScoreType.TOTAL_WITH_NEGATIVE_PEN.ordinal()] = score[ScoreType.AUTO.ordinal()] +
                    + score[ScoreType.AUTOB.ordinal()]
                    + score[ScoreType.TELE.ordinal()]
                    + score[ScoreType.END.ordinal()]
                    - score[ScoreType.PEN_INCURRED.ordinal()];
        }
        return score[t.ordinal()];
    }

    public void setScore(ScoreType t, int val) {
        if ((t!=ScoreType.TOTAL)&(t!=ScoreType.TOTAL_WITH_NEGATIVE_PEN))
            score[t.ordinal()] = val;
    }

    @Override
    public int score() {
        return score(ScoreType.TOTAL);
    }

}
