package server;

import general.Car;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
//import java.sql.*;


public class CarDB {
    private static ArrayList<Car> carList;
    private static Connection conn;
    private static boolean isLoaded=false;

    static void load() throws IOException, SQLException {
        if(isLoaded) return;
        conn = DriverManager.getConnection("jdbc:sqlite:cars.db");
        System.out.println("Connected to car database");
    }

    synchronized static Car[] searchCar(String make, String model) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet resultSet;
        if(make.equalsIgnoreCase("ANY") && model.equalsIgnoreCase("ANY"))
        {
            resultSet = statement.executeQuery("SELECT * FROM cars");
        }
        else if(model.equalsIgnoreCase("ANY"))
        {
            resultSet = statement.executeQuery("SELECT * FROM cars WHERE carmake = \'"+make.toLowerCase()+"\';");
        }
        else
        {
            resultSet = statement.executeQuery("SELECT * FROM cars WHERE carmake = \'"+make.toLowerCase()+"\' AND carmodel = \'"+model.toLowerCase()+"\';");
        }
        ArrayList<Car> ret = new ArrayList<>();
        while(resultSet.next())
        {
            ret.add(new Car(resultSet.getString(2),resultSet.getInt(3),resultSet.getString(4),resultSet.getString(5),resultSet.getString(6),resultSet.getString(7),resultSet.getString(8),resultSet.getInt(9),resultSet.getInt(10)));
        }
        statement.close();
        return (Car[]) ret.toArray(new Car[0]);
    }

    synchronized static Car[] searchCar(String reg) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM cars WHERE registration = \'"+reg.toLowerCase()+"\';");
        //resultSet.last();
        int i=0, size = resultSet.getRow();
        //resultSet.first();
        //Car[] ret = new Car[size];
        LinkedList<Car> ret = new LinkedList<>();
        while(resultSet.next())
        {
            ret.add(new Car(resultSet.getString(2),resultSet.getInt(3),resultSet.getString(4),resultSet.getString(5),resultSet.getString(6),resultSet.getString(7),resultSet.getString(8),resultSet.getInt(9),resultSet.getInt(10)));
            //ret[i++] = new Car(resultSet.getString(2),resultSet.getInt(3),resultSet.getString(4),resultSet.getString(5),resultSet.getString(6),resultSet.getString(7),resultSet.getString(8),resultSet.getInt(9),resultSet.getInt(10));
        }
        statement.close();
        return ret.toArray(new Car[0]);
    }

    synchronized static int addCar(Car car) throws SQLException
    {

        return addCar(car.getReg(),car.getYear(), car.getColor1(), car.getColor2(),car.getColor3(),car.getMake(),car.getModel(),car.getPrice(),car.getQuantity());
    }

    private synchronized static int addCar(String reg, int year, String color1, String color2, String color3, String make, String model, int price, int quantity) throws SQLException
    {
        if(searchCar(reg).length!=0) return -1;
        PreparedStatement statement = conn.prepareStatement("INSERT INTO cars(registration, year, color1, color2, color3, carmake, carmodel, price, quantity) VALUES(?,?,?,?,?,?,?,?,?);");
        statement.setString(1,reg.toLowerCase());
        statement.setInt(2,year);
        statement.setString(3,color1.toLowerCase());
        statement.setString(4,color2.toLowerCase());
        statement.setString(5,color3.toLowerCase());
        statement.setString(6,make.toLowerCase());
        statement.setString(7,model.toLowerCase());
        statement.setInt(8,price);
        statement.setInt(9,quantity);
        int count = statement.executeUpdate();
        statement.close();
        return count;
    }

    synchronized static int deleteCar(String reg) throws SQLException {
        if (searchCar(reg).length==0) return -1;
        PreparedStatement statement = conn.prepareStatement("DELETE FROM cars WHERE registration = \'"+reg.toLowerCase()+"\';");
        int count = statement.executeUpdate();
        statement.close();
        return count;
    }

    synchronized static int editCar(String reg, Car car) throws SQLException { // SET command use kora lagbe. na hoile ek registration number dui jon er. mane edit kore jodi arek joner ta boshaye dey.
        if(searchCar(reg).length==0)
        {
            return -1;
        }
        if((!reg.equals(car.getReg()))&&searchCar(car.getReg()).length!=0)
        {
            return -2;
        }
        deleteCar(reg);
        return addCar(car);
    }

    synchronized static int decrease(String reg) throws SQLException
    {
        Car[] res = searchCar(reg);
        if (res.length==0||res[0].getQuantity()==0) return -1;
        PreparedStatement statement = conn.prepareStatement("UPDATE cars SET quantity = quantity - 1 WHERE registration = \'"+reg.toLowerCase()+"\';");
        int count = statement.executeUpdate();
        statement.close();
        return count;
    }

}

