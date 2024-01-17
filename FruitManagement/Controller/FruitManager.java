
package Controller;

import Common.Library;
import Model.Fruit;
import Model.Order;
import View.Menu;
import java.util.ArrayList;
import java.util.Hashtable;

public class FruitManager extends Menu<String> {
    static String[] mc = { "Create Fruit", "View orders", "Shopping(for buyer)", "Exit" };
    Library l;
    ArrayList<Fruit> fruitsList;
    Hashtable<String, ArrayList<Order>> ht;

    public FruitManager() {
        super("\nFruit Shop Manager:", mc);
        l = new Library();
        fruitsList = new ArrayList<>();
        ht = new Hashtable<>();
    }

    @Override
    public void execute(int n) {
        switch (n) {
            case 1:
                createFruit();
                break;
            case 2:
                viewListOrder();
                break;
            case 3:
                shopping();
                break;
            case 4:
                System.exit(0);
        }
    }

    public void createFruit() {
        while (true) {
            int id = generateID();
            String name = l.getString("Enter fruit name: ");
            double price = l.getDouble("Enter fruit price");
            int quantity = l.getInt("Enter fruit quantity", 1, 100);
            String origin = l.getString("Enter fruit origin: ");
            fruitsList.add(new Fruit(id, name, price, quantity, origin));
            System.out.println("Add Successfull");
            if (!l.getString("Do you want to add another fruit?(Y,N): ").equalsIgnoreCase("Y")) {
                break;
            }
        }
    }

    public void displayFruit() {
        for (Fruit f : fruitsList) {
            // System.out.println("Id: " + f.getId() + " | Name: " + f.getName() + " |
            // Price: " + f.getPrice() + " | Quantity:" + f.getQuantity() + " | Origin: " +
            // f.getOrigin());
            System.out.printf("|  %2d  |  %10s  |  %5.2f  |  %7d  |  %10s  |\n", f.getId(), f.getName(), f.getPrice(),
                    f.getQuantity(), f.getOrigin());
        }
    }

    public void viewListOrder() {
        if (ht.isEmpty()) {
            System.out.println("\nThere are no order!!!");
            return;
        }
        for (String name : ht.keySet()) {
            System.out.println("\nCustomer: " + name);
            ArrayList<Order> orders = ht.get(name);
            displayListOrder(orders);
        }
    }

    public void shopping() {
        fruitsList = refreshFruitList(fruitsList);
        if (fruitsList.isEmpty()) {
            System.out.println("\nThere are no product!!!");
            return;
        }
        System.out.println("|  ID  |     Name     |  Price  | Quanlity  |    Origin    |");
        // System.out.println("");
        displayFruit();
        ArrayList<Order> odersList = new ArrayList<>();
        while (true) {
            int idF = l.getInt("Select item", 1, fruitsList.size());
            for (Fruit f : fruitsList) {
                if (idF == f.getId()) {
                    int id = generateIDOrder(odersList);
                    String name = f.getName();
                    System.out.println("You selected: " + name);
                    double price = f.getPrice();
                    int quantity = f.getQuantity();
                    int quantityOrder = l.getInt("Please input quantity", 1, quantity);
                    f.setQuantity(quantity - quantityOrder);
                    odersList.add(new Order(id, name, quantityOrder, price));
                }
            }
            if (!l.getString("Do you want to buy another fruit?(Y/N): ").equalsIgnoreCase("Y")) {
                break;
            }
        }
        displayListOrder(odersList);
        String customer = l.getString("Enter Customer of name: ");
        ht.put(customer, odersList);
        System.out.println("Add customer successfull!!!");
    }

    private void displayListOrder(ArrayList<Order> ordersList) {
        double total = 0;
        System.out.println("|   Product    |  Quanlity  |  Price  |   Amount  |");
        for (Order o : ordersList) {
            // System.out.println("Product: " + o.getId()+"."+o.getName() + " | Quanlity: "
            // + o.getQuanlity() + " | Price: " + o.getPrice() + " | Amount: "+
            // o.getQuanlity() * o.getPrice());
            System.out.printf("|  %10s  |  %8d  |  %5.2f  |  %7.2f  | ", o.getName(), o.getQuanlity(), o.getPrice(),
                    o.getQuanlity() * o.getPrice());
            System.out.println("");
            total += o.getPrice() * o.getQuanlity();
        }
        System.out.println("");
        System.out.println("Total: " + total);
    }

    public int generateID() {
        int id = 0;
        if (fruitsList.isEmpty()) {
            return 1;
        } else {

            for (Fruit s : fruitsList) {
                if (s.getId() == fruitsList.size()) {
                    id = s.getId() + 1;
                }
            }
        }
        return id;
    }

    public int generateIDOrder(ArrayList<Order> ordersList) {
        int id = 0;
        if (ordersList.isEmpty()) {
            return 1;
        } else {
            for (Order s : ordersList) {
                if (s.getId() == ordersList.size()) {
                    id = s.getId() + 1;
                }
            }
        }
        return id;
    }

    public ArrayList<Fruit> refreshFruitList(ArrayList<Fruit> fruitsList) {
        ArrayList<Fruit> temp = new ArrayList<>();
        int n = fruitsList.size();
        for (int i = 0; i < n; i++) {
            Fruit currentFruit = fruitsList.get(i);
            if (currentFruit.getQuantity() > 0) {
                temp.add(currentFruit);
            } else if (i + 1 < n) {
                Fruit nextFruit = fruitsList.get(i + 1);
                nextFruit.setId(currentFruit.getId());
                temp.add(nextFruit);
                i++;
            }
        }
        return temp;
    }
}
