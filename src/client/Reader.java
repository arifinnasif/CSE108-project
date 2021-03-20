package client;


import javafx.application.Platform;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;

public class Reader implements Runnable{
    private final ObjectInputStream ois;

    private Main main;

    Reader(ObjectInputStream ois)
    {
        this.ois=ois;
    }



    public void setMain(Main main) {
        this.main = main;
    }



    @Override
    public void run() {
        String str[], str1="";
        while(true)
        {
            try {
                str1 = (String) ois.readObject();



                if(str1.charAt(0)=='$')
                {
                    str1=str1.substring(1);
                    str = str1.split(":");
                    if(str[0].equals("RESPONSE"))
                    {
                        if(str[1].equals("USERNAME_NOT_FOUND"))
                        {

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {

                                    main.alert("Error","Username not found","The username doesn't exist in our database. Please try checking the spelling. Contact the admin if necessary",3);
                                }
                            });
                        }
                        else if(str[1].equals("USERNAME_FOUND"))
                        {
                            //main.showLoginPagePassword();
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    main.showLoginPagePassword();
                                }
                            });
                        }
                        else if(str[1].equals("LOGGED_IN_AS_VIEWER"))
                        {
                            //main.showLoginPagePassword();
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    main.showViewerHome();
                                    main.alert("Welcome","Welcome, viewer","You are logged in as a viewer",4);
                                }
                            });
                        }
                        else if(str[1].equals("LOGGED_IN_AS_MANUFACTURER"))
                        {
                            //main.showLoginPagePassword();
                            String[] finalStr = str;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    main.setUsername(finalStr[2]);
                                    main.showManuHome();
                                    main.alert("Welcome","Welcome, "+ finalStr[2],"You are logged in as a manufacturer",4);
                                }
                            });
                        }
                        else if(str[1].equals("FOR_VIEWER_OBJECT_TYPE-Car"))
                        {
                            int size= Integer.parseInt(str[2].substring(str[2].indexOf('-')+1));

                            CarRecord[] cars=new CarRecord[size];
                            for(int i=0;i<size;i++)
                            {



                                    String carinfo = (String) ois.readObject();
                                    cars[i] = new CarRecord(carinfo);

                                    cars[i].setMain(main);



                            }

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {

                                    main.showViewerData(cars);
                                }
                            });

                        }
                        else if(str[1].equals("FOR_MANU_OBJECT_TYPE-Car"))
                        {
                            int size= Integer.parseInt(str[2].substring(str[2].indexOf('-')+1));

                            CarRecord2[] cars=new CarRecord2[size];
                            for(int i=0;i<size;i++)
                            {



                                String carinfo = (String) ois.readObject();
                                cars[i] = new CarRecord2(carinfo);

                                cars[i].setMain(main);



                            }

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {

                                    main.showManuData(cars);
                                }
                            });

                        }
                        else if(str[1].equals("LOGGED_OUT"))
                        {
                            main.reconnectWithServer();
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    main.showLoginPageUsername();
                                }
                            });
                            return;
                        }

                        else if(str[1].equals("ALREADY_LOGGED_IN"))
                        {



                            main.reconnectWithServer();
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    main.alert("Error","User instance active","You are already logged in from another instance. Please try logging out from there and try again.",3);
                                    main.showLoginPageUsername();
                                }
                            });
                            return;
                        }

                        else if(str[1].equals("PASSWORD_INCORRECT"))
                        {

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {

                                    main.alert("Error","Wrong Password","Password you entered didn't match. Try again. Contact the admin if necessary.",3);
                                }
                            });

                        }

                        else if(str[1].equals("CAR_EDITING_SUCCESSFUL"))
                        {

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    main.alert("Action successful","Info updated successfully","You have successfully updated the info",4);
                                }
                            });

                            try {

                                main.oos.writeObject(new String("$COMMAND:VIEW_BY_TYPE-ANY,ANY"));

                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                                System.exit(-1);
                            }

                        }
                        else if(str[1].equals("CAR_ADDITION_SUCCESSFUL"))
                        {

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    main.alert("Action successful","New car added successfully","You have successfully added a new car.",4);
                                    main.showManuHome();
                                }
                            });

                        }
                        else if(str[1].equals("CAR_ADDITION_FAILED"))
                        {
                            if(str[2].equals("REGISTRATION_NUMBER_ALREADY_TAKEN")) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {

                                        main.alert("Error","Invalid registration number","Registration number is not available",2);
                                    }
                                });
                            }
                            else
                            {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {

                                        main.alert("Error","Unexpected error occurred","Car cannot be added. Try again.",3);
                                    }
                                });
                            }
                        }
                        else if(str[1].equals("CAR_EDITING_FAILED"))
                        {

                            if(str[2].equals("CAR_DOES_NOT_EXIST")) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        //main.editCarPagectrl.warn("car was deleted by an user while you were editing");
                                        main.alert("Error","Update failed","Car was deleted by an user while you were editing",3);
                                        main.showManuHome();
                                    }
                                });

                            }
                            else if(str[2].equals("REGISTRATION_NUMBER_ALREADY_TAKEN")) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        main.alert("Error","Update failed","Registration number not available",3);
                                        main.showManuHome();
                                    }
                                });

                            }
                            else
                            {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        main.alert("Error","Unexpected error occurred","Car cannot be edited. Try again.",3);
                                    }
                                });
                            }

                        }
                        else if(str[1].equals("PURCHASE_FAILED"))
                        {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    main.alert("Sorry","Failed to purchase","The car is out of stock",3);
                                }
                            });
                        }
                        else if(str[1].equals("FOR_ADMIN_OBJECT_TYPE-User"))
                        {
                            int size= Integer.parseInt(str[2].substring(str[2].indexOf('-')+1));

                            UserRecord[] users=new UserRecord[size];
                            for(int i=0;i<size;i++)
                            {



                                String userinfo = (String) ois.readObject();
                                users[i] = new UserRecord(userinfo);

                                users[i].setMain(main);



                            }

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {

                                    main.showAdminData(users);
                                }
                            });
                        }
                        else if(str[1].equals("USER_ADDITION_FAILED"))
                        {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    main.alert("Error","Username not available","Try another username",3);
                                }
                            });
                        }
                        else if(str[1].equals("PASSWORD_CHANGE_SUCCESSFUL"))
                        {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    main.alert("Action successful","Password changed","You have successfully changed the password",5);
                                }
                            });
                        }
                        else if(str[1].equals("LOGGED_IN_AS_ADMIN"))
                        {
                            //main.showLoginPagePassword();
                            String[] finalStr = str;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    main.setUsername(finalStr[2]);
                                    main.showAdminHome();
                                    main.alert("Welcome","Welcome, "+ finalStr[2],"You are logged in as an admin",4);
                                }
                            });
                        }
                        else if(str[1].equals("USER_ADDITION_SUCCESSFUL"))
                        {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    main.showAdminHome();
                                }
                            });
                        }
                        else
                        {
                            System.out.println("Unhandled response : "+str1);
                        }
                    }
                }
                else
                {
                    System.out.println("From server : "+str1);
                }



            } catch (EOFException e)
            {
                //server down...

                System.exit(-1);
            }
            catch (SocketException e)
            {
                //e.printStackTrace();
                System.out.println("Exiting...");
                return;
            }
            catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
                continue;
            }





        }
    }
}
