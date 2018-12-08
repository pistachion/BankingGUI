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


public class CloseForm extends JFrame
{

    /**
     * 
     */
    private static final long  serialVersionUID = 1L;

    private JLabel             accountLabel;

    private JComboBox<String>  dropDown         = new JComboBox<String> ();

    private JButton            okButton;

    private OkButtonListner    okButtonAction;

    private ArrayList<Account> accountList;
    private String[]           existingAccount;
    private String[]           accountType;

    private int                accountNum;
    private int                size             = 0;

    private JPanel             noCustomer;
    private JPanel             noAccount;
    private JPanel             confirmation;
    private JPanel             nullAccount;

    private Atm                thisAtm;

    // Constructor
    public CloseForm (Atm thisAtm)
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

        accountLabel = new JLabel ("Please choose an account you wish to close.\n");
        accountLabel.setVisible (true);
        add (accountLabel);

        dropDown = new JComboBox<String> ();
        dropDown.setVisible (true);
        add (dropDown);

        for (int j = 0; j < existingAccount.length; j++)
        {

            // String[] items = { new ComboItem("A"), new ComboItem("B"),
            // new ComboItem("1", false), new ComboItem("2", false),
            // new ComboItem("abc"), new ComboItem("def") };

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
            if (dropDown.getSelectedItem () == null)
            {
                JOptionPane.showMessageDialog (nullAccount, "Please select the visible account.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            accountNum = dropDown.getSelectedIndex ();
            boolean completed = thisAtm.close_account (accountNum);

            JOptionPane.showMessageDialog (confirmation,
                    "Closed Account Information:\n" + " Account Number :" + existingAccount[accountNum] + " ("
                            + accountType[accountNum] + ")\n" + " has been closed. ",
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
