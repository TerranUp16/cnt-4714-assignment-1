
/*Name:Robert Schwyzer
  Course:CNT 4714 – Fall 2020
  Assignment title: Project1 – Event-driven Enterprise Simulation
  Date: Sunday September 13, 2020
*/
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.scene.layout.Panel;

public class main extends Application {

  @Override
  public void start(Stage primaryStage) {
    Inventory inv = new Inventory();
    inv.readInventoryFromFile("inventory.txt");
    //inv.printItems();

    Order order = new Order(1);

    BorderPane pane = new BorderPane();

    GridPane inputGrid = new GridPane();
    inputGrid.setAlignment(Pos.CENTER);
    inputGrid.setHgap(10);
    inputGrid.setVgap(10);
    inputGrid.setPadding(new Insets(10));

    ColumnConstraints col1 = new ColumnConstraints();
    col1.setPercentWidth(50);
    col1.setHalignment(HPos.RIGHT);
    
    ColumnConstraints col2 = new ColumnConstraints();
    col2.setPercentWidth(50);

    inputGrid.getColumnConstraints().addAll(col1, col2);

    Label numberOfItems = new Label("Enter number of items in this order:");
    Label itemID = new Label("Enter item ID for item #" + order.getCurrentItemNumber() + ":");
    Label itemQuantity = new Label("Enter quantity for item #" + order.getCurrentItemNumber() + ":");
    Label itemInfo = new Label("Item #" + order.getCurrentItemNumber() + " info:");
    Label orderSubtotal = new Label("Order subtotal for " + order.getTotalItems() + " item(s):");

    TextField numberOfItemsInput = new TextField();
    TextField itemIDInput = new TextField();
    TextField itemQuantityInput = new TextField();
    TextField itemInfoInput = new TextField();
    TextField orderSubtotalInput = new TextField();

    inputGrid.add(numberOfItems, 0, 0, 1, 1);
    inputGrid.add(numberOfItemsInput, 1, 0, 1, 1);
    inputGrid.add(itemID, 0, 1, 1, 1);
    inputGrid.add(itemIDInput, 1, 1, 1, 1);
    inputGrid.add(itemQuantity, 0, 2, 1, 1);
    inputGrid.add(itemQuantityInput, 1, 2, 1, 1);
    inputGrid.add(itemInfo, 0, 3, 1, 1);
    inputGrid.add(itemInfoInput, 1, 3, 1, 1);
    inputGrid.add(orderSubtotal, 0, 4, 1, 1);
    inputGrid.add(orderSubtotalInput, 1, 4, 1, 1);
    
    HBox buttons = new HBox();
    buttons.getStyleClass().setAll("btn-group-horizontal");

    // Process the current item's input
    Button button1 = new Button("Process Item #1");
    button1.getStyleClass().setAll("btn", "btn-lg", "btn-primary", "first");

    // Confirm the current item is correct; move on to next item?
    Button button2 = new Button("Confirm Item #1");
    button2.getStyleClass().setAll("btn", "btn-lg", "btn-default", "middle");
    button2.setDisable(true);

    // Generates a pop-up showing the user the order so far
    Button button3 = new Button("View Order");
    button3.getStyleClass().setAll("btn", "btn-lg", "btn-default", "middle");
    button3.setDisable(true);

    // Completes an order; generates an invoice; adds a transaction to the transaction log
    Button button4 = new Button("Finish Order");
    button4.getStyleClass().setAll("btn", "btn-lg", "btn-default", "middle");
    button4.setDisable(true);

    // Clicking this resets the order, resetting GUI to starting state; any orders which have not been committed to transactions are lost
    Button button5 = new Button("New Order");
    button5.getStyleClass().setAll("btn", "btn-lg", "btn-primary", "middle");

    // Clicking this exits the program; any orders which have not been committed to transactions are lost
    Button button6 = new Button("Exit");
    button6.getStyleClass().setAll("btn", "btn-lg", "btn-danger", "last");

    // Process item into order
    button1.setOnAction(event -> {
      // Get number of items in this order
      String numItemsInput = numberOfItemsInput.getText();

      // Validate input is an integer
      Pattern integer = Pattern.compile("^[0-9]+$");
      Matcher matcher = integer.matcher(numItemsInput);
      boolean matchFound = matcher.find();

      if (matchFound) {
        // Set number of items in order
        int numItems = Integer.parseInt(numItemsInput);
        order.setNumberOfItems(numItems);
      } else {
        System.out.println("Non-integer input into number of items in this order field: " + numItemsInput);
      }

      // Get item ID
      String itemInput = itemIDInput.getText();

      // Validate input is an integer
      matcher = integer.matcher(itemInput);
      matchFound = matcher.find();

      if (matchFound) {
        // Parse item ID number
        int item = Integer.parseInt(itemInput);
        
        // Get quantity
        String quantityInput = itemQuantityInput.getText();

        // Validate input is an integer
        matcher = integer.matcher(quantityInput);
        matchFound = matcher.find();

        if (matchFound) {
          // Parse item quantity
          int quantity = Integer.parseInt(quantityInput);

          // Add item to order
          order.addItem(inv, item, quantity);

          // Populate info node
          if (inv.hasItem(item)) {
            Item i = inv.getItem(item);
            itemInfo.setText("Item #" + order.getCurrentItemNumber() + " info:");
            itemInfoInput.setText(i.getID() + " " + '"' + i.getInfo() + '"' + " " + NumberFormat.getCurrencyInstance().format(i.getCost()) + " " + order.getOrderItem(order.getCurrentItemNumber()).getCount() + " " + order.getOrderItem(order.getCurrentItemNumber()).getDiscount() + "% " + NumberFormat.getCurrencyInstance().format(order.getOrderItem(order.getCurrentItemNumber()).getCost()));
          }

          // Disable Process button
          button1.setDisable(true);
          button1.getStyleClass().setAll("btn", "btn-lg", "btn-default", "first");

          // Enable Confirmation button
          button2.setDisable(false);
          button2.getStyleClass().setAll("btn", "btn-lg", "btn-primary", "middle");

          //NumberFormat.getCurrencyInstance().format(order.getSubtotal())
        } else {
          System.out.println("Non-integer input into item quantity field: " + quantityInput);
        }
      } else {
        System.out.println("Non-integer input into item ID field: " + itemInput);
      }
    });

    // Confirm order
    button2.setOnAction(event -> {
      int id = order.getItem(order.getCurrentItemNumber()).getID();
      if (inv.hasItem(id)) {
        Alert confirm = new Alert(AlertType.INFORMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Item #" + id + " accepted");
        confirm.showAndWait()
          .filter(response -> response == ButtonType.OK)
          .ifPresent(response -> {
            orderSubtotal.setText("Order subtotal for " + order.getTotalItems() + " item(s):");
            orderSubtotalInput.setText(NumberFormat.getCurrencyInstance().format(order.getSubtotal()));

            if (order.getNumberOfItems() > order.getCurrentItemNumber()) {
              order.incrementItemNumber();
              button1.setText("Process Item #" + order.getCurrentItemNumber());
              button1.setDisable(false);
              button1.getStyleClass().setAll("btn", "btn-lg", "btn-primary", "first");
              button2.setText("Confirm Item #" + order.getCurrentItemNumber());
              button2.setDisable(true);
              button2.getStyleClass().setAll("btn", "btn-lg", "btn-default", "middle");
              itemID.setText("Enter item ID for item #" + order.getCurrentItemNumber() + ":");
              itemQuantity.setText("Enter quantity for item #" + order.getCurrentItemNumber() + ":");
              itemIDInput.clear();
              itemQuantityInput.clear();
            } else {
              button2.setDisable(true);
              button2.getStyleClass().setAll("btn", "btn-lg", "btn-default", "middle");
              button2.setText("Confirm Item");
              button1.setText("Process Item");
              itemID.setText("");
              itemIDInput.clear();
              itemQuantity.setText("");
              itemQuantityInput.clear();
            }

            button3.setDisable(false);
            button3.getStyleClass().setAll("btn", "btn-lg", "btn-primary", "middle");
            button4.setDisable(false);
            button4.getStyleClass().setAll("btn", "btn-lg", "btn-primary", "middle");
          });
      } else {
        Alert error = new Alert(AlertType.ERROR);
        error.setTitle("Item Not Found");
        error.setHeaderText("Item ID " + id + " not in file");
        error.showAndWait()
          .filter(response -> response == ButtonType.OK)
          .ifPresent(response -> {
            order.removeOrderItem(order.getCurrentItemNumber());
            itemIDInput.clear();
            itemQuantityInput.clear();
            button2.setDisable(true);
            button2.getStyleClass().setAll("btn", "btn-lg", "btn-default", "middle");
            button1.setDisable(false);
            button1.getStyleClass().setAll("btn", "btn-lg", "btn-primary", "first");
          });
      }
    });

    // View order
    button3.setOnAction(event -> {
      Alert viewOrder = new Alert(AlertType.INFORMATION);
      viewOrder.setResizable(true);
      viewOrder.setTitle("View Order");
      viewOrder.setHeaderText("");

      int orderedList = 1;
      StringBuilder currentOrder = new StringBuilder();

      for (OrderItem oi : order.getOrderItems()) {
        Item i = oi.getItem();
        currentOrder.append(orderedList + ". " + i.getID() + " " + '"' + i.getInfo() + '"' + " " + NumberFormat.getCurrencyInstance().format(i.getCost()) + " " + oi.getCount() + " " + oi.getDiscount() + "% " + NumberFormat.getCurrencyInstance().format(oi.getCost()));
        if (orderedList < order.getTotalItems()) {
          currentOrder.append(String.format("%n"));
        }
        orderedList++;
      }

      viewOrder.setContentText(currentOrder.toString());
      viewOrder.showAndWait()
        .filter(response -> response == ButtonType.OK)
        .ifPresent(response -> {
        });
    });

    // Finalize transaction
    button4.setOnAction(event -> {
      Transaction tran = new Transaction(order);
      tran.writeTransaction();

      Alert viewTransaction = new Alert(AlertType.INFORMATION);
      viewTransaction.setResizable(true);
      viewTransaction.setTitle("View Transaction");
      Format theDate = new SimpleDateFormat("M/d/yy");
      Format time = new SimpleDateFormat("h:mm:ss a z");
      viewTransaction.setHeaderText("Date: " + theDate.format(tran.getDate()) + ", " + time.format(tran.getDate()));

      // Output rest of alert and handle OK response
      StringBuilder currentTransaction = new StringBuilder();

      currentTransaction.append("Number of line items: " + order.getNumberOfItems());
      currentTransaction.append(String.format("%n%n"));
      currentTransaction.append("Item# / ID / Title / Price / Qty / Disc % / Subtotal:");
      currentTransaction.append(String.format("%n%n"));
      int orderedList = 1;

      for (OrderItem oi : order.getOrderItems()) {
        Item i = oi.getItem();
        currentTransaction.append(orderedList + ". " + i.getID() + " " + '"' + i.getInfo() + '"' + " " + NumberFormat.getCurrencyInstance().format(i.getCost()) + " " + oi.getCount() + " " + oi.getDiscount() + "% " + NumberFormat.getCurrencyInstance().format(oi.getCost()));
        currentTransaction.append(String.format("%n"));
        orderedList++;
      }

      currentTransaction.append(String.format("%n") + "Order subtotal:" + String.format("%c", '\t') + NumberFormat.getCurrencyInstance().format(tran.getOrderSubtotal()));
      currentTransaction.append(String.format("%n%n") + "Tax rate:" + String.format("%c", '\t') + (tran.getTaxRate() * 100) + "%");
      currentTransaction.append(String.format("%n%n") + "Tax amount:" + String.format("%c", '\t') + NumberFormat.getCurrencyInstance().format(tran.getTaxAmount()));
      currentTransaction.append(String.format("%n%n") + "Order total:" + String.format("%c", '\t') + NumberFormat.getCurrencyInstance().format(tran.getOrderTotal()));
      currentTransaction.append(String.format("%n%n") + "Thanks for shopping at Nile Dot Com!");

      viewTransaction.setContentText(currentTransaction.toString());

      viewTransaction.showAndWait()
        .filter(response -> response == ButtonType.OK)
        .ifPresent(response -> {
          numberOfItemsInput.clear();
          itemIDInput.clear();
          itemQuantityInput.clear();
          itemInfoInput.clear();
          orderSubtotalInput.clear();
          order.wipe();

          // NEED TO RESET LABELS TO CORRECT ITEM #
          itemID.setText("Enter item ID for item #" + order.getCurrentItemNumber() + ":");
          itemQuantity.setText("Enter quantity for item #" + order.getCurrentItemNumber() + ":");
          itemInfo.setText("Item #" + order.getCurrentItemNumber() + " info:");
          orderSubtotal.setText("Order subtotal for " + order.getTotalItems() + " item(s):");

          // NEED TO RESET BUTTONS TO STARTING STATES
          button1.setDisable(false);
          button1.setText("Process Item #1");
          button1.getStyleClass().setAll("btn", "btn-lg", "btn-primary", "first");

          button2.setText("Confirm Item #1");
          button2.getStyleClass().setAll("btn", "btn-lg", "btn-default", "middle");
          button2.setDisable(true);

          button3.getStyleClass().setAll("btn", "btn-lg", "btn-default", "middle");
          button3.setDisable(true);

          button4.getStyleClass().setAll("btn", "btn-lg", "btn-default", "middle");
          button4.setDisable(true);
        });
    });

    // Clear order
    button5.setOnAction(event -> {
      numberOfItemsInput.clear();
      itemIDInput.clear();
      itemQuantityInput.clear();
      itemInfoInput.clear();
      orderSubtotalInput.clear();
      order.wipe();

      // NEED TO RESET LABELS TO CORRECT ITEM #
      itemID.setText("Enter item ID for item #" + order.getCurrentItemNumber() + ":");
      itemQuantity.setText("Enter quantity for item #" + order.getCurrentItemNumber() + ":");
      itemInfo.setText("Item #" + order.getCurrentItemNumber() + " info:");
      orderSubtotal.setText("Order subtotal for " + order.getTotalItems() + " item(s):");

      // NEED TO RESET BUTTONS TO STARTING STATES
      button1.setDisable(false);
      button1.setText("Process Item #1");
      button1.getStyleClass().setAll("btn", "btn-lg", "btn-primary", "first");

      button2.setText("Confirm Item #1");
      button2.getStyleClass().setAll("btn", "btn-lg", "btn-default", "middle");
      button2.setDisable(true);

      button3.getStyleClass().setAll("btn", "btn-lg", "btn-default", "middle");
      button3.setDisable(true);

      button4.getStyleClass().setAll("btn", "btn-lg", "btn-default", "middle");
      button4.setDisable(true);
    });

    // Exit
    button6.setOnAction(event -> System.exit(0));

    buttons.getChildren().addAll(button1, button2, button3, button4, button5, button6);

    pane.setCenter(inputGrid);
    pane.setBottom(buttons);

    Scene scene = new Scene(pane);
    scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");

    primaryStage.setTitle("Nile Dot Com - Fall 2020");
    primaryStage.setScene(scene);
    primaryStage.sizeToScene();
    primaryStage.show();

    /*Label lab = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
    Scene scene = new Scene(new StackPane(lab), 640, 480);
    stage.setScene(scene);
    stage.show();*/
  }

  public static void main(String[] args) {
    launch();
  }
}

class Transaction {
  String transactionID;
  Order order;
  Date date;
  double taxRate;
  double taxAmount;
  double orderSubtotal;
  double orderTotal;
  String filepath;

  /**
   * Constructor of a Transction object which takes an Order object and converts it into a finalized transaction which can be written to a file
   * @param o Order item used to generate the transaction
   */
  public Transaction(Order o) {
    order = o;

    // Initialize date to now
    date = new Date();

    // Default tax rate to 6%
    taxRate = 0.06;

    // Default filepath for transaction file
    filepath = "transactions.txt";

    orderSubtotal = order.getSubtotal();

    // Create the transaction ID which is the ddMMyyyyHHmm of date
    Format f = new SimpleDateFormat("ddMMyyyyHHmm");
    transactionID = f.format(date);

    // Calculate how much tax to charge for the order
    taxAmount = taxRate * orderSubtotal;

    // Calculate final charge for the order, including tax
    orderTotal = orderSubtotal + taxAmount;
  }

  /**
   * 
   * @return  amount of tax to charge for the order
   */
  public double getTaxAmount() {
    return taxAmount;
  }

  /**
   * 
   * @return  amount to charge without tax for the order
   */
  public double getOrderSubtotal() {
    return orderSubtotal;
  }

  /**
   * 
   * @return  amount to charge including tax for the order
   */
  public double getOrderTotal() {
    return orderTotal;
  }

  /**
   * 
   * @return  ID of the transaction based on the date the transaction was created
   */
  public String getTransactionID() {
    return transactionID;
  }

  /**
   * 
   * @return  date the transaction was started
   */
  public Date getDate() {
    return date;
  }

  /**
   * 
   * @return  rate order was taxed at
   */
  public double getTaxRate() {
    return taxRate;
  }

  /**
   * Write this Transaction to a file
   */
  public void writeTransaction() {
    Format theDate = new SimpleDateFormat("M/d/yy");
    Format time = new SimpleDateFormat("h:mm:ss a z");
    try {
      File transact = new File(filepath);
      transact.createNewFile();
      try (FileWriter writer = new FileWriter(filepath, true)) {
        for (OrderItem oi : order.getOrderItems()) {
          Item i = oi.getItem();
          writer.write(transactionID + ", " + i.getID() + ", " + '"' + i.getInfo() + '"' + ", " + NumberFormat.getCurrencyInstance().format(i.getCost()) + ", " + oi.getCount() + ", " + oi.getDiscount() + "%, " + NumberFormat.getCurrencyInstance().format(oi.getCost()) + ", " + theDate.format(date) + ", " + time.format(date) + String.format("%n"));
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

/**
 * Stores inventory of items available for purchase
 */
class Inventory {
  HashMap<Integer, Item> items;

  /**
   * Constructor of an Inventory object which maintains a HashMap of Item IDs to Item objects
   */
  public Inventory() {
    items = new HashMap<>();
  }

  /**
   * Retrieve an item based on ID from Inventory
   * @param id  ID of Item to retrieve
   * @return    retrieved Item
   * @see       Item
   */
  public Item getItem(int id) {
    return items.get(id);
  }

  /**
   * Verify if a provided item number is in this Inventory
   * @param id  ID of item to check
   * @return    True if item exists in this inventory, false if it does not
   */
  public boolean hasItem(int id) {
    return items.containsKey(id);
  }

  /**
   * Add a new item to Inventory
   * @param id    Item's ID
   * @param cost  Item's cost
   * @param info  Item's description
   * @see         Item
   */
  public void addItem(int id, double cost, String info) {
    items.put(id, new Item(id, cost, info));
  }

  /**
   * Remove an item from Inventory
   * @param id  Item's ID
   */
  public void removeItem(int id) {
    items.remove(id);
  }

  public void printItems() {
    Set<Integer> keySet = items.keySet();

    for(Integer key : keySet) {
      Item i = items.get(key);
      System.out.println("ID #" + i.getID() + " - " + i.getInfo() + " costs $" + i.getCost());
    }
  }

  /**
   * Reads an inventory.txt file formatted as id, cost, info to populate Inventory
   * @param filepath  Full filepath to a valid inventory.txt file
   * @see             addItem
   */
  public void readInventoryFromFile(String filepath) {
    File input = new File(filepath);

    try (Scanner reader = new Scanner(input)) {
      while (reader.hasNextLine()) {
        String data = reader.nextLine();
        //System.out.println(data);
        if (data.contains(",")) {
          String[] fields = data.split(",");
          fields[0] = fields[0].trim();
          fields[2] = fields[2].trim();

          Pattern pattern0 = Pattern.compile("^[0-9]+$");
          Pattern pattern2 = Pattern.compile("^[0-9.]+$");
          Matcher matcher0 = pattern0.matcher(fields[0]);
          Matcher matcher2 = pattern2.matcher(fields[2]);
          boolean matchFound0 = matcher0.find();
          boolean matchFound2 = matcher2.find();

          if (matchFound0 && matchFound2) {  
            addItem(Integer.parseInt(fields[0]), Double.parseDouble(fields[2]), fields[1].trim().replace("\"", ""));
          } else {
            System.out.println("Invalid input file line entry: " + data);
          }
        }
      }
    } catch (FileNotFoundException e) {
      System.out.println("File not found.");
      e.printStackTrace();
    }
  }
}

/**
 * Stores items like a shopping cart would
 */
class Order {
  int number;
  int numberOfItems;
  int currentItemNumber;
  double subtotal;
  ArrayList<OrderItem> orderItems;

  /**
   * Constructor of an Order object which only takes the order number. Defaults number of items to 1 (can be changed later) and initializes a fresh subtotal and list.
   * @param orderNumber a number which can be used to uniquely identify this Order from other Orders
   */
  public Order(int orderNumber) {
    number = orderNumber;
    numberOfItems = 1;
    currentItemNumber = 1;
    subtotal = 0.00;
    orderItems = new ArrayList<>();
  }

  /**
   * Constructor of an Order object with a total number of items the order will have, a current subtotal for the order, and a list of items currently in the order (where each item also includes its count)
   * @param orderNumber a number which can be used to uniquely identify this Order from other Orders
   * @param numItems    total number of items this order is expected to contain
   */
  public Order(int orderNumber, int numItems) {
    number = orderNumber;
    numberOfItems = numItems;
    currentItemNumber = 1;
    subtotal = 0.00;
    orderItems = new ArrayList<>();
  }

  /**
   * 
   * @return  numeric ID for this Order
   */
  public int getOrderNumber() {
    return number;
  }

  /**
   * 
   * @return  cost of order so far
   */
  public double getSubtotal() {
    return subtotal;
  }

  /**
   * 
   * @return  current item number for the order
   */
  public int getCurrentItemNumber() {
    return currentItemNumber;
  }

  /**
   * 
   * @return  expected number of items this order will contain
   */
  public int getNumberOfItems() {
    return numberOfItems;
  }

  /**
   * 
   * @return  current number of items in this order
   */
  public int getTotalItems() {
    return orderItems.size();
  }

  /**
   * Use numeric item number in this order to fetch an OrderItem object
   * @param id  Order item number for the desired item
   * @return    desired OrderItem
   * @see       OrderItem
   */
  public OrderItem getOrderItem(int id) {
    return orderItems.get(id-1);
  }

  /**
   * Use numeric item number in this order to fetch an Item object
   * @param id  Order item number for the desired item
   * @return    desired Item
   * @see       Item
   */
  public Item getItem(int id) {
    return orderItems.get(id-1).getItem();
  }

  /**
   * Get a count of how many of a specified item is in this Order
   * @param id  Order item number for the desired item
   * @return    how many of an Item (based on given id) are in this Order
   * @see       OrderItem
   */
  public int getItemCount(int id) {
    return orderItems.get(id-1).getCount();
  }

  /**
   * Sets the number of items this order expects to have
   * @param numItems  total number of items this order is expected to contain
   */
  public void setNumberOfItems(int numItems) {
    numberOfItems = numItems;
  }

  /**
   * Use a provided Inventory object to search for and add an Item based on provided ID. Also sets the count of the provided Item
   * @param inventory Inventory object to use to retrieve items from
   * @param id        Inventory ID of the item to add to the order
   * @param count     number of the Item to add to the order
   * @see             addOrderItem
   */
  public void addItem(Inventory inventory, int id, int count) {
    Item item;
    if (inventory.hasItem(id)) {
      item = inventory.getItem(id);
    } else {
      item = new Item(id, 0.00, "");
    }
    addOrderItem(item, count);
  }

  /**
   * Take an Item and how many of that Item to add to the Order
   * @param item  Item to add to Order
   * @param count how many of Item to add to Order
   */
  public void addOrderItem(Item item, int count) {
    OrderItem oi = new OrderItem(item, count);
    orderItems.add(oi);
    subtotal += oi.getCost();
  }

  /**
   * Remove an OrderItem from the Order
   * @param id  Order item number for the desired item
   */
  public void removeOrderItem(int id) {
    subtotal -= orderItems.get(id-1).getCost();
    orderItems.remove(id-1);
  }

  /**
   * 
   * @return  list of OrderItems in Order
   */
  public ArrayList<OrderItem> getOrderItems() {
    return orderItems;
  }

  /**
   * Increment current item number for the order
   */
  public void incrementItemNumber() {
    currentItemNumber++;
  }

  /**
   * Decrement current item number for the order
   */
  public void decrementItemNumber() {
    currentItemNumber--;
  }

  /**
   * Clear the entire order, removing all items from it, setting subtotal to $0.00, and numberOfItems to 0. Leaves order number intact
   */
  public void wipe() {
    orderItems.clear();
    subtotal = 0.00;
    numberOfItems = 0;
    currentItemNumber = 1;
  }
}

/**
 * Convenience class which holds an Item and a count of that Item
 */
class OrderItem {
  Item item;
  int count;
  int discount;

  /**
   * Constructor of an OrderItem object which takes an Item and a count
   * @param i Item
   * @param c count of Item
   */
  public OrderItem(Item i, int c) {
    item = i;
    count = c;

    if (c <= 4) {
      discount = 0;
    } else if (c >= 5 && c <= 9) {
      discount = 10;
    } else if (c >=10 && c <= 14) {
      discount = 15;
    } else if (c >= 15) {
      discount = 20;
    }
  }

  /**
   * Get just the Item
   * @return  Item
   */
  public Item getItem() {
    return item;
  }

  /**
   * Get just the count
   * @return  count of Item
   */
  public int getCount() {
    return count;
  }

  /**
   * Get the discount (based on count)
   * @return  discount
   */
  public int getDiscount() {
    return discount;
  }

  /**
   * Get the combined cost of these items based on their quantity (automatically applies discount)
   * @return  cost of the items
   */
  public double getCost() {
    double disc = 1 - (discount / 100.0);
    return item.getCost() * disc * count;
  }
}

/**
 * Represents an item which has an ID, a cost, and a description
 */
class Item {
  int id;
  double cost;
  String info;

  /**
   * Constructor of an Item which takes an ID, a cost, and a description
   * @param i ID
   * @param c cost
   * @param s description
   */
  public Item(int i, double c, String s) {
    id = i;
    cost = c;
    info = s;
  }

  /**
   * Retrieve this Item's ID
   * @return  ID number of Item
   */
  public int getID() {
    return id;
  }

  public double getCost() {
    return cost;
  }

  public String getInfo() {
    return info;
  }
}