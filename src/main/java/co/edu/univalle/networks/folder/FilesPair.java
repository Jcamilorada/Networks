package co.edu.univalle.networks.folder;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Class tha represents the a pair of files. The metadata one and the match passes information.
 *
 * @author Juan Camilo Rada
 */
@Data @AllArgsConstructor
public class FilesPair
{
    private String passesFile;
    private String metadataFile;
}
