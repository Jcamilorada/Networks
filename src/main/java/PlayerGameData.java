import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data @AllArgsConstructor
public class PlayerGameData
{
    private String name;
    private int number;

    private List<Integer> passesInformation;
}
