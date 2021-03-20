package client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class ViewerDatactrl {
    private Main main;

    @FXML
    private TableView tableView;

    private ObservableList<CarRecord> data;

    @FXML
    private Button backButton;

    private boolean init = true;

    void setMain(Main main) {
        this.main = main;
    }

    private void initializeColumns() {
        TableColumn<CarRecord, String> regCol = new TableColumn<>("Registration");
        regCol.setMinWidth(110);
        regCol.setCellValueFactory(new PropertyValueFactory<CarRecord,String>("reg"));

        TableColumn<CarRecord, String> yearCol = new TableColumn<>("Year");
        yearCol.setMinWidth(70);
        yearCol.setCellValueFactory(new PropertyValueFactory<CarRecord,String>("year"));

        TableColumn<CarRecord, String> colorsCol = new TableColumn<>("Colors");
        colorsCol.setMinWidth(150);
        colorsCol.setCellValueFactory(new PropertyValueFactory<CarRecord,String>("colors"));

        TableColumn<CarRecord, String> makeCol = new TableColumn<>("Make");
        makeCol.setMinWidth(100);
        makeCol.setCellValueFactory(new PropertyValueFactory<CarRecord,String>("make"));

        TableColumn<CarRecord, String> modelCol = new TableColumn<>("Model");
        modelCol.setMinWidth(100);
        modelCol.setCellValueFactory(new PropertyValueFactory<CarRecord,String>("model"));

        TableColumn<CarRecord, String> priceCol = new TableColumn<>("Price");
        priceCol.setMinWidth(80);
        priceCol.setCellValueFactory(new PropertyValueFactory<CarRecord,String>("price"));

        TableColumn<CarRecord, String> quanCol = new TableColumn<>("Quantity");
        quanCol.setMinWidth(80);
        quanCol.setCellValueFactory(new PropertyValueFactory<CarRecord,String>("quantity"));

        TableColumn<CarRecord, Button> buttonCol = new TableColumn<>("Action(s)");
        buttonCol.setMinWidth(80);
        buttonCol.setCellValueFactory(new PropertyValueFactory<CarRecord,Button>("button"));

        tableView.getColumns().addAll(regCol, yearCol, colorsCol, makeCol, modelCol, priceCol, quanCol, buttonCol);
    }

    public void load(CarRecord[] carRecords) {
        if (init) {

            initializeColumns();

            init = false;
        }



        data = FXCollections.observableArrayList();
        data.clear();
        for (CarRecord record : carRecords)
        {
            data.add(record);
        }


        tableView.setEditable(true);
        tableView.setItems(data);

        tableView.refresh();
    }

    public void backButtonAction(ActionEvent actionEvent)
    {
        try {
            main.oos.writeObject(new String("$COMMAND:EXITQUERY"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }

        main.showViewerHome();
    }

}
