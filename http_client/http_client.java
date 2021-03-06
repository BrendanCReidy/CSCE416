/*
By Brendan Reidy
9/27/2019
Gets html and header info given url
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class http_client {
    public static String outputFileName = "http_client_output";
    public static void main(String[] args) throws IOException {
        if(args.length<1){
            System.out.println("Usage: http_client <URL>");
        }else {
            String url = args[0];
            BufferedReader input = null;
            boolean cont = true;
            String finalURL;
            String redirectUrl = url;
            do{
                finalURL = redirectUrl;
                HttpURLConnection connection = (HttpURLConnection) new URL(redirectUrl).openConnection();
                connection.setInstanceFollowRedirects(false);
                connection.connect();
                connection.getInputStream();
                if(connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP || connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM)
                    redirectUrl = connection.getHeaderField("Location");
                else
                    cont = false;
            }while(cont);
            URL urlObject = new URL(finalURL);
            HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
            connection.setReadTimeout(10000);
            String output = "Printing HTTP header info from " + finalURL + "\n";
            Map<String, List<String>> map = connection.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : map.entrySet())
                output += entry.getKey() + ": " + entry.getValue() + "\n";

            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;

            while ((inputLine = input.readLine()) != null)
                output += inputLine + "\n";
            try (PrintWriter out = new PrintWriter(outputFileName)) {
                out.println(output);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (input != null) {
                input.close();
            }
        }
    }
}