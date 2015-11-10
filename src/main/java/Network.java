import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Set;

/**
 * Class that represent a network information.
 *
 * @author Juan Camilo Rada
 */
@AllArgsConstructor @Getter
public class Network
{
    private Set<String> nodes;
    private List<String> relations;
}
