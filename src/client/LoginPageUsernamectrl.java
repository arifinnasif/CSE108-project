package client;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;
import java.io.IOException;

public class LoginPageUsernamectrl {
    private Main main;
    @FXML
    private Button usernameSubmit;

    @FXML
    private TextField usernameText;

    @FXML
    private Label warnLabelLogin;


    void setMain(Main main)
    {
        this.main=main;
        BooleanBinding usernameTextValid = Bindings.createBooleanBinding(() -> {
            if(usernameText.getText().isEmpty()) return false;
            return true;
        }, usernameText.textProperty());

        usernameSubmit.disableProperty().bind(usernameTextValid.not());
    }


    public void sendUsername(ActionEvent actionEvent)
    {
        try {
            main.oos.writeObject(usernameText.getText().trim());
        }
        catch (IOException e)
        {
            System.exit(-1);
        }
    }


}
