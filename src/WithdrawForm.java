import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class WithdrawForm extends JFrame
{

    /**
     * 
     */
    private static final long  serialVersionUID = 1L;

    private JLabel             accountLabel;
    private JLabel             amountLabel;

    private JTextField         amountField;

    private JComboBox<String>  dropDown         = new JComboBox<String> ();

    private JButton            okButton;

    private OkButtonListner    okButtonAction;

    private ArrayList<Account> accountList;
    private String[]           existingAccount;
    private String[]           accountType;

    private int                accountNum;
    private double             amount           = 0;
    private int                size             = 0;

    private JPanel             noCustomer;
    private JPanel             noAccount;
    private JPanel             amountOver;
    private JPanel             confirmation;
    private JPanel             noAmount;
    private JPanel             nullAccount;

    private Atm                thisAtm;

    private double             accountBalance;

    // Constructor
    public WithdrawForm (Atm thisAtm)
    {
        this.thisAtm = thisAtm;

        setLayout (new FlowLayout ());

        boolean custArray = thisAtm.checkCustArray ();
        if (custArray == false)
        {
            JOptionPane.showMessageDialog (noCustomer, "We don't have any customer yet. ", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!getAccountInfo ())
        {
            setVisible (false);
            dispose ();
            return;
        }

        accountLabel = new JLabel ("Please choose an account.\n");
        accountLabel.setVisible (true);
        add (accountLabel);

        dropDown = new JComboBox<String> ();
        dropDown.setVisible (true);
        add (dropDown);

        for (int j = 0; j < existingAccount.length; j++)
        {
            dropDown.addItem (existingAccount[j]);
        }

        dropDown.addActionListener (new ActionListener ()
        {
            @SuppressWarnings("deprecation")
            @Override
            public void actionPerformed (ActionEvent e)
            {
                if (dropDown.getSelectedItem () == null)
                {
                    JOptionPane.showMessageDialog (nullAccount, "Please select the visible account.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        });

        amountLabel = new JLabel ("Please enter a withdraw amount.\n");
        amountLabel.setVisible (true);
        add (amountLabel);

        amountField = new JTextField (8);
        amountField.setVisible (true);
        add (amountField);

        okButton = new JButton ("OK");
        okButton.setVisible (true);
        add (okButton);

        okButtonAction = new OkButtonListner ();
        okButton.addActionListener (okButtonAction);
    }


    public class OkButtonListner implements ActionListener
    {

        @Override
        public void actionPerformed (ActionEvent click) throws NumberFormatException
        {
            String text = amountField.getText ();

            if (dropDown.getSelectedItem () == null)
            {
                JOptionPane.showMessageDialog (nullAccount, "Please select the visible account.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (text.equals (""))
            {
                JOptionPane.showMessageDialog (noAmount, "Please enter the amount", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            amount = Double.parseDouble (text);

            accountNum = dropDown.getSelectedIndex ();

            accountBalance = accountList.get (accountNum).get_balance ();

            System.out.println ("acccountBalance " + accountBalance);
            System.out.println ("acccountNum " + accountNum);

            if (accountBalance < amount)
            {
                JOptionPane.showMessageDialog (amountOver, "Withdraw limit is $" + accountBalance, "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            accountNum = dropDown.getSelectedIndex ();

            System.out.println ("from DropDown " + dropDown.getSelectedIndex ());

            try
            {
                thisAtm.withdraw (accountNum, amount);
            }
            catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace ();
            }

            double accountBalance = thisAtm.getAccountBalance (accountNum);

            JOptionPane.showMessageDialog (confirmation,
                    "Account Information:\n" + " Account Number :" + existingAccount[accountNum] + " ("
                            + accountType[accountNum] + ")\n" + "New Balance: " + accountBalance,
                    "Confirmation", JOptionPane.OK_OPTION);

        }
    }

    public void createWindow ()
    {
        setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
        setSize (500, 200);
        setVisible (true);
        setTitle ("Withdraw Form");
    }

    public boolean getAccountInfo ()
    {
        accountList = thisAtm.getAccoutList ();
        System.out.println ("accountList" + accountList);
        size = accountList.size ();
        existingAccount = new String[size];
        accountType = new String[size];

        if (size <= 0)
        {
            JOptionPane.showMessageDialog (noAccount, "You don't have any account with us. ", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        else
        {
            for (int i = 0; i < size; i++)
            {
                existingAccount[i] = accountList.get (i).get_account_number ();
                accountType[i] = accountList.get (i).get_account_type ();
            }
            return true;
        }
    }
}
