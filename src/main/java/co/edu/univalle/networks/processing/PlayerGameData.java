package co.edu.univalle.networks.processing;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Class that represent an specific player information.
 *
 * @author Juan Camilo Rada
 */
@Data @AllArgsConstructor
public class PlayerGameData
{
    private String name;
    private int number;

    private List<Integer> passesInformation;
}
