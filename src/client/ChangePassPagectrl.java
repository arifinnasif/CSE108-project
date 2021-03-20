package client;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;

import java.io.IOException;

public class ChangePassPagectrl {
    @FXML private PasswordField pass1Tf;

    @FXML private PasswordField pass2Tf;

    @FXML private Button submitButton;

    private Main main;
    private String username;

    void setMain(Main main) {
        this.main = main;

        BooleanBinding pass1TextValid = Bindings.createBooleanBinding(() -> {
            if (pass1Tf.getText().isEmpty()) return false;
            return true;
        }, pass1Tf.textProperty());

        BooleanBinding pass2TextValid = Bindings.createBooleanBinding(() -> {
            if (pass2Tf.getText().isEmpty()) return false;
            return true;
        }, pass2Tf.textProperty());

        submitButton.disableProperty().bind(pass1TextValid.not().or(pass2TextValid.not()));
    }

    public void submitButtonAction(ActionEvent actionEvent)
    {
        if(pass1Tf.getText().equals(pass2Tf.getText()))
        {
            try {
                main.oos.writeObject(new String("$COMMAND:CHANGE_PASS-"+username+","+pass1Tf.getText()));
            } catch (IOException ioException) {
                ioException.printStackTrace();
                System.exit(-1);
            }
        }
        else
        {
            main.alert("Warning","Password didn't match","Please reenter the password",3);
        }
    }

    public void backButtonAction(ActionEvent actionEvent)
    {
        try {
            main.oos.writeObject(new String("$COMMAND:VIEW_ALL_USER"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
