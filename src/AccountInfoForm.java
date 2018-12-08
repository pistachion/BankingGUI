import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;


public class AccountInfoForm extends JFrame
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private JTextArea         infoLabel;

    private Atm               thisAtm;

    private JPanel            middlePanel;
    private JPanel            noCustomer;

    private String            accountInfo      = " ";

    // Constructor
    public AccountInfoForm (Atm thisAtm)
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

        middlePanel = new JPanel ();
        middlePanel.setBorder (new TitledBorder (new EtchedBorder (), "Accounts Information"));

        infoLabel = new JTextArea (20, 20);
        infoLabel.setText (thisAtm.printout_accounts ());
        infoLabel.setEditable (false);
        // infoLabel.setVisible (true);
        // add (infoLabel);

        JScrollPane scroll = new JScrollPane (infoLabel);
        scroll.setVerticalScrollBarPolicy (ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        middlePanel.add (scroll);
        add (middlePanel);
        pack ();
        setLocationRelativeTo (null);
    }

    public void createWindow ()
    {
        setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
        setSize (300, 400);
        setVisible (true);
        setTitle ("Withdraw Form");

    }
}
