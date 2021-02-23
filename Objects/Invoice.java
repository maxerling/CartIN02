package Objects;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import Objects.Shoe.Shoe;

/**
 * Created by Max Erling
 * Date: 2021-02-19
 * Copyright: MIT
 * Class: Java20B
 */
public class Invoice {
    private int id;
    private Customer customer;
    private List<Shoe> shoes;
    private String toStreet;
    private int toZip;
    private String toCity;
    private Date date;

    public Invoice() {
        this.shoes = new ArrayList<>();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }




    public List<Shoe> getShoes() {
        return shoes;
    }

    public void setShoes(List<Shoe> shoes) {
        this.shoes = shoes;
    }

    public String getToStreet() {
        return toStreet;
    }

    public void setToStreet(String toStreet) {
        this.toStreet = toStreet;
    }

    public int getToZip() {
        return toZip;
    }

    public void setToZip(int toZip) {
        this.toZip = toZip;
    }

    public String getToCity() {
        return toCity;
    }

    public void setToCity(String toCity) {
        this.toCity = toCity;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                 id +
                "," + customer +
                "," + shoes +
                "," + toStreet + '\'' +
                "," + toZip +
                "," + toCity + '\'' +
                '}';
    }
}
