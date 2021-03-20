package server;

import general.Car;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

public class SyncHandler {
    private static HashMap<User, String> syncedUsers;

    static {
        syncedUsers = new HashMap<>();
    }

    synchronized static void addSyncedUser(User user, String query) {
        if (syncedUsers.containsKey(user) && syncedUsers.get(user).equals(query)) return;
        if (syncedUsers.containsKey(user) && !syncedUsers.get(user).equals(query))
        {
            syncedUsers.remove(user);
            syncedUsers.put(user,query);
            return;
        }
        syncedUsers.put(user, query);
    }

    synchronized static void sync() {
        for (User user : syncedUsers.keySet()) {
            try {
                if (syncedUsers.get(user).indexOf(',') != -1) {
                    String[] str = syncedUsers.get(user).split(",");
                    Car[] results = CarDB.searchCar(str[0], str[1]);
                    if(user.getType().equals("MANUFACTURER")) {
                        user.oos.writeObject(new String("$RESPONSE:FOR_MANU_OBJECT_TYPE-Car:TOTAL-" + results.length));
                    }
                    else {
                        user.oos.writeObject(new String("$RESPONSE:FOR_VIEWER_OBJECT_TYPE-Car:TOTAL-" + results.length));
                    }
                    for (Car c : results) {
                        user.oos.writeObject(c.info());
                    }
                } else {
                    Car[] results = CarDB.searchCar(syncedUsers.get(user));
                    if(user.getType().equals("MANUFACTURER")) {
                        user.oos.writeObject(new String("$RESPONSE:FOR_MANU_OBJECT_TYPE-Car:TOTAL-" + results.length));
                    }
                    else {
                        user.oos.writeObject(new String("$RESPONSE:FOR_VIEWER_OBJECT_TYPE-Car:TOTAL-" + results.length));
                    }
                    for (Car c : results) {
                        user.oos.writeObject(c.info());
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Database Error. Exiting ...");
                System.exit(-1);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Unknown IOException. Cannot sync user");
                //continue;
            }
        }
    }

    synchronized static void remove(User user)
    {
        if (syncedUsers.containsKey(user))
            syncedUsers.remove(user);
    }
}
