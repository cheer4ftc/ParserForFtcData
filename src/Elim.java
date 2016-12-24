public class Elim {
    enum Result {
        WINNING("Winning"),
        FINALIST("Finalist"),
        SEMIFINALIST("Semifinalist"),
        NONE("None");

        String name;

        Result(String n) {
            name = n;
        }

        public String toString() {
            return name;
        }

        public String toChar() {
            return String.valueOf(name.charAt(0));
        }
    }

    enum Position {
        CAPTAIN("Captain"),
        FIRST_PICK("1st Pick"),
        SECOND_PICK("2nd Pick");

        String name;

        Position(String n) {
            name = n;
        }

        public String toString() {
            return name;
        }
        public String toChar() {
            return String.valueOf(name.charAt(0));
        }
    }

    Result allianceType;
    Position position;

    Elim() {
        allianceType= Result.NONE;

    }

    public void setPosition(int i) {
        switch (i) {
            case 0:
                position = Position.CAPTAIN;
                break;
            case 1:
                position = Position.FIRST_PICK;
                break;
            case 2:
                position = Position.SECOND_PICK;
                break;
            default:
                System.err.println("Error: invalid Elimination position type");
                System.exit(-1);
        }
    }

    public String toString() {
        String out="";
        out += allianceType;
        if (allianceType!= Result.NONE) {
            out += " " + position;
        }
        return out;
    }
    public String to2Char() {
        String out="";
        if (allianceType== Result.NONE) {
            return out;
        }
        out += allianceType.toChar() + position.toChar();
        return out;
    }
}