/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steamgiftsbot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import static java.nio.file.StandardOpenOption.APPEND;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static steamgiftsbot.BrowsersMethods.getCookieMapSteam;

/**
 *
 * @author Vlastee
 */
public class FileProcessing implements Runnable{
    
    String path;
    File selectedGamesFile;
    File tempFile;
    File allGamesFile;
    static File cookiesSteamGifts;
    static File cookiesSteam;
    File cookiesTempFile;
    String cookiesFromBrowser;
    
    public void stringToFile(String string, String filename) throws FileNotFoundException{
        try (PrintWriter out = new PrintWriter(path+File.separator+filename+".txt")) {
        out.println(string);
        }
    }
    
    /**
     * If game don't exists in selectedGames.txt
     * adds game to selectedGames.txt file and returns
     * false, otherwise not making any changes to file and returns true. 
     */
    public Boolean addGameToSelectedGamesFile(String picture, String title) throws FileNotFoundException, UnsupportedEncodingException, IOException{
        //checkFile(selectedGamesFile);
        Boolean isGameInFile = false;
        Path path = Paths.get(selectedGamesFile.toURI());
        try (BufferedReader reader = Files.newBufferedReader(path);) {
            String currentLine;
            while((currentLine = reader.readLine()) != null) {
                System.out.println("current line readed and = "+currentLine);
                if(currentLine.contains(title)){
                    isGameInFile = true;
                    System.out.println("current line contains title so isGameInFile = "+isGameInFile);
                }
            }
        }
        if(isGameInFile==false){
            try(FileWriter fw = new FileWriter(selectedGamesFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)){
            out.print(picture+", ");
            out.println(title+";");
            fw.flush();
            bw.flush();
            bw.close();
            } catch (IOException e) {
                Logger.getLogger(Options.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return isGameInFile;
    }
    
    
    /**
     * If game don't exists in allGames.txt
     * adds game to allGames.txt file.
     */
    public void addGameToAllGamesFile(String picture, String title) throws FileNotFoundException, UnsupportedEncodingException, IOException{
        //checkFile(allGamesFile);
        Boolean isGameInFile = false; 
        Path allGameFilePath = Paths.get(allGamesFile.toURI());
        try (BufferedReader reader = Files.newBufferedReader(allGameFilePath);) {
            String currentLine;
            while((currentLine = reader.readLine()) != null) {
                System.out.println("current line readed and = "+currentLine);
                if(currentLine.contains(title)){
                    isGameInFile = true;
                    System.out.println("current line contains title so isGameInFile = "+isGameInFile);
                }
            }
        }
        if(isGameInFile==false){
            try(FileWriter fw = new FileWriter(allGamesFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)){
            out.print(picture+", ");
            out.println(title+";");
            fw.flush();
            fw.close();
            bw.flush();
            bw.close();
            } catch (IOException e) {
                Logger.getLogger(Options.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
    
    
    
    public static void saveCookies(){
      
        try (FileOutputStream fileOutGifts = new FileOutputStream(cookiesSteamGifts); 
                ObjectOutputStream outGifts = new ObjectOutputStream(fileOutGifts);
                FileOutputStream fileOutSteam = new FileOutputStream(cookiesSteam); 
                ObjectOutputStream outSteam = new ObjectOutputStream(fileOutSteam)) {
            BrowsersMethods.setCookiesToMaps();
            outGifts.writeObject(BrowsersMethods.getCookieMapSteam());
            outSteam.writeObject(BrowsersMethods.getCookieMapSteamGifts());
            System.out.println("Cookies saved");
        } catch (IOException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(FileProcessing.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Serialized data is saved in "+cookiesSteamGifts.toString());
        System.out.println("Serialized data is saved in "+cookiesSteam.toString());
        
    }
    
    
    
    public static void openCookies(){
        try (FileInputStream fileInGifts = new FileInputStream(cookiesSteamGifts); 
                ObjectInputStream inGifts = new ObjectInputStream(fileInGifts);
                FileInputStream fileInSteam = new FileInputStream(cookiesSteam); 
                ObjectInputStream inSteam = new ObjectInputStream(fileInSteam)) {
            BrowsersMethods.setCookieMapSteamGifts((Map<String, List<String>>) inGifts.readObject());
            BrowsersMethods.setCookieMapSteam((Map<String, List<String>>) inSteam.readObject());
            System.out.println("Cookies opened");
            
            if(getCookieMapSteam()!=null && getCookieMapSteam().isEmpty()){
                System.out.println("cookiemap Steam is empty");
            }else{
                for (Map.Entry<String, List<String>> entry : getCookieMapSteam().entrySet()) {
                    System.out.println("Iterating fresh opened cookiemap Steam " + entry.getKey() + " => " + entry.getValue());
                    if (entry.getKey().contains("Cookie")) {
                        for (String cookie : entry.getValue()) {
                            System.out.println("Found cookie: " + cookie);
                        }
                    }
                    try {
                        BrowsersMethods.setCookies();
                    } catch (URISyntaxException ex) {
                        Logger.getLogger(FileProcessing.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            
            
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
            return;
        } catch (ClassNotFoundException ex) {
            System.out.println("Map class not found");
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        
        
    }
    
    
    public String[] selectedGamesFileToStringArray() throws FileNotFoundException, IOException{
        StringBuilder selectedGamesStringBuffer = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(selectedGamesFile));){
            
            String currentLine;
            
            while((currentLine = reader.readLine()) != null) {
                selectedGamesStringBuffer.append(currentLine);
            }
        }
        String[] selectedGames = selectedGamesStringBuffer.toString().split(";");
        return selectedGames;
    }
    
    public String[] allGamesFileToStringArray() throws FileNotFoundException, IOException{
        StringBuilder allGamesStringBuffer = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(allGamesFile));){
            
            String currentLine;
            
            while((currentLine = reader.readLine()) != null) {
                allGamesStringBuffer.append(currentLine);
            }
        }
        String[] selectedGames = allGamesStringBuffer.toString().split(";");
        return selectedGames;
    }
    
    public void removeGameFromSelectedGamesFile(String picture, String title) throws FileNotFoundException, UnsupportedEncodingException, IOException{
        //checkFile(selectedGamesFile);
        tempFile = new File(path+ File.separator +"myTempFile.txt");
        tempFile.getParentFile().mkdirs();
        tempFile.createNewFile();

        try (BufferedReader reader = new BufferedReader(new FileReader(selectedGamesFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            ) {
            
            String currentLine;
            
            while((currentLine = reader.readLine()) != null) {
                if(!currentLine.contains(title)){
                    writer.write(currentLine + System.getProperty("line.separator"));
                }
                
            }
            writer.close();
            
            
        }catch(IOException e){
            Logger.getLogger(Options.class.getName()).log(Level.SEVERE, null, e);
        }
        renameFile(tempFile,selectedGamesFile);
    }
    
    public static void renameFile(File srcFile, File destFile) throws IOException {
        boolean bSucceeded = false;
        try {
            if (destFile.exists()) {
                if (!destFile.delete()) {
                    throw new IOException(srcFile + " was not successfully renamed to " + destFile); 
                }
            }
            if (!srcFile.renameTo(destFile))        {
                throw new IOException(srcFile + " was not successfully renamed to " + destFile);
            } else {
                    bSucceeded = true;
            }
        } finally {
              if (bSucceeded) {
                    srcFile.delete();
              }
        }
    }
    
    public void createGamesFiles(){
        selectedGamesFile = new File(path+ File.separator + "selectedGames.txt");
        allGamesFile = new File(path+ File.separator + "allGames.txt");
        System.out.println("steamgiftsbot.FileProcessing.run()");
        
        try {
            if(!selectedGamesFile.exists()){
                selectedGamesFile.getParentFile().mkdirs();
                selectedGamesFile.createNewFile();
                System.out.println(selectedGamesFile.toString()+" : Created from FileProcessing");
            }
            if(!allGamesFile.exists()){
                allGamesFile.getParentFile().mkdirs();
                allGamesFile.createNewFile(); 
                System.out.println(allGamesFile.toString()+" : Created from FileProcessing");
            }
            
        } catch (IOException ex) {
            Logger.getLogger(FileProcessing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void createCookies(){
        cookiesSteamGifts = new File(path+ File.separator + "cookiesSteamGifts.ser");
        cookiesSteam = new File(path+ File.separator + "cookiesSteam.ser");
        try {
            if(!cookiesSteamGifts.exists()){
                cookiesSteamGifts.getParentFile().mkdirs();
                cookiesSteamGifts.createNewFile();
                System.out.println(cookiesSteamGifts.toString()+" : Created from FileProcessing");
            }
            if(!cookiesSteam.exists()){
                cookiesSteam.getParentFile().mkdirs();
                cookiesSteam.createNewFile();
                System.out.println(cookiesSteam.toString()+" : Created from FileProcessing");
            }
        } catch (IOException ex) {
            Logger.getLogger(FileProcessing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
   
    
  
    
    

    @Override
    public void run() {
        path = new File("Files").getAbsolutePath();
    }

    
}
