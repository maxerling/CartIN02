import Objects.Customer;
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

        //System.out.println(repo.getAllInvoices());
        //System.out.println(repo.getAllShoes());
        //logIn(sc,repo);

        //System.out.println(repo.getAllInvoices());
        logIn(sc,repo);
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

        if (repo.auth(user,pass)) {
            System.out.println("Welcome " + user);
            getMenu(sc, repo,user);

        } else {
            System.out.println("Invalid credentials");
        }

    }

    public void getMenu(Scanner sc, Repository repo,String user) {
        System.out.println("Type the number based on the action you would like to do");
        System.out.println("1.Add product to cart");
        // lista på prdoukter man kan välja mellan (ej quantity 0) namn,storlek,färg,antal,kvantiet
        //väljer en av dom, necarssy info AddToCart(?.?.?)
        // läggga in det i en nuvarande beställning, skapa en ny beställning
        // confirm or denied (SQLEXCPETION???)
        System.out.println("2.See cart");
        //Tar fram alla ens beställningar baserat på användarnamn
        //getAllInvoices based on username, loop thorugh
        // after found correct match loop through getAllShoes
        System.out.println("3.Review product");
        //Tar fram alla skor och
        System.out.println("4.See average rating and comments of a product");
        String answer = sc.nextLine();
        if (answer.equals("1")) {
            addProductToCart(repo,sc,user);
        } else if (answer.equals("2")) {

        } else if (answer.equals("3")) {

        } else if (answer.equals("4")) {

        } else {
            System.out.println("Invalid input");
        }

    }

    public void addProductToCart(Repository repo, Scanner sc, String user) {
        int i = 0;
        List<Shoe> availableShoes = new ArrayList<>();
        for (Shoe s : repo.getAllShoes()) {

            if (!(s.getQuantity() == 0)) {
                System.out.println(i+1 + " ----");
                System.out.println("Name: " + s.getName() + '\n'
                        + "Size: " + s.getSize() + '\n'
                        + "Color: " + s.getColor() + '\n'
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
        System.out.println(availableShoes.size());
        System.out.println(availableShoes);
        try {
            int options = sc.nextInt();
            options -= 1;
            if (options >= 0 && options < availableShoes.size()) {
                Shoe selectedShoe = availableShoes.get(options);
                System.out.println(selectedShoe);

                for (Customer c : repo.getAllCustomers()) {
                    if (c.getFirstName().equals(user)) {
                        System.out.println(c);
                        repo.addToCart(c.getId(),0,selectedShoe.getId());
                    }
                }

            } else {
                System.out.println("Not valid input");
            }
        } catch (NumberFormatException n) {
            System.out.println("Must be a number!");
        }
    }
}
