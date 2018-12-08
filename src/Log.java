import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/*
 * Log file class to store the timestamp, transaction , customer ,
 * account #, amount (either a + or – double number) for each transaction;
 * add 2 entries for XFER (one dep and one wd) but it must have the same
 * transaction for both;
 * 
 * Each interest update will add an entry into the log, make transaction ID =0
 * for all.
 * 
 * Make the log persistence by adding an array_list[] to the ATM class; then
 * stream the ATM class to a p2.log file.
 */
public class Log implements Serializable
{
    private static final long      serialVersionUID = -2233742987301014394L;
    private final String           FILE_NAME        = "p2.log";
    private ArraySort<Transaction> transactions;
    private int                    transactionCount;

    Log ()
    {
        read ();

        if (this.transactions == null)
        {
            this.transactions = new ArraySort<Transaction> (100);
        }
    }


    private class Transaction implements Comparable<Transaction>, Serializable
    {
        private static final long serialVersionUID = 2858454560098885644L;
        private long              timestamp;
        private int               id;
        private String            customer;
        private String            account;
        private double            amount;

        Transaction ()
        {
            super ();
            timestamp = new Date ().getTime ();
        }

        Transaction (int id, String customer, String account, double amount)
        {
            this ();
            this.id = id;
            this.customer = customer;
            this.account = account;
            this.amount = amount;
        }

		@Override
		public int compareTo (Transaction transaction)
		{
	        return Long.compare (transaction.getTimestamp(), timestamp);
		}

		private long getTimestamp ()
		{
			return timestamp;
		}
		
		public String toString ()
		{
			String info = "";
            NumberFormat dollar = NumberFormat.getCurrencyInstance ();
            Date date = new Date (timestamp);

            info += new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format (date) + "\t"
			     +  id                    + "\t\t"
				 +  customer              + "\t\t"
			     +  account               + "\t\t"
                 + dollar.format (amount) + "\n";

			return info;
		}
    }

	public ArraySort<Transaction> getTransactions ()
	{
		return transactions;
	}

    public void interest (double amount, String customer, String account)
    {
        int id = 0;
        Transaction t = new Transaction (id, customer, account, amount);
        transactions.add (t);

        write ();
    }

    public void transaction (double amount, String customer, String account)
    {
        int id = ++transactionCount;
        Transaction t = new Transaction (id, customer, account, amount);
        transactions.add (t);

        write ();
    }

    public void transfer (double amount, String customer, String from, String to)
    {
        int id = ++transactionCount;

        Transaction withdrawal = new Transaction (id, customer, from, amount);
        transactions.add (withdrawal);

        Transaction deposit = new Transaction (id, customer, to, amount);
        transactions.add (deposit);

        write ();
    }

    @SuppressWarnings("unchecked")
    public void read ()
    {
        try
        {
            File file = new File (FILE_NAME);   // Get access to the log file
            file.createNewFile ();              // Make sure it exists

            FileInputStream fs = new FileInputStream (FILE_NAME);
            ObjectInputStream os = new ObjectInputStream (fs);
            transactions = (ArraySort<Transaction>) os.readObject ();  // read
                                                                       // log
                                                                       // from
                                                                       // file

            os.close ();
            fs.close ();
        }
        catch (EOFException e)
        {
            // Do nothing. This happens when the file is empty.
        }
        catch (Exception e)
        {
            System.out.println ("\nError reading " + FILE_NAME + "\t" + transactions);
            e.printStackTrace ();
        }
    }

    public void write ()
    {
        try
        {
            FileOutputStream fs = new FileOutputStream (FILE_NAME);
            ObjectOutputStream os = new ObjectOutputStream (fs);

            os.writeObject (transactions);         // persist all transactions
            os.close ();
            fs.close ();
        }
        catch (Exception e)
        {
            System.out.println ("\nError writing " + FILE_NAME);
            e.printStackTrace ();
        }
    }
}
