import org.apache.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

class RandomUrls {

    private final static Logger logger = Logger.getLogger(RandomUrls.class);
    private static FileWriter fileWriter;

    static void generateUrls() {
        createFile();
        generateRandomUrls();
        closeFile();
    }

    // function to generate a random string of length n
    private static String getAlphaNumericString(int n)
    {
        // chose a Character random from this String
        //warning ! ! ! Url have more Character
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return "/"+sb.toString();
    }

    private static void generateRandomUrls()
    {
        for (int i = 0; i<Configuration.COUNT_REQUESTS; i++)
        {
            URL url = null;
            try {
                url = new URL("https", Configuration.DOMAIN, getAlphaNumericString(Configuration.AVERAGE_URL_LENGTH));
            } catch (MalformedURLException e) {
                e.printStackTrace();
                logger.error("could not create url");
            }
            assert url != null;
            String output = url.toExternalForm();
            addNewUrl(output);
            logger.debug("Generate Random Url :"+output);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void createFile()
    {
        FileWriter f0;
        try {
            f0 = new FileWriter(Configuration.FILE_LOCATION);
            fileWriter = f0;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addNewUrl(String url)
    {
        String newLine = System.getProperty("line.separator");
        try {
            fileWriter.write(url+newLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void closeFile()
    {
        try {
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
