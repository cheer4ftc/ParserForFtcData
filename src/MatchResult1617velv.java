public class MatchResult1617velv extends MatchResultDetails {
	AllianceResult1617velv[] alliance1617;
	String [] rawString;
	
	MatchResult1617velv() {
		super();
		alliance1617 = new AllianceResult1617velv[2];
		
		alliance1617[0]=new AllianceResult1617velv();
		allianceDetails[0]=alliance1617[0];
		alliance[0]=alliance1617[0];
		
		alliance1617[1]=new AllianceResult1617velv();		
		allianceDetails[1]=alliance1617[1];
		alliance[1]=alliance1617[1];
	}

	public static String header(Format.File outputFileFormat, String tournamentName) {
		String out = "";
		if (outputFileFormat == Format.File.CSV) {
			out = "TournamentMatchCode,Div,MType,MNum,Red1,Red2,Red3,Blue1,Blue2,Blue3," +
					"R1DQ,R2DQ,R3DQ,R1YC,R2YC,R3YC,B1DQ,B2DQ,B3DQ,B1YC,B2YC,B3YC,R1Sur,R2Sur,R3Sur,B1Sur,B2Sur,B3Sur," +
					"Saved," +
					"RABeac,RACap,RACent,RACorn,RA1Pk,RA2Pk,RTBeac,RTCent,RTCorn,RECap,RPMin,RPMaj,RPMinRx,RPMajRx," +
					"BABeac,BACap,BACent,BACorn,BA1Pk,BA2Pk,BTBeac,BTCent,BTCorn,BECap,BPMin,BPMaj,BPMinRx,BPMajRx,";		}
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
