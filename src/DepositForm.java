import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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


public class DepositForm extends JFrame
{

    /**
     * 
     */
    private static final long  serialVersionUID = 1L;

    // private JLabel message;
    // private JLabel accountsLabel;
    private JLabel             dropdownLabel;
    private JLabel             amountLabel;

    private JTextField         amountField;

    private JComboBox<String>  dropDown         = new JComboBox<String> ();

    private JButton            okButton;

    private OkButtonListner    okButtonAction;

    private ArrayList<Account> accountList;
    private String[]           existingAccount;
    private String[]           accountType;

    private int                itemNum;
    private double             amount           = 0;
    private int                size             = 0;

    private JPanel             amountNull;
    private JPanel             noAccount;
    private JPanel             confirmation;
    private JPanel             nullAccount;

    Atm                        thisAtm;

    // Constructor
    public DepositForm (Atm thisAtm)
    {
        setLayout (new FlowLayout ());
        this.thisAtm = thisAtm;

        // message = new JLabel ("You have following accounts with us. ");
        // add(message);
        //
        getAccountInfo ();
        GridBagLayout gridBagLayout = new GridBagLayout ();
        gridBagLayout.columnWidths = new int[] { 217, 132, 94, 0 };
        gridBagLayout.rowHeights = new int[] { 27, 29, 0, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        getContentPane ().setLayout (gridBagLayout);
        //
        // for (int i =0; i < size; i++)
        //
        // {
        // accountsLabel = new JLabel ("Account Number: " + existingAccount[i] +
        // " (" +accountType[i] + " Account )");
        // add(accountsLabel);
        // }

        dropdownLabel = new JLabel ("Please choose the account.\n");
        GridBagConstraints gbc_dropdownLabel = new GridBagConstraints ();
        gbc_dropdownLabel.anchor = GridBagConstraints.WEST;
        gbc_dropdownLabel.insets = new Insets (0, 0, 5, 5);
        gbc_dropdownLabel.gridx = 0;
        gbc_dropdownLabel.gridy = 0;
        getContentPane ().add (dropdownLabel, gbc_dropdownLabel);

        GridBagConstraints gbc_dropDown = new GridBagConstraints ();
        gbc_dropDown.fill = GridBagConstraints.HORIZONTAL;
        gbc_dropDown.anchor = GridBagConstraints.NORTH;
        gbc_dropDown.insets = new Insets (0, 0, 5, 5);
        gbc_dropDown.gridx = 1;
        gbc_dropDown.gridy = 0;
        getContentPane ().add (dropDown, gbc_dropDown);

        amountLabel = new JLabel ("Please enter the desposit amount.\n");
        GridBagConstraints gbc_amountLabel = new GridBagConstraints ();
        gbc_amountLabel.anchor = GridBagConstraints.WEST;
        gbc_amountLabel.insets = new Insets (0, 0, 5, 5);
        gbc_amountLabel.gridx = 0;
        gbc_amountLabel.gridy = 1;
        getContentPane ().add (amountLabel, gbc_amountLabel);

        amountField = new JTextField (8);
        GridBagConstraints gbc_amountField = new GridBagConstraints ();
        gbc_amountField.fill = GridBagConstraints.HORIZONTAL;
        gbc_amountField.insets = new Insets (0, 0, 5, 5);
        gbc_amountField.gridx = 1;
        gbc_amountField.gridy = 1;
        getContentPane ().add (amountField, gbc_amountField);

        okButton = new JButton ("OK");
        GridBagConstraints gbc_okButton = new GridBagConstraints ();
        gbc_okButton.fill = GridBagConstraints.HORIZONTAL;
        gbc_okButton.insets = new Insets (0, 0, 5, 0);
        gbc_okButton.anchor = GridBagConstraints.NORTH;
        gbc_okButton.gridx = 2;
        gbc_okButton.gridy = 1;
        getContentPane ().add (okButton, gbc_okButton);

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

        okButtonAction = new OkButtonListner ();
        okButton.addActionListener (okButtonAction);
    }


    public class OkButtonListner implements ActionListener
    {

        @Override
        public void actionPerformed (ActionEvent click) throws NumberFormatException

        {

            if (dropDown.getSelectedItem () == null)
            {
                JOptionPane.showMessageDialog (nullAccount, "Please select the visible account.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String text = amountField.getText ();

            if (text == null || text.equals (""))
            {
                JOptionPane.showMessageDialog (amountNull, "An amount needs to be more than $1.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                amount = Double.parseDouble (text);
                itemNum = dropDown.getSelectedIndex ();

                thisAtm.deposit (itemNum, amount);

                double updatedBalance = thisAtm.getAccountBalance (itemNum);

                JOptionPane.showMessageDialog (confirmation,
                        "Account Information:\n" + " Account Number :" + existingAccount[itemNum] + " ("
                                + accountType[itemNum] + ")\n" + "New Balance: " + updatedBalance,
                        "Confirmation", JOptionPane.OK_OPTION);
            }
        }
    }

    public void createWindow ()
    {
        setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
        setSize (500, 200);
        setVisible (true);
        setTitle ("Deposit Form");
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
