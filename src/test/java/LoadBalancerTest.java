import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoadBalancerTest {
    private static LoadBalancer loadBalancer = LoadBalancer.getInstance();
    private final static Logger logger = Logger.getLogger(LoadBalancerTest.class);

    @BeforeClass
    public static void init()
    {
        logger.debug("Generate urls for testing");
        RandomUrls.generateUrls();
        logger.debug("Start Clustering Of the Urls");
        loadBalancer.handleRequest();
    }

    @Test
    public void handleRequestTest()
    {
        logger.debug("Start loading requests From All Cluster");
        List<String> beforeFirstRequestFile = loadRequests(Configuration.FIRST_DIRECTORY.concat(Configuration.REQUESTS_FILE_NAME));
        List<String> beforeSecondRequestFile = loadRequests(Configuration.SECOND_DIRECTORY.concat(Configuration.REQUESTS_FILE_NAME));
        List<String> beforeThirdRequestFile = loadRequests(Configuration.THIRD_DIRECTORY.concat(Configuration.REQUESTS_FILE_NAME));
        logger.debug("ReAgain Clustering Of the Urls");
        loadBalancer.handleRequest();
        List<String> newFirstRequestFile = loadRequests(Configuration.FIRST_DIRECTORY.concat(Configuration.REQUESTS_FILE_NAME));
        List<String> newSecondRequestFile = loadRequests(Configuration.SECOND_DIRECTORY.concat(Configuration.REQUESTS_FILE_NAME));
        List<String> newThirdRequestFile = loadRequests(Configuration.THIRD_DIRECTORY.concat(Configuration.REQUESTS_FILE_NAME));
        logger.debug("Compare results together");
        assertEquals("Compare First Cluster",beforeFirstRequestFile,newFirstRequestFile);
        assertEquals("Compare Second Cluster",beforeSecondRequestFile,newSecondRequestFile);
        assertEquals("Compare Third Cluster",beforeThirdRequestFile,newThirdRequestFile);
    }

    @Test
    public void checkMistakeLessThanEps(){
        logger.debug("Strat loading Request File From All Cluster");
        List<String> firstRequestFile = loadRequests(Configuration.FIRST_DIRECTORY.concat(Configuration.REQUESTS_FILE_NAME));
        List<String> secondRequestFile = loadRequests(Configuration.SECOND_DIRECTORY.concat(Configuration.REQUESTS_FILE_NAME));
        List<String> thirdRequestFile = loadRequests(Configuration.THIRD_DIRECTORY.concat(Configuration.REQUESTS_FILE_NAME));
        double clusterSize = Configuration.COUNT_REQUESTS/3 ;
        double firstMistake = Math.abs(clusterSize -firstRequestFile.size())/clusterSize;
        double secondMistake = Math.abs(clusterSize -secondRequestFile.size())/clusterSize;
        double thirdMistake = Math.abs(clusterSize -thirdRequestFile.size())/clusterSize;
        double averageMistake = (firstMistake+secondMistake+thirdMistake)/3*100;
        assertTrue("Check Average Mistake Less Than EPS", averageMistake < Configuration.EPS_ERROR);
    }

    private List<String> loadRequests(String address)
    {
        List<String> requests = new ArrayList<String>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    address));
            String line = reader.readLine();
            logger.debug("Start Loading Request From".concat(address).concat(" ,Cluster"));
            while (line != null) {
                requests.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Could not Loading Request From :".concat(address).concat(" ,Cluster"));
        }
        return requests;
    }
}
