package client;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

import java.io.IOException;

public class UserRecord implements Comparable{
    private final SimpleStringProperty username;

    private final Button changePassButton;
    private final Button deleteButton;

    private Main main;

    void setMain(Main main) {
        this.main = main;
    }


    public UserRecord(String username)
    {


        this.username=new SimpleStringProperty(username);
        changePassButton=new Button("Change Password");
        changePassButton.setOnAction(e->
                {
                    try {
                        main.oos.writeObject(new String("$COMMAND:EXITQUERY"));
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                        System.exit(-1);
                    }

                    main.showChangePassPage();
                    main.changePassPagectrl.setUsername(this.username.get());

                }
        );
        deleteButton=new Button("Delete");
        deleteButton.setOnAction(e->
                {
                    try
                    {
                        main.oos.writeObject(new String("$COMMAND:DELETE_USER-"+this.username.get()));
                    }
                    catch (IOException ioException)
                    {
                        ioException.printStackTrace();
                        System.exit(-1);
                    }
                }
        );
    }


    public String getUsername() {
        return username.get();
    }


    public Button getChangePassButton() {
        return changePassButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }


    @Override
    public String toString() {
        return username.get() ;
    }

    @Override
    public int compareTo(Object o) {
        return this.username.get().compareTo(((client.UserRecord) o).getUsername());
    }
}
