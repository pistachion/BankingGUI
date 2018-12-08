
// Atm.java

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.NumberFormat;


public class Atm
{
    static final String         ADMIN_ID = "0000";
    private String              admin_pin;
    private boolean             isAdmin;

    private Log                 log;
    private ArraySort<Customer> customerArrayList;
    private int                 starting_account_number;
    private int                 starting_customer_number;
    @SuppressWarnings("unused")
    private int                 interest_rate;

    // to count log-in from any customers for saving interest calculation
    private int                 transaction_counter;

    // variables created by Roxie
    private Customer            customer;
    private Account             account;
    private ArraySort<Account>  account_list;
    private int                 customer_count;
    private int                 account_count;
    ConsoleReader               console  = new ConsoleReader (System.in);

    public Atm () // constructor
    {
        log = new Log ();
        customerArrayList = new ArraySort<Customer> (100);
        account_list = new ArraySort<Account> (100);
        account = new Account ();
        admin_pin = new String ("abcd"); // Same as admin_pin = "abcd";

        // System.out.println("\nAdmin PIN: " + admin_pin + "\n");
        // System.out.println("\nAdmin PIN length: " + admin_pin.length() +
        // "\n");

        starting_account_number = 1001;
        starting_customer_number = 101;
        interest_rate = 5;

        read_customer_info_from_file ();
    }

    /*
     * Call all methods to get the information needed for creating customers
     */
    public void create_customer (String enteredName, String pin)
    {
        System.out.println ("create_customer is called");
        customer = new Customer ();
        customer.set_name (enteredName);
        create_new_pin (pin);
        create_customer_id ();
        add_to_array ();
        update_interest ();
        write_customer_info_to_file ();

        System.out.println (customer);
    }

    public Customer getThisCustomer ()
    {
        return customer;
    }

    /*
     * prompt a pin from
     *
     */
    public void create_new_pin (String pin)
    {

        customer.set_pin (pin);
        // calls create_new_pin method in Customer.java class and return the pin
        // value generated

        ++transaction_counter;
        // Increment transaction_counter for the first time a new customer log
        // in
    }

    /*
     * generate the customer id
     */
    public void create_customer_id ()
    {
        int generated_id = starting_customer_number + customer_count;
        // adding the # of existing customer to the starting id number
        // System.out.println("starting_customer_number + counter_string " +
        // generated_id);

        customer_count++;
        // this counter increments every time we create the
        // customer id

        String id_string = String.valueOf (generated_id);
        // converting the id (integer) to string so we can return the string
        // value to Customer.java class

        customer.set_id (id_string);
        // calls set_id method in Customer.java
        // class and return the id value generated
    }

    /*
     * add newly created customer to the array
     */
    public void add_to_array ()
    {
        customerArrayList.add (customer);
    }

    /*
     * Call all methods to get the information needed for opening accounts
     */
    public void open_account (String accountType)
    {
        if (0 == customerArrayList.size ())
        {
            System.out.println ("\nWe don't have any customer yet.");
            return;
        }

        generate_acc (accountType);
        update_interest ();
        write_customer_info_to_file ();
        account.show_account_info ();
    }

    public String getThisAccountNum ()
    {
        String accountNum = account.get_account_number ();
        return accountNum;
    }

    public double getAccountBalance ()
    {
        double accountBal = account.get_balance ();
        return accountBal;
    }

    public Atm getAtmClass ()
    {
        return Atm.this;
    }

    public boolean validate_id (String idEntry)
    {
        if (ADMIN_ID.equals (idEntry))
        {
            adminModeOn ();
            return true;
        }
        else
        {
            adminModeOff ();
        }

        boolean id_found = false;
        // Used for while loop to find the customer ID from ArrayList; will be
        // set to false once we find the matching ID

        int i = 0;
        // Used to increment the array until we find the matched
        // customer ID

        String id_from_class = null;
        // Used to hold the id value returned from Customer Class

        while (i < customerArrayList.size ())
        {
            Customer possible_customer = customerArrayList.get (i);
            id_from_class = possible_customer.get_id ();

            if (id_from_class.equals (idEntry))
            {
                id_found = true;
                customer = possible_customer;
                break;
            }

            else
            {
                i++;
                if (customerArrayList.size () == i)
                {
                    System.out.println ("We cannot locate your ID number. Please try again.\n");

                }

            }
            System.out.println ("Loop");

            System.out.println ("i counter " + i);
            System.out.println ("cust_ArrayList " + customerArrayList.size ());

        }

        System.out.println ("reach to the end of " + customerArrayList);
        return id_found;
    }

    public String getValidPin ()
    {
        String stored_pin = isAdmin ? admin_pin : customer.get_pin ();
        System.out.println ("stored_pin " + stored_pin);
        return stored_pin;
    }

    public void validate_pin (String pin_entry)
    {
        ++transaction_counter;
        // Increment transaction counter everytime a customer logs in.
        // This method is called everytime s/he logs in.
    }

    /*
     * generate the customer account number
     */
    public void generate_acc (String account_choice)
    {
        // variable to hold the user's account choice

        int generated_num;
        // validation of user account type choice

        String checking = "checking";

        // If a customer choose Checking, we create Account object
        if (account_choice.equalsIgnoreCase (checking))
        {
            account = new Account ();
        }

        else
        {
            account = new Sav_Acct ();
        }

        generated_num = starting_account_number + account_count;
        // adding the # of existing customer to the starting id number

        String num_string = String.valueOf (generated_num);
        // converting the id (integer) to string so we can return the string
        // value to Customer.java class

        account.set_acct_num (num_string); // calls set_id method in
        // Customer.java
        // class and return the id value
        // generated
        account.set_account_type (account_choice);

        customer.add_account (account);
        account_count++;
    }

    public boolean update_interest ()
    {
        int size = 0;
        int inactive_accounts = 0;

        int cust_size = customerArrayList.size ();
        // if (cust_size == 0)
        // {
        // return false;
        // }

        for (int j = 0; j < cust_size; j++)
        {
            account_list = customerArrayList.get (j).get_all_accounts ();
            size = account_list.size ();

            if (size == 0)
            {
                break;
            }
            else
            {
                Account account_temp = null;
                double interest;

                for (int i = 0; i < size; i++)
                {
                    account_temp = account_list.get (i);

                    String account_type = account_temp.get_account_type ();

                    if (customer == account_temp.get_customer () && account_type == null)
                    {
                        inactive_accounts++;
                    }

                    if (account_type != null && account_type.equalsIgnoreCase ("saving"))
                    {
                        interest = ((Sav_Acct) account_temp).calculate_interest (transaction_counter);

                        if (interest > 0)
                        {
                            log.interest (interest, customer.get_id (), account.get_account_number ());
                        }
                    }
                }
            }

        }

        size = customer.get_all_accounts ().size ();

        if (size - inactive_accounts > 0)
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    public void deposit (int arrayIndex, double amount)
    {

        if (0 == customerArrayList.size ())
        {
            System.out.println ("\nWe don't have any customer yet.");

        }

        // updating the interest right after a customer logged in successfully
        boolean validate = update_interest ();

        transaction_deposit (arrayIndex, amount);
        write_customer_info_to_file ();
        account.show_account_info ();
    }

    public ArraySort<Account> getAccoutList ()
    {
        account_list = customer.get_all_accounts ();
        return account_list;
    }

    public double getAccountBalance (int arrayIndex)
    {
        double balance = account_list.get (arrayIndex).get_balance ();
        System.out.println ("Returned Balance from getAccountBalance " + balance);
        return balance;
    }

    public void transaction_deposit (int arrayIndex, double amount)
    {
        account_list = customer.get_all_accounts ();
        double balance = account_list.get (arrayIndex).get_balance ();
        balance = balance + amount;
        account_list.get (arrayIndex).update_balance (balance);
        log.transaction (amount, customer.get_id (), account.get_account_number ());
    }

    public void withdraw (int accountNum, double amount) throws InterruptedException
    {
        if (0 == customerArrayList.size ())
        {
            System.out.println ("\nWe don't have any customer yet.");
            return;
        }

        boolean valid = update_interest ();

        if (valid != false)
        {
            transaction_withdraw (accountNum, amount);
            write_customer_info_to_file ();
            account.show_account_info ();
        }

        else
        {
            System.out.println ("You don't have any account with us. ");
        }
    }

    public void transaction_withdraw (int accountNum, double withdraw_amount)
    {
        account_list = customer.get_all_accounts ();

        // updating the interest before we show the balances to a customer
        int size = account_list.size ();

        account = account_list.get (accountNum);
        double balance = account.get_balance ();

        balance -= withdraw_amount;
        account_list.get (accountNum).update_balance (balance);
        log.transaction (withdraw_amount, customer.get_id (), account.get_account_number ());

    }

    public String printout_accounts ()
    // this method creates arrayList from Account class
    // to hold customer's all account information
    {
        update_interest ();
        account_list = customer.get_all_accounts ();
        String accountInfo = " ";

        int j = account_list.size ();
        for (int i = 0; i < j; i++)
        {
            accountInfo += account_list.get (i).show_account_info () + "\n";
        }

        return accountInfo;
    }

    public boolean transfer (int toAccount, int fromAccount, double amount) throws InterruptedException
    {
        if (0 == customerArrayList.size ())
        {
            System.out.println ("\nWe don't have any customer yet.");
            return false;
        }

        boolean valid = update_interest ();

        if (valid == true)
        {
            transaction_transfer (toAccount, fromAccount, amount);
            write_customer_info_to_file ();
            return true;
        }

        else
        {
            System.out.println ("You don't have any account with us.");
            return false;

        }

    }

    public boolean checkCustArray ()
    {
        System.out.println ("Array Size " + customerArrayList.size ());
        if (0 < customerArrayList.size ())
            return true;

        else
            return false;
    }

    public void transaction_transfer (int toAccount, int fromAccount, double amount)
    {
        account_list = customer.get_all_accounts ();

        double balance_from = 0;
        double balance_to = 0;

        Account from = account_list.get (fromAccount);
        balance_from = from.get_balance ();

        Account to = account_list.get (toAccount);
        balance_to = to.get_balance ();

        balance_from -= amount;
        balance_to += amount;

        from.update_balance (balance_from);
        to.update_balance (balance_to);
        log.transfer (amount, customer.get_id (), from.get_account_number (), to.get_account_number ());

        System.out.println ("\n" + "Updated balance : ");

        for (int i = 0; i < account_list.size (); i++)
        {
            account_list.get (i).show_account_info ();
        }

    }

    public boolean close_account (int accountIndex)
    {
        if (0 == customerArrayList.size ())
        {
            System.out.println ("\nWe don't have any customer yet.");
            return false;
        }

        boolean valid = update_interest ();

        if (valid == true)
        {
            close_account_transaction (accountIndex);
            write_customer_info_to_file ();
            return true;
        }

        else
        {
            System.out.println ("You don't have any account with us.");
            return true;
        }

    }

    public void close_account_transaction (int accountIndex)
    {
        account_list = customer.get_all_accounts ();

        account_list.get (accountIndex).close_account ();

    }

    public void adminModeOn ()
    {
        isAdmin = true;
    }

    public void adminModeOff ()
    {
        isAdmin = false;
    }

    public boolean isAdmin ()
    {
        return isAdmin;
    }

    public void admin () throws InterruptedException
    {
        admin_validate_pin ();
        admin_transaction ();
    }

    public void admin_validate_pin ()
    {
        int pin_len = 0;
        String pin_entry = "0000";
        boolean found = false;

        while (!found)
        {
            pin_len = 0;

            // Prompt admin to enter four digits PIN. Limit the entry
            // to four digits.
            while (pin_len > 4 || pin_len < 4)
            {
                System.out.println ("Please enter your admin PIN:");
                pin_entry = console.readLine ();
                pin_len = pin_entry.length ();

                if (pin_len > 4 || pin_len < 4)
                {
                    System.out.println ("Valid PIN is four digits.\n");
                }
            }

            if (admin_pin.equals (pin_entry))
            {
                found = true;
            }
        }

    }

    public void admin_transaction () throws InterruptedException
    {
        final int DISPLAY_ABC = 1;
        final int DISPLAY_HIGHEST = 2;
        final int DISPLAY_ACCOUNT = 3;
        int user_choice = 0;
        int display_choice = 0;

        boolean valid_entry_1 = false;
        boolean valid_entry_2 = false;

        while (!valid_entry_1)
        {
            user_choice = 0;
            display_choice = 0;

            System.out.println ();
            System.out
                    .println (DISPLAY_ABC + ") See all the accounts in alphabetical order based on the customer name.");
            System.out.println (
                    DISPLAY_HIGHEST + ") See all the accounts in order of highest balance to lowest balance.");
            System.out.println (DISPLAY_ACCOUNT + ") See all the accounts belonging to the same customer ID.");

            System.out.print ("\nEnter choice ==> ");
            try
            {
                user_choice = console.readInt ();
                valid_entry_1 = true;
            }
            catch (NumberFormatException e1)
            {
                System.out.println ("Please enter a digit.");
            }
        }
        while (!valid_entry_2)
        {
            try
            {
                System.out.println ("Show\n 1) Only active accounts\n 2) All accounts including closed accounts\n");
                display_choice = console.readInt ();
                valid_entry_2 = true;
            }
            catch (NumberFormatException e2)
            {
                System.out.println ("Please enter a digit.");
            }
        }

        switch (user_choice)
        {
            // 1. Display all the accounts in alphabetical order based on the
            // customer name.

            case DISPLAY_ABC:
            {
                admin_show_accounts_by_name (display_choice);

                break;
            }

                // 2. Display all the accounts in order of highest balance to
                // lowest
                // balance.
            case DISPLAY_HIGHEST:
            {
                admin_show_accounts_by_balance (display_choice);
                break;
            }

                // 3. Display all the accounts belonging to the same customer
                // ID.
            case DISPLAY_ACCOUNT:
            {
                admin_show_customer_accounts ("");
                break;
            }
        }

    }

    public String admin_show_accounts_by_name (int DISPLAY_ACTIVE)
    {
        String info = "";

        if (0 == customerArrayList.size ())
        {
            info = "\nWe don't have any customer yet.";
            System.out.println (info);
            return info;
        }

        info += "\n[Name]\t[ID]\t[Account#]\t[Pin#]\t[Balance]\n";

        // Sort customers by their names
        customerArrayList.sort (null);

        // Look at all customers
        int c_size = customerArrayList.size ();
        for (int c = 0; c < c_size; c++)
        {
            Customer customer = customerArrayList.get (c);
            ArraySort<Account> accounts = customer.get_all_accounts ();

            // Show all of the accounts this customer has
            int a_size = accounts.size ();
            for (int a = 0; a < a_size; a++)
            {
                Account account = accounts.get (a);
                String account_status = account.get_account_type ();

                if (account_status != null && DISPLAY_ACTIVE == 1)
                {    // User choice 1 which is to show only active accounts
                    double balance = account.get_balance ();
                    NumberFormat dollar = NumberFormat.getCurrencyInstance ();

                    info += customer.get_customer_name () + "\t" + customer.get_id () + "\t"
                            + account.get_account_number () + "\t\t" + customer.get_pin () + "\t"
                            + dollar.format (balance) + "\n";
                }
                else
                    if (DISPLAY_ACTIVE == 2)
                    {   // User choice 2 which is to show all accounts
                        double balance = account.get_balance ();
                        NumberFormat dollar = NumberFormat.getCurrencyInstance ();

                        info += customer.get_customer_name () + "\t" + customer.get_id () + "\t"
                                + account.get_account_number () + "\t\t" + customer.get_pin () + "\t"
                                + dollar.format (balance) + "\t" + "\t"
                                + (account_status != null ? "Active\n" : "Closed\n");
                    }
            }
        }

        System.out.print (info);
        return info;
    }

    public String admin_show_accounts_by_balance (int DISPLAY_ACTIVE)
    {
        String info = "";

        if (0 == customerArrayList.size ())
        {
            info = "\nWe don't have any customer yet.";
            System.out.println (info);
            return info;
        }

        // Put all customer accounts into 1 list
        ArraySort<Account> all_accounts = new ArraySort<Account> (20);

        for (int c = 0, c_size = customerArrayList.size (); c < c_size; c++)
        {
            Customer customer = customerArrayList.get (c);
            ArraySort<Account> accounts = customer.get_all_accounts ();
            all_accounts.addAll (accounts);
        }

        // Sort list of all accounts by highest to lowest balance
        all_accounts.sort (null);

        // Show all accounts
        info += "\n[Name]\t[ID]\t[Account#]\t[Pin#]\t[Balance]\n";

        for (int a = 0, a_size = all_accounts.size (); a < a_size; a++)
        {
            Account account = all_accounts.get (a);
            Customer customer = account.get_customer ();

            String account_status = account.get_account_type ();

            if (account_status != null && DISPLAY_ACTIVE == 1)
            // User choice 1 which is to show only active accounts
            {
                double balance = account.get_balance ();
                NumberFormat dollar = NumberFormat.getCurrencyInstance ();

                info += customer.get_customer_name () + "\t" + customer.get_id () + "\t" + account.get_account_number ()
                        + "\t\t" + customer.get_pin () + "\t" + dollar.format (balance) + "\n";
            }

            else
                if (DISPLAY_ACTIVE == 2)
                // User choice 2 which is to show all accounts
                {
                    double balance = account.get_balance ();
                    NumberFormat dollar = NumberFormat.getCurrencyInstance ();

                    info += customer.get_customer_name () + "\t" + customer.get_id () + "\t"
                            + account.get_account_number () + "\t\t" + customer.get_pin () + "\t"
                            + dollar.format (balance) + "\t" + "\t"
                            + (account_status != null ? "Active\n" : "Closed\n");
                }
        }

        System.out.print (info);
        return info;
    }

    public String admin_show_customer_accounts (String customerID)
    {
        String info = "";

        if (0 == customerArrayList.size ())
        {
            info = "\nWe don't have any customer yet.";
            System.out.println (info);
            return info;
        }

        // Get a customer ID and customer object
        validate_id (customerID);

        info += "\n[Name]\t[ID]\t[Account#]\t[Pin#]\t[Balance]\n";

        ArraySort<Account> accounts = customer.get_all_accounts ();
        int a_size = accounts.size ();

        for (int a = 0; a < a_size; a++)
        {
            Account account = accounts.get (a);
            double balance = account.get_balance ();
            NumberFormat dollar = NumberFormat.getCurrencyInstance ();

            // Show the details for each of the customer's accounts
            info += customer.get_customer_name () + "\t" + customer.get_id () + "\t" + account.get_account_number ()
                    + "\t\t" + customer.get_pin () + "\t" + dollar.format (balance) + "\n";
        }

        System.out.print (info);
        return info;
    }

    public String showTransactions ()
    {
        String info = "";
        ArraySort transactions = log.getTransactions ();

        // Sort list of all transactions by descending timestamp
        transactions.sort (null);

        info += "\n[Date]\t\t\t[Transaction]\t[Customer]\t[Account]\t[Amount]\n";

        int size = transactions.size ();
        for (int t = 0; t < size; t++)
        {
            // Show the details for each transaction
            info += transactions.get (t);
        }

        System.out.print (info);
        return info;
    }

    @SuppressWarnings("unchecked")
    public void read_customer_info_from_file ()
    {
        try
        {
            File file = new File ("p1.dat");	// Get access to the p1.dat file
            file.createNewFile (); 				// Make sure it exists

            FileInputStream fs = new FileInputStream ("p1.dat");
            ObjectInputStream os = new ObjectInputStream (fs);

            customerArrayList = (ArraySort<Customer>) os.readObject ();	// read
            // all
            // customers
            // from
            // file
            customer_count = os.readInt ();					// read customer
                                           					// count from file
            account_count = os.readInt ();					// read account
                                          					// count from file
            transaction_counter = os.readInt ();	// read transaction counter
                                                	// (for counting log-in)

            os.close ();
            fs.close ();
        }
        catch (EOFException e)
        {
            // Do nothing. This happens when the file is empty.
        }
        catch (Exception e)
        {
            System.out.println ("\nError reading p1.dat\t" + customerArrayList);
            e.printStackTrace ();
        }
    }

    public void write_customer_info_to_file ()
    {
        try
        {
            FileOutputStream fs = new FileOutputStream ("p1.dat");
            ObjectOutputStream os = new ObjectOutputStream (fs);

            os.writeObject (customerArrayList);			// write all customers
                                               			// to file
            os.writeInt (customer_count);	// write customer count to file
            os.writeInt (account_count);	// write account count to file
            os.writeInt (transaction_counter);	// write transaction counter
                                              	// (for counting log-in)

            os.close ();
            fs.close ();
        }
        catch (Exception e)
        {
            System.out.println ("\nError writing p1.dat");
            e.printStackTrace ();
        }
    }

} // Last braces (to close the class)

/*
 * Description: You will implement the constructor and methods for the following
 * class to support a simple online banking system. Continue to use
 * ConsoleReader.java code and add the code to hw2.java. For HW #2, you just
 * need to implement options 1, 2, 3, 6, and 9. Option 1,2,3 can share the Get
 * Balance at the end of each option processed.
 */

//
// Description: You will implement the constructor and methods for the
// following class to support a simple online banking system. Continue
// to use ConsoleReader.java code and add on top of homework #2 For
// P1, you will complete the remain options: 0, 4, 5, and 7. In
// addition, Here are a few additional requirements.
//
// -- Rename hw2.java to p1.java
// -- Existing customer information will be saved/retrieved in a file call
// "p1.dat" in the same directory.
// -- Handle all the appropriate exceptions such as bad input.
// -- The administrator function is now expanded to the following:
// -- After the correct hardcoded password "abcd" is entered, display the
// following choices.
// -- 1. Display all the accounts in alphabetical order based on the customer
// name.
// -- 2. Display all the accounts in order of highest balance to lowest balance.
// -- 3. Display all the accounts belonging to the same customer ID.
// -- New information should be saved to "p1.dat" whenever there are any changes
// to customer balance and/or accounts such as Open, Deposit, Withdraw,
// Transfer,
// and any internal interest updates.
// -- The new output format for each account should fit in 1 horizontal line.
// -- When a customer closes an account, that account will never be deleted.
// Instead it will be cleared and marked as inactive.
// -- Initially the data file will not be there, therefore you must handle the
// "FileNotFoundException" and continue.

/*
 * Welcome to the CS49J Banking System
 *
 * 1) Create Customer
 * 2) Open Account
 * 3) Deposit
 * 4) Withdraw
 * 5) Transfer
 * 6) Get Balance
 * 7) Close Account
 * 9) Exit
 *
 * Actions for each option chosen within the Atm Class:
 *
 * 1) Create customer --hw2
 * - Prompt for customer name and a 4 digits/characters PIN
 * - Generate a customer ID
 * - Add new customer to array list
 *
 * 2) Open account--hw2
 * - Get customer ID and pin to login.
 * - Validate user information.
 * - Generate an account number, add to acct array list
 * - Display account information.
 *
 * 3) Deposit--hw2
 * - Get customer ID and pin to login.
 * - Validate user information.
 * - Get the deposit amount.
 * - Update the balance.
 * - Display account information.
 *
 * 4) Withdraw
 * - Get customer ID and pin to login.
 * - Validate user information.
 * - Get the withdraw amount.
 * - Validate and update balance
 * - Display account information.
 *
 * 5) Transfer
 * - Get customer ID and pin to login.
 * - Validate user information.
 * - Get the transfer amount.
 * - Validate and update balance on both accounts.
 * - Display account information.
 *
 * 6) Get Balance--hw2
 * - Get customer ID and pin to login.
 * - Validate user information.
 * - Display account information.
 *
 * 7) Close account
 * - Get customer ID and pin to login.
 * - Validate user information.
 *
 *
 * Notes: PIN is a 4 digit character string. Both Customer ID and account # are
 * “system” generated. ID starts with 101 and Account # starts with 1001 (Hint:
 * add a public static attribute in both Customer and Account) You should not
 * make any changes to ConsoleReader.java file. You need to submit 5 files: 4
 * *.java files and hw2.readme. Print the appropriate error message when
 * necessary. Make sure there are plenty of comments in the code. You should
 * have a log-in method inside Atm to prompt for customer ID and pin. You should
 * have a validate_ID_PW method inside Atm to validate the ID and pin from the
 * customer array-list.
 */
