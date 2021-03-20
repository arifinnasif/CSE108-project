package client;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;
import java.io.IOException;

public class LoginPagePasswordctrl {
    private Main main;
    @FXML
    private Button passwordSubmit;

    @FXML
    private TextField passwordText;

    @FXML
    private Label warnLabelLogin;


//    void warn(String message)
//    {
//        warnLabelLogin.setText(message);
//    }


    void setMain(Main main)
    {
        this.main=main;
        BooleanBinding passwordTextValid = Bindings.createBooleanBinding(() -> {
            if(passwordText.getText().isEmpty()) return false;
            return true;
        }, passwordText.textProperty());

        passwordSubmit.disableProperty().bind(passwordTextValid.not());
    }

    public void sendPassword(ActionEvent actionEvent)
    {
        try {
            main.oos.writeObject(passwordText.getText());
        }
        catch (IOException e)
        {
            System.exit(-1);
        }
    }

    public void back(ActionEvent actionEvent)
    {
        main.reconnectWithServer();
        main.showLoginPageUsername();
    }

}
