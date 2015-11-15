package co.edu.univalle.networks.folder;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class that process information folder.
 *
 * @author Juan Camilo Rada
 *
 */
public class FolderProcessor
{
    public List<FilesPair> getFilesPairs(String folderName)
    {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(folderName).getFile());

        List<String> filesNames = Arrays.asList(file.list());


        return getFilesPair(filesNames, folderName);
    }

    private List<FilesPair> getFilesPair(List<String> filesNames, String folderName)
    {
        int filesSize = filesNames.size() / 2;

        List<FilesPair> files = new ArrayList<>(filesSize);
        for (int i = 0; i < filesSize; i++)
        {
            files.add(new FilesPair(getFileName(filesNames.get(i)), getFileName(filesNames.get(filesSize + i))));
        }

        return files;
    }

    String getFileName(String fileName)
    {
        return String.format("%s/%s", "csv", fileName);
    }
}
