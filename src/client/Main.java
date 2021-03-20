package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;

public class Main extends Application {

    Stage stage;
    Socket socket;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    Thread bgthread;
    Reader reader;
    String username;

    LoginPageUsernamectrl loginPageUsernamectrl;
    LoginPagePasswordctrl loginPagePasswordctrl;
    ViewerHomectrl viewerHomectrl;
    ViewerDatactrl viewerDatactrl;
    SearchByPropctrl searchByPropctrl;
    SearchByRegctrl searchByRegctrl;

    ManuHomectrl manuHomectrl;
    ManuDatactrl manuDatactrl;
    EditCarPagectrl editCarPagectrl;

    AdminDatactrl adminDatactrl;
    AdminHomectrl adminHomectrl;
    ChangePassPagectrl changePassPagectrl;
    AddUserPagectrl addUserPagectrl;

    void reconnectWithServer()
    {

        try {
            if(bgthread!=null&&(bgthread.isAlive()|| !bgthread.isInterrupted()))
            {
                bgthread.interrupt();
            }

            if(socket!=null&&!socket.isClosed())
            {
                socket.close();
            }

            socket = new Socket("127.0.0.1", 1234);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            reader = new Reader(ois);
            reader.setMain(this);


            bgthread = new Thread(reader);
            bgthread.start();
        }
        catch (IOException e)
        {
            // Failed to connect to the server

            System.exit(-1);

        }

    }

    @Override
    public void start(Stage primaryStage) {// throws Exception{
        stage = primaryStage;
        stage.setFullScreen(false);
        stage.setResizable(false);
        stage.setTitle("Car Showroom");



        reconnectWithServer();

        showLoginPageUsername();

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void showLoginPageUsername(){
        // throws IOException {
        // XML Loading using FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("loginPageUsername.fxml"));
        Parent root=null;

        try {
            root = loader.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }


        // Loading the controller
        loginPageUsernamectrl = loader.getController();
        loginPageUsernamectrl.setMain(this);


        // Set the primary stage
        stage.setOnCloseRequest(e-> {
            try{
                //oos.writeObject(new String("$COMMAND:EXIT"));
                bgthread.interrupt();
                oos.close();
                ois.close();
                socket.close();
            }
            catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
            finally {
                System.exit(0);
            }

        });
        stage.setTitle("Car Showroom - Login");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();

    }

    public void showLoginPagePassword(){
        // throws IOException {
        // XML Loading using FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("loginPagePassword.fxml"));
        Parent root=null;
        try {
            root = loader.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }

        // Loading the controller
        loginPagePasswordctrl = loader.getController();
        loginPagePasswordctrl.setMain(this);


        // Set the primary stage
        stage.setOnCloseRequest(e-> {
            try{
                //oos.writeObject(new String("$COMMAND:EXIT"));
                bgthread.interrupt();
                oos.close();
                ois.close();
                socket.close();
            }
            catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
            finally {
                System.exit(0);
            }

        });
        stage.setTitle("Car Showroom - Login");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();

    }

    public void showViewerHome(){
        // throws IOException {
        // XML Loading using FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("viewerHome.fxml"));
        Parent root=null;
        try {
            root = loader.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }

        // Loading the controller
        viewerHomectrl = loader.getController();
        viewerHomectrl.setMain(this);


        // Set the primary stage
        stage.setOnCloseRequest(e-> {
            try{
                oos.writeObject(new String("$COMMAND:EXIT"));
                bgthread.interrupt();
                oos.close();
                ois.close();
                socket.close();
            }
            catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
            finally {
                System.exit(0);
            }

        });
        stage.setTitle("Car Showroom - Home");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    public void showViewerData(CarRecord[] carRecords){
        // throws IOException {
        // XML Loading using FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("viewerData.fxml"));
        Parent root=null;
        try {
            root = loader.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }

        // Loading the controller
        viewerDatactrl = loader.getController();
        viewerDatactrl.setMain(this);

        Arrays.sort(carRecords);
        viewerDatactrl.load(carRecords);

        // Set the primary stage
        stage.setOnCloseRequest(e-> {
            try{
                oos.writeObject(new String("$COMMAND:EXIT"));
                bgthread.interrupt();
                oos.close();
                ois.close();
                socket.close();
            }
            catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
            finally {
                System.exit(0);
            }

        });
        stage.setTitle("Car Showroom - Cars");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    public void showSearchByProp(){
        // throws IOException {
        // XML Loading using FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("searchByProp.fxml"));
        Parent root=null;
        try {
            root = loader.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }

        // Loading the controller
        searchByPropctrl = loader.getController();
        searchByPropctrl.setMain(this);

        // Set the primary stage
        stage.setOnCloseRequest(e-> {
            try{
                oos.writeObject(new String("$COMMAND:EXIT"));
                bgthread.interrupt();
                oos.close();
                ois.close();
                socket.close();
            }
            catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
            finally {
                System.exit(0);
            }

        });
        stage.setTitle("Car Showroom - Search");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    public void showSearchByReg(){
        // throws IOException {
        // XML Loading using FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("searchByReg.fxml"));
        Parent root=null;
        try {
            root = loader.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }

        // Loading the controller
        searchByRegctrl = loader.getController();
        searchByRegctrl.setMain(this);

        // Set the primary stage
        stage.setOnCloseRequest(e-> {
            try{
                oos.writeObject(new String("$COMMAND:EXIT"));
                bgthread.interrupt();
                oos.close();
                ois.close();
                socket.close();
            }
            catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
            finally {
                System.exit(0);
            }

        });
        stage.setTitle("Car Showroom - Search");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    public void showManuHome(){
        // throws IOException {
        // XML Loading using FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("manuHome.fxml"));
        Parent root=null;
        try {
            root = loader.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }

        // Loading the controller
        manuHomectrl = loader.getController();
        manuHomectrl.setMain(this);
        manuHomectrl.setHead(username+" - Manufacturer");


        // Set the primary stage
        stage.setOnCloseRequest(e-> {
            try{
                oos.writeObject(new String("$COMMAND:EXIT"));
                bgthread.interrupt();
                oos.close();
                ois.close();
                socket.close();
            }
            catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
            finally {
                System.exit(0);
            }

        });
        stage.setTitle("Car Showroom - Home");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    public void showEditCarPage(String action){
        // throws IOException {
        // XML Loading using FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("editCarPage.fxml"));
        Parent root=null;
        try {
            root = loader.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }

        // Loading the controller

        editCarPagectrl = loader.getController();
        editCarPagectrl.setMain(this);
        editCarPagectrl.setAction(action);


        // Set the primary stage
        stage.setOnCloseRequest(e-> {
            try{
                oos.writeObject(new String("$COMMAND:EXIT"));
                bgthread.interrupt();
                oos.close();
                ois.close();
                socket.close();
            }
            catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
            finally {
                System.exit(0);
            }

        });
        if(action.equalsIgnoreCase("EDIT"))
            stage.setTitle("Car Showroom - Edit");
        else if(action.equalsIgnoreCase("ADD"))
            stage.setTitle("Car Showroom - Add");

        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    public void showManuData(CarRecord2[] carRecords){
        // throws IOException {
        // XML Loading using FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("manuData.fxml"));
        Parent root=null;
        try {
            root = loader.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }

        // Loading the controller
        manuDatactrl = loader.getController();
        manuDatactrl.setMain(this);
        //CarRecord carRecord[]=new CarRecord[1];
        //carRecord[0]=new CarRecord("sdsd,dsdsds,sdsds,dsd,sds,fff,wk,gh,www");
        Arrays.sort(carRecords);
        manuDatactrl.load(carRecords);
        //System.out.println("In main - showManuData");
        //reader.setViewerDatactrl(controller);

        // Set the primary stage
        stage.setOnCloseRequest(e-> {
            try{
                oos.writeObject(new String("$COMMAND:EXIT"));
                bgthread.interrupt();
                oos.close();
                ois.close();
                socket.close();
            }
            catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
            finally {
                System.exit(0);
            }

        });
        stage.setTitle("Car Showroom - Cars");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    public void alert(String title, String headerText, String contentText, int level)
    {
        Alert alert;
        if(level==0)
        {
            alert = new Alert(Alert.AlertType.CONFIRMATION);
        }
        if(level==1)
        {
            alert = new Alert(Alert.AlertType.INFORMATION);
        }
        if(level==2)
        {
            alert = new Alert(Alert.AlertType.WARNING);
        }
        if(level==3)
        {
            alert = new Alert(Alert.AlertType.ERROR);
        }
        else
        {
            alert = new Alert(Alert.AlertType.NONE);
            ButtonType noneButton=new ButtonType("OK");
            alert.getButtonTypes().setAll(noneButton);
        }

        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public void showAdminHome(){
        // throws IOException {
        // XML Loading using FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("adminHome.fxml"));
        Parent root=null;
        try {
            root = loader.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }

        // Loading the controller
        adminHomectrl = loader.getController();
        adminHomectrl.setMain(this);
        adminHomectrl.setHead(username+" - Admin");


        // Set the primary stage
        stage.setOnCloseRequest(e-> {
            try{
                oos.writeObject(new String("$COMMAND:EXIT"));
                bgthread.interrupt();
                oos.close();
                ois.close();
                socket.close();
            }
            catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
            finally {
                System.exit(0);
            }

        });
        stage.setTitle("Car Showroom - Home");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    public void showAdminData(UserRecord[] userRecords){
        // throws IOException {
        // XML Loading using FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("adminData.fxml"));
        Parent root=null;
        try {
            root = loader.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }

        // Loading the controller
        adminDatactrl = loader.getController();
        adminDatactrl.setMain(this);

        Arrays.sort(userRecords);
        adminDatactrl.load(userRecords);

        // Set the primary stage
        stage.setOnCloseRequest(e-> {
            try{
                oos.writeObject(new String("$COMMAND:EXIT"));
                bgthread.interrupt();
                oos.close();
                ois.close();
                socket.close();
            }
            catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
            finally {
                System.exit(0);
            }

        });
        stage.setTitle("Car Showroom - Manufacturers");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    public void showChangePassPage()
    {
        // throws IOException {
        // XML Loading using FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("changePassPage.fxml"));
        Parent root=null;
        try {
            root = loader.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }

        // Loading the controller
        changePassPagectrl = loader.getController();
        changePassPagectrl.setMain(this);

        // Set the primary stage
        stage.setOnCloseRequest(e-> {
            try{
                oos.writeObject(new String("$COMMAND:EXIT"));
                bgthread.interrupt();
                oos.close();
                ois.close();
                socket.close();
            }
            catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
            finally {
                System.exit(0);
            }

        });
        stage.setTitle("Car Showroom - Change Password");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    public void showAddUserPage()
    {
        // throws IOException {
        // XML Loading using FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("addUserPagectrl.fxml"));
        Parent root=null;
        try {
            root = loader.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }

        // Loading the controller
        addUserPagectrl = loader.getController();
        addUserPagectrl.setMain(this);

        // Set the primary stage
        stage.setOnCloseRequest(e-> {
            try{
                oos.writeObject(new String("$COMMAND:EXIT"));
                bgthread.interrupt();
                oos.close();
                ois.close();
                socket.close();
            }
            catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
            finally {
                System.exit(0);
            }

        });
        stage.setTitle("Car Showroom - Add User");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
