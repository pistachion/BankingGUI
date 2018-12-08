# Fall 2015 CS 49J – Program #2 (100 points); Due 11/07/15

## Description

+ You will implement the GUI interfaces for Program #1.
+ Here are a few additional rules/requirements.

## TODO

+ Wrap all the command line input/output operations into the Java `JFrame` window.
+ Add the display of the transaction log in customer option 6 of “Get Balance” in descending time order.


## DONE

+ Provide a login screen initially to accept customer ID and Pin.

+ If it is a new customer, provide a button at the 1st screen to initiate the
	`CREATE CUSTOMER` operation.

+ For administrator function, customer ID is hardcoded to `000` and Pin
	remains as `abcd`.

+ Once the customer log in successfully, provide the options to perform
	customer options `2-9`.

+ For `CREATE ACCOUNT` operation, make sure the user has a choice for Savings or
 	Checking.

+ Add a log file class to store the timestamp, transaction ID, customer ID,
	account #, amount (either a + or – double number) for each transaction; add
	2 entries for XFER (one dep and one wd) but it must have the same
	transaction ID for both;

+ Each interest update will add an entry into the log, make transaction ID=0
	for all.

+ Make the log persistence by adding an array_list[] to the ATM class; then
	stream the ATM class to a `p2.log` file.

+ Add an option #4 in the Admin menu to display all the transaction log
    entries in descending time order.
