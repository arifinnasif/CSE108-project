package server;

import general.Car;

import java.io.IOException;
import java.sql.SQLException;

public class AdminHandler implements Runnable{
    private User user;
    private boolean isViewingData;
    AdminHandler(User user)
    {
        this.user=user;
        this.isViewingData=false;
    }

    @Override
    public void run() {
        Object obj;
        int t=5;
        while(t>0)
        {
            try {
                obj = user.ois.readObject();

                if(!(obj instanceof String))
                {
                    System.err.println("Command from "+user.getUsername()+" is not string type. Killing connection to "+user.getUsername()+" ...");
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

                if(command.equals("$COMMAND:VIEW_ALL_USER"))
                {
                    String[] results = LoginDB.getAllManu();
                    user.oos.writeObject(new String("$RESPONSE:FOR_ADMIN_OBJECT_TYPE-User:TOTAL-" + results.length));
                    for (String c : results) {
                        user.oos.writeObject(c.split(",")[0]);
                    }
                    isViewingData=true;
                }
//                //else if(command.startsWith("$COMMAND:VIEW_BY_REG-"))
//                //{
//                    String reg = command.substring(command.indexOf('-')+1);
//                    Car[] results = CarDB.searchCar(reg);
//                    user.oos.writeObject(new String("$RESPONSE:FOR_MANU_OBJECT_TYPE-Car:TOTAL-" + results.length));
//                    for (Car c : results) {
//                        user.oos.writeObject(c.info());
//                    }
//                    SyncHandler.addSyncedUser(user,reg);
//                }
                else if(command.equals("$COMMAND:EXITQUERY"))
                {
                    isViewingData=false;
                }
                else if(command.startsWith("$COMMAND:ADD_USER-"))
                {
                    String usrStr = command.substring(command.indexOf('-')+1);

                    if(LoginDB.addManu(usrStr)==-1)
                    {
                        user.oos.writeObject(new String("$RESPONSE:USER_ADDITION_FAILED:USERNAME_NUMBER_ALREADY_TAKEN"));
                        continue;
                    }
                    user.oos.writeObject(new String("$RESPONSE:USER_ADDITION_SUCCESSFUL"));
                }
                else if(command.startsWith("$COMMAND:CHANGE_PASS-"))
                {
                    String[] usrPass = command.substring(command.indexOf('-')+1).split(",");


                    int r = LoginDB.changePass(usrPass[0],usrPass[1]);
                    if(r==-1)
                    {
                        user.oos.writeObject(new String("$RESPONSE:PASSWORD_CHANGE_FAILED:USERNAME_DOES_NOT_EXIST"));
                        continue;
                    }

                    else if (r==1)
                    {
                        user.oos.writeObject(new String("$RESPONSE:PASSWORD_CHANGE_SUCCESSFUL"));
                    }

                    //<----------------sync-------------->
                    String[] results = LoginDB.getAllManu();
                    user.oos.writeObject(new String("$RESPONSE:FOR_ADMIN_OBJECT_TYPE-User:TOTAL-" + results.length));
                    for (String c : results) {
                        user.oos.writeObject(c.split(",")[0]);
                    }
                    //<----------------sync-------------->
                }
                else if(command.startsWith("$COMMAND:DELETE_USER-"))
                {
                    String username = command.substring(command.indexOf('-')+1);
                    if(LoginDB.deleteManu(username)==-1)
                    {
                        user.oos.writeObject(new String("$RESPONSE:USER_DELETION_FAILED:USER_DOES_NOT_EXIST"));
                    }
                    User.kickout(username);
                    //<----------------sync-------------->
                    String[] results = LoginDB.getAllManu();
                    user.oos.writeObject(new String("$RESPONSE:FOR_ADMIN_OBJECT_TYPE-User:TOTAL-" + results.length));
                    for (String c : results) {
                        user.oos.writeObject(c.split(",")[0]);
                    }
                    //<----------------sync-------------->
                }
                else if(command.equals("$COMMAND:LOGOUT"))
                {
                    isViewingData=false;
                    user.oos.writeObject(new String("$RESPONSE:LOGGED_OUT"));
                    user.kill();
                    System.out.println(user.getUsername()+"<ADMIN> logged out");
                    return;
                }
                else if(command.equals("$COMMAND:EXIT"))
                {
                    isViewingData=false;
                    user.kill();
                    System.out.println(user.getUsername()+"<ADMIN> logged out");
                    return;
                }
                else
                {
                    System.out.println(command);
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
