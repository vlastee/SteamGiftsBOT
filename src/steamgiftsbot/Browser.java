/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steamgiftsbot;

import java.awt.Composite;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Vlastee
 */
public class Browser {
    
    FileProcessing fileProc;
    
    private Stage stage;
    private Double stageHeight;
    private Double stageWidth;
    private Double locationX;
    private Double locationY;
    WebView browser;
    Map<String, List<String>> headers;
    
    public void runBrowser() {
        
        fileProc = Options.fileProcessing;
        
        stageHeight = 500.0;
        stageWidth = 800.0;
        
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.prefHeightProperty().set(480.0);
        anchorPane.prefWidthProperty().set(640.0);
            
            
        browser = new WebView();
        browser.prefHeightProperty().set(454.0);
        browser.prefWidthProperty().set(640.0);
        AnchorPane.setBottomAnchor(browser, 26.0);
        AnchorPane.setTopAnchor(browser, 0.0);
        AnchorPane.setLeftAnchor(browser, 0.0);
        AnchorPane.setRightAnchor(browser, 0.0);
        
        //Set CookieManager
        //CookieHandler.setDefault(BrowsersCookieManager.getCookieManager());
            
        TextField webLink = new TextField();
        webLink.setText("https://www.steamgifts.com");
        webLink.layoutYProperty().set(374.0);
        webLink.prefHeightProperty().set(25.0);
        webLink.prefWidthProperty().set(573.0);
        AnchorPane.setBottomAnchor(webLink, 0.0);
        AnchorPane.setLeftAnchor(webLink, 0.0);
        AnchorPane.setRightAnchor(webLink, 246.0);


        Button webButtonGo = new Button();
        webButtonGo.layoutXProperty().set(536.0);
        webButtonGo.layoutYProperty().set(362.0);
        webButtonGo.mnemonicParsingProperty().set(false);
        webButtonGo.prefHeightProperty().set(25.0);
        webButtonGo.prefWidthProperty().set(63.0);
        webButtonGo.setText("Go");
        webButtonGo.onMouseClickedProperty().set(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                browser.getEngine().load(webLink.getText());
               
            }
        });
        AnchorPane.setBottomAnchor(webButtonGo, 0.0);
        AnchorPane.setRightAnchor(webButtonGo, 0.0);
        
        Button webButtonSaveCookie = new Button();
        webButtonSaveCookie.layoutXProperty().set(536.0);
        webButtonSaveCookie.layoutYProperty().set(362.0);
        webButtonSaveCookie.mnemonicParsingProperty().set(false);
        webButtonSaveCookie.prefHeightProperty().set(25.0);
        webButtonSaveCookie.prefWidthProperty().set(90.0);
        webButtonSaveCookie.setText("Save Cookie");
        webButtonSaveCookie.onMouseClickedProperty().set(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                browser.getEngine().load(webLink.getText());
                FileProcessing.saveCookies();
            }
        });
        AnchorPane.setBottomAnchor(webButtonSaveCookie, 0.0);
        AnchorPane.setRightAnchor(webButtonSaveCookie, 64.0);
        
        Button webButtonLoadCookie = new Button();
        webButtonLoadCookie.layoutXProperty().set(536.0);
        webButtonLoadCookie.layoutYProperty().set(362.0);
        webButtonLoadCookie.mnemonicParsingProperty().set(false);
        webButtonLoadCookie.prefHeightProperty().set(25.0);
        webButtonLoadCookie.prefWidthProperty().set(90.0);
        webButtonLoadCookie.setText("Load Cookie");
        webButtonLoadCookie.onMouseClickedProperty().set(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                
                FileProcessing.openCookies();
                browser.getEngine().reload();
            }
        });
        AnchorPane.setBottomAnchor(webButtonLoadCookie, 0.0);
        AnchorPane.setRightAnchor(webButtonLoadCookie, 155.0);

        anchorPane.getChildren().addAll(browser, webLink, webButtonGo, webButtonSaveCookie, webButtonLoadCookie);
        Scene scene = new Scene(anchorPane);

        stage = new Stage();
        stage.setTitle("Browser");
        stage.setScene(scene);
        stage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, event -> {
            event.consume();
        });
        locationX = 0.0;
        locationY = 0.0;
            
       
    }
   
    
    
    
    public void showBrowser(){
        satSizeAndLocation();
        stage.show();
    }
    
    public void hideBrowser(){
        saveSizeAndLocation();
        stage.hide();
    }
    
    public void saveSizeAndLocation(){
        locationX = stage.getX();
        locationY = stage.getY();
        stageHeight = stage.getHeight();
        stageWidth = stage.getWidth();
    }
    
    public void satSizeAndLocation(){
        stage.setHeight(stageHeight);
        stage.setWidth(stageWidth);
        stage.setX(locationX);
        stage.setY(locationY);
    }
   
    
}
    

