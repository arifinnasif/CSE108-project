package client;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

public class SearchByPropctrl {
    @FXML private TextField makeTf;

    @FXML private TextField modelTf;

    @FXML private Button search;

    @FXML private Button backButton;

    private Main main;

    void setMain(Main main) {
        this.main = main;
        BooleanBinding makeTextValid = Bindings.createBooleanBinding(() -> {
            if(makeTf.getText().isEmpty()) return false;
            return true;
        }, makeTf.textProperty());

        search.disableProperty().bind(makeTextValid.not());
    }

    public void sendQuery(ActionEvent actionEvent)
    {
        String make, model;
        make=makeTf.getText().trim();
        model=modelTf.getText().trim();
        if(model.isEmpty()) model="ANY";


        try {
            main.oos.writeObject(new String("$COMMAND:VIEW_BY_TYPE-"+make+","+model));
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
