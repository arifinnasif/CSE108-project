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

public class ManuDatactrl {
    private Main main;

    @FXML
    private TableView tableView;

    private ObservableList<CarRecord2> data;

    @FXML
    private Button backButton;

    private boolean init = true;

    void setMain(Main main) {
        this.main = main;
    }

    private void initializeColumns() {
        TableColumn<CarRecord2, String> regCol = new TableColumn<>("Registration");
        regCol.setMinWidth(110);
        regCol.setCellValueFactory(new PropertyValueFactory<CarRecord2,String>("reg"));

        TableColumn<CarRecord2, String> yearCol = new TableColumn<>("Year");
        yearCol.setMinWidth(60);
        yearCol.setCellValueFactory(new PropertyValueFactory<CarRecord2,String>("year"));

        TableColumn<CarRecord2, String> colorsCol = new TableColumn<>("Colors");
        colorsCol.setMinWidth(130);
        colorsCol.setCellValueFactory(new PropertyValueFactory<CarRecord2,String>("colors"));

        TableColumn<CarRecord2, String> makeCol = new TableColumn<>("Make");
        makeCol.setMinWidth(85);
        makeCol.setCellValueFactory(new PropertyValueFactory<CarRecord2,String>("make"));

        TableColumn<CarRecord2, String> modelCol = new TableColumn<>("Model");
        modelCol.setMinWidth(80);
        modelCol.setCellValueFactory(new PropertyValueFactory<CarRecord2,String>("model"));

        TableColumn<CarRecord2, String> priceCol = new TableColumn<>("Price");
        priceCol.setMinWidth(80);
        priceCol.setCellValueFactory(new PropertyValueFactory<CarRecord2,String>("price"));

        TableColumn<CarRecord2, String> quanCol = new TableColumn<>("Quantity");
        quanCol.setMinWidth(80);
        quanCol.setCellValueFactory(new PropertyValueFactory<CarRecord2,String>("quantity"));

        TableColumn<CarRecord2, Button> buttonCol1 = new TableColumn<>("Edit");
        buttonCol1.setMinWidth(70);
        buttonCol1.setPrefWidth(70);
        buttonCol1.setCellValueFactory(new PropertyValueFactory<CarRecord2,Button>("editButton"));

        TableColumn<CarRecord2, Button> buttonCol2 = new TableColumn<>("Delete");
        buttonCol2.setMinWidth(70);
        buttonCol2.setPrefWidth(70);
        buttonCol2.setCellValueFactory(new PropertyValueFactory<CarRecord2,Button>("deleteButton"));

        tableView.getColumns().addAll(regCol, yearCol, colorsCol, makeCol, modelCol, priceCol, quanCol, buttonCol1, buttonCol2);
    }

    public void load(CarRecord2[] carRecords) {
        if (init) {

            initializeColumns();

            init = false;
        }




        data = FXCollections.observableArrayList();
        data.clear();
        for (CarRecord2 record : carRecords)
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

        main.showManuHome();
    }
}
