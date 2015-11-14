package co.edu.univalle.networks;

import org.junit.Test;

public class ProcessorTest
{
    private FileProcessor fileProcessor = new FileProcessor();
    private NetworkCreator networkCreator = new NetworkCreator();

    @Test
    public void generateNetworksInformation()
    {
        GameData data  = fileProcessor.readPlays("body_example.csv");
        MatchNetworks networks = networkCreator.createNetworks(data);

        System.out.println(networks.getNetworkTeamA().getRelations().toString());
        System.out.println("------------");
        System.out.println(networks.getNetworkTeamB().getRelations().toString());
    }
}
