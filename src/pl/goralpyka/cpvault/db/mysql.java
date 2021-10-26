package pl.goralpyka.cpvault.db;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import java.sql.*;

public class mysql {
    public Connection connection;
    public String host, db, username, passwd;
    public int port;

    public mysql(){
        connSetup();
    }

    public void connSetup() {
        host = "localhost";
        port = 3306;
        db = "edvault";
        username = "root";
        passwd = "";

        try {
            synchronized (this){
                if (getConnection() != null && !getConnection().isClosed()){
                    return;
                }
                Class.forName("com.mysql.jdbc.Driver");
                setConnection( DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port
                        + "/" + this.db, this.username, this.passwd));

                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "ESTABLISHED DATABASE CONNECTION");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void checkIfUserExists(String login, String uuid ,Connection connection) {
        String query = "SELECT uuid FROM edvault.users WHERE uuid = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, uuid);
            ResultSet rs = statement.executeQuery();
            if (!rs.next()){
                String query2 = "INSERT INTO edvault.users (uuid, login) VALUES (?, ?)";
                PreparedStatement statement2 = connection.prepareStatement(query2);
                statement2.setString(1 , uuid);
                statement2.setString(2, login);
                int result = statement2.executeUpdate();
                if (result != 1){
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "<DBINFO> ERROR OCCURRED WHILE INSERTING NEW USER" +
                            " TO DATABASE");
                } else {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "<DBINFO> ADDED NEW USER TO DATABASE : LOGIN - "+
                            login);
                }
            } else{
                String qry =  "SELECT login FROM edvault.users WHERE uuid = ?";
                PreparedStatement stmt = connection.prepareStatement(qry);
                stmt.setString(1, uuid);
                ResultSet rs2 = stmt.executeQuery();

                if (!rs2.next()){
                    String qry2 = "UPDATE users set login = ? WHERE uuid = ?";
                    PreparedStatement stmt2 = connection.prepareStatement(qry2);
                    stmt2.setString(1, login);
                    stmt2.setString(2, uuid);
                    int result2 = stmt2.executeUpdate();
                    if (result2 != 1){
                        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "<DBINFO> ERROR OCCURRED WHILE CHANGING USERS" +
                                " LOGIN");
                    } else {
                        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "<DBINFO> UPDATED USER LOGIN - "+
                                login);
                    }
                }

                Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "<DBINFO> USER ALREADY EXISTS IN DATABASE");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
