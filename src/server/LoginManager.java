package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginManager {
    private static ServerSocket serverSocket;

    private static ExecutorService executorService;
    private static ExecutorService userExecutor;



    static
    {
        try {
            serverSocket = new ServerSocket(1234);
            System.out.println("Server started");

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to open the specified socket. Exiting...");
            System.exit(-1);
        }

        executorService = Executors.newFixedThreadPool(128);
        userExecutor=Executors.newFixedThreadPool(64);
    }



    static void listenForRequests() {
        System.out.println("Listening for requests");
        Socket clientSocket;
        while (true) {
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Unknown IOException. Ignoring request ...");
                continue;
            }
            executorService.execute(new VerifyLogin(clientSocket));
        }
    }

    private static class VerifyLogin implements Runnable {
        private Socket socket;
        private ObjectInputStream ois;
        private ObjectOutputStream oos;
        private boolean isOkay;

        VerifyLogin(Socket socket) {
            isOkay = true;
            this.socket = socket;
            try {
                ois = new ObjectInputStream(socket.getInputStream());
                oos = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                isOkay = false;
                /*
                System.err.println("Failed to get streams. Killing connection to the client");
                try
                {
                    socket.close();
                }
                catch (IOException e2)
                {
                    e2.printStackTrace();
                    System.err.println("Failed to close connection!");
                }
                */
            }
        }

        private void kill(String message)
        {
            if(message.length()==0)
                System.err.println("Killing connection to the client ...");
            else
                System.err.println(message+". Killing connection to the client ...");

            try {
                socket.close();
            } catch (IOException e2) {
                e2.printStackTrace();
                System.err.println("Failed to close connection!");
            }
        }

        @Override
        public void run() {


            try {
                if (!isOkay) throw new IOException("Failed to get streams");
                oos.writeObject(new String("Connected to cardatabase server. Send username and then password respectively")); // [infotype message]

                while (socket.isConnected() || !socket.isClosed()) {
                    String username = null, pass = null;
                    Object object = ois.readObject();
                    if (!(object instanceof String))
                        throw new Exception("username is not String type");
                    username = (String) object;

                    if (username.equalsIgnoreCase("VIEWER")) {
                        // viewer handler
                        oos.writeObject(new String("$RESPONSE:LOGGED_IN_AS_VIEWER"));
                        userExecutor.execute(new ViewerHandler(new User(socket, ois, oos, username, "VIEWER")));
                        return;
                    }


                    String[] resp=LoginDB.searchUsername(username);
                    if (resp.length==0) {
                        oos.writeObject(new String("$RESPONSE:USERNAME_NOT_FOUND"));
                        continue;
                    }


                    oos.writeObject(new String("$RESPONSE:USERNAME_FOUND"));

                    while(!socket.isClosed()) {
                        object = ois.readObject();
                        if (!(object instanceof String))
                            throw new InvalidObjectException("password is not a String type object");
                        pass = (String) object;

                        if (resp[1].equals(pass)) {
                            if (resp[2].equals("MANUFACTURER")) {
                                // manufacture handle


                                User user;
                                try
                                {
                                    user=new User(socket, ois, oos, username, "MANUFACTURER");
                                }
                                catch (UserException userException)
                                {
                                    oos.writeObject("$RESPONSE:ALREADY_LOGGED_IN");
                                    return;
                                }
                                oos.writeObject(new String("$RESPONSE:LOGGED_IN_AS_MANUFACTURER:"+username));
                                userExecutor.execute(new ManuHandler(user));
                                System.out.println(username+" logged in");

                            } else if (resp[2].equals("ADMIN")) {
                                // admin handler


                                User user;
                                try
                                {
                                    user=new User(socket, ois, oos, username, "ADMIN");
                                }
                                catch (UserException userException)
                                {
                                    oos.writeObject("$RESPONSE:ALREADY_LOGGED_IN");
                                    return;
                                }
                                oos.writeObject(new String("$RESPONSE:LOGGED_IN_AS_ADMIN:"+username));
                                userExecutor.execute(new AdminHandler(user));
                                System.out.println(username+"<ADMIN> logged in");

                            }
                            return;
                        }
                        oos.writeObject("$RESPONSE:PASSWORD_INCORRECT");
                    }


                    /* --------------------------------------------------------------------------------------------- */

                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
                System.err.println("Database error. Exiting ...");
                System.exit(-1);
            }

            catch (Exception e)
            {
                e.printStackTrace();
                kill("");
            }
        }
    }
}
