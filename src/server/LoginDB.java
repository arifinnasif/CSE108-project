package server;

import general.Car;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class LoginDB {
    private static Connection conn;
    private static boolean isLoaded=false;

    static void load() throws IOException, SQLException {
        if(isLoaded) return;
        conn = DriverManager.getConnection("jdbc:sqlite:logins.db");
        System.out.println("Connected to login credential database");
    }

    synchronized static String[] searchUsername(String username) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet resultSet;
        resultSet = statement.executeQuery("SELECT * FROM logins WHERE username = \'"+username+"\';");

        if(!resultSet.next()) return new String[0];

        String ret[] = new String[3];
        for(int i=0;i<3;i++)
        {
            ret[i]=resultSet.getString(i+2);
        }
        statement.close();
        return ret;
    }

    private synchronized static String[] searchManu(String username) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet resultSet;
        resultSet = statement.executeQuery("SELECT * FROM logins WHERE username = \'"+username+"\' AND type = \'MANUFACTURER\';");
        if(!resultSet.next()) return new String[0];
        String ret[] = new String[3];

        for(int i=0;i<3;i++)
        {
            ret[i]=resultSet.getString(i+2);
        }
        statement.close();
        return ret;
    }

    synchronized static String[] getAllManu() throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet resultSet;
        resultSet = statement.executeQuery("SELECT * FROM logins WHERE  type = \'MANUFACTURER\';");
        ArrayList<String> arrayList=new ArrayList<>();
        while(resultSet.next()) {
            arrayList.add(resultSet.getString(2)+","+resultSet.getString(3)+","+resultSet.getString(4));
        }
        statement.close();
        return arrayList.toArray(new String[0]);
    }


    synchronized static int addManu(String arg) throws SQLException
    {
        String[] str=arg.split(",");
        if(searchManu(str[0]).length!=0) return -1;
        PreparedStatement statement = conn.prepareStatement("INSERT INTO logins(username,pass,type) VALUES(?,?,?);");
        for(int i=0;i<2;i++) {
            statement.setString(i+1, str[i]);
        }

        statement.setString(3,"MANUFACTURER");

        int count = statement.executeUpdate();
        statement.close();
        return count;
    }


    synchronized static int deleteManu(String username) throws SQLException {
        if(searchManu(username).length==0) return -1;
        PreparedStatement statement = conn.prepareStatement("DELETE FROM logins WHERE username = \'"+username+"\' AND type = \'MANUFACTURER\';");
        int count = statement.executeUpdate();
        statement.close();
        return count;
    }

    synchronized static int changePass(String username, String pass) throws SQLException {
        if(searchManu(username).length==0)
        {
            return -1;
        }

        PreparedStatement statement = conn.prepareStatement("UPDATE logins SET pass = \'"+pass+"\' WHERE username = \'"+username+"\' AND type = \'MANUFACTURER\';");
        int count = statement.executeUpdate();
        statement.close();

        return count;
    }

}
