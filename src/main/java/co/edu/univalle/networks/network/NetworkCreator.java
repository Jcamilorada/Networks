package co.edu.univalle.networks.network;

import co.edu.univalle.networks.processing.PlayerGameData;
import co.edu.univalle.networks.processing.GameData;

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
    private Map<String, Integer> nodeCounts;
    private Map<String, List<String>> nodeInstances;
    private List<String> links;
    private Map<String, PlayerInformation> playerInformationMap;

    /**
     * Create the network representation of the given list.
     *
     * @param gameData specific game information
     *
     * @return the network representation of the given list.
     */
    public MatchNetworks createNetworks(GameData gameData)
    {
        return new MatchNetworks(
            getNetwork(gameData.getTeamA(), gameData.getMetadata().getTeamAName()),
            getNetwork(gameData.getTeamB(), gameData.getMetadata().getTeamBName()));
    }

    private Network getNetwork(List<PlayerGameData> playerGameDataList, String teamName)
    {
        nodeCounts = new HashMap<>();
        nodeInstances = new HashMap<>(15);
        links = new ArrayList<>(150);
        playerInformationMap = new HashMap<>(15);

        Map<Integer, String> playerIndexNameMap = getIndexPlayerNameMap(playerGameDataList);
        Set<String> nodes = new HashSet<>(50);

        for (PlayerGameData playerGameData : playerGameDataList)
        {
            String currentNode = playerGameData.getName();

            nodes.add(currentNode);
            playerInformationMap.put(currentNode, new PlayerInformation(playerGameData.getNumber(), teamName));

            List<Integer> passesInformation = playerGameData.getPassesInformation();
            int passesListSize = playerIndexNameMap.size();

            for (int i = 0; i < passesListSize; i++)
            {
                addLinks(getLink(currentNode, playerIndexNameMap.get(i)), passesInformation.get(i));
            }
        }

        return new Network(nodes, links, playerInformationMap);
    }

    private String getLink(String nodeA, String nodeB)
    {
        return String.format("\"%s\"->\"%s\"", nodeA, nodeB);
    }

    private void addInstance(String playerId, String nodeName)
    {
        List<String> instances = nodeInstances.containsKey(playerId) ?
            nodeInstances.get(playerId) : new ArrayList<String>(1);

        instances.add(nodeName);
        nodeInstances.put(playerId, instances);
    }

    private void addLinks(String link, int passesNumber)
    {
        for (int i = 0; i < passesNumber; i++)
        {
            links.add(link);
        }
    }

    private Map<Integer, String> getIndexPlayerNameMap(List<PlayerGameData> playerGameDataList)
    {
        Map<Integer, String> indexNumberMap = new HashMap<>(playerGameDataList.size());

        int index = 0;
        for (PlayerGameData playerGameData : playerGameDataList)
        {
            indexNumberMap.put(index, playerGameData.getName());
            index++;
        }

        return indexNumberMap;
    }
}
