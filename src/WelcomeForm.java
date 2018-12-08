import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class WelcomeForm extends JFrame
{
    public static void main (String[] args)
    {
        createWindow ();
    }

    public static void createWindow ()
    {
        Atm atm = new Atm ();
        WelcomeForm userLogin = null;
        userLogin = new WelcomeForm (atm, userLogin);
        userLogin.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
        userLogin.setSize (200, 200);
        userLogin.setVisible (true);
        userLogin.setTitle ("User Login");

        System.out.println ("Atm created in WelcomeForm " + atm);
    }

    private static final long     serialVersionUID = 1L;

    private JLabel                iDLabel;
    private JLabel                passwordLabel;

    private JTextField            idField;
    private JTextField            passwordField;

    private JButton               idButton;
    private JButton               pinButton;

    private JButton               newCustomer;

    private String                userId;

    private JPanel                noId;
    private JPanel                digitsNotMatch;
    private JPanel                noPinMatch;

    private IdButtonListener      idButtonAction;
    private PinButtonListener     pinButtonAction;
    private NewCustButtonListener newCustAction;

    int                           pinWindowCounter;
    String                        userPin, validated_pin;

    // Constructor
    public WelcomeForm (Atm atm, WelcomeForm f)
    {
        setLayout (new FlowLayout ());
        pinWindowCounter = 0;

        noId = (JPanel) getContentPane ();
        digitsNotMatch = (JPanel) getContentPane ();

        newCustomer = new JButton ("New Customer");
        add (newCustomer);
        newCustAction = new NewCustButtonListener (atm);
        newCustomer.addActionListener (newCustAction);

        iDLabel = new JLabel ("ID");
        add (iDLabel);

        idField = new JTextField (10);
        add (idField);

        idButton = new JButton ("OK");
        add (idButton);

        idButtonAction = new IdButtonListener (atm, f);
        idButton.addActionListener (idButtonAction);

        passwordLabel = new JLabel ("PIN");
        add (passwordLabel);
        passwordLabel.setVisible (false);

        passwordField = new JTextField (10);
        add (passwordField);
        passwordField.setVisible (false);

        pinButton = new JButton ("OK");
        add (pinButton);
        pinButton.setVisible (false);
    }


    public class NewCustButtonListener implements ActionListener
    {
        Atm atm;

        NewCustButtonListener (Atm atm)
        {
            this.atm = atm;
        }

        @Override
        public void actionPerformed (ActionEvent click)
        {
            NewCustomerForm newCustForm = new NewCustomerForm (atm);
            newCustForm.createWindow ();
        }
    }


    // subclass of AtmFrameClass
    public class IdButtonListener implements ActionListener
    {
        Atm         currentInst;
        WelcomeForm thisClass;

        IdButtonListener (Atm atm, WelcomeForm f)
        {
            currentInst = atm;
            thisClass = f;
        }

        @Override
        public void actionPerformed (ActionEvent click)
        {
            boolean found;
            pinWindowCounter++;

            userId = (idField.getText ());
            System.out.println ("User ID" + userId);

            found = currentInst.validate_id (userId);
            System.out.println ("found " + found);

            if (found == true)
            {
                passwordLabel.setVisible (true);
                passwordField.setVisible (true);
                pinButton.setVisible (true);

                pinButtonAction = new PinButtonListener (currentInst);
                pinButton.addActionListener (pinButtonAction);
            }

            else if (found == false)
            {
                JOptionPane.showMessageDialog (noId, "We cannot locate your customer ID. ", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    public class PinButtonListener implements ActionListener
    {
        Atm atm;

        PinButtonListener (Atm atm)
        {
            this.atm = atm;
        }

        @Override
        public void actionPerformed (ActionEvent click)
        {
            userPin = (passwordField.getText ());

            String storedPin = atm.getValidPin ();
            int pin_len = 0;
            pin_len = userPin.length ();

            if (userPin.equals (storedPin))
            {
                dispose ();

                if (atm.isAdmin ())
                {
                    AdminMenu adminMenu = new AdminMenu (atm);
                    adminMenu.createWindow ();
                }
                else
                {
                    BankMenu bankMenu = new BankMenu (atm);
                    bankMenu.createWindow ();
                }
            }

            else if (pin_len > 4 || pin_len < 4)
            {
                JOptionPane.showMessageDialog (digitsNotMatch, "User pin is four digits. ", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

            else if (!(userPin.equals (storedPin)))
            {
                JOptionPane.showMessageDialog (noPinMatch, "We cannot locate the pin ", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
