package general;

import java.io.Serializable;

public class Car implements Serializable {
    private String reg;
    private int year;
    private String color1, color2, color3;
    private String make;
    private String model;
    private int price;
    private int quantity;


    public Car(String arg)
    {
        String[] str=arg.split(",");
        this.reg=str[0];
        this.year=Integer.parseInt(str[1]);
        this.color1=((str[2]==null)? "": str[2]);
        this.color2=((str[3]==null)? "": str[3]);
        this.color3=((str[4]==null)? "": str[4]);
        this.make=str[5];
        this.model=str[6];
        this.price=Integer.parseInt(str[7]);
        this.quantity=Integer.parseInt(str[8]);
    }


    public Car(String reg, String year, String color1, String color2, String color3, String make, String model, String price)
    {
        this.reg=reg;
        this.year=Integer.parseInt(year);
        this.color1=((color1==null)? "": color1);
        this.color2=((color2==null)? "": color2);
        this.color3=((color3==null)? "": color3);
        this.make=make;
        this.model=model;
        this.price=Integer.parseInt(price);
    }

    public Car(String reg, int year, String color1, String color2, String color3, String make, String model, int price, int quantity) {
        this.reg = reg;
        this.year = year;
        this.color1=((color1==null)? "": color1);
        this.color2=((color2==null)? "": color2);
        this.color3=((color3==null)? "": color3);
        this.make = make;
        this.model = model;
        this.price = price;
        this.quantity=quantity;
    }

    public String getReg() {
        return reg;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getYear() {
        return year;
    }

    public String getColor1() {
        return color1;
    }

    public String getColor2() {
        return color2;
    }

    public String getColor3() {
        return color3;
    }

    public int getPrice() {
        return price;
    }

    public String info()
    {
        return reg+","+year+","+color1+","+color2+","+color3+","+make+","+model+","+price+","+quantity;
    }

    @Override
    public String toString() {
        return  "Registration : " + reg + '\n' +
                "Year Made : " + year + '\n' +
                "Color(s) : " + color1 + ((color2.isEmpty())? "":", ") + color2 + ((color3.isEmpty())? "":", ") + color3 + '\n' +
                "Car Make : " + make + '\n' +
                "Car Model : " + model + '\n' +
                "Price : " + price;
    }
}
