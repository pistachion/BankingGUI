
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;


public class AdminMenu extends JFrame
{
    private static final long serialVersionUID  = 1L;

    private JComboBox<String> dropDown          = new JComboBox<String> ();
    private JButton           okButton          = new JButton ("ok");
    private OkButtonListener  okButtonAction;
    private JTextArea         textArea          = new JTextArea ();
    private JLabel            customerIDLabel   = new JLabel ("Customer ID");
    private JTextField        customerIDText    = new JTextField (8);
    private JPanel            noEntry;
    private JPanel            middlePanel;

    final int                 SHOW_BY_NAME      = 1;
    final int                 SHOW_BY_BALANCE   = 2;
    final int                 SHOW_BY_CUSTOMER  = 3;
    final int                 SHOW_TRANSACTIONS = 4;

    private String[]          description       = {
            SHOW_BY_NAME + ") See all the accounts in alphabetical order based on the customer name.",
            SHOW_BY_BALANCE + ") See all the accounts in order of highest balance to lowest balance.",
            SHOW_BY_CUSTOMER + ") See all the accounts belonging to the same customer ID.",
            SHOW_TRANSACTIONS + ") See transaction log in descending time order." };

    // Container cp = getContentPane ();
    Atm                       atm;

    public AdminMenu (Atm atm)
    {
        this.atm = atm;

        setLayout (new FlowLayout ());

        // customerIDText.setSize (1000, 800);

        textArea.setVisible (false);
        // textArea.setSize (780, 250);
        textArea.setEditable (false);

        for (int i = 0; i < description.length; i++)
        {
            dropDown.addItem (description[i]);
        }

        middlePanel = new JPanel ();
        middlePanel.setBorder (new TitledBorder (new EtchedBorder (), "Accounts Information"));

        textArea = new JTextArea (20, 30);

        textArea.setEditable (false);
        // infoLabel.setVisible (true);
        // add (infoLabel);

        JScrollPane scroll = new JScrollPane (textArea);
        scroll.setVerticalScrollBarPolicy (ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        middlePanel.add (scroll);
        add (middlePanel);
        pack ();
        setLocationRelativeTo (null);

        add (dropDown);
        add (okButton);
        add (customerIDText);
        add (customerIDLabel);
        // cp.add (textArea);
        // cp.setLayout (new FlowLayout ());

        okButtonAction = new OkButtonListener ();
        okButton.addActionListener (okButtonAction);

        dropDown.addActionListener (new ActionListener ()
        {
            @Override
            public void actionPerformed (ActionEvent e)
            {
                boolean showCustomerIDText = dropDown.getSelectedIndex () == 2;
                customerIDText.setVisible (showCustomerIDText);
            }
        });
    }

    public void createWindow ()
    {
        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        setSize (700, 500);
        setVisible (true);
        System.out.println ("Atm passed in AdminMenu " + atm);
    }


    public class OkButtonListener implements ActionListener
    {
        OkButtonListener ()
        {
        }

        @Override
        public void actionPerformed (ActionEvent click)
        {
            String info = "";
            int itemNum = dropDown.getSelectedIndex () + 1;

            // dispose ();
            System.out.println ("Admin Menu Item #: " + itemNum);

            switch (itemNum)
            {
                case SHOW_BY_NAME:
                {
                    info = atm.admin_show_accounts_by_name (1);
                    break;
                }

                case SHOW_BY_CUSTOMER:
                {
                    String customerID = customerIDText.getText ();
                    if (customerID.equals (""))
                    {
                        JOptionPane.showMessageDialog (noEntry, "Please enter an ID.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    info = atm.admin_show_customer_accounts (customerID);
                    break;
                }

                case SHOW_BY_BALANCE:
                {
                    info = atm.admin_show_accounts_by_balance (1);
                    break;
                }

                case SHOW_TRANSACTIONS:
                {
                    info = atm.showTransactions ();
                    break;
                }
            }

            textArea.setText (info);
        }
    }
}
