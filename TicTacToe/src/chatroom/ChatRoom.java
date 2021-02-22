/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatroom;

import chatroom.Classes.Cell;
import chatroom.Classes.GameResponse;
import chatroom.Classes.MyMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.DataInputStream;
import chatroom.Classes.Player;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;

import java.util.Random;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;

/**
 *
 * @author atef
 */
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Toggle;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

/**
 *
 * @author atef
 */
public class ChatRoom extends Application {

    Socket mysocket;
    DataInputStream dis;
    PrintStream ps;
    Thread chatThread;
    TextArea textArea;
    Label errorLabel;
    Scene scene;
    List<Player> playerList;
    String username;
    int userActiveFlag;
    Button alertInitButton;
    Label alertLabel;
    TextArea textMessageArea;
    TextField loginUserTextField;
    PasswordField loginpwBox;
    private String winner;
    Label labelTrial;
    ComboBox playerComboBox;
    Circle circle;
    Label labelTrial2;
    GridPane gridPanes;
    private BorderPane borderPane;
    private Button backToMenuButton;
    private GridPane gridPane;
    private Cell[][] board = new Cell[3][3];
    private String map[][];
    private Button playButton;
    private String turn = "o";
    boolean isX;
    private String myUserName;
    private boolean playWithBot;
    private boolean resumeGame;
    private Label scoreLabel;
    private int myScore = 0 ;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Tic Tac Toe");

        //primaryStage.setScene(scene);
        scene = new Scene(login(), 800, 500);
        scene.getStylesheets().addAll(this.getClass().getResource("ChatRoomStyle.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                ps.println("exit");
                System.exit(0);
            }
        });

    }

    /**
     * @param args the command line arguments
     */
    @Override
    public void init() throws IOException {
        mysocket = new Socket("127.0.0.1", 5005);
        dis = new DataInputStream(mysocket.getInputStream());
        ps = new PrintStream(mysocket.getOutputStream());
}
//To edit combobox shape

class ShapeCell extends ListCell<String> {
}

class ShapeCellFactory implements Callback<ListView<String>, ListCell<String>> {

    @Override
    public ListCell<String> call(ListView<String> param) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}


