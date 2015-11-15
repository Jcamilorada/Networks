package co.edu.univalle.networks.processing;

import co.edu.univalle.networks.folder.FilesPair;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * File processor class. Has utility methods to read file content with game plays.
 *
 * @author Juan Camilo Rada
 */
public class FileProcessor
{
    private static final String NUMBER_WITH_SPACES_REGEX = "([0-9]+)\\s([0-9]+)";
    private static final String FIRST_COLUMN_WORD = "From";
    private static final String LAST_COLUMN_WORD = "Total passes received";
    public static final String IS_NUMBER_REGEX = "[0-9]";
    public static final String IS_NOT_NUMBER_REGEX = "[^0-9]";

    public static final String JUST_CARACTER_REGEX = "[^a-zA-Z-]+";

    public static final String FILE_STRING_SEPARATOR = ",";

    /**
     * Read the resource file and generated a list with the play of the game.
     *
     * @return a List of plays that are represented using list.
     */
    public GameData readPlays(FilesPair filesPair)
    {
        File passesDataFile = getFile(filesPair.getPassesFile());
        File metadataFile = getFile(filesPair.getMetadataFile());

        GameData gameData = loadTeamsData(passesDataFile);
        Metadata metadata = loadMetadata(metadataFile);

        gameData.setMetadata(metadata);

        return gameData;
    }

    private File getFile(String fileName)
    {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        return file;
    }

    private Metadata loadMetadata(File metadataFile)
    {
        List<String> fileLines = new ArrayList<>();

        try (Scanner scanner = new Scanner(metadataFile))
        {
            while (scanner.hasNextLine())
            {
                fileLines.add(scanner.nextLine());
            }
        }

        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        String[] teamLine = getTeamsArray(fileLines.get(2));
        // Some files include an extra line
        if (teamLine.length < 2)
        {
            teamLine = getTeamsArray(fileLines.get(3));
        }

        String teamAName = teamLine[0];
        String teamBName = teamLine[1];

        return new Metadata(teamAName, teamBName);
    }

    private String[] getTeamsArray(String line)
    {
        String teamsLine = line.replaceAll(IS_NUMBER_REGEX, "");
        teamsLine = teamsLine.replaceAll(JUST_CARACTER_REGEX, "").trim();
        teamsLine = teamsLine.replaceAll("AET", "");
        teamsLine = teamsLine.replaceAll("PSO", "");

        return teamsLine.split("-");
    }

    private GameData loadTeamsData(File file)
    {
        List<PlayerGameData> teamA = new ArrayList<>(15);
        List<PlayerGameData> teamB = new ArrayList<>(15);

        try (Scanner scanner = new Scanner(file))
        {
            ProcesingMode procesingMode = ProcesingMode.SearchingFirstTeam;

            while (scanner.hasNextLine())
            {
                String lineValue = scanner.nextLine();
                String firstLineWord = lineValue.split(FILE_STRING_SEPARATOR)[0].trim();

                if (procesingMode == ProcesingMode.SecondTeam && firstLineWord.equals(LAST_COLUMN_WORD))
                {
                    break;
                }

                else if ((procesingMode == ProcesingMode.SearchingFirstTeam ||
                    procesingMode == ProcesingMode.SearchingSecondTeam ) &&
                    firstLineWord.equals(FIRST_COLUMN_WORD))
                {
                    procesingMode = procesingMode == ProcesingMode.SearchingFirstTeam ?
                        ProcesingMode.FirstTeam : ProcesingMode.SecondTeam;
                }

                else if (procesingMode == ProcesingMode.FirstTeam && firstLineWord.equals(LAST_COLUMN_WORD))
                {
                    procesingMode = ProcesingMode.SearchingSecondTeam;
                }

                else if (procesingMode == ProcesingMode.FirstTeam)
                {
                    PlayerGameData playerGameData = processLine(lineValue);
                    teamA.add(playerGameData);
                }

                else if (procesingMode == ProcesingMode.SecondTeam)
                {
                    PlayerGameData playerGameData = processLine(lineValue);
                    teamB.add(playerGameData);
                }
            }

            scanner.close();

        }

        catch (IOException e)
        {
            e.printStackTrace();
        }

        return new GameData(teamA, teamB);
    }

    private PlayerGameData processLine(String line)
    {
        List<String> lineValues = Arrays.asList(line.split(FILE_STRING_SEPARATOR));

        String playerSectionInfo = lineValues.get(0).trim();
        String playerName = WordUtils.capitalizeFully(playerSectionInfo.replaceAll(IS_NUMBER_REGEX, ""));
        int playerNumber = Integer.parseInt(playerSectionInfo.replaceAll(IS_NOT_NUMBER_REGEX, ""));

        List<String> passesData = lineValues.subList(2, lineValues.size());
        List<Integer> playerData = new ArrayList<>(passesData.size());

        for (String value : passesData)
        {
            String fixedValue = getFixedValue(value);


            if (isNumberWithSpaces(fixedValue))
            {
                break;
            }

            playerData.add(Integer.parseInt(fixedValue));
        }

        return new PlayerGameData(playerName.trim(), playerNumber, playerData);
    }

    private boolean isNumberWithSpaces(String value)
    {
        return value.matches(NUMBER_WITH_SPACES_REGEX);
    }

    private String getFixedValue(String value)
    {
        return value.replaceAll("\"\"", "0").trim();
    }
}
