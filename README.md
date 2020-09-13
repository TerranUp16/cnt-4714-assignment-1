# Nile Dot Com
![Starting screen](https://github.com/TerranUp16/cnt-4714-assignment-1/blob/master/screenshots/start.png)
This project was created under the direction of Dr. Mark Llewellyn for University of Central Florida course CNT-4714.

This project simulates an "e-store". The store maintains inventory from a supplied file (default `inventory.txt` included). E-store application exposes a GUI which enables users to create and process orders based on items from `inventory.txt`. When a user finalizes an order, a transaction is generated which appends entries to `transactions.txt`.

# Dependencies
* [Java 14](https://www.oracle.com/java/technologies/javase-downloads.html)
* [Apache Maven](https://maven.apache.org/download.cgi)
* [JavaFX](https://openjfx.io/openjfx-docs/#maven) (`maven` and included `pom.xml` take care of this dependency)
* [BootstrapFX](https://github.com/kordamp/bootstrapfx) (`maven` and included `pom.xml` take care of this dependency)

# Install and Run

## Using `git`
```
git clone https://github.com/TerranUp16/cnt-4714-assignment-1.git ;
cd cnt-4714-assignment-1 ;
mvn javafx:run ;
```

## Download Code
1. [Download `.zip`](https://github.com/TerranUp16/cnt-4714-assignment-1/archive/master.zip)
2. Unzip `cnt-4714-assignment-1-master.zip`
3. Open a shell/terminal application (ex- PowerShell on Windows, Terminal on Mac)
4. Navigate to extracted `cnt-4714-assignment-1-master` directory/folder
5. Run `mvn javafx:run`

## Troubleshooting
> `mvn` not found
Please review [Maven's installation and configuration documentation](https://maven.apache.org/install.html). In particular, make sure `mvn` is configured for `PATH` for the shell/terminal you are using.

> `JAVA_HOME` not found
Please review [Maven's installation and configuration documentation](https://maven.apache.org/install.html). In particular, make sure `JAVA_HOME` is configured for `PATH` for the shell/terminal you are using.

Please also review Oracle's documentation on this for-

* [Windows](https://docs.oracle.com/en/java/javase/14/install/installation-jdk-microsoft-windows-platforms.html#GUID-96EB3876-8C7A-4A25-9F3A-A2983FEC016A)
* [Mac](https://docs.oracle.com/en/java/javase/14/install/installation-jdk-macos.html#GUID-F9183C70-2E96-40F4-9104-F3814A5A331F)
* [Linux](https://docs.oracle.com/en/java/javase/14/install/installation-jdk-linux-platforms.html#GUID-737A84E4-2EFF-4D38-8E60-3E29D1B884B8)

# Selecting Items
Open `inventory.txt` in your favorite text editor or viewer to see what items are available. The item ID is the first number on each line. The item's description is between two commas on the same line as the item's ID. The item's cost is at the end of the line. Use the item ID number in the `Enter item ID for item #` to specify which item to order.

# Viewing Transaction Log
Use your favorite text editor or viewer to open `transactions.txt` in the base directory you cloned or extracted the code into.

# Editing Inventory
Use your favorite text editor to modify the contents of `inventory.txt` which is located in the base directory you cloned or extracted the code into.

# Screenshots

## New Order
![start screen](https://github.com/TerranUp16/cnt-4714-assignment-1/blob/master/screenshots/start.png)

## Enter First Item
![preprocess first item](https://github.com/TerranUp16/cnt-4714-assignment-1/blob/master/screenshots/pending_process_1.png)

## Process First Item
![post-process first item](https://github.com/TerranUp16/cnt-4714-assignment-1/blob/master/screenshots/post_process_1.png)

## Confirmation Dialog
![confirmation dialog](https://github.com/TerranUp16/cnt-4714-assignment-1/blob/master/screenshots/confirm_1.png)

## First Item Confirmed
![post-confirm first item](https://github.com/TerranUp16/cnt-4714-assignment-1/blob/master/screenshots/post_confirm_1.png)

## After Confirming Last Item
![last item confirmed](https://github.com/TerranUp16/cnt-4714-assignment-1/blob/master/screenshots/post_confirm_last.png)

## View Order
![view order](https://github.com/TerranUp16/cnt-4714-assignment-1/blob/master/screenshots/view_order_5.png)

## View Transaction
![view transaction](https://github.com/TerranUp16/cnt-4714-assignment-1/blob/master/screenshots/view_transaction.png)

## Transaction Log Entries
![appended transaction log entries](https://github.com/TerranUp16/cnt-4714-assignment-1/blob/master/screenshots/transaction_log.png)
