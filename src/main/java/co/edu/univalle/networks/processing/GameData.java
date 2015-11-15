package co.edu.univalle.networks.processing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Class that represent a game information.
 *
 * @author Juan Camilo Rada
 */
@Data @RequiredArgsConstructor
public class GameData
{
    private final List<PlayerGameData> teamA;
    private final List<PlayerGameData> teamB;
    private Metadata metadata;
}
