public class MatchResult1516resq extends MatchResultDetails {
	AllianceResult1516resq[] alliance1516;
	String [] rawString;
	
	MatchResult1516resq() {
		super();
		alliance1516 = new AllianceResult1516resq[2];
		
		alliance1516[0]=new AllianceResult1516resq();
		allianceDetails[0]=alliance1516[0];
		alliance[0]=alliance1516[0];
		
		alliance1516[1]=new AllianceResult1516resq();		
		allianceDetails[1]=alliance1516[1];
		alliance[1]=alliance1516[1];
	}

	public static String header(Format.File outputFileFormat, String tournamentName) {
		String out = "";
		if (outputFileFormat == Format.File.CSV) {
			out = "TournamentMatchCode,Div,MType,MNum,Red1,Red2,Red3,Blue1,Blue2,Blue3," +
					"R1DQ,R2DQ,R3DQ,B1DQ,B2DQ,B3DQ,R1Sur,R2Sur,R3Sur,B1Sur,B2Sur,B3Sur," +
					"Saved,"; // need to add game specific header below
		}
		return out;
	}

	public static String bodyLine(MatchResult1617velv match, Format.File outputFileFormat, String code) {
		String out = "";
		if (outputFileFormat == Format.File.CSV) {
			out = code;
			out += "-" + match.title() + ",";

			for (int i=0; i< match.rawString.length; i++) {
				out += match.rawString[i]+",";
			}
			out += "\n";
		}

		return out;
	}
}
