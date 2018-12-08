import java.io.Serializable;
import java.util.*;


public class Customer implements Comparable<Customer>, Serializable
// When I use sort () in Atm class, this is what I have to write (A)
{
    // Need this to correctly write and save Customer objects
    private static final long serialVersionUID = 4389595949708913961L;

    private String             name;
    private String             id;                						// 3-digits
    private String             pin;               				// 4-digits
    private ArrayList<Account> accounts_ArrayList;			// list of customer
                                                  			// accounts
    private double             total_balance;     		// for all accounts

    // variables created by Roxie
    private double saving_balance;
    private double checking_balance;

    public Customer () // Constructor
    {
        accounts_ArrayList = new ArrayList<Account> (10);

    }

    // When I use sort() in Atm class, this is what I have to write (B)
    // Write both (A) and (B)
    @Override
    public int compareTo (Customer c)
    {
        return this.name.compareTo (c.name);
    }

    public double cal_total_balance ()
    {
        int size = accounts_ArrayList.size ();

        for (int i = 0; i < size; i++)
        {
            total_balance += accounts_ArrayList.get (i).get_balance ();
        }

        return total_balance;
    }

    public String get_customer_name ()
    {
        return name;
    }

    public double cal_checking_balance ()
    {
        int size = accounts_ArrayList.size ();

        for (int i = 0; i < size; i++)
        {
            String account_type = accounts_ArrayList.get (i).get_account_type ();
            if (account_type.equalsIgnoreCase ("Checking"))
            {
                checking_balance += accounts_ArrayList.get (i).get_balance ();
            }
        }

        return checking_balance;
    }

    public double cal_saving_balance ()
    {
        int size = accounts_ArrayList.size ();

        for (int i = 0; i < size; i++)
        {
            String account_type = accounts_ArrayList.get (i).get_account_type ();
            if (account_type.equalsIgnoreCase ("Saving"))
            {
                saving_balance += accounts_ArrayList.get (i).get_balance ();
            }
        }

        return saving_balance;
    }

    public String toString ()
    {
        return "\nCustomer\nname	: " + name + "\nid	: " + id + "\npin	: " + pin + "\n";
    }

    public void set_name (String name_entry)
    {
        name = name_entry;
    }

    public void set_pin (String pin_entry)
    {
        pin = pin_entry;
    }

    public void set_id (String g_id)
    {
        id = g_id;
    }

    public String get_id ()
    {
        return id;
    }

    public String get_pin ()
    {
        return pin;
    }

    public void add_account (Account account)
    {
        account.set_customer (this);
        accounts_ArrayList.add (account);
    }

    public Account get_account ()
    {
        return accounts_ArrayList.get (0);
    }

    public ArraySort<Account> get_all_accounts ()
    {
        ArraySort<Account> new_list = new ArraySort<Account> ();
        int size = accounts_ArrayList.size ();

        for (int i = 0; i < size; i++)
        {
            new_list.add (i, accounts_ArrayList.get (i));
        }

        return new_list;
    }
}
