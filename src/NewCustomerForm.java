import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class NewCustomerForm extends JFrame
{

    /**
     * 
     */
    private static final long  serialVersionUID = 1L;

    private JLabel             nameLabel;
    private JLabel             pinLabel;
    private JLabel             idLabel;

    private JTextField         nameField;
    private JTextField         pinField;

    private JButton            okButton;
    private JButton            gotItButton;

    private String             userName;
    private String             userPin;

    private JPanel             pinError_1;
    private JPanel             pinError_2;

    private JPanel             pinEmpty;
    private JPanel             nameEmpty;

    private OkButtonListner    okButtonAction;
    private GotItButtonListner gotItButtonAction;
    private String             generatedId;

    Atm                        thisAtm;

    // Constructor
    public NewCustomerForm (Atm atm)
    {
        setLayout (new FlowLayout ());

        pinError_1 = (JPanel) getContentPane ();
        pinError_2 = (JPanel) getContentPane ();

        nameLabel = new JLabel ("Please enter your name");
        add (nameLabel);

        nameField = new JTextField (10);
        add (nameField);

        pinLabel = new JLabel ("Please create your four digits pin. ");
        add (pinLabel);

        pinField = new JTextField (10);
        add (pinField);

        okButton = new JButton ("OK");
        add (okButton);
        okButtonAction = new OkButtonListner ();
        okButton.addActionListener (okButtonAction);

        // idLabel = new JLabel (idMessage);
        // add(idLabel);
        // idLabel.setVisible (false);

        gotItButton = new JButton ("Got it.");
        add (gotItButton);
        gotItButton.setVisible (false);
        gotItButtonAction = new GotItButtonListner ();
        gotItButton.addActionListener (gotItButtonAction);

        thisAtm = atm;
    }


    public class OkButtonListner implements ActionListener
    {
        @Override
        public void actionPerformed (ActionEvent click)
        {
            userName = (nameField.getText ());
            if (userName.equals (""))
            {
                JOptionPane.showMessageDialog (nameEmpty, "Please enter your name", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            userPin = (pinField.getText ());

            if (userPin.equals (""))
            {
                JOptionPane.showMessageDialog (pinEmpty, "Please enter a four digits pin", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int len = userPin.length ();
            if (len > 4 || len < 4 || userPin.matches ("0000"))
            {

                if (userPin.matches ("0000"))
                {
                    JOptionPane.showMessageDialog (pinError_1, "Please choose a different PIN.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

                else
                    if (len > 4 || len < 4)
                    {
                        JOptionPane.showMessageDialog (pinError_2, "PIN needs to be four digits.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
            }

            else
            {
                thisAtm.create_customer (userName, userPin);
                Customer thisCustomer = thisAtm.getThisCustomer ();
                generatedId = thisCustomer.get_id ();
                // idMessage = "Hello, " + userName + ". Your ID is "+
                // generatedId + ".";
                idLabel = new JLabel ("Hello, " + userName + ". Your ID is " + generatedId + ".");

                add (idLabel);
                idLabel.setVisible (true);
                gotItButton.setVisible (true);
            }
        }
    }


    public class GotItButtonListner implements ActionListener
    {
        @Override
        public void actionPerformed (ActionEvent click)
        {
            dispose ();
        }
    }

    public void createWindow ()
    {

        setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
        setSize (500, 200);
        setVisible (true);
        setTitle ("Create New Customer");

        System.out.println ("CreateWindow in New Customer Form");
    }

    public String getPin ()
    {
        return userPin;
    }
}
