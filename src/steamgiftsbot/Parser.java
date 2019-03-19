/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steamgiftsbot;

import com.sun.webkit.dom.HTMLDivElementImpl;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.scene.web.WebEngine;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLInputElement;


/**
 *
 * @author Vlastee
 */
public class Parser{

    /**
     * @return the userPoints
     */
    public String getUserPoints() {
        return userPoints;
    }

    /**
     * @return the userAvatar
     */
    public String getUserAvatar() {
        return userAvatar;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }
    
    private WebEngine parseEngine;
    private String userPoints;
    private String userAvatar;
    private String userName;
    HTMLFormElement participateForm;
    HTMLInputElement divClickElement1;
    FileProcessing fileProcessingForGames = Options.fileProcessing;
    FileProcessing fileProcessingForParce = new FileProcessing();
    
    
    
    
    public void getUserInfo() {
        try{
            Browser browserClass = new Browser();
            
           
            browserClass.runBrowser();
            parseEngine = browserClass.browser.getEngine();
            parseEngine.load("https://www.steamgifts.com/");
            parseEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
                    @Override public void changed(ObservableValue ov, State oldState, State newState) {

                        if (newState == Worker.State.SUCCEEDED) {

                          org.jsoup.nodes.Document readyforParcing = Jsoup.parse(convertDocToSring());
                          Elements userInfo = readyforParcing.select("div.nav__right-container");
                            for (Element src : userInfo) {
                                userAvatar = src.select(".nav__avatar-inner-wrap").attr("style").toString();
                                userAvatar = getUserAvatar().substring(21, getUserAvatar().length()-2);
                                System.out.println(getUserAvatar());
                                userPoints = src.select("span.nav__points").text().toString();
                                userName = src.select(".nav__avatar-outer-wrap").attr("href").toString().substring(6);
                                System.out.println(getUserName());
                                System.out.println("User: "+getUserName()+", Points: "+getUserPoints()+", avatar link: "+getUserAvatar());

                                //Update GUI here
                                SteamGiftsBot.setUserName(getUserName());
                                SteamGiftsBot.setInstructionTestDisable(true);
                                SteamGiftsBot.setUserAvatar(getUserAvatar());
                                SteamGiftsBot.setUserPoints("Points: "+getUserPoints());
                                
                                //TEST
                                //participateInGiveaway();
                                //String cookie = (String)parseEngine.executeScript("document.cookie;");
                                //System.out.println(cookie);
                                
                            }
                            
                        }       
                    }
                }   
            );
        }catch(StringIndexOutOfBoundsException ex){
            //Show a small notification for user: Do not forget to login to SteamGifts.com first!!!
        }
    }
    
    
    
    public void getAllGames() {
       
        try{
            
            Platform.runLater(new Runnable() {
                public void run() {
                    Browser browserClass = new Browser();
                   
                    browserClass.runBrowser();
                    parseEngine = browserClass.browser.getEngine();
                    for(int page = 1; page<=16; page++){
                        parseEngine.load("https://www.steamgifts.com/giveaways/search?page="+page);
                        parseEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
                            @Override public void changed(ObservableValue ov, State oldState, State newState) {

                                if (newState == Worker.State.SUCCEEDED) {

                                  org.jsoup.nodes.Document readyforParcing = Jsoup.parse(convertDocToSring());
                                  Elements giveawayElement = readyforParcing.select("div.giveaway__row-outer-wrap");
                                    for (Element src : giveawayElement) {
                                        String gameTitle = src.select("a.giveaway__heading__name").text();
                                        //gameTitle = getUserAvatar().substring(21, getUserAvatar().length()-2);
                                        System.out.println(gameTitle);
                                        String gamePicture = src.select("a.giveaway_image_thumbnail").attr("style").toString().substring(21);
                                        gamePicture = gamePicture.substring(0, gamePicture.length()-2);
                                        System.out.println(gamePicture);
                                        //System.out.println("User: "+getUserName()+", Points: "+getUserPoints()+", avatar link: "+getUserAvatar());
                                        try {
                                            //Update File here
                                            fileProcessingForGames.addGameToAllGamesFile(gamePicture, gameTitle);
                                        } catch (UnsupportedEncodingException ex) {
                                            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
                                        } catch (IOException ex) {
                                            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }       
                                }       
                            }
                        });
                    }
                }
            });
            
        }catch(StringIndexOutOfBoundsException ex){
            //Show a small notification for user: Do not forget to login to SteamGifts.com first!!!
        }
    }
    
    
    public void participateInGiveaway(){
        Browser browserClass = new Browser();
       
        browserClass.runBrowser();
        
        parseEngine = browserClass.browser.getEngine();
        
        
    
        parseEngine.load("https://www.steamgifts.com/giveaway/rKusu/metal-gear-solid-v-ground-zeroes");
            parseEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
                @Override public void changed(ObservableValue ov, State oldState, State newState) {
                    if (newState == Worker.State.SUCCEEDED) {
                        
                    
                       
                        org.jsoup.nodes.Document readyforParcing = Jsoup.parse(convertDocToSring());
                        Elements giveawayElement = readyforParcing.select("div.widget-container");
                        for (Element src : giveawayElement) {
                            String gameEntryCode = src.toString();
                            //System.out.println("ELEMENT: "+gameEntryCode);
                            if(gameEntryCode.contains("type=\"hidden\"")){
                                System.out.println("gameEntryCode : "+gameEntryCode);
                                String xsrfTokenAndCode = gameEntryCode.substring(
                                        gameEntryCode.indexOf("name=\"xsrf_token\" value="),
                                        gameEntryCode.indexOf("name=\"code\" value=")+109);
                                System.out.println("Founded Entry Giveaway : "+xsrfTokenAndCode);
                            }
                            
                        }
                        String xsrfToken = " ";
                        String refer = " ";
                        String code = "https://www.steamgifts.com/giveaway/rKusu/metal-gear-solid-v-ground-zeroes";
                        try {
                            BrowsersMethods.postMethod(BrowsersMethods.getPhpsessid(), code, xsrfToken, refer);
                        } catch (IOException ex) {
                            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                }
            });
    }
    
     public static void listAllAttributes(org.w3c.dom.Element element) {

	        System.out.println("List attributes for node: " + element.getNodeName());
	        // get a map containing the attributes of this node
	        NamedNodeMap attributes = element.getAttributes();
	        // get the number of nodes in this map
	        int numAttrs = attributes.getLength();
	        for (int i = 0; i < numAttrs; i++) {
	            Attr attr = (Attr) attributes.item(i);
	            String attrName = attr.getNodeName();
	            String attrValue = attr.getNodeValue();
	            System.out.println("Found attribute: " + attrName + " with value: " + attrValue);
	        }
     }
    
     
    /*
    public String convertDocToSring(){
        Document page = parseEngine.getDocument();
        DOMSource domSource = new DOMSource(page);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = null;
                  try {
                      transformer = tf.newTransformer();
                  } catch (TransformerConfigurationException ex) {
                      Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
                  }
                  try {
                      transformer.transform(domSource, result);
                  } catch (TransformerException ex) {
                      Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
                  }
        //System.out.println("XML IN String format is: \n" + writer.toString());
                  try {
                      fileProcessingForParce.run();
                      fileProcessingForParce.stringToFile(result.toString(),"TemporaryFile");
                  } catch (FileNotFoundException ex) {
                      Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
                  }
        return writer.toString();
        //System.out.println(result.toString());
    }
    */
    
    
    public String convertDocToSring(){
        

        String html = (String) parseEngine.executeScript("document.documentElement.outerHTML");


        return html;
        //System.out.println(result.toString());
    }              
              
    
}
