package encryption_api;

import java.net.URL;
import java.util.Scanner;
import java.net.HttpURLConnection;
import java.util.logging.Logger;

public class REST_API_Connect
{
    private String inline;
    private URL url;

    public REST_API_Connect()
    {
        inline = "";
    }

    /**
     * API supplied by https://rot47.net/
     * @param data
     * @param encryption_type
     * @return
     */
    public String encrypt(String data, int encryption_type)
    {
        Logger log = Logger.getLogger(REST_API_Connect.class.getName());
        inline = "";
        try
        {
            if(encryption_type == 1)
                url = new URL("https://str.justyy.workers.dev/rot47/?s=" + data);   // encrypt/decrypt rot47
            else if(encryption_type == 2)
                url = new URL("https://str.justyy.workers.dev/btoa/?s=" + data);    // encrypt to base64
            else
                url = new URL("https://str.justyy.workers.dev/atob/?s=" + data);    // decrypt from base64
            //Parse URL into HttpURLConnection in order to open the connection in order to get the JSON data
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            //Set the request to GET or POST as per the requirements
            conn.setRequestMethod("GET");
            log.info("Request Method Set: GET");
            //Use the connect method to create the connection bridge
            conn.connect();
            //Get the response status of the Rest API
            int responsecode = conn.getResponseCode();
            log.info("Response code: " + responsecode);
            //Iterating condition to if response code is not 200 then throw a runtime exception
            //else continue the actual process of getting the JSON data
            if(responsecode != 200)
            {
                log.severe("API Response Unsuccessful");
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            }
            else
            {
                //Scanner functionality will read the JSON data from the stream
                Scanner sc = new Scanner(url.openStream());
                while(sc.hasNext())
                {
                    inline += sc.nextLine();
                }
                inline = inline.replaceAll("^\"|\"$", "");
                System.out.println("\nJSON Response in String format");
                System.out.println(inline);
                //Close the stream when reading the data has been finished
                sc.close();
            }

            //Disconnect the HttpURLConnection stream
            conn.disconnect();
        }
        catch(Exception e)
        {
            log.severe("Connection Failed");
            System.out.println(e.toString());
        }

        return inline;
    }
}
