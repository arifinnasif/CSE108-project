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

public class AdminDatactrl {
    Main main;

    @FXML
    private TableView tableView;

    private ObservableList<UserRecord> data;

    @FXML
    private Button backButton;

    private boolean init = true;

    void setMain(Main main) {
        this.main = main;
    }

    private void initializeColumns() {
        TableColumn<UserRecord, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setMinWidth(520);
        usernameCol.setCellValueFactory(new PropertyValueFactory<UserRecord,String>("username"));

        TableColumn<UserRecord, Button> buttonCol1 = new TableColumn<>("Change Password");
        buttonCol1.setCellValueFactory(new PropertyValueFactory<UserRecord,Button>("changePassButton"));

        TableColumn<UserRecord, Button> buttonCol2 = new TableColumn<>("Delete");
        buttonCol2.setMinWidth(90);
        buttonCol2.setCellValueFactory(new PropertyValueFactory<UserRecord,Button>("deleteButton"));

        tableView.getColumns().addAll(usernameCol, buttonCol1, buttonCol2);
    }

    public void load(UserRecord[] userRecords) {
        if (init) {

            initializeColumns();

            init = false;
        }



        data = FXCollections.observableArrayList();
        data.clear();
        for (UserRecord record : userRecords)
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

        main.showAdminHome();
    }
}
