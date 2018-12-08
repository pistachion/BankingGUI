
// Account.java :

import java.io.Serializable;
import java.text.NumberFormat;


public class Account implements Comparable<Account>, Serializable
{
    // Need this to correctly write and save Account objects
    private static final long serialVersionUID = 6419845614489957963L;

    private String            number;                                                                        						// 4-digit
                                                                                                             						// string
    protected double          balance;
    private boolean           active;
    private String            account_type;

    // variables created by Roxie
    private Customer          customer;

    private int               login_counter;

    public Account ()
    {

    }

    @Override
    public int compareTo (Account account)
    {
        return Double.compare (account.get_balance (), balance);
    }

    public void set_acct_num (String num_string)
    {
        number = num_string;
        active = true;
    }

    public void set_account_type (String account_choice)
    {
        if (account_choice.equalsIgnoreCase ("s"))
        {
            account_type = "Saving";
        }
        else
        {
            account_type = "Checking";
        }
    }

    public String get_account_type ()
    {
        if (active == false)
        {
            return null;
        }
        else
        {
            return account_type;
        }
    }

    public String show_account_info ()
    {
        String info = "";

        if (active == false)
        {
            // System.out.println("");
            // System.out.println("Account #" + number + " has been closed.");
        }

        else
        {
            NumberFormat dollar = NumberFormat.getCurrencyInstance ();

            info += "\nAccount Number : " + number + "\nAccount Type   : " + account_type + "\nBalance        : "
                    + dollar.format (balance);
            // System.out.println ("Active : " + active);
        }

        return info;
    }

    public String get_account_number ()
    {
        if (active == false)
        {
            return null;
        }
        else
        {
            return number;
        }
    }

    public String get_null_account_number ()
    {
        if (active == false)
        {
            return number;
        }

        else
            return null;
    }

    public String get_all_account_number ()
    {
        return number;
    }

    public double get_balance ()
    {
        if (active == false)
        {
            return -1;// 0.000001;
        }
        else
        {
            return balance;
        }
    }

    public void update_balance (double update_balance)
    {
        balance = update_balance;
    }

    public void close_account ()
    {
        active = false;
        System.out.println ("Your account #" + number + " is closed.");
    }

    public void set_customer (Customer c)
    {
        customer = c;
    }

    public Customer get_customer ()
    {
        return customer;
    }
}
