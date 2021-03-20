package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;

public class ViewerHomectrl {
    private Main main;
    @FXML
    private Button viewAllCarsButton;

    @FXML
    private Button searchRegButton;

    @FXML
    private Button searchPropButton;

    @FXML
    private Button logoutButton;

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

    public void searchByProp(ActionEvent actionEvent)
    {
        main.showSearchByProp();
    }

    public void searchByReg(ActionEvent actionEvent)
    {
        main.showSearchByReg();
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
