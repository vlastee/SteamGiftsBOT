/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steamgiftsbot;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Shadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javax.xml.transform.TransformerException;

/**
 *
 * @author Vlastee
 */
public class SteamGiftsBot extends Application {
    private double dragAnchorX;
    private double dragAnchorY;
    //private static TextField userName; 
    private static Label userName;
    private static ImageView userAvatar;
    private static Button startBot;
    private static Button showOptions;
    private static Label userPoints;
    private static Button parseUser;
    private static Text instructionTest;
    private static ToggleButton showBrowserButton;
    Browser browser;
    Parser parser;
    Options options;
    
    
    @Override
    public void start(Stage stage) throws Exception {
        browser = new Browser();
        
        parser = new Parser();
        options = new Options();
        
        
        Stage stageMain = stage;
        //Parent root1 = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        browser.runBrowser();
        
        
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.prefHeight(697);
        anchorPane.prefWidth(422);
        anchorPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
        
        
        Rectangle backgroundRectangle = new Rectangle();
        //final DoubleProperty doubleProp = new SimpleDoubleProperty(5.0);
        backgroundRectangle.arcHeightProperty().set(50.0);
        backgroundRectangle.arcWidthProperty().set(50.0);
        backgroundRectangle.fillProperty().setValue(Color.DODGERBLUE);
        backgroundRectangle.heightProperty().set(533.0);
        backgroundRectangle.widthProperty().set(400.0);
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
        
        
        ImageView imageSteamGiftsBot = new ImageView();
        imageSteamGiftsBot.fitHeightProperty().set(313.0);
        imageSteamGiftsBot.fitWidthProperty().set(282.0);
        imageSteamGiftsBot.layoutXProperty().set(70.0);
        imageSteamGiftsBot.layoutYProperty().set(-14.0);
        imageSteamGiftsBot.pickOnBoundsProperty().set(true);
        imageSteamGiftsBot.preserveRatioProperty().set(true);
        Image imgSteamGiftsBot = new Image(getClass().getResourceAsStream("SteamgiftsBot.png"));
        imageSteamGiftsBot.setImage(imgSteamGiftsBot);
        
        
        ImageView closeIcon = new ImageView();
        closeIcon.setBlendMode(BlendMode.LIGHTEN);
        closeIcon.fitHeightProperty().set(26.0);
        closeIcon.fitWidthProperty().set(28.0);
        closeIcon.layoutXProperty().set(370.0);
        closeIcon.layoutYProperty().set(165.0);
        closeIcon.onMouseClickedProperty().set(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0){
                Platform.exit();
            }
        });
        
        closeIcon.onMouseEnteredProperty().set(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0){
                closeIcon.setBlendMode(BlendMode.DIFFERENCE);
            }
        });
        
        closeIcon.onMouseExitedProperty().set(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0){
                closeIcon.setBlendMode(BlendMode.LIGHTEN);
            }
        });
        
        closeIcon.pickOnBoundsProperty().set(true);
        closeIcon.preserveRatioProperty().set(true);
        Image imgCloseIcon = new Image(SteamGiftsBot.class.getResourceAsStream("CloseIcon.png"));
        closeIcon.setImage(imgCloseIcon);
        
        
        showBrowserButton = new ToggleButton();
        showBrowserButton.layoutXProperty().set(287.0);
        showBrowserButton.layoutYProperty().set(626.0);
        showBrowserButton.prefHeightProperty().set(32.0);
        showBrowserButton.prefWidthProperty().set(101.0);
        showBrowserButton.mnemonicParsingProperty().set(false);
        showBrowserButton.setText("Show Browser");
        showBrowserButton.onMouseClickedProperty().set(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0){
                if(showBrowserButton.getText().equalsIgnoreCase("Show Browser")){
                    showBrowserButton.setText("Hide Browser");
                    
                    browser.showBrowser();
                }else{
                    showBrowserButton.setText("Show Browser");
                    browser.hideBrowser();
                }
            }
        });
        
        
        instructionTest = new Text();
        instructionTest.layoutXProperty().set(119.0);
        instructionTest.layoutYProperty().set(264.0);
        instructionTest.strokeTypeProperty().set(StrokeType.OUTSIDE);
        instructionTest.strokeWidthProperty().set(0.01);
        instructionTest.setText("Enter SteamGifts.com using SteamGiftsBOT browser.                                          "
                + "Open Browser by clicking Show Browser in the bottom of this window");
        instructionTest.wrappingWidthProperty().set(183.00000566244125);
        
        
        parseUser = new Button();
        parseUser.layoutXProperty().set(31.0);
        parseUser.layoutYProperty().set(626.0);
        parseUser.mnemonicParsingProperty().set(false);
        parseUser.prefHeightProperty().set(32.0);
        parseUser.prefWidthProperty().set(101.0);
        parseUser.setText("Test");
        parseUser.onMouseClickedProperty().set(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0){
                    parser.getUserInfo();
                    parser.participateInGiveaway();
            }
        });
        
        
        showOptions = new Button();
        //showOptions.layoutXProperty().set(31.0);
        //showOptions.layoutYProperty().set(606.0);
        showOptions.mnemonicParsingProperty().set(false);
        showOptions.prefHeightProperty().set(32.0);
        showOptions.prefWidthProperty().set(101.0);
        showOptions.setText("Show Options");
        showOptions.onMouseClickedProperty().set(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0){
                if(showOptions.getText().equalsIgnoreCase("Show Options")){
                    showOptions.setText("Hide Options");
                    
                    options.showOptions();
                }else{
                    showOptions.setText("Show Options");
                    options.hideOptions();
                }
            }
        });
        
        
        userPoints = new Label();
        userPoints.layoutXProperty().set(139.0);
        userPoints.layoutYProperty().set(496.0);
        userPoints.prefHeightProperty().set(32.0);
        userPoints.prefWidthProperty().set(150.0);
        userPoints.setStyle("-fx-border-color: #1E90FF; -fx-font-size: 18; -fx-background-color: #808080;");
        userPoints.setText("Points :");
        userPoints.textAlignmentProperty().set(TextAlignment.CENTER);
        userPoints.textOverrunProperty().set(OverrunStyle.CLIP);
        
        
        startBot = new Button();
        startBot.layoutXProperty().set(160.0);
        startBot.layoutYProperty().set(626.0);
        startBot.prefHeightProperty().set(32.0);
        startBot.prefWidthProperty().set(101.0);
        startBot.setText("Start Bot");
        startBot.onMouseClickedProperty().addListener(e->{
        
        });
        
        
        userAvatar = new ImageView();
        userAvatar.disableProperty().set(true);
        userAvatar.fitHeightProperty().set(150.0);
        userAvatar.fitWidthProperty().set(200.0);
        userAvatar.layoutXProperty().set(111.0);
        userAvatar.layoutYProperty().set(267.0);
        userAvatar.pickOnBoundsProperty().set(true);
        userAvatar.preserveRatioProperty().set(true);
        
        
        userName = new Label();
        userName.alignmentProperty().set(Pos.CENTER);
        userName.layoutXProperty().set(149.0);
        userName.layoutYProperty().set(213.0);
        userName.prefHeightProperty().set(32.0);
        userName.prefWidthProperty().set(150.0);
        userName.setStyle("-fx-background-color: #DCDCDC;");
        userName.setText("Welcome User");
        userName.textAlignmentProperty().set(TextAlignment.CENTER);
        
        
        AnchorPane.setRightAnchor(closeIcon, 30.0);
        AnchorPane.setTopAnchor(closeIcon, 175.0);
        AnchorPane.setRightAnchor(showBrowserButton, 40.0);
        AnchorPane.setTopAnchor(showBrowserButton, 640.0);
        AnchorPane.setRightAnchor(instructionTest, 100.0);
        AnchorPane.setTopAnchor(instructionTest, 300.0);
        AnchorPane.setRightAnchor(parseUser, 280.0);
        AnchorPane.setTopAnchor(parseUser, 600.0);
        AnchorPane.setRightAnchor(showOptions, 280.0);
        AnchorPane.setTopAnchor(showOptions, 640.0);
        AnchorPane.setRightAnchor(userPoints, 135.0);
        AnchorPane.setTopAnchor(userPoints, 530.0);
        AnchorPane.setRightAnchor(startBot, 160.0);
        AnchorPane.setTopAnchor(startBot, 640.0);
        AnchorPane.setRightAnchor(userAvatar, 130.0);
        AnchorPane.setTopAnchor(userAvatar, 300.0);
        AnchorPane.setRightAnchor(userName, 135.0);
        AnchorPane.setTopAnchor(userName, 230.0);
        AnchorPane.setLeftAnchor(imageSteamGiftsBot, 73.0);
        AnchorPane.setTopAnchor(imageSteamGiftsBot, 0.0);
        AnchorPane.setTopAnchor(backgroundRectangle, 160.0);
        AnchorPane.setLeftAnchor(backgroundRectangle, 11.0);
        AnchorPane.setRightAnchor(backgroundRectangle, 11.0);
        AnchorPane.setBottomAnchor(backgroundRectangle, 11.0);
        
        
        anchorPane.getChildren().addAll(backgroundRectangle, imageSteamGiftsBot, closeIcon,
            showBrowserButton, instructionTest, parseUser, showOptions, userPoints, startBot, userAvatar,
            userName);
        
        
        Scene scene = new Scene(anchorPane);
        stageMain.setScene(scene);
        stageMain.getScene().setFill(Color.TRANSPARENT);
    
        
    
        
        StageStyle stageStyle = StageStyle.TRANSPARENT;
        stage.setTitle("SteamGiftsBot");
        //StageStyle stageStyle = StageStyle.UNDECORATED;
        stage.initStyle(stageStyle);
         //when mouse button is pressed, save the initial position of screen 
        scene.setOnMousePressed((MouseEvent me) -> {
             dragAnchorX = me.getScreenX() - stageMain.getX();
             dragAnchorY = me.getScreenY() - stageMain.getY();
         });
        //when screen is dragged, translate it accordingly
        scene.setOnMouseDragged((MouseEvent me) -> {
            stageMain.setX(me.getScreenX() - dragAnchorX);
            stageMain.setY(me.getScreenY() - dragAnchorY);
        });
        //if exited thru window_close_even it's closing main and secondary windows
        stageMain.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, event -> {
                FileProcessing.saveCookies();
                Platform.exit();
            });
        
        stage.show();
    }
    
    
    
  
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public static void setUserName(String string){
        userName.setText("Welcome "+string+" !!!");
    }
    
    public static void setInstructionTestDisable(Boolean bool){
        instructionTest.setDisable(bool);
        instructionTest.setText("");
    }
    
    public static void setUserAvatar(String string){
        userAvatar.setImage(new Image(string));
    }
    
    public static void setUserPoints(String string){
        userPoints.setText(string);
    }
    
}
