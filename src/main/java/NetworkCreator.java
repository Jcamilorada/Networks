import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class that process play list and create a network based on
 *
 * @author Juan Camilo Rada
 */
public class NetworkCreator
{
    private Map<String, Integer> nodeCounts = new HashMap<>();
    private Map<String, List<String>> nodeInstances = new HashMap<>(10);
    private List<String> links = new ArrayList<>(150);

    /**
     * Create the network representation of the given list.
     *
     * @param plays the list of plays.
     * @param linkSamePlayer boolean flag to indicate if links all players nodes with the main one.
     *
     * @return the network representation of the given list.
     */
    public Network createNetwork(List<List<String>> plays, boolean linkSamePlayer)
    {
        Set<String> nodes = new HashSet<>(50);

        for (List<String> play : plays)
        {
            String currentNode = increaseNode(play.get(0));

            nodes.add(currentNode);
            addInstance(play.get(0), currentNode);

            int playSize = play.size();
            for (String playerId : play.subList(1, playSize))
            {
                String nodeName = this.increaseNode(playerId);
                links.add(getLink(currentNode, nodeName));

                currentNode = nodeName;

                nodes.add(currentNode);
                addInstance(playerId, currentNode);
            }
        }

        // Links nodes of the same player
        if (linkSamePlayer)
        {
            for (Map.Entry<String, List<String>> entry : nodeInstances.entrySet())
            {
                String mainNodeName = String.format("p%s", entry.getKey());
                nodes.add(mainNodeName);

                for (String nodeName : entry.getValue())
                {
                    links.add(getLink(mainNodeName, nodeName));
                }
            }
        }

        return new Network(nodes, links);
    }

    public String increaseNode(String playerId)
    {
        int value = nodeCounts.containsKey(playerId) ? nodeCounts.get(playerId) + 1 : 1;
        nodeCounts.put(playerId, value);

        return String.format("p%s_%s", playerId, value);
    }

    public String getLink(String nodeA, String nodeB)
    {
        return String.format("\"%s\"->\"%s\"", nodeA, nodeB);
    }

    public void addInstance(String playerId, String nodeName)
    {
        List<String> instances = nodeInstances.containsKey(playerId) ?
            nodeInstances.get(playerId) : new ArrayList<String>(1);

        instances.add(nodeName);
        nodeInstances.put(playerId, instances);
    }
}
