import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data @AllArgsConstructor
public class GameData
{
    private final List<PlayerGameData> teamA;
    private final List<PlayerGameData> teamB;
}
