package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class User {
    Socket socket;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    private String type;
    private String username;

    private static ArrayList<User> loggedInUsers;

    static {
        loggedInUsers = new ArrayList<>();
    }

    public User(Socket socket, String type) throws IOException, UserException {
        this.socket = socket;
        ois = new ObjectInputStream(socket.getInputStream());
        oos = new ObjectOutputStream(socket.getOutputStream());
        this.type = type.toUpperCase();

        if (!type.equalsIgnoreCase("VIEWER")) {
            synchronized (loggedInUsers) {
                for(User u : loggedInUsers)
                {
                    if(u.getUsername().equals(username)) throw new UserException();
                }
                loggedInUsers.add(this);
            }
        }
    }

    public User(Socket socket, ObjectInputStream ois, ObjectOutputStream oos, String username, String type) throws UserException {
        this.socket = socket;
        this.ois = ois;
        this.oos = oos;
        this.username = username;
        this.type = type.toUpperCase();
        if (!type.equalsIgnoreCase("VIEWER")) {
            synchronized (loggedInUsers) {
                for(User u : loggedInUsers)
                {
                    if(u.getUsername().equals(username)) throw new UserException();
                }
                loggedInUsers.add(this);
            }
        }
    }

    void setUsername(String username) {
        this.username = username;
    }

    String getUsername() {
        return this.username;
    }

    void setType(String type) {
        this.type = type;
    }

    String getType() {
        return type;
    }

    void kill() {
        try {
            ois.close();
            oos.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to kill user");
        } finally {
            synchronized (loggedInUsers) {
                if (!type.equalsIgnoreCase("VIEWER"))
                    loggedInUsers.remove(this);
            }
        }
    }

    static void kickout(String username) {
        synchronized (loggedInUsers) {
            for (User u : loggedInUsers) {
                if (u.getUsername().equals(username)) {
                    try {
                        u.oos.writeObject(new String("$RESPONSE:LOGGED_OUT"));
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    } finally {
                        u.kill();
                        return;
                    }
                }
            }
        }
    }

}
