package server;

import general.Car;

import java.io.IOException;
import java.sql.SQLException;

public class ManuHandler implements Runnable {
    private User user;
    ManuHandler(User user)
    {
        this.user=user;
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

                if(command.startsWith("$COMMAND:VIEW_BY_TYPE-"))
                {
                    String[] str = command.substring(command.indexOf('-')+1).split(",");
                    Car[] results = CarDB.searchCar(str[0], str[1]);
                    user.oos.writeObject(new String("$RESPONSE:FOR_MANU_OBJECT_TYPE-Car:TOTAL-" + results.length));
                    for (Car c : results) {
                        user.oos.writeObject(c.info());
                    }
                    SyncHandler.addSyncedUser(user,str[0]+","+str[1]);
                }
                else if(command.startsWith("$COMMAND:VIEW_BY_REG-"))
                {
                    String reg = command.substring(command.indexOf('-')+1);
                    Car[] results = CarDB.searchCar(reg);
                    user.oos.writeObject(new String("$RESPONSE:FOR_MANU_OBJECT_TYPE-Car:TOTAL-" + results.length));
                    for (Car c : results) {
                        user.oos.writeObject(c.info());
                    }
                    SyncHandler.addSyncedUser(user,reg);
                }
                else if(command.equals("$COMMAND:EXITQUERY"))
                {
                    SyncHandler.remove(user);
                }
                else if(command.startsWith("$COMMAND:ADD_CAR-"))
                {
                    String carStr = command.substring(command.indexOf('-')+1);

                    if(CarDB.addCar(new Car((String) carStr))==-1)
                    {
                        user.oos.writeObject(new String("$RESPONSE:CAR_ADDITION_FAILED:REGISTRATION_NUMBER_ALREADY_TAKEN"));
                        continue;
                    }
                    user.oos.writeObject(new String("$RESPONSE:CAR_ADDITION_SUCCESSFUL"));
                    SyncHandler.sync();
                }
                else if(command.startsWith("$COMMAND:EDIT_CAR-"))
                {
                    String[] editStr = command.substring(command.indexOf('-')+1).split(":");


                    int r = CarDB.editCar(editStr[0], new Car(editStr[1]));
                    if(r==-1)
                    {
                        user.oos.writeObject(new String("$RESPONSE:CAR_EDITING_FAILED:CAR_DOES_NOT_EXIST"));
                        continue;
                    }
                    else if(r==-2)
                    {
                        user.oos.writeObject(new String("$RESPONSE:CAR_EDITING_FAILED:REGISTRATION_NUMBER_ALREADY_TAKEN"));
                    }
                    else if (r==1)
                    {
                        user.oos.writeObject(new String("$RESPONSE:CAR_EDITING_SUCCESSFUL"));
                    }
                    SyncHandler.sync();
                }
                else if(command.startsWith("$COMMAND:DELETE_CAR-"))
                {
                    String reg = command.substring(command.indexOf('-')+1);
                    if(CarDB.deleteCar(reg)==-1)
                    {
                        user.oos.writeObject(new String("$RESPONSE:CAR_DELETION_FAILED:CAR_DOES_NOT_EXIST"));
                    }
                    SyncHandler.sync();
                }
                else if(command.equals("$COMMAND:LOGOUT"))
                {
                    SyncHandler.remove(user);
                    user.oos.writeObject(new String("$RESPONSE:LOGGED_OUT"));
                    user.kill();
                    System.out.println(user.getUsername()+" logged out");
                    return;
                }
                else if(command.equals("$COMMAND:EXIT"))
                {
                    SyncHandler.remove(user);

                    user.kill();
                    System.out.println(user.getUsername()+" logged out");
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
