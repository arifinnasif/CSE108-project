package client;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class EditCarPagectrl {
    private Main main;
    private String reg;
    private Boolean isEdit;

    @FXML private Label headingLabel;

    @FXML private Label warnLabel;

    @FXML private TextField regTf;

    @FXML private TextField yearTf;

    @FXML private TextField color1Tf;

    @FXML private TextField color2Tf;

    @FXML private TextField color3Tf;

    @FXML private TextField makeTf;

    @FXML private TextField modelTf;

    @FXML private TextField priceTf;

    @FXML private TextField quantityTf;

    @FXML private Button submitButton;

    void setMain(Main main) {
        this.main = main;

        BooleanBinding regTextValid = Bindings.createBooleanBinding(() -> {
            if(regTf.getText().isEmpty()) return false;
            return true;
        }, regTf.textProperty());

        BooleanBinding yearTextValid = Bindings.createBooleanBinding(() -> {
            if(yearTf.getText().isEmpty()) return false;
            return true;
        }, yearTf.textProperty());

        BooleanBinding makeTextValid = Bindings.createBooleanBinding(() -> {
            if(makeTf.getText().isEmpty()) return false;
            return true;
        }, makeTf.textProperty());

        BooleanBinding modelTextValid = Bindings.createBooleanBinding(() -> {
            if(modelTf.getText().isEmpty()) return false;
            return true;
        }, modelTf.textProperty());

        BooleanBinding priceTextValid = Bindings.createBooleanBinding(() -> {
            if(priceTf.getText().isEmpty()) return false;
            return true;
        }, priceTf.textProperty());

        BooleanBinding quantityTextValid = Bindings.createBooleanBinding(() -> {
            if(quantityTf.getText().isEmpty()) return false;
            return true;
        }, quantityTf.textProperty());

        submitButton.disableProperty().bind(regTextValid.not().or(yearTextValid.not().or(makeTextValid.not().or(modelTextValid.not().or(priceTextValid.not().or(quantityTextValid.not()))))));

        BooleanBinding color1TextValid = Bindings.createBooleanBinding(() -> {
            if(color1Tf.getText().isEmpty()) return false;
            return true;
        }, color1Tf.textProperty());

        color2Tf.disableProperty().bind(color1TextValid.not());

        BooleanBinding color2TextValid = Bindings.createBooleanBinding(() -> {
            if(color2Tf.getText().isEmpty()) return false;
            return true;
        }, color2Tf.textProperty());

        color3Tf.disableProperty().bind(color2TextValid.not());
    }

    void setData(String reg, String year, String colors, String make, String model, String price, String quantity)
    {
        regTf.setText(reg);
        yearTf.setText(year);

        String[] str = colors.split(",");
        if(str.length>0&&str[0].length()>0) color1Tf.setText(str[0]);
        if(str.length>1) color2Tf.setText(str[1]);
        if(str.length>2) color3Tf.setText(str[2]);

        makeTf.setText(make);
        modelTf.setText(model);
        priceTf.setText(price);
        quantityTf.setText(quantity);
    }

    void setHeading (String heading)
    {
        headingLabel.setText(heading);
    }

    void setAction(String buttonType)
    {
        if(buttonType.equalsIgnoreCase("EDIT"))
        {
            isEdit=true;
        }
        else if(buttonType.equalsIgnoreCase("ADD"))
        {
            isEdit=false;
        }
    }

    void setReg(String reg)
    {
        this.reg=reg;
    }

    void warn(String message)
    {
        main.alert("Warning","Warning",message,3);
    }

    public void backButtonAction(ActionEvent actionEvent)
    {
        if(isEdit) {
            try {
                main.oos.writeObject(new String("$COMMAND:VIEW_BY_TYPE-ANY,ANY"));
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
        else
        {
            main.showManuHome();
        }
    }

    public void submitAction(ActionEvent actionEvent)
    {




        String buf="";
        int i=0;
        if(isInt(yearTf.getText())<1)
        {
            buf=buf+(++i)+"Year";
        }
        if(isInt(priceTf.getText())<1)
        {
            buf=buf+(++i)+"Price";
        }
        if(isInt(quantityTf.getText())<0)
        {
            buf=buf+(++i)+"Quantity";
        }




        if(i>0)
        {
            warn("Invalid "+buf.replaceAll(String.valueOf(1),"").replaceAll(String.valueOf(i)," and ").replaceAll(String.valueOf(2),", "));
            return;
        }

        try {
            if(isEdit) {
                main.oos.writeObject(new String("$COMMAND:EDIT_CAR-" + reg+":"+regTf.getText() + "," + yearTf.getText() + "," + color1Tf.getText() + "," + color2Tf.getText() + "," + color3Tf.getText() + "," + makeTf.getText() + "," + modelTf.getText() + "," + priceTf.getText() + "," + quantityTf.getText()));
            }
            else {
                main.oos.writeObject(new String("$COMMAND:ADD_CAR-" + regTf.getText() + "," + yearTf.getText() + "," + color1Tf.getText() + "," + color2Tf.getText() + "," + color3Tf.getText() + "," + makeTf.getText() + "," + modelTf.getText() + "," + priceTf.getText() + "," + quantityTf.getText()));
            }
            //main.oos.writeObject(new String(regTf.getText() + "," + yearTf.getText() + "," + color1Tf.getText() + "," + color2Tf.getText() + "," + color3Tf.getText() + "," + makeTf.getText() + "," + modelTf.getText() + "," + priceTf.getText() + "," + quantityTf.getText()));
            } catch (IOException ioException) {
                ioException.printStackTrace();
                System.exit(-1);
            }

    }

    private int isInt(String str)
    {
        try {
            int i=Integer.parseInt(str);
            return i;
        }
        catch (NumberFormatException e)
        {
            return -1;
        }
    }
}
