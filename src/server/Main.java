package server;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            CarDB.load();
            LoginDB.load();
        }
        catch (Exception e)
        {
            System.out.println("Exception occurred. Exiting...");
            System.exit(-1);
        }
        LoginManager.listenForRequests();
    }
}
