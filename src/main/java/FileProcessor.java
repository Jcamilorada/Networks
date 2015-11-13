
import org.apache.commons.lang.WordUtils;

import java.io.File;
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

    /**
     * Read the resource file and generated a list with the play of the game.
     *
     * @return a List of plays that are represented using list.
     */
    public GameData readPlays(String fileName)
    {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());


        List<PlayerGameData> teamA = new ArrayList<>(15);
        List<PlayerGameData> teamB = new ArrayList<>(15);

        try (Scanner scanner = new Scanner(file))
        {
            ProcesingMode procesingMode = ProcesingMode.SearchingFirstTeam;

            while (scanner.hasNextLine())
            {
                String lineValue = scanner.nextLine();
                String firstLineWord = lineValue.split(",")[0].trim();

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
        List<String> lineValues = Arrays.asList(line.split(","));

        String playerName = WordUtils.capitalizeFully(lineValues.get(0).replaceAll("[0-9]","").trim());
        int playerNumber = Integer.parseInt(lineValues.get(0).replaceAll("[^0-9]","").trim());

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

        return new PlayerGameData(playerName, playerNumber, playerData);
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
