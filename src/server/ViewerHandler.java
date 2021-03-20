package server;

import general.Car;

import java.io.IOException;

import java.sql.SQLException;

public class ViewerHandler implements Runnable {
    private User user;



    ViewerHandler(User user)
    {
        this.user=user;
    }

    @Override
    public void run() {
        Object obj;
        int t=5;
        while(t>0&&user.socket.isConnected())
        {
            try {
                obj = user.ois.readObject();

                if(!(obj instanceof String))
                {
                    System.err.println("Command from viewer is not string type. Killing connection to the viewer ...");
                    try {
                        user.socket.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        System.err.println("Failed to close connection!");
                    }
                    finally {
                        return;
                    }
                }

                String command = (String) obj;
                //System.out.println(command);

                if(command.startsWith("$COMMAND:VIEW_BY_TYPE-"))
                {
                    String[] str = command.substring(command.indexOf('-')+1).split(",");
                    Car[] results = CarDB.searchCar(str[0], str[1]);
                    user.oos.writeObject(new String("$RESPONSE:FOR_VIEWER_OBJECT_TYPE-Car:TOTAL-" + results.length));
                    for (Car c : results) {
                        user.oos.writeObject(c.info());
                    }
                    SyncHandler.addSyncedUser(user,str[0]+","+str[1]);
                }
                else if(command.startsWith("$COMMAND:VIEW_BY_REG-"))
                {
                    String reg = command.substring(command.indexOf('-')+1);
                    Car[] results = CarDB.searchCar(reg);
                    user.oos.writeObject(new String("$RESPONSE:FOR_VIEWER_OBJECT_TYPE-Car:TOTAL-" + results.length));
                    for (Car c : results) {
                        user.oos.writeObject(c.info());
                    }
                    SyncHandler.addSyncedUser(user,reg);
                }
                else if(command.equals("$COMMAND:EXITQUERY"))
                {
                    SyncHandler.remove(user);
                }
                else if(command.startsWith("$COMMAND:BUY-"))
                {
                    String reg = command.substring(command.indexOf('-')+1);
                    if(CarDB.decrease(reg)==-1)
                    {
                        user.oos.writeObject(new String("$RESPONSE:PURCHASE_FAILED"));
                        continue;
                    }
                    user.oos.writeObject(new String("$RESPONSE:PURCHASE_SUCCESSFUL"));
                    SyncHandler.sync();
                }
                else if(command.equals("$COMMAND:LOGOUT"))
                {
                    SyncHandler.remove(user);
                    user.oos.writeObject(new String("$RESPONSE:LOGGED_OUT"));
                    user.kill();
                    System.out.println("A viewer logged out");
                    return;
                }
                else if(command.equals("$COMMAND:EXIT"))
                {
                    SyncHandler.remove(user);

                    user.kill();
                    System.out.println("A viewer logged out");
                    return;
                }
                else
                {
                    user.oos.writeObject(new String("Command not found"));
                }
            } catch (IOException e) {
                e.printStackTrace();
                t--;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                t--;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                System.err.println("Database error. Exiting ...");
                user.kill();
                System.exit(-1);
            }
        }
        user.kill();
    }
}
