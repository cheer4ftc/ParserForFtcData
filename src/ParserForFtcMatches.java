import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParserForFtcMatches {

    private static final String TOURNAMENT = "Tournament";
    private static String tournamentName;
    private static String tournamentCode;

    private static Format.Data inputDataFormat;
    private static Format.File inputFileFormat;

    private static Format.Data outputDataFormat;
    private static Format.File outputFileFormat;
    private static Format.Results outputResultsFormat;

    private static Format.Season season;


    public static void main(String[] args) throws IOException {

        tournamentName = TOURNAMENT;
        tournamentCode = "NONE";

        season = Format.Season.s1617VELV;

        // default input is matches.txt file format
        inputDataFormat = Format.Data.RESULTS_DETAILS_ALL;
        inputFileFormat = Format.File.TXT;
        String inputFileName = "";

        // default output is MatchResultsDetails html file
        outputDataFormat = Format.Data.RESULTS_DETAILS;
        outputFileFormat = Format.File.HTML;
        outputResultsFormat = Format.Results.ORIGINAL;
        String outputFileName = "";

        // parse command line arguments
        if (args.length == 0) {
            printErrorMessageAndExit("No command line arguments");
        }

        boolean fnIn = false;
        boolean fnOut = false;
        boolean readingMultipleEvents = false;

        int loopCount = 0;
        int clptr = 0;
        while (clptr < args.length) {
            switch (args[clptr]) {
                case "-i":
                    clptr++;
                    inputFileName = args[clptr++];
                    fnIn = true;
                    break;
                case "-o":
                    clptr++;
                    outputFileName = args[clptr++];
                    fnOut = true;
                    break;
                case "-n":
                    clptr++;
                    tournamentName = args[clptr++];
                    break;
                case "-c":
                    clptr++;
                    tournamentCode = args[clptr++];
                    break;
                case "-s":
                    clptr++;
                    int sNum = Integer.valueOf(args[clptr++]);
                    season = null;
                    for (Format.Season s : Format.Season.values()) {
                        if (sNum == s.yearCode()) {
                            season = s;
                        }
                    }
                    if (season == null) {
                        printErrorMessageAndExit("ERROR: invalid season");
                    }
                    break;

                case "-iA":
                    inputDataFormat = Format.Data.RESULTS_DETAILS_ALL;
                    clptr++;
                    break;

                case "-iC":
                    inputFileFormat = Format.File.CSV;
                    clptr++;
                    break;
                case "-iT":
                    inputFileFormat = Format.File.TXT;
                    clptr++;
                    break;

                case "-iM":
                    readingMultipleEvents = true;
                    clptr++;
                    break;


                case "-oN":
                    outputDataFormat = Format.Data.EVENT_NAMES;
                    clptr++;
                    break;


                case "-or":
                    outputDataFormat = Format.Data.RANKINGS;
                    clptr++;
                    break;
                case "-oR":
                    outputDataFormat = Format.Data.RESULTS;
                    clptr++;
                    break;
                case "-oD":
                    outputDataFormat = Format.Data.RESULTS_DETAILS;
                    clptr++;
                    break;

                case "-oSR":
                    outputDataFormat = Format.Data.STATS_RESULTS;
                    clptr++;
                    break;
                case "-oSD":
                    outputDataFormat = Format.Data.STATS_DETAILS;
                    clptr++;
                    break;

                case "-oC":
                    outputFileFormat = Format.File.CSV;
                    clptr++;
                    break;
                case "-oH":
                    outputFileFormat = Format.File.HTML;
                    clptr++;
                    break;
            }
            loopCount++;
            if (loopCount > 100) {
                printErrorMessageAndExit("Error in processing arguments. Unknown argument entered?");
            }
        }

        if (!fnIn | !fnOut) {
            printErrorMessageAndExit("Error: input and output filenames not given");
        }

        if (readingMultipleEvents && tournamentName.equals(TOURNAMENT) && (outputDataFormat != Format.Data.EVENT_NAMES)) {
            printErrorMessageAndExit("Error: must enter a specific tournament name when using a Multi-Event file");
        }

        // open input file stream
        FileInputStream fis = new FileInputStream(inputFileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));

        // open output file stream
        FileOutputStream fos = new FileOutputStream(outputFileName);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        List<MatchResultDetails> matchList = new ArrayList<>();

        // output any desired header
        if (outputDataFormat == Format.Data.RANKINGS) {
            bw.write(TeamTournamentRanking.header(outputFileFormat,tournamentName)+"\n");
        }
        if (outputDataFormat == Format.Data.RESULTS_DETAILS) {
            bw.write(MatchResultDetails.header(outputFileFormat, tournamentName)+"\n");
        }
        if (outputDataFormat == Format.Data.STATS_DETAILS) {
            bw.write(TeamTournamentStatsDetails.header(outputFileFormat,tournamentName)+"\n");
        }
        if (outputDataFormat == Format.Data.STATS_RESULTS) {
            bw.write(TeamTournamentStatsResults.header(outputFileFormat,tournamentName)+"\n");
        }

        // parse matches
        String inLine;
        MatchResult match = null;
        String[] matchInCol = null;
        String previousName = "";
        boolean headerRead = false;

        while ((inLine = br.readLine()) != null) {
            boolean goodMatch = false;

            // read in a match line and parse it
            if (readingMultipleEvents) {
                if ((inputDataFormat == Format.Data.RESULTS_DETAILS_ALL) && (inputFileFormat == Format.File.CSV)) {
                    if (headerRead) {
                        matchInCol = inLine.split(",");
                        // remove non-tournament matches and practice matches
                        if ((matchInCol[1].equals(tournamentName)) && (Integer.valueOf(matchInCol[5]) != 0)) {
                            switch (season) {
                                case s1617VELV:
                                    match = ParserForMatch1617velv.parseMatch(Arrays
                                            .copyOfRange(matchInCol, 4, matchInCol.length));
                                    break;
                                case s1516RESQ:
                                    match = ParserForMatch1516resq.parseMatch(Arrays
                                            .copyOfRange(matchInCol, 4, matchInCol.length));
                                    break;
                                default:
                                    printErrorMessageAndExit("Error: unsupported season");
                            }
                            goodMatch = true;
                        }
                    } else {
                        headerRead = true; // ignore first line
                    }
                } else {
                    printErrorMessageAndExit("ERROR: invalid input data and file format combination. C0");
                }
            } else if ((inputDataFormat == Format.Data.RESULTS_DETAILS_ALL) && (inputFileFormat == Format.File.TXT)) {
                matchInCol = inLine.split("[|]");

                switch (season) {
                    case s1617VELV:
                        match = ParserForMatch1617velv.parseMatch(matchInCol);
                        break;
                    case s1516RESQ:
                        match = ParserForMatch1516resq.parseMatch(matchInCol);
                        break;
                    default:
                        printErrorMessageAndExit("Error: unsupported season");
                }
                goodMatch = true;
            } else {
                printErrorMessageAndExit("ERROR: invalid input data and file format combination. C1");
            }


            // process/ output the match info
            if (outputDataFormat == Format.Data.RESULTS_DETAILS) {
                if (goodMatch) {
                    bw.write(MatchResultDetails.bodyLine((MatchResultDetails) match, outputFileFormat,season.code()+"-"+tournamentCode ));
                }
            }

            if ((outputDataFormat == Format.Data.RANKINGS)
                    || (outputDataFormat == Format.Data.STATS_DETAILS)) {
                if (goodMatch) {
                    matchList.add((MatchResultDetails) match);
                }
            }

            if (outputDataFormat == Format.Data.EVENT_NAMES) {
                if ((matchInCol != null) && !matchInCol[1].equals(previousName)) {
                    String outStr = "";
                    for (int i = 0; i < 5; i++) {
                        outStr += matchInCol[i] + "|";
                    }
                    bw.write(outStr + "\n");
                    previousName = matchInCol[1];
                }
            }
        }


        // output postprocessing and footers
        if (outputDataFormat == Format.Data.RESULTS_DETAILS) {
            if (outputFileFormat == Format.File.HTML)
                bw.write(HtmlFooter());
        }

        if (outputDataFormat == Format.Data.RANKINGS) {
            // compute Rankings
            List<TeamTournamentRanking> teamT = new ArrayList<>();

            List<MatchResult> matchResultList = new ArrayList<>();
            matchResultList.addAll(matchList);
            TournamentRankings.computeRankings(matchResultList, teamT);

            String fileOfTeams = season.code() + "-teams.csv";
            TournamentRankings.addTeamNames(teamT, fileOfTeams);

            String outS = "";
            for (int t = 0; t < teamT.size(); t++) {
                outS+=teamT.get(t).bodyLine(outputFileFormat,season.code()+"-"+tournamentCode)+"\n";
            }
            if (outputFileFormat == Format.File.HTML) {
                outS += HtmlFooter();
            }
            bw.write(outS);
        }
        if ((outputDataFormat == Format.Data.STATS_DETAILS) ||
                (outputDataFormat == Format.Data.STATS_RESULTS)) {
            // compute Rankings
            List<TeamTournamentStatsDetails> teamT = new ArrayList<>();

            List<MatchResult> matchResultList = new ArrayList<>();
            matchResultList.addAll(matchList);

            String fileOfTeams = season.code() + "-teams.csv";
            TournamentStats.computeRankingsBeforeStats(matchResultList, teamT, fileOfTeams); // for OPR

            TournamentStats.computeStatsDetails(matchList, teamT, 0); // for OPR
            TournamentStats.computeStatsDetails(matchList, teamT, 1); // for OPR MMSE1

            // write out stats here

            String outS = "";
            for (int t = 0; t < teamT.size(); t++) {
                outS+= teamT.get(t).bodyLine(outputFileFormat, season.code()+"-"+tournamentCode);
                outS += "\n";
            }
            bw.write(outS);
            //          bw.write(outS);
        }

        // close file streams
        br.close();
        bw.close();
    }


    private static String HtmlFooter() {
        String out;
        out = "</TABLE></DIV></HTML>";

        return out;
    }

    private static void printErrorMessageAndExit(String s) {
        System.err.println(s);
        System.err.println(helpMessage());
        System.exit(-1);
    }

    private static String helpMessage() {
        String helpString;
        helpString = "  -i inputFile -o outputFile \n";
        helpString += "  Options:\n";
        helpString += "    -s season (1516, 1617, ...)\n";
        helpString += "    -n \"Tournament Name\" \n";
        helpString += "    -c tournamentCode\n";


        helpString += "    -iA : convert from a MatchResultsAll file\n";

        helpString += "    -iC : input file in csv format\n";
        helpString += "    -iT : input file in txt format (matches.txt)\n";

        helpString += "    -iM : input file has multiple events\n";


        helpString += "    -oN : output event names\n";

        helpString += "    -or : convert to a Rankings.html file\n";
        helpString += "    -oR : convert to a MatchResults file\n";
        helpString += "    -R1 : output MatchResults 1 per line\n";
        helpString += "    -oD : convert to a MatchResultsDetails file\n";

//        helpString += "    -oSR : convert to a StatsResults file\n";
        helpString += "    -oSD : convert to a StatsDetails file\n";

        helpString += "    -oC : output files in csv format\n";
        helpString += "    -oH : output files in HTML format\n";


        return helpString;
    }

}

