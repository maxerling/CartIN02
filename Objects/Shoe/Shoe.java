package Objects.Shoe;

import Objects.Feedback.Feedback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Max Erling
 * Date: 2021-02-19
 * Copyright: MIT
 * Class: Java20B
 */
public class Shoe {
    private int id;
    private String name;
    private int size;
    private String color;
    private int price;
    private int quantity;
    private List<Feedback> feedbacks;
    private Brand brand;
    private List<Category> categories;

    public Shoe() {
        this.feedbacks = new ArrayList<>();
        this.categories = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "Shoe{" +
                 id +
                "," + name + '\'' +
                "," + size +
                ",'" + color + '\'' +
                "," + price +
                "," + quantity +
                "," + "|" + feedbacks + "|"+
                "," + brand +
                "," + categories +
                '}';
    }
}
