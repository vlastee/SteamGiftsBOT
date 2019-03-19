/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steamgiftsbot;

import com.sun.javafx.geom.Shape;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Shadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 *
 * @author Vlastee
 */
public class Options{
    
    private double dragAnchorX;
    private double dragAnchorY;
    private Double stageHeight;
    private Double stageWidth;
    private Double locationX;
    private Double locationY;
    private Stage stageO;
    private Boolean backgroundColorAllGames;
    private Boolean backgroundColorSelectedGames;
    TabPane tabPane;
    VBox vBoxSelectedGames;
    VBox vBoxAllGames;
    public static FileProcessing fileProcessing;
    Parser parser;
    String[] allGamesStringArray;
    Map <String, String> imageTitleMap;
    Task getAllGamesToMapTask;
    
    Options() throws Exception{
        runOptions();
        
    }

    public void runOptions() throws Exception {
       fileProcessing = new FileProcessing(); 
       parser = new Parser();
       fileProcessing.run();
       backgroundColorAllGames = true;
       backgroundColorSelectedGames = false;
       locationX = 0.0;
       locationY = 0.0;
       stageHeight = 700.0;
       stageWidth = 700.0;
       stageO = new Stage();
       fileProcessing.createGamesFiles();
       fileProcessing.createCookies();
       
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.prefHeight(697);
        anchorPane.prefWidth(722);
        anchorPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
        
        Rectangle backgroundRectangle = new Rectangle();
        //final DoubleProperty doubleProp = new SimpleDoubleProperty(5.0);
        backgroundRectangle.arcHeightProperty().set(50.0);
        backgroundRectangle.arcWidthProperty().set(50.0);
        backgroundRectangle.fillProperty().setValue(Color.DODGERBLUE);
        backgroundRectangle.heightProperty().set(533.0);
        backgroundRectangle.widthProperty().set(600.0);
        backgroundRectangle.layoutXProperty().set(11.0);
        backgroundRectangle.layoutYProperty().set(150.0);
        backgroundRectangle.strokeProperty().set(Color.BLACK);
        backgroundRectangle.strokeTypeProperty().set(StrokeType.INSIDE);
        backgroundRectangle.strokeWidthProperty().set(0.0);
        backgroundRectangle.setStyle("-fx-arc-height: 50; -fx-arc-width: 50;");
        Shadow shadowForRectangle = new Shadow();
        shadowForRectangle.setColor(Color.web("#11375efa"));
        Effect ef = shadowForRectangle;
        backgroundRectangle.setEffect(ef);
        
        
        
        
        
        
       
        
        vBoxSelectedGames = new VBox();
        vBoxSelectedGames.prefHeight(334.0);
        vBoxSelectedGames.prefWidth(391.0);
        vBoxSelectedGames.setStyle("-fx-background-color: #668cff, gainsboro;;"
                + "-fx-background-insets: 0, 2 0 0 0; -fx-spacing: 5;");
        
        ScrollPane scrollPaneTabSelectedGames = new ScrollPane();
        scrollPaneTabSelectedGames.prefHeight(334.0);
        scrollPaneTabSelectedGames.prefWidth(404.0);
        scrollPaneTabSelectedGames.setContent(vBoxSelectedGames);
        
        Tab selectedGames = new Tab();
        selectedGames.setText("Selected games");
        selectedGames.setContent(scrollPaneTabSelectedGames);
        
        vBoxAllGames = new VBox();
        vBoxAllGames.prefHeight(334.0);
        vBoxAllGames.prefWidth(391.0);
        vBoxAllGames.setStyle("-fx-background-color: #668cff, gainsboro;\n" +
"  -fx-background-insets: 0, 2 0 0 0; -fx-spacing: 5;");
        
        ScrollPane scrollPaneTabAllGames = new ScrollPane();
        scrollPaneTabAllGames.prefHeight(334.0);
        scrollPaneTabAllGames.prefWidth(404.0);
        scrollPaneTabAllGames.setContent(vBoxAllGames);
        
        Tab allGames = new Tab();
        allGames.setText("All games");
        allGames.setContent(scrollPaneTabAllGames);
        
        tabPane = new TabPane(allGames, selectedGames);
        tabPane.prefHeightProperty().set(350.0);
        tabPane.prefWidthProperty().set(555.0);
        tabPane.layoutXProperty().set(30.0);
        tabPane.layoutYProperty().set(165.0);
        tabPane.setStyle("-fx-border-width: 2; -fx-border-color: #3333ff;");
        tabPane.tabClosingPolicyProperty().set(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        Button getAllGames = new Button();
        getAllGames.layoutXProperty().set(30.0);
        getAllGames.layoutYProperty().set(540.0);
        getAllGames.prefHeightProperty().set(20.0);
        getAllGames.prefWidthProperty().set(150.0);
        getAllGames.setText("Get all aviable games");
        getAllGames.mnemonicParsingProperty().set(false);
        getAllGames.onMouseClickedProperty().set(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                Task<Void> runGetAllGamesToMapTask = getAllGamesToMap();
                runGetAllGamesToMapTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent t)
                    {
                        // Code to run once runFactory() is completed **successfully**
                        //nextFunction()   // also invokes a Task.
                        getAllGamesToList();
                    }
                });
                new Thread(runGetAllGamesToMapTask).start();
                getAllGames.setText("Refresh all games");
            }
        });
        
        
        anchorPane.getChildren().addAll(backgroundRectangle, tabPane, getAllGames);
        
        Scene scene = new Scene(anchorPane);
        stageO.setScene(scene);
        stageO.getScene().setFill(Color.TRANSPARENT);
        
        StageStyle stageStyle = StageStyle.TRANSPARENT;
        stageO.setTitle("Options");
        stageO.initStyle(stageStyle);
        //when mouse button is pressed, save the initial position of screen 
        scene.setOnMousePressed((MouseEvent me) -> {
             dragAnchorX = me.getScreenX() - stageO.getX();
             dragAnchorY = me.getScreenY() - stageO.getY();
         });
        //when screen is dragged, translate it accordingly
        scene.setOnMouseDragged((MouseEvent me) -> {
            stageO.setX(me.getScreenX() - dragAnchorX);
            stageO.setY(me.getScreenY() - dragAnchorY);
        });
        
        stageO.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, event -> {
                event.consume();
            });
        
        String[] selectedGamesStringArray = fileProcessing.selectedGamesFileToStringArray();
        for(String s: selectedGamesStringArray){
            if(!s.isEmpty()){
                String image = s.substring(0, s.indexOf(","));
                String title = s.substring(s.indexOf(",")+1, s.length()-1);
                System.out.println("ListOfSelectedGames   image :"+image+", title :"+title);
                addGameToListOfSelectedGames(image,title);
            }
        }
        
        //parser.getAllGames();
        //String[] allGamesStringArray = fileProcessing.allGamesFileToStringArray();
        //for(String s: allGamesStringArray){
        //    if(!s.isEmpty()){
        //        String image = s.substring(0, s.indexOf(","));
        //        String title = s.substring(s.indexOf(",")+1, s.length()-1);
        //        System.out.println("ListOfAllGames   image :"+image+", title :"+title);
        //        addGameToListOfAllGames(image,title);
        //    }
        //}
        
    }
    
    public void showOptions(){
        satSizeAndLocation();
        
        //Test
      
        
        
        
        
               
        
        
        
        stageO.show();
    }
    
    public void hideOptions(){
        saveSizeAndLocation();
        stageO.hide();
    }
    
    public void saveSizeAndLocation(){
        locationX = stageO.getX();
        locationY = stageO.getY();
        stageHeight = stageO.getHeight();
        stageWidth = stageO.getWidth();
    }
    
    public void satSizeAndLocation(){
        stageO.setHeight(stageHeight);
        stageO.setWidth(stageWidth);
        stageO.setX(locationX);
        stageO.setY(locationY);
    }
    
    public void addGameToListOfAllGames(String image, String title){
        Pane paneAllGames = new Pane();
        paneAllGames.prefHeight(132.0);
        paneAllGames.prefWidth(391.0);
        if(backgroundColorAllGames){
            paneAllGames.setStyle("-fx-background-color: #668cff");
            backgroundColorAllGames = false;
        }else{
            paneAllGames.setStyle("-fx-background-color: #99b3ff");
            backgroundColorAllGames = true;
        }
        
        
        ImageView gameImage = new ImageView(image);
        gameImage.fitHeightProperty().set(132.0);
        gameImage.fitWidthProperty().set(200.0);
        gameImage.pickOnBoundsProperty().set(true);
        gameImage.preserveRatioProperty().set(true);
        
        TextField gameTitle = new TextField();
        gameTitle.layoutXProperty().set(200.0);
        gameTitle.layoutYProperty().set(12.0);
        gameTitle.prefHeightProperty().set(50.0);
        gameTitle.prefWidthProperty().set(265.0);
        gameTitle.setEditable(false);
        gameTitle.setText(title);
        
        Button addGame = new Button();
        addGame.layoutXProperty().set(465.0);
        addGame.layoutYProperty().set(25.0);
        addGame.prefHeightProperty().set(20.0);
        addGame.prefWidthProperty().set(80.0);
        addGame.setText("Add Game");
        addGame.mnemonicParsingProperty().set(false);
        addGame.onMouseClickedProperty().set(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                try {
                    if(fileProcessing.addGameToSelectedGamesFile(image, title)==false){
                       addGameToListOfSelectedGames(image, title); 
                       System.out.println(fileProcessing.toString()+" : Game added");
                    }else{
                        System.out.println(fileProcessing.toString()+" : Already exists");
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Options.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(Options.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Options.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
        
        paneAllGames.getChildren().addAll(gameImage, gameTitle, addGame);
        
        vBoxAllGames.getChildren().add(paneAllGames);
        
    }
    
    public void addGameToListOfSelectedGames(String image, String title){
        
        Pane paneSelectedGames = new Pane();
        paneSelectedGames.prefHeight(132.0);
        paneSelectedGames.prefWidth(391.0);
        if(backgroundColorSelectedGames){
            paneSelectedGames.setStyle("-fx-background-color: #668cff");
            backgroundColorSelectedGames = false;
        }else{
            paneSelectedGames.setStyle("-fx-background-color: #99b3ff");
            backgroundColorSelectedGames = true;
        }
        
        ImageView gameImage = new ImageView(image);
        gameImage.fitHeightProperty().set(132.0);
        gameImage.fitWidthProperty().set(200.0);
        gameImage.pickOnBoundsProperty().set(true);
        gameImage.preserveRatioProperty().set(true);
        
        TextField gameTitle = new TextField();
        gameTitle.layoutXProperty().set(200.0);
        gameTitle.layoutYProperty().set(12.0);
        gameTitle.prefHeightProperty().set(50.0);
        gameTitle.prefWidthProperty().set(276.0);
        gameTitle.setEditable(false);
        gameTitle.setText(title);
        
        Button removeGame = new Button();
        removeGame.layoutXProperty().set(475.0);
        removeGame.layoutYProperty().set(25.0);
        removeGame.prefHeightProperty().set(20.0);
        removeGame.prefWidthProperty().set(60.0);
        removeGame.setText("Delete");
        removeGame.mnemonicParsingProperty().set(false);
        removeGame.onMouseClickedProperty().set(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                    try {
                        fileProcessing.removeGameFromSelectedGamesFile(image, title);
                        System.out.println(fileProcessing.toString()+" : Game removed");
                        vBoxSelectedGames.getChildren().remove(paneSelectedGames);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(Options.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) { 
                        Logger.getLogger(Options.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        
        paneSelectedGames.getChildren().addAll(gameImage, gameTitle, removeGame);
        
        vBoxSelectedGames.getChildren().add(paneSelectedGames);
    }
    
    public Task<Void> getAllGamesToMap(){
        imageTitleMap = new HashMap<String, String>();
        return new Task<Void>() {
                
            @Override 
            public Void call() {
                    parser.getAllGames();
                try {
                    allGamesStringArray = fileProcessing.allGamesFileToStringArray();
                } catch (IOException ex) {
                    Logger.getLogger(Options.class.getName()).log(Level.SEVERE, null, ex);
                }
                for(String s: allGamesStringArray){
                    if(!s.isEmpty()){
                        String image = s.substring(0, s.indexOf(","));
                        String title = s.substring(s.indexOf(",")+1, s.length()-1);
                        System.out.println("ListOfAllGames   image :"+image+", title :"+title);
                        imageTitleMap.put(image, title);
                    }
                }

                return null;
            }
        };
    }
        
    
    
    public void getAllGamesToList(){
        for (Map.Entry<String,String> entry : imageTitleMap.entrySet()){
            System.out.println("Key = " + entry.getKey() + 
                             ", Value = " + entry.getValue()); 
            addGameToListOfAllGames(entry.getKey(),entry.getValue());
        }
    }
    
    
    
}
