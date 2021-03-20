package client;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AddUserPagectrl {
    @FXML
    private TextField usernameTf;

    @FXML
    private PasswordField pass1Tf;

    @FXML private PasswordField pass2Tf;

    @FXML
    private Button submitButton;

    private Main main;


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

        BooleanBinding usernameTextValid = Bindings.createBooleanBinding(() -> {
            if (usernameTf.getText().isEmpty()) return false;
            return true;
        }, usernameTf.textProperty());

        submitButton.disableProperty().bind(pass1TextValid.not().or(pass2TextValid.not().or(usernameTextValid.not())));
    }

    public void submitButtonAction(ActionEvent actionEvent)
    {
        if(pass1Tf.getText().equals(pass2Tf.getText()))
        {
            try {
                main.oos.writeObject(new String("$COMMAND:ADD_USER-"+usernameTf.getText()+","+pass1Tf.getText()));
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
        main.showAdminHome();
    }

}
