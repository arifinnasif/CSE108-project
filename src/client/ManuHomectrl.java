package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class ManuHomectrl {
    private Main main;
    @FXML
    private Button viewAllCarsButton;

    @FXML
    private Button searchRegButton;

    @FXML
    private Button searchPropButton;

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

            main.oos.writeObject(new String("$COMMAND:VIEW_BY_TYPE-ANY,ANY"));

        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void addCarButtonAction(ActionEvent actionEvent)
    {
        main.showEditCarPage("ADD");
        main.editCarPagectrl.setHeading("Add Car");
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
