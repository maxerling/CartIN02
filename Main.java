import Objects.Customer;
import Objects.Feedback.Feedback;
import Objects.Feedback.Grade;
import Objects.Invoice;
import Objects.Shoe.Shoe;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Max Erling
 * Date: 2021-02-19
 * Copyright: MIT
 * Class: Java20B
 */
public class Main {
    Main() {

        Repository repo = new Repository();
        Scanner sc = new Scanner(System.in);
        while (true) {
            logIn(sc, repo);
        }

    }

    public static void main(String[] args) {
        new Main();


    }

    public void logIn(Scanner sc, Repository repo) {
        System.out.println("Welcome! Insert username and password in order to add to cart");
        System.out.println("Username?");
        String user = sc.nextLine();
        System.out.println("Password");
        String pass = sc.nextLine();

        if (repo.auth(user, pass)) {
            System.out.println("Welcome " + user);
            getMenu(sc, repo, user);

        } else {
            System.out.println("Invalid credentials");
        }

    }

    public void getMenu(Scanner sc, Repository repo, String user) {
        while (true) {
            refresh(repo);
            System.out.println("Type the number based on the action you would like to do");
            System.out.println("1.Add product to cart");

            System.out.println("2.See cart");
            System.out.println("3.Review product");
            System.out.println("4.See average rating and comments of a product");
            System.out.println("5.Quit");
            String answer = sc.nextLine();
            if (answer.equals("1")) {
                addProductToCart(repo, sc, user);
            } else if (answer.equals("2")) {
                printCart(repo, user);

            } else if (answer.equals("3")) {
                reviewProduct(repo, sc, user);
            } else if (answer.equals("4")) {
                getProductAverageRating(repo, sc);
            } else if (answer.equals("5")) {
                System.out.println("Avslutar...");
                System.exit(0);
            } else {
                System.out.println("Invalid input");
            }

            System.out.println();
        }


    }

    public void addProductToCart(Repository repo, Scanner sc, String user) {
        int i = 0;
        List<Shoe> availableShoes = new ArrayList<>();
        for (Shoe s : repo.getShoes()) {

            if (!(s.getQuantity() == 0)) {
                System.out.println(i + 1 + " ----");
                System.out.println("Name: " + s.getName() + '\n'
                        + "Size: " + s.getSize() + '\n'
                        + "Color: " + s.getColor() + '\n'
                        + "Brand: " + s.getBrand() + '\n'
                        + "Quantity: " + s.getQuantity() + '\n'
                        + "Price: " + s.getPrice());
                Shoe temp = new Shoe();
                temp.setId(s.getId());
                temp.setName(s.getName());
                temp.setSize(s.getSize());
                temp.setColor(s.getColor());
                temp.setBrand(s.getBrand());
                temp.setQuantity(s.getQuantity());
                temp.setPrice(s.getPrice());
                temp.setCategories(s.getCategories());
                availableShoes.add(temp);
                System.out.println("----");
                i++;
            }
        }

        System.out.println("Pick a shoe");

        try {
            int options = sc.nextInt();
            options -= 1;
            if (options >= 0 && options < availableShoes.size()) {
                Shoe selectedShoe = availableShoes.get(options);
                int invoiceid = 0;
                for (Invoice in : repo.getInvoices()) {
                    if (in.getCustomer().getFirstName().equals(user)) {
                        invoiceid = in.getId();
                    }
                }

                for (Customer c : repo.getCustomers()) {
                    if (c.getFirstName().equals(user)) {
                        repo.addToCart(c.getId(), invoiceid, selectedShoe.getId());
                        break;
                    }
                }

            } else {
                System.out.println("Not valid input");
            }
        } catch (NumberFormatException n) {
            System.out.println("Must be a number!");
        }
        sc.nextLine();
        System.out.println();
    }

    public void printCart(Repository repo, String user) {
        int sum = 0;
        System.out.println("***CART***");
        for (Invoice i : repo.getInvoices()) {
            if (i.getCustomer().getFirstName().equals(user)) {
                for (Shoe s : i.getShoes()) {
                    System.out.println("Name: " + s.getName() + " Size: " + s.getSize() + " Color: " + s.getColor() + " Brand: " + s.getBrand() + " Quantity: " + i.getQuantity() + " Price: " + (i.getQuantity() * s.getPrice()));
                    sum += i.getQuantity() * s.getPrice();
                }
            }
        }
        System.out.println("Total price: " + sum);
        System.out.println();
    }

    public void reviewProduct(Repository repo, Scanner sc, String user) {
        int i = 0;
        List<Shoe> availableShoes = new ArrayList<>();
        for (Shoe s : repo.getShoes()) {
            System.out.println((i + 1) + ". Name: " + s.getName() + " Size: " + s.getSize() + " Color: " + s.getColor() + "\nBrand: " + s.getBrand());
            Shoe temp = new Shoe();
            temp.setId(s.getId());
            temp.setName(s.getName());
            temp.setSize(s.getSize());
            temp.setColor(s.getColor());
            temp.setBrand(s.getBrand());
            temp.setQuantity(s.getQuantity());
            temp.setPrice(s.getPrice());
            temp.setCategories(s.getCategories());
            availableShoes.add(temp);
            i++;

        }

        System.out.println("Pick a shoe");

        try {
            int options = sc.nextInt();
            options -= 1;
            if (options >= 0 && options < availableShoes.size()) {
                Shoe selectedShoe = availableShoes.get(options);

                System.out.println(selectedShoe.getName());
                setRatingForProduct(repo, sc, user,selectedShoe);


            } else {
                System.out.println("Not valid input");
            }
        } catch (NumberFormatException n) {
            System.out.println("Must be a number!");
        }
    }



    public int getCustomerId(Repository repo, String user) {
        for (Customer c : repo.getCustomers()) {
            if (c.getFirstName().equals(user)) {
                return c.getId();
            }
        }
        return 0;
    }

    public void setRatingForProduct(Repository repo, Scanner sc, String user,Shoe selectedShoe) {
        System.out.println("Rating?");
        for (Grade g : repo.getGrades()) {
            System.out.println(g.getRating() + ". " + g.getRatingText());
        }
        int options = sc.nextInt();

        System.out.println("Comment?");
        sc.nextLine();
        String comment = sc.nextLine();


        if (options > 0 && options <= 5) {
            repo.rateProduct(getCustomerId(repo, user),selectedShoe.getId(),options,comment);
        } else {
            System.out.println("Invalid input");
        }
    }


    public void getProductAverageRating(Repository repo, Scanner sc) {
        int i = 0;
        List<Shoe> availableShoes = new ArrayList<>();
        for (Shoe s : repo.getShoes()) {
            System.out.println((i+1) + ". Name: " + s.getName() + " Color: " + s.getColor());
            Shoe temp = new Shoe();
            temp.setId(s.getId());
            temp.setName(s.getName());
            temp.setSize(s.getSize());
            temp.setColor(s.getColor());
            temp.setBrand(s.getBrand());
            temp.setQuantity(s.getQuantity());
            temp.setPrice(s.getPrice());
            temp.setCategories(s.getCategories());
            availableShoes.add(temp);
            i++;
        }



        System.out.println("Vilken sko?");

        try {
            int input = sc.nextInt();
            input -= 1;
            if (input >= 0 && input < availableShoes.size()) {
                Shoe selectedShoe = availableShoes.get(input);
                System.out.println(selectedShoe.getName());
                System.out.println("Average Grade: " + repo.getAvgGrade(selectedShoe.getId()));
                System.out.println("Reviews:");
                for (Feedback f : repo.getFeedbacks()) {
                    if (selectedShoe.getId() == f.getShoeid()) {

                        if (f.getComment() != null) {
                            System.out.println("Comment: " + f.getComment());
                        } else {
                            System.out.println("Comment: ");
                        }

                    }

                }


            } else {
                System.out.println("Not valid input");
            }



            sc.nextLine();
            System.out.println();



        } catch (NumberFormatException n) {
            System.out.println("Must be a number!");
        }
    }

    public void refresh(Repository repo) {
        clearLists(repo);

        repo.setGrades(repo.getAllGrades());
        repo.setCustomers(repo.getAllCustomers());
        repo.setBrands(repo.getAllBrands());
        repo.setCategories(repo.getAllCategories());
        repo.setFeedbacks(repo.getAllFeedbacks());
        repo.setShoes(repo.getAllShoes());
        repo.setInvoices(repo.getAllInvoices());

    }

    public void clearLists(Repository repo) {
        repo.getDistinct().clear();
        repo.getShoes().clear();
        repo.getInvoices().clear();
        repo.getFeedbacks().clear();
        repo.getCustomers().clear();
        repo.getGrades().clear();
        repo.getBrands().clear();
        repo.getCategories().clear();



    }
}
