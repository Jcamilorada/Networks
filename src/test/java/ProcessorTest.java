import org.junit.Test;

import java.util.List;

public class ProcessorTest
{
    private FileProcessor fileProcessor = new FileProcessor();
    private NetworkCreator networkCreator = new NetworkCreator();

    @Test
    public void generateCaliNetwork()
    {
        List<List<String>> plays =  fileProcessor.readPlays("Cali.txt");
        Network network = networkCreator.createNetwork(plays, false, true);

        System.out.println(network.getRelations().toString());
    }

    @Test
    public void generateMadridNetwork()
    {
        List<List<String>> plays =  fileProcessor.readPlays("Cali.txt");
        Network network = networkCreator.createNetwork(plays, false, true);

        System.out.println(network.getRelations().toString());
    }
}