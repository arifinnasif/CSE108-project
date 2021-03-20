package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class AdminHomectrl {
    private Main main;
    @FXML
    private Button viewAllUsersButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Label head;

    void setHead(String arg)
    {
        head.setText(arg);
    }

    void setMain(Main main) {
        this.main = main;
    }

    public void viewAll(ActionEvent actionEvent)
    {
        try {

            main.oos.writeObject(new String("$COMMAND:VIEW_ALL_USER"));

        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void addUserButtonAction(ActionEvent actionEvent)
    {
        main.showAddUserPage();
    }

    public void logout(ActionEvent actionEvent)
    {
        try {
            main.oos.writeObject("$COMMAND:LOGOUT");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
