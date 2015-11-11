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
    /**
     * Read the resource file and generated a list with the play of the game.
     *
     * @return a List of plays that are represented using list.
     */
    public List<List<String>> readPlays(String fileName)
    {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        List<List<String>> result = new ArrayList<>(20);
        try (Scanner scanner = new Scanner(file))
        {

            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine().replaceAll("\\s","");
                result.add(Arrays.asList(line.split(",")));
            }

            scanner.close();

        }

        catch (IOException e)
        {
            e.printStackTrace();
        }

        return result;
    }
}
