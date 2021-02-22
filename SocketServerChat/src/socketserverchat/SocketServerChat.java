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




