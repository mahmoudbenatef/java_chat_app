/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketserverchat;

import socketserverchat.Classes.Player;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gameDB.DbTask;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import socketserverchat.Classes.GameResponse;
import socketserverchat.Classes.GameResponse;
import socketserverchat.Classes.MyMessage;
import socketserverchat.Classes.MyMessage;
import socketserverchat.Classes.Player;
import socketserverchat.Classes.Room;
import socketserverchat.Classes.Room;


public class SocketServerChat {

    /**
     * @param args the command line arguments
     */
    private static ServerSocket serverSocket;
    private ChatHandler chatHandler;
    // static ServerGUI myGui ;

    static ArrayList<Player> allPlayers = new ArrayList<>();
    static boolean isUpdatedUser = false;

    public static ServerSocket getServerSocket() {
        return serverSocket;
    }

    public ChatHandler getChatHandler() {
        return chatHandler;
    }

    public static void stopServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
                ChatHandler.pauseAll();
            }
        } catch (IOException ex) {
            Logger.getLogger(SocketServerChat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void resumeServerSocket() {
        ChatHandler.resumeAll();
    }

    public SocketServerChat() {
        try {
            serverSocket = new ServerSocket(5005);
        } catch (IOException ex) {
            Logger.getLogger(SocketServerChat.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (true) {
            if (!serverSocket.isClosed()) {
                Socket s;
                try {
                    s = serverSocket.accept();
                    chatHandler = new ChatHandler(s);
                } catch (IOException ex) {
                    String message = ex.getMessage();
                    System.out.println(message);

                }

            }
        }
    }
}




class ChatHandler extends Thread {

    String userName = "";
    DataInputStream dis;
    PrintStream ps;
    static ArrayList<ChatHandler> clientsArrayList = new ArrayList<>();
    private GameResponse gameResponse;
    private static int index;
    private static int roomNumber;
    private String[] cellItem = {"x", "o"};
    private static ArrayList<Room> rooms = new ArrayList<>();
    private Socket socket;

    public ChatHandler(Socket s) throws IOException {
        dis = new DataInputStream(s.getInputStream());
        ps = new PrintStream(s.getOutputStream());
        socket = s;
        clientsArrayList.add(this);
        start();
    }

    public static boolean checkExistence(String username) {
        if (clientsArrayList.size() > 0) {
            for (ChatHandler user : clientsArrayList) {
                if (user.userName.equals(username)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void sendSelfMessageWithFlag(String flag, String body) {
        this.ps.println(flag);
        this.ps.println(body);
    }

    public void sendSelfMessage(String body) {
        this.ps.println(body);
    }

    public Player convertJsonToPlayer(String json_string) {
        Gson gson = new Gson();
        Player p = gson.fromJson(json_string, Player.class);
        return p;
    }

    //To update the list of players 
    public void sendAllPlayers(String username, String flag) {
        this.userName = username;
        ArrayList<Player> players = DbTask.getAll(username);
        sendSelfMessageWithFlag(flag, new Gson().toJson(players));
        sendMessageToAll();
    }

    public void sendLoginDataAfterLoginSuccessfully(Player p) {
        if (ChatHandler.checkExistence(p.getUsername())) {
            sendSelfMessage("login failed");
        } else {
            sendSelfMessageWithFlag("myPoints", String.valueOf(p.getPoints()));
            sendAllPlayers(p.getUsername(), "login successfully");
            SocketServerChat.allPlayers = DbTask.getAll("");
            SocketServerChat.isUpdatedUser = true;
        }
    }

    public void playerLogin(String str, String optionFlag) {
        Player p = convertJsonToPlayer(str);
        if (p != null) {
            Player dataBasePlayer = DbTask.getPerson(p);
            if (dataBasePlayer != null) {
                sendLoginDataAfterLoginSuccessfully(dataBasePlayer);
            } else {
                sendSelfMessage("login failed");
            }
        }
    }
    