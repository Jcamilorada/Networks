package co.edu.univalle.networks;

import co.edu.univalle.networks.folder.FilesPair;
import co.edu.univalle.networks.folder.FolderProcessor;
import co.edu.univalle.networks.network.NetworkCreator;
import co.edu.univalle.networks.network.PlayerInformation;
import co.edu.univalle.networks.processing.FileProcessor;
import co.edu.univalle.networks.processing.GameData;
import co.edu.univalle.networks.network.MatchNetworks;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProcessorTest
{
    private FolderProcessor folderProcessor = new FolderProcessor();
    private FileProcessor fileProcessor = new FileProcessor();
    private NetworkCreator networkCreator = new NetworkCreator();

    @Test
    public void generateNetworksInformation() throws FileNotFoundException, UnsupportedEncodingException
    {
        FilesPair filesPair = new FilesPair("csv/body_01_0612_bra-cro_passingdistribution.csv", "csv/header_01_0612_bra-cro_passingdistribution.csv/");

        GameData data  = fileProcessor.readPlays(filesPair);
        MatchNetworks networks = networkCreator.createNetworks(data);

        System.out.println(networks.getNetworkTeamA().getRelations().toString());
        System.out.println("------------");
        System.out.println(networks.getNetworkTeamB().getRelations().toString());
        System.out.println("------------");
        System.out.println(data.getMetadata().getTeamAName());
        System.out.println(data.getMetadata().getTeamBName());

        PrintWriter writer = new PrintWriter("networks.txt", "UTF-8");
        writer.println(networks.getNetworkTeamA().getRelations().toString());
        writer.println("\n\n");
        writer.println(networks.getNetworkTeamB().getRelations().toString());
        writer.close();
    }

    @Test
    public void getFilesPairs() throws FileNotFoundException, UnsupportedEncodingException
    {
        List<FilesPair> filesPairs = folderProcessor.getFilesPairs("csv");

        int i = 1;
        for (FilesPair filesPair : filesPairs)
        {
            GameData data = fileProcessor.readPlays(filesPair);
            MatchNetworks networks = networkCreator.createNetworks(data);

            String fileName = String.format("networks/%d_%s.txt", i, data.getMetadata().getTeamAName());
            writeFile(fileName, networks.getNetworkTeamA().getRelations());
            writeMetaFile(fileName, networks.getNetworkTeamA().getPlayerInformationMap());

            fileName = String.format("networks/%d_%s.txt", i + 1, data.getMetadata().getTeamBName());
            writeFile(fileName, networks.getNetworkTeamB().getRelations());
            writeMetaFile(fileName, networks.getNetworkTeamA().getPlayerInformationMap());

            i = i + 2;
        }
    }

    private void writeMetaFile(String fileName, Map<String, PlayerInformation> playerInformationMap) throws FileNotFoundException, UnsupportedEncodingException
    {
        String relations = getStringRepresentation(playerInformationMap.entrySet());
        PrintWriter writer = new PrintWriter("metadata/" + fileName, "UTF-8");
        writer.println(relations);
        writer.close();
    }

    private void writeFile(String fileName, List<String> nodesRelations) throws FileNotFoundException, UnsupportedEncodingException
    {
        String relations = getStringRepresentation(nodesRelations);
        PrintWriter writer = new PrintWriter(fileName, "UTF-8");
        writer.println(relations);
        writer.close();
    }

    private String getStringRepresentation(Set<Map.Entry<String, PlayerInformation>> entries)
    {
        StringBuilder sb = new StringBuilder();

        String prefix = "";
        sb.append("data = { \n");
        for (Map.Entry<String, PlayerInformation> entry : entries)
        {
            sb.append(prefix);

            sb.append("<| ");
            sb.append(String.format("\"playerName\" -> \"%s\", ", entry.getKey()));
            sb.append(String.format("\"playerNumber\" -> %d, ", entry.getValue().getNumber()));
            sb.append(String.format("\"country\" -> \"%s\"", entry.getValue().getCountry()));
            sb.append(" |>");
            prefix = ",\n";
        }

        sb.append("\n};");

        return sb.toString();
    }

    private String getStringRepresentation(List<String> relations)
    {
        StringBuilder sb = new StringBuilder();

        sb.append("{ ");
        sb.append(relations.get(0));

        for (String relation : relations.subList(1, relations.size()))
        {
            sb.append(",");
            sb.append(relation);
        }

        sb.append(" }");

        return sb.toString();
    }
}
