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
chatThread = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean successfull = false;
                String flagName = "";
                while (true) {
                    String str = null;
                    try {
                        str = dis.readLine();
                        if (str != null) {

                            if (str.equals("registration failed")) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        errorLabel.setVisible(true);
                                    }
                                });

                            } else if (str.equals("login failed")) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        errorLabel.setVisible(true);
                                    }
                                });
                            } else if (str.equals("login successfully")) {
                                successfull = true;

                            } else if (str.equals("registered successfully")) {
                                successfull = true;
                            } else if (str.equals("request chat")) {
                                flagName = "request";

                            } else if (flagName.equals("request")) {
                                final String temp = str;
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        alertLabel.getScene().setRoot(requestPage(temp));
                                    }
                                });

                                flagName = "";
                            } else if (str.equals("accept chat")) {
                                flagName = "accept";
                            } else if (flagName.equals("accept")) {
                                final String playerName = str;
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        alertLabel.getScene().setRoot(playPage(playerName));
                                    }
                                });
                                flagName = "";
                            } else if (str.equals("text message")) {
                                flagName = "message";
                            } else if (flagName.equals("message")) {
                                textMessageArea.appendText(str + "\n");
                                flagName = "";
                            } else if (successfull == true) {

                                String json_string = str;
                                Gson gson = new Gson();
                                ArrayList<Player> players = new ArrayList<>();
                                Type playerListType = new TypeToken<ArrayList<Player>>() {
                                }.getType();
                                playerList = gson.fromJson(json_string, playerListType);

                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        errorLabel.getScene().setRoot(mainPage());
                                    }

                                });

                                int i = 0;

                                successfull = false;

                            } else if (str.equals("update player list")) {
                                flagName = "update player list";

                            } else if (flagName.equals("update player list")) {
                                Gson gson = new Gson();

                                ArrayList<Player> players = new ArrayList<>();
                                Type playerListType = new TypeToken<ArrayList<Player>>() {
                                }.getType();
                                playerList = gson.fromJson(str, playerListType);
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (playerComboBox != null) {
                                            playerComboBox.getItems().clear();

                                            for (Player pp : playerList) {
                                                playerComboBox.getItems().add(
                                                        pp.getUsername() + "," + pp.getPoints() + "," + pp.getFlag()
                                                );
                                            }

                                            playerComboBox.setValue("please choose one player");
                                            // Set the CellFactory property
                                            playerComboBox.setCellFactory(new ShapeCellFactory());
                                            // Set the ButtonCell property
                                            playerComboBox.setButtonCell(new ShapeCell());
                                            ////////////////////////////////////////////////
                                            playerComboBox.setValue("please choose one player");
                                            playerComboBox.setId(("combo-box"));
                                        }
                                    }

                                });
                                flagName = "";
                            } else if (flagName.equals("get map")) {
                                Gson gson = new Gson();
                                GameResponse gameResponse = gson.fromJson(str, GameResponse.class);

                                if (!gameResponse.getTurn().isEmpty()) {
                                    turn = gameResponse.getTurn();
                                }
                                String[][] stringArr = gameResponse.getArr();
                                //test loop
                                for (int i = 0; i < 3; i++) {
                                    for (int j = 0; j < 3; j++) {
                                    }
                                }
                                for (int i = 0; i < board.length; i++) {
                                    for (int j = 0; j < board[i].length; j++) {
                                        if (!stringArr[i][j].isEmpty()) {
                                            board[i][j].getPlayerMove().setText(stringArr[i][j]);
                                        }
                                    }

                                }
                                if (gameResponse.isGameOver()) {
                                    winMessage(gameResponse.getTurn());

                                } else if (gameResponse.isDraw()) {

                                    drawMessage();
                                }
                                flagName = "";
                            } else if (str.equals("myName")) {
                                flagName = "getName";
                            } else if (flagName.equals("getName")) {
                                myUserName = str;
                                flagName = "";
                            } else if (str.equals("update game")) {
                                flagName = "get map";
                            } else if (str.equals("pause")) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        Alert alertActive = new Alert(AlertType.WARNING);
                                        alertActive.setTitle("Warning Message");
                                        alertActive.setHeaderText("Server is down now but your message will arrive as soon as server is back");
                                        alertActive.showAndWait();
                                    }
                                });
                            } else if (str.equals("resume-game-play")) {
                                flagName = "resume-game";
                            } else if (flagName.equals("resume-game")) {
                                Gson gson = new Gson();

                                map = gson.fromJson(str, String[][].class);
                                resumeGame = true;
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        playButton.getScene().setRoot(playPage(""));

                                    }
                                });

                                flagName = "";
                            }
                            else if(str.equals("myPoints"))
                            {
                                flagName = "myPoints";
                            }
                            else if(flagName.equals("myPoints"))
                            {
                                myScore= Integer.valueOf(str);
                               Platform.runLater(new Runnable() {
                                   @Override
                                   public void run() {
                                       if(scoreLabel!=null) {
                                           scoreLabel.setText("points:" + myScore);
                                       }
                                   }
                               });
                                
                            flagName="";
                            }else if(str.equals("back-pressed")){
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        Alert alertActive = new Alert(AlertType.ERROR);
                                        alertActive.setTitle("player left");
                                        alertActive.setHeaderText("a player has left the game!");
                                        alertActive.showAndWait();
                                        turn = "o";
                                        isX = false;
                                        backToMenuButton.getScene().setRoot(mainPage());

                                    }
                                });
                            }

                        }
                    } catch (IOException ex) {
                    }
                }
            }
        });
        chatThread.start();

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


