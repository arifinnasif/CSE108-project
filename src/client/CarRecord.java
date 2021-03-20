package client;

import general.Car;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

import java.io.IOException;

public class CarRecord implements Comparable{
    private final SimpleStringProperty reg;
    private final SimpleStringProperty year;
    private final SimpleStringProperty colors;
    private final SimpleStringProperty make;
    private final SimpleStringProperty model;
    private final SimpleStringProperty price;
    private final SimpleStringProperty quantity;
    private final Button button;

    private Main main;

    void setMain(Main main) {
        this.main = main;
    }


    public CarRecord(String c)
    {
        String[] prop = c.split(",");
        this.reg=new SimpleStringProperty(prop[0]);
        this.year=new SimpleStringProperty(prop[1]);
        this.colors=new SimpleStringProperty(prop[2] + ((prop[3].isEmpty())? "":", ") + prop[3] + ((prop[4].isEmpty())? "":", ") + prop[4]);
        this.make=new SimpleStringProperty(prop[5]);
        this.model=new SimpleStringProperty(prop[6]);
        this.price=new SimpleStringProperty(prop[7]);
        this.quantity=new SimpleStringProperty(prop[8]);
        button=new Button("Buy");
        button.setOnAction(e->
                {
                    try {
                        main.oos.writeObject("$COMMAND:BUY-"+prop[0]);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
        );
    }


    public String getReg() {
        return reg.get();
    }

    public String getYear() {
        return year.get();
    }

    public String getColors() {
        return colors.get();
    }

    public String getMake() {
        return make.get();
    }

    public String getModel() {
        return model.get();
    }

    public String getPrice() {
        return price.get();
    }

    public String getQuantity() {
        return quantity.get();
    }

    public Button getButton() {
        return button;
    }

    public void setReg(String reg) {
        this.reg.set(reg);
    }

    public void setYear(String year) {
        this.year.set(year);
    }

    public void setColors(String colors) {
        this.colors.set(colors);
    }

    public void setMake(String make) {
        this.make.set(make);
    }

    public void setModel(String model) {
        this.model.set(model);
    }

    public void setPrice(String price) {
        this.price.set(price);
    }

    public void setQuantity(String quantity) {
        this.quantity.set(quantity);
    }

    @Override
    public String toString() {
        return reg +
                ", " + year +
                ", " + colors +
                ", " + make +
                ", " + model +
                ", " + price +
                ", " + quantity ;
    }

    @Override
    public int compareTo(Object o) {
        return this.reg.get().compareTo(((CarRecord) o).getReg());
    }
}
