/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steamgiftsbot;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author Vlastee
 */
public class BrowsersMethods {
    static CookieHandler handler;
    private static String phpsessid;
    private static Map<String, List<String>> cookieMapSteam;
    private static Map<String, List<String>> cookieMapSteamGifts;
    BrowsersMethods(){ 
        
        
        
    }
    
    public static void setCookies() throws IOException, URISyntaxException{
            //CookieHandler.setDefault(CookieHandler.getDefault());
            java.net.CookieHandler.getDefault().put(new URI("https://steamcommunity.com/"), cookieMapSteam);
            java.net.CookieHandler.getDefault().put(new URI("https://www.steamgifts.com/"), cookieMapSteamGifts);
            System.out.println("Cookies sated: "+cookieMapSteam.entrySet().toString()); 
    }
    
    
    static void setCookiesToMaps() throws IOException, URISyntaxException {
        
        handler = CookieHandler.getDefault();
        if (handler == null) {
            System.out.println("Failing due to lack of CookieHandler class!");
            return;
        }
        System.out.println("Using CookieHandler class: " + handler.getClass().getCanonicalName());

        setCookieMapSteam(handler.get(new URI("https://steamcommunity.com/"), new HashMap<>()));
        for (Map.Entry<String, List<String>> entry : getCookieMapSteam().entrySet()) {
            System.out.println("Iterating cookiemap Steam " + entry.getKey() + " => " + entry.getValue());
            if (entry.getKey().contains("Cookie")) {
                for (String cookie : entry.getValue()) {
                    System.out.println("Found cookie: " + cookie);
                }
            }
        }
        setCookieMapSteamGifts(handler.get(new URI("https://www.steamgifts.com/"), new HashMap<>()));
        for (Map.Entry<String, List<String>> entry : getCookieMapSteamGifts().entrySet()) {
            System.out.println("Iterating cookiemap SteamGifts " + entry.getKey() + " => " + entry.getValue());
            if (entry.getKey().contains("Cookie")) {
                for (String cookie : entry.getValue()) {
                    System.out.println("Found cookie: " + cookie);
                    if(cookie.contains("PHPSESSID")){
                        phpsessid = cookie.substring(0, cookie.indexOf("_ga=GA"));
                        System.out.println("varible phpsessid : "+phpsessid);
                    }
                }
            }
        }
    }
    
    public static void postMethod(String phpSessid, String code, String xsrfToken, String refer) throws IOException{
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost("https://www.steamgifts.com/ajax.php");

        // Request parameters and other properties.
        List<NameValuePair> params = new ArrayList<NameValuePair>(4);
        params.add(new BasicNameValuePair("Cookie", phpSessid));
        params.add(new BasicNameValuePair("Refer", refer));
        params.add(new BasicNameValuePair("code", code));
        params.add(new BasicNameValuePair("xsrfToken", xsrfToken));
        params.add(new BasicNameValuePair("do", "entry_insert"));
        
        try {
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(BrowsersMethods.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Execute and get the response.
        HttpResponse response = null;
        try {
            response = httpclient.execute(httppost);
        } catch (IOException ex) {
            Logger.getLogger(BrowsersMethods.class.getName()).log(Level.SEVERE, null, ex);
        }
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            try (InputStream instream = entity.getContent()) {
                // do something useful
            }
        }
    }
    
    /**
     * @return the phsessid
     */
    public static String getPhpsessid() {
        return phpsessid;
    }

    /**
     * @return the cookieMapSteam
     */
    public static Map<String, List<String>> getCookieMapSteam() {
        return cookieMapSteam;
    }

    /**
     * @param aCookieMapSteam the cookieMapSteam to set
     */
    public static void setCookieMapSteam(Map<String, List<String>> aCookieMapSteam) {
        cookieMapSteam = aCookieMapSteam;
    }

    /**
     * @return the cookieMapSteamGifts
     */
    public static Map<String, List<String>> getCookieMapSteamGifts() {
        return cookieMapSteamGifts;
    }

    /**
     * @param aCookieMapSteamGifts the cookieMapSteamGifts to set
     */
    public static void setCookieMapSteamGifts(Map<String, List<String>> aCookieMapSteamGifts) {
        cookieMapSteamGifts = aCookieMapSteamGifts;
    }
    
}
