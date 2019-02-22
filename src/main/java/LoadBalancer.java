import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

class LoadBalancer {
    private final static Logger logger = Logger.getLogger(LoadBalancer.class);
    private static LoadBalancer loadBalancer;
    private List<String> urls;
    private FileWriter firstFile;
    private FileWriter secondFile;
    private FileWriter thirdFile;

    private LoadBalancer() {
        urls = new ArrayList<String>();
        loadUrls();
    }

    void handleRequest()
    {
        logger.debug("Start Handle Request");
        openFiles();
        for (String url : urls)
        {
                long urlMapTOInt = ELFHash(url);
                if (urlMapTOInt % 3 == 0)
                {
                    writeRequestINFile(firstFile,url);
                }
                else if (urlMapTOInt %3 ==1)
                {
                    writeRequestINFile(secondFile,url);
                }
                else {
                    writeRequestINFile(thirdFile,url);
                }
        }
        closeFile();
    }

    private void loadUrls()
    {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    Configuration.FILE_LOCATION));
            String line = reader.readLine();
            logger.debug("Reading The Url File");
            while (line != null) {
                urls.add(line);
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            logger.error("Could Not loading urls");
            e.printStackTrace();
        }
    }

    private void openFiles()
    {
        logger.debug("open the cluster file");
        try {
            firstFile = new FileWriter(Configuration.FIRST_DIRECTORY.concat(Configuration.REQUESTS_FILE_NAME));
            secondFile = new FileWriter(Configuration.SECOND_DIRECTORY.concat(Configuration.REQUESTS_FILE_NAME));
            thirdFile = new FileWriter(Configuration.THIRD_DIRECTORY.concat(Configuration.REQUESTS_FILE_NAME));

        } catch (IOException e) {
            logger.error("could not the open cluster file");
            e.printStackTrace();
        }
    }

    private void writeRequestINFile(FileWriter fileWriter,String request)
    {
        String newLine = System.getProperty("line.separator");
        try {
            logger.debug("write the url :".concat(request).concat("in cluster"));
            fileWriter.write(request+newLine);
        } catch (IOException e) {
            logger.error("could not write ");
            e.printStackTrace();
        }
    }

    static LoadBalancer getInstance(){
        if (loadBalancer == null)
            loadBalancer = new LoadBalancer();
        return loadBalancer;
    }

    private  void closeFile()
    {
        try {
            logger.debug("close all cluster file");
            firstFile.close();
            secondFile.close();
            thirdFile.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("could not the close file");
        }
    }

    private static long ELFHash(String str)
    {
        long hash = 0;
        long x    = 0;
        for(int i = 0; i < str.length(); i++)
        {
            hash = (hash << 4) + str.charAt(i);
            if((x = hash & 0xF0000000L) != 0)
            {
                hash ^= (x >> 24);
            }
            hash &= ~x;
        }
        return hash;
    }
}
