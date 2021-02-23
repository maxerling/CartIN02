import Objects.Customer;
import Objects.Feedback.Feedback;
import Objects.Feedback.Grade;
import Objects.Invoice;
import Objects.Shoe.Brand;
import Objects.Shoe.Category;
import Objects.Shoe.Shoe;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Max Erling
 * Date: 2021-02-19
 * Copyright: MIT
 * Class: Java20B
 */

/*

 */
public class Repository {
    private List<Customer> customers;
    private List<Invoice> invoices;
    private List<Feedback> feedbacks;
    private List<Grade> grades;
    private List<Shoe> shoes;
    private List<Brand> brands;
    private List<Category> categories;
    private List<Integer> distinct;

    Repository() {
        this.customers = new ArrayList<>();
        this.invoices = new ArrayList<>();
        this.feedbacks = new ArrayList<>();
        this.grades = new ArrayList<>();
        this.shoes = new ArrayList<>();
        this.brands = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.distinct = new ArrayList<>();

        this.customers = getAllCustomers();
        this.feedbacks = getAllFeedbacks();
        this.grades = getAllGrades();
        this.categories = getAllCategories();
        this.brands = getAllBrands();
        this.shoes = getAllShoes();
        this.invoices = getAllInvoices();

    }

    public boolean auth(String username, String password) {

        for (Customer customer : customers) {
            if (customer.getFirstName().equals(username) && customer.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public void addToCart(int customerid,int invoiceid,int productid) {
        try (Connection con = getConnection()) {
            CallableStatement cs = con.prepareCall("CALL addToCart(?,?,?,?)");
            cs.setInt(1,customerid);
            cs.setInt(2,invoiceid);
            cs.setInt(3,productid);
            cs.registerOutParameter(4, Types.VARCHAR);

            cs.executeQuery();
            System.out.println(cs.getString((4)));

        } catch (SQLException sql) {
            System.out.println(sql.getMessage() + " " + sql.getErrorCode());
        }
    }

    public void rateProduct(int customerid,String productName,int size, String color, int grade, String comment) {
        
    }


    public List<Customer> getAllCustomers() {
        String selectStm = "SELECT * FROM customer";

        try (Connection con = getConnection()) {
            Statement stm = con.createStatement();

            ResultSet rs = stm.executeQuery(selectStm);

            while (rs.next()) {
                customers = getCustomerModel(rs, customers);
            }

        } catch (SQLException sql) {
            System.out.println(sql.getMessage() + " " + sql.getErrorCode());
        }


        return customers;

    }

    public List<Invoice> getAllInvoices() {
        String selectStm = "SELECT * FROM invoice JOIN shoeinvoice ON invoice.id = shoeinvoice.invoiceid";

        try (Connection con = getConnection()) {
            Statement stm = con.createStatement();

            ResultSet rs = stm.executeQuery(selectStm);

            while (rs.next()) {
                invoices = getInvoiceModel(rs, invoices);
            }

        } catch (SQLException sql) {
            System.out.println(sql.getMessage() + " " + sql.getErrorCode());
        }


        return invoices;

    }

    public List<Feedback> getAllFeedbacks() {
        String selectStm = "SELECT * FROM feedback";

        try (Connection con = getConnection()) {
            Statement stm = con.createStatement();

            ResultSet rs = stm.executeQuery(selectStm);

            while (rs.next()) {
                feedbacks = getFeedbackModel(rs, feedbacks);
            }

        } catch (SQLException sql) {
            System.out.println(sql.getMessage() + " " + sql.getErrorCode());
        }


        return feedbacks;

    }

    public List<Grade> getAllGrades() {
        String selectStm = "SELECT * FROM grade";

        try (Connection con = getConnection()) {
            Statement stm = con.createStatement();

            ResultSet rs = stm.executeQuery(selectStm);

            while (rs.next()) {
                grades = getGradeModel(rs, grades);
            }

        } catch (SQLException sql) {
            System.out.println(sql.getMessage() + " " + sql.getErrorCode());
        }


        return grades;

    }

    public List<Shoe> getAllShoes() {
        String selectStm = "SELECT * FROM shoe LEFT JOIN feedback ON shoe.id = feedback.shoeid JOIN shoecategory ON shoe.id = shoecategory.shoeid ORDER BY shoe.name";

        try (Connection con = getConnection()) {
            Statement stm = con.createStatement();

            ResultSet rs = stm.executeQuery(selectStm);

            while (rs.next()) {
                shoes = getShoeModel(rs, shoes);
            }

        } catch (SQLException sql) {
            System.out.println(sql.getMessage() + " " + sql.getErrorCode());
        }


        return shoes;

    }

    public List<Brand> getAllBrands() {
        String selectStm = "SELECT * FROM brand";

        try (Connection con = getConnection()) {
            Statement stm = con.createStatement();

            ResultSet rs = stm.executeQuery(selectStm);

            while (rs.next()) {
                brands = getBrandModel(rs, brands);
            }

        } catch (SQLException sql) {
            System.out.println(sql.getMessage() + " " + sql.getErrorCode());
        }


        return brands;

    }


    public List<Category> getAllCategories() {
        String selectStm = "SELECT * FROM category";

        try (Connection con = getConnection()) {
            Statement stm = con.createStatement();

            ResultSet rs = stm.executeQuery(selectStm);

            while (rs.next()) {
                categories = getCategoryModel(rs, categories);
            }

        } catch (SQLException sql) {
            System.out.println(sql.getMessage() + " " + sql.getErrorCode());
        }


        return categories;

    }

    public List<Customer> getCustomerModel(ResultSet rs, List<Customer> list) {
        Customer temp = new Customer();
        try {
            temp.setId(rs.getInt("id"));
            temp.setFirstName(rs.getString("first_name"));
            temp.setLastName(rs.getString("last_name"));
            temp.setPassword(rs.getString("password"));
            temp.setStreetAddress(rs.getString("street_address"));
            temp.setSSN(rs.getString("ssn"));
            temp.setZip(rs.getInt("zip"));
            temp.setCity(rs.getString("city"));
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage() + " " + sqlException.getErrorCode());
        }

        list.add(temp);

        return list;
    }

    public List<Invoice> getInvoiceModel(ResultSet rs, List<Invoice> list) {
        Invoice temp = new Invoice();
        try {
            temp.setId(rs.getInt("id"));

            for (Customer c : customers) {
                if (c.getId() == rs.getInt("customerid")) {
                    temp.setCustomer(c);
                }
            }

            temp.setToStreet(rs.getString("to_street"));
            temp.setToZip(rs.getInt("to_zip"));
            temp.setToCity(rs.getString("to_city"));

            for (Shoe s : shoes) {
                if (s.getId() == rs.getInt("shoeid")) {
                    temp.getShoes().add(s);
                }
            }

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage() + " " + sqlException.getErrorCode());
        }
        list.add(temp);

        return list;

    }

    public List<Shoe> getShoeModel(ResultSet rs, List<Shoe> list) {

        Shoe temp = new Shoe();
        try {
            temp.setId(rs.getInt("shoe.id"));
            temp.setName(rs.getString("name"));
            temp.setSize(rs.getInt("size"));
            temp.setColor(rs.getString("color"));
            temp.setPrice(rs.getInt("price"));
            temp.setQuantity(rs.getInt("quantity"));

            for (Feedback f : feedbacks) {
                if (f.getShoeid() == rs.getInt("shoe.id")) {
                    temp.getFeedbacks().add(f);

                }

            }


            if (!(distinct.contains(temp.getId()))) {
                list.add(temp);
            }
            distinct.add(rs.getInt("id"));

            for (Brand b : brands) {
                if (b.getId() == rs.getInt("brandid"))
                temp.setBrand(b);
            }

            for (Category c : categories) {
                if (c.getId() == rs.getInt("shoecategory.categoryid")) {
                    temp.getCategories().add(c);
                }
            }



        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage() + " " + sqlException.getErrorCode());
        }


        return list;
    }

    public List<Brand> getBrandModel(ResultSet rs, List<Brand> list) {
        Brand temp = new Brand();
        try {
            temp.setId(rs.getInt("id"));
            temp.setName(rs.getString("name"));
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage() + " " + sqlException.getErrorCode());
        }
        list.add(temp);

        return list;
    }

    public List<Category> getCategoryModel(ResultSet rs, List<Category> list) {
        Category temp = new Category();
        try {
            temp.setId(rs.getInt("id"));
            temp.setName(rs.getString("name"));
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage() + " " + sqlException.getErrorCode());
        }
        list.add(temp);

        return list;
    }


    public List<Feedback> getFeedbackModel(ResultSet rs, List<Feedback> list) {
        Feedback temp = new Feedback();
        try {
            temp.setId(rs.getInt("id"));
            temp.setComment(rs.getString("comment"));
            temp.setShoeid(rs.getInt("shoeid"));

            if (temp.getComment() == null) {
                temp.setComment("");
            }

            for (Customer c : customers) {
                if (c.getId() == rs.getInt("customerid")) {
                    temp.setCustomer(c);
                }
            }


            for (Grade g : grades) {
                if (g.getId() == rs.getInt("gradeid")) {
                    if (g.getRatingText() == null) {
                        g.setRatingText("");
                    }
                    temp.setGrade(g);
                }
            }

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage() + " " + sqlException.getErrorCode());
        }
        list.add(temp);

        return list;

    }

    public List<Grade> getGradeModel(ResultSet rs, List<Grade> list) {
        Grade temp = new Grade();
        try {
            temp.setId(rs.getInt("id"));
            temp.setRating(rs.getInt("rating"));
            temp.setRatingText(rs.getString("ratingtext"));
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage() + " " + sqlException.getErrorCode());
        }
        list.add(temp);

        return list;
    }


    public Connection getConnection() throws SQLException {
        String user = "";
        String pass = "";
        Properties p = new Properties();
        try {
            FileInputStream in = new FileInputStream("C:\\Users\\m\\Desktop\\Java20\\Databasteknik och Java\\Cart\\recourses\\db.properties");
            p.load(in);
            in.close();

            user = p.getProperty("user");
            pass = p.getProperty("pass");

        } catch (FileNotFoundException file) {
            file.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        }

        return DriverManager.getConnection("jdbc:mysql://localhost:3306/shopdb?serverTimezone=UTC&useSSL=false", user, pass);
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public List<Shoe> getShoes() {
        return shoes;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<Integer> getDistinct() {
        return distinct;
    }
}