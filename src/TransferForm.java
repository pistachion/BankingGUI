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


public class TransferForm extends JFrame
{

    /**
     * 
     */
    private static final long  serialVersionUID = 1L;

    private JLabel             toAccountLabel;
    private JLabel             fromAccountLabel;
    private JLabel             amountLabel;

    private JTextField         amountField;

    private JComboBox<String>  toDropDown       = new JComboBox<String> ();
    private JComboBox<String>  fromDropDown     = new JComboBox<String> ();

    private JButton            okButton;

    private OkButtonListner    okButtonAction;

    private ArrayList<Account> accountList;
    private String[]           existingAccount;
    private String[]           accountType;

    private int                fromAccountNum;
    private int                toAccountNum;
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
    private boolean            duplicate;

    // Constructor
    public TransferForm (Atm thisAtm)
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

        getAccountInfo ();

        fromAccountLabel = new JLabel ("Please choose FROM account.\n");
        fromAccountLabel.setVisible (true);
        add (fromAccountLabel);

        fromDropDown = new JComboBox<String> ();
        fromDropDown.setVisible (true);
        add (fromDropDown);

        toAccountLabel = new JLabel ("Please choose TO account.\n");
        toAccountLabel.setVisible (false);
        add (toAccountLabel);

        toDropDown = new JComboBox<String> ();
        toDropDown.setVisible (false);
        add (toDropDown);

        amountLabel = new JLabel ("Please enter the transfer amount.\n");
        amountLabel.setVisible (false);
        add (amountLabel);

        amountField = new JTextField (8);
        amountField.setVisible (false);
        add (amountField);

        okButton = new JButton ("OK");
        okButton.setVisible (false);
        add (okButton);

        okButtonAction = new OkButtonListner ();
        okButton.addActionListener (okButtonAction);

        for (int j = 0; j < existingAccount.length; j++)
        {
            fromDropDown.addItem (existingAccount[j]);
        }

        fromDropDown.addActionListener (new ActionListener ()
        {
            @SuppressWarnings("deprecation")
            @Override
            public void actionPerformed (ActionEvent e)
            {
                if (fromDropDown.getSelectedItem () == null)
                {
                    JOptionPane.showMessageDialog (nullAccount, "Please select the visible account.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                fromAccountNum = fromDropDown.getSelectedIndex ();
                accountBalance = accountList.get (fromAccountNum).get_balance ();

                for (int k = 0; k < existingAccount.length; k++)
                {
                    if (fromAccountNum != k)
                    {
                        toDropDown.addItem (existingAccount[k]);
                    }

                    else
                        if (fromAccountNum == k)
                        {
                            duplicate = true;

                        }
                }

                fromDropDown.setEnabled (false);
                toAccountLabel.setVisible (true);
                toDropDown.setVisible (true);
                amountLabel.setVisible (true);
                amountField.setVisible (true);
                okButton.setVisible (true);
                toDropDown.addActionListener (new ActionListener ()
                {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void actionPerformed (ActionEvent e)
                    {
                        if (toDropDown.getSelectedItem () == null)
                        {
                            JOptionPane.showMessageDialog (nullAccount, "Please select the visible account.", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                    }
                });

            }
        });

    }


    public class OkButtonListner implements ActionListener
    {

        @Override
        public void actionPerformed (ActionEvent click) throws NumberFormatException
        {
            if ((fromDropDown.getSelectedItem () == null) || (toDropDown.getSelectedItem () == null))
            {
                JOptionPane.showMessageDialog (nullAccount, "Please select the visible account.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String text = amountField.getText ();

            if (text.equals (""))
            {
                JOptionPane.showMessageDialog (noAmount, "Please enter the amount", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            amount = Double.parseDouble (text);

            accountBalance = accountList.get (fromAccountNum).get_balance ();

            if (accountBalance < amount)
            {
                JOptionPane.showMessageDialog (amountOver, "Transfer limit is $" + accountBalance, "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            fromAccountNum = fromDropDown.getSelectedIndex ();
            toAccountNum = toDropDown.getSelectedIndex ();

            if (duplicate == true && (fromAccountNum <= toAccountNum))
            {
                toAccountNum++;
            }

            System.out.println ("from DropDown " + fromDropDown.getSelectedIndex ());
            System.out.println ("to DropDown " + toDropDown.getSelectedIndex ());

            try
            {
                thisAtm.transfer (toAccountNum, fromAccountNum, amount);
            }
            catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace ();
            }

            double toAccountBalance = thisAtm.getAccountBalance (toAccountNum);
            double fromAccountBalance = thisAtm.getAccountBalance (fromAccountNum);

            JOptionPane.showMessageDialog (confirmation,
                    "Account Information:\n" + " Account Number :" + existingAccount[fromAccountNum] + " ("
                            + accountType[fromAccountNum] + ")\n" + "New Balance: " + fromAccountBalance
                            + "\n Account Number :" + existingAccount[toAccountNum] + " (" + accountType[toAccountNum]
                            + ")\n" + "New Balance: " + toAccountBalance,
                    "Confirmation", JOptionPane.OK_OPTION);

        }
    }

    public void createWindow ()
    {
        setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
        setSize (500, 200);
        setVisible (true);
        setTitle ("Transfer Form");
    }

    public void getAccountInfo ()
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
        }

        else
        {
            for (int i = 0; i < size; i++)
            {
                existingAccount[i] = accountList.get (i).get_account_number ();
                accountType[i] = accountList.get (i).get_account_type ();
            }
        }
    }
}
