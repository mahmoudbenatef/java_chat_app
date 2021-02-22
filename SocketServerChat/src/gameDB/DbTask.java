/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import socketserverchat.Classes.Player;

/**
 *
 * @author atef
 */
public class DbTask {
    static Connection con = null;

    public static void defineConnection() {

        try {
            //String url = "jdbc:mysql://localhost:3306/game";
            //String user = "atef";
            //String password = "Iwasbornin1998$";
            // con = DriverManager.getConnection(url, user, password);
            if (con == null) {
                con = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/game?createDatabaseIfNotExist=true&user=ateef&password=p@ssword");
                Statement stmt = con.createStatement();
                stmt.execute("CREATE TABLE IF NOT EXISTS players"
                        + "(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, username VARCHAR(255) NOT NULL UNIQUE KEY, "+
                        "password VARCHAR(255) NOT NULL, points INT NOT NULL, flag int NOT NULL, " +
                        "nickname VARCHAR(255) NOT NULL, map VARCHAR(255))" +
                        "ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8");
                stmt.close();
            }

        } catch (Exception e) {
        }
    }

    public static Player getPerson(Player p) {

        String sql = "SELECT * FROM players WHERE username = ? and password = ?";
        try (
                PreparedStatement pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            pstmt.setString(1, p.getUsername());
            pstmt.setString(2, p.getPassword());
            ResultSet resultSet = pstmt.executeQuery();
            p = null;
            if (resultSet.next()) {
                p = new Player();
                p.setId(Integer.parseInt(resultSet.getString(1)));
                p.setUsername(resultSet.getString(2));
                p.setPassword(resultSet.getString(3));
                p.setPoints(Integer.parseInt(resultSet.getString(4)));
                p.setFlag(Integer.parseInt(resultSet.getString(5)));
                p.setNickName(resultSet.getString(6));
            }

        } catch (SQLException e) {
        }

        if (p != null) {
            setActive(p.getId());
        }

        return p;
    }

    public static void setActive(int personId) {
        try {
            PreparedStatement pst = con.prepareStatement("UPDATE players SET flag = ?  where id = ?");
            pst.setInt(1, 1);
            pst.setInt(2, personId);
            pst.executeUpdate();

        } catch (Exception e) {
        }
    }

    public static int register(Player p) {
        String insertTableSQL = "INSERT INTO players"
                + "(username,password,nickname,points,flag) VALUES"
                + "(?,?,?,?,?)";
        try {
            PreparedStatement pst = con.prepareStatement(insertTableSQL);
            pst.setString(1, p.getUsername());
            pst.setString(2, p.getPassword());
            pst.setString(3, p.getNickName());
            pst.setInt(4, 0);
            pst.setInt(5, 1);
            pst.executeUpdate();
            return DbTask.getID(p.getUsername());

        } catch (SQLException e) {
            return -1;
        }
    }
    

