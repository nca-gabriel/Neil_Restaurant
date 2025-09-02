import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.*;

public class Main
{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Neil's Restaurant!\n");

        int choice;
        double totalAmount = 0; //this is where the price is going to get added
        List<String> orderItems = new ArrayList<>(); //this where the order is going to go

        while (true) //this is an ongoing loop so the user can always take their time and choose
        {
            System.out.println("Please select an option:");
            System.out.println("1. Menu");
            System.out.println("2. Desserts");
            System.out.println("3. Drinks");
            System.out.println("4. Finish Order"); //this the exit method
            System.out.println("5. Clear Order"); //this for wrong order to reset the list
            System.out.print("\nEnter your choice: ");

            try { //in case of wrong input
                choice = scanner.nextInt();
            } catch (InputMismatchException wrongchoice){
                System.out.println("Invalid choice. Try using numerical value (1-5)\n");
                scanner.nextLine();
                continue;
            }


            switch (choice) {
                case 1 -> {
                    dmenu(); // Display menu items
                    totalAmount += Orderp(menu, orderItems, scanner); // Process order for menu items
                }
                case 2 -> {
                    ddessert(); // Display dessert items
                    totalAmount += Orderp(dessert, orderItems, scanner); // Process order for dessert items
                }
                case 3 -> {
                    ddrinks(); // Display drink items
                    totalAmount += Orderp(drinks, orderItems, scanner); // Process order for drink items
                }
                case 4 -> {
                    if (orderItems.isEmpty()){ //In case of user trying to cash out without ordering
                        System.out.println("No order found. Please place an order first (order ka muna)\n");
                        continue;
                    }
                    double tax = totalAmount * taxRATE;
                    double Grandtotal = totalAmount + tax;
                    System.out.println("\nTotal Amount is PHP " + Grandtotal);
                    double paymentAmount;

                    try{ //in case user paid with word cash (five hundred pesos)
                        paymentAmount = Payment(scanner);
                    }catch (InputMismatchException pay){
                        System.out.println("\nInvalid input. Please use numerical values (0-9). Your order is still pending (Select 4 to transact again)");
                        System.out.println("or (Select 5 to clear your order)\n");
                        scanner.nextLine();
                        continue;
                    }

                    if (paymentAmount < Grandtotal) {
                        System.out.println("Insufficient fund please try again! Your order is still pending (Select 4 to transact again)");
                        System.out.println("or (Select 5 to clear your order)\n");
                        continue;
                    }
                    double change = paymentAmount - Grandtotal;
                    System.out.println("\nYour Change is PHP " + change);
                    System.out.println("\n--- RECEIPT ---\n");
                    Receipt(orderItems); // Print the receipt
                    scanner.close();
                    return;
                }
                case 5 -> {
                    System.out.println("Your order has been nullified\n");
                    totalAmount = 0;
                    orderItems.clear();
                }
                default -> System.out.println("Invalid choice. Please try again.\n");
            }
        }
    }

    public static double taxRATE = 0.1; //Tax cuz government somehow
    //ArrayList of food
    public static String[] menu = {
            "1. Barbeque - Php 80.00",
            "2. Fried Chicken - Php 100.00",
            "3. Dumplings - Php 150.00",
            "4. Sinigang (Fish) - Php 200.00",
            "5. Sinigang (Pork) - Php 220.00",
            "6. Bulalo - Php 250.00",
            "7. Sisig - Php 160.00",
            "8. Siomai Rice - Php 50.00",
            "9. Bulalo - Php 160.00",
            "10. Lomi - Php 80.00"
    };

    public static String[] dessert = {
            "1. Halo-halo - Php 100.00",
            "2. Puto - Php 50.00",
            "3. Pichi-pichi - Php 80.00",
            "4. Leche Flan - Php 70.00",
            "5. Ginataang Bilo-bilo - Php 100.00",
            "6. Cake Ube falvor - Php 90.00"
    };

    public static String[] drinks = {
            "1. Coffee - Php 50.00",
            "2. Coke - Php 50.00",
            "3. Sprite - Php 50.00",
            "4. Iced tea - Php 55.00",
            "5. Milk tea - Php 70.00",
            "6. Water - Php 15.00"
    };

    public static void dmenu() //DISPLAY MENU'S
    {
        System.out.println("\n~~~ MENU ~~~");
        for (String m : menu){
            System.out.println(m);
        }
    }

    public static void ddessert()
    {
        System.out.println("\n~~~ DESSERT ~~~");
        for (String d : dessert){
            System.out.println(d);
        }
    }

    public static void ddrinks()
    {
        System.out.println("\n~~~ DRINKS ~~~");
        for (String dd : drinks){
            System.out.println(dd);
        }
    }

    public static double Orderp(String[] items, List<String> orderitems, Scanner sc){ //Order Method
        System.out.print("\nEnter the item number you want to order: (1-" + items.length + "): "); //Runs when arraylist gets called so number of choice is adjusted
        int itemNumber = sc.nextInt();
        System.out.print("Enter the quantity: ");
        int quantity = sc.nextInt();
        String orderItem = (items[itemNumber - 1] + " - Quantity (" +  quantity + "x)"); //This is where the selected item is addded into orderlist along with the quantity
        orderitems.add(orderItem);
        System.out.println("\nThe item has been added to order.\n");
        return quantity*Price(items[itemNumber - 1]);
    }

    public static double Price(String item){ //Where the price (Php) get turn into double value
        String[] p = item.split(" - ");
        String priceS = p[1].substring(4);
        return Double.parseDouble(priceS);
    }

    public static double Payment(Scanner sc){ //Payment with user input
        System.out.print("\nPlease enter the amount of payment: Php ");
        return sc.nextDouble();
    }

    public static void Receipt(List<String> orderitems) //Receipt method
    {
        SimpleDateFormat dateform = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateform.format(new Date());
        System.out.println("Neil's Restaurant");
        System.out.println("Date: " + currentDate);
        System.out.println("Order:\n ");
        double subtotal = 0;

        for (String item : orderitems){ //used to split the item and price via " - "
            String[] p = item.split(" - ");
            String itemName = p[0].substring(p[0].indexOf(".") + 2);
            String pString = p[1].substring(4);
            double Price2 = Double.parseDouble(pString);
            int quantity = 1; //because when you order something its always 1 at default

            if (p.length == 3) {
                String quantityString = p[2].substring(10, p[2].length() - 2);
                quantity = Integer.parseInt(quantityString);
            }

            double itemTotal = Price2*quantity;
            subtotal += itemTotal;
            //for printing (visuals)
            System.out.println(itemName + " - Php " + Price2 + " - Quantity (" + quantity + "x)" + " - Total: Php " + itemTotal);
        }

        double tax = subtotal * taxRATE;
        double GrandTotal = subtotal + tax;

        System.out.println("\nSubtotal: Php " + subtotal);
        System.out.println("Tax (10%): Php " + tax);
        System.out.println("Total: Php " + GrandTotal);
        System.out.println("\nThank you for dining at Neil's Restaurant! We appreciate your patronage! Enjoy your meal!");
    }
}
