package co.edu.univalle.networks;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Class that represent a game information.
 *
 * @author Juan Camilo Rada
 */
@Data @AllArgsConstructor
public class GameData
{
    private final List<PlayerGameData> teamA;
    private final List<PlayerGameData> teamB;
}
