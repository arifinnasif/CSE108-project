package client;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

public class SearchByRegctrl {

    @FXML private Button search;

    @FXML private Button backButton;

    @FXML private TextField regTf;

    private Main main;

    void setMain(Main main) {
        this.main = main;

        BooleanBinding regTextValid = Bindings.createBooleanBinding(() -> {
            if(regTf.getText().isEmpty()) return false;
            return true;
        }, regTf.textProperty());

        search.disableProperty().bind(regTextValid.not());
    }

    public void sendQuery(ActionEvent actionEvent)
    {
        String reg;
        reg=regTf.getText().trim();


        try {
            main.oos.writeObject(new String("$COMMAND:VIEW_BY_REG-"+reg));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void goBack(ActionEvent actionEvent)
    {
        main.showViewerHome();
    }
}
