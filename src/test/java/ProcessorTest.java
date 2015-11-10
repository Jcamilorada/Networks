import org.junit.Test;

import java.util.List;

public class ProcessorTest
{
    private FileProcessor fileProcessor = new FileProcessor();
    private NetworkCreator networkCreator = new NetworkCreator();

    @Test
    public void testReadPlays()
    {
        List<List<String>> plays =  fileProcessor.readPlays();
        Network network = networkCreator.createNetwork(plays, true);

        System.out.println(network.getNodes().toString());
        System.out.println(network.getRelations().toString());
    }
}