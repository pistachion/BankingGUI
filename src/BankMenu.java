
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class BankMenu extends JFrame
{

    /**
     * 
     */
    private static final long serialVersionUID    = 1L;

    private String[]          description         = { "1) Open Account", "2) Deposit", "3) Withdraw", "4) Transfer",
            "5) Account Information", "6) Close Account", "7)Exit ATM" };

    private JComboBox<String> dropDown            = new JComboBox<String> ();
    private JButton           okButton            = new JButton ("ok");
    private OkButtonListener  okButtonAction;

    final int                 ACTION_OPEN         = 0;
    final int                 ACTION_DEPOSIT      = 1;
    final int                 ACTION_WITHDRAW     = 2;
    final int                 ACTION_TRANSFER     = 3;
    final int                 ACTION_ACCOUNT_INFO = 4;
    final int                 ACTION_CLOSE        = 5;
    final int                 ACTION_EXIT         = 6;

    Container                 cp                  = getContentPane ();
    OpenAccountForm           newOpenAccount;
    DepositForm               newDepositForm;
    WithdrawForm              newWithdrawForm;
    TransferForm              newTransferForm;
    AccountInfoForm           newAccountInfoForm;
    CloseForm                 newCloseForm;
    private JPanel            nullAccount;

    Atm                       currentAtm;

    public BankMenu (Atm currentInst)
    {
        currentAtm = currentInst;

        for (int i = 0; i < description.length; i++)
        {
            dropDown.addItem (description[i]);
        }

        cp.add (dropDown);
        cp.add (okButton);
        cp.setLayout (new FlowLayout ());

        okButtonAction = new OkButtonListener ();
        okButton.addActionListener (okButtonAction);
    }

    public void createWindow ()
    {
        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        setSize (200, 125);
        setVisible (true);
        System.out.println ("Atm passed in BankMenu " + currentAtm);
    }


    public class OkButtonListener implements ActionListener
    {
        OkButtonListener ()
        {
        }

        @Override
        public void actionPerformed (ActionEvent click)
        {
            int itemNum = -1;
            itemNum = dropDown.getSelectedIndex ();
            int size = currentAtm.getAccoutList ().size ();

            // dispose ();
            System.out.println ("item Num " + itemNum);

            switch (itemNum)
            {

                case ACTION_OPEN:
                {
                    newOpenAccount = new OpenAccountForm (currentAtm);
                    newOpenAccount.createWindow ();

                    break;
                }

                case ACTION_DEPOSIT:
                {
                    if (size <= 0)
                    {
                        JOptionPane.showMessageDialog (nullAccount, "You don't have any account with us.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    newDepositForm = new DepositForm (currentAtm);
                    newDepositForm.createWindow ();

                    break;
                }

                case ACTION_WITHDRAW:
                {
                    if (size <= 0)
                    {
                        JOptionPane.showMessageDialog (nullAccount, "You don't have any account with us.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    newWithdrawForm = new WithdrawForm (currentAtm);
                    newWithdrawForm.createWindow ();
                    break;
                }

                case ACTION_TRANSFER:
                {
                    if (size <= 0)
                    {
                        JOptionPane.showMessageDialog (nullAccount, "You don't have any account with us.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    newTransferForm = new TransferForm (currentAtm);
                    newTransferForm.createWindow ();
                    break;
                }

                case ACTION_ACCOUNT_INFO:
                {
                    if (size <= 0)
                    {
                        JOptionPane.showMessageDialog (nullAccount, "Please select the visible account.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    newAccountInfoForm = new AccountInfoForm (currentAtm);
                    newAccountInfoForm.createWindow ();
                    break;
                }

                case ACTION_CLOSE:
                {
                    if (size <= 0)
                    {
                        JOptionPane.showMessageDialog (nullAccount, "You don't have any account with us.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    newCloseForm = new CloseForm (currentAtm);
                    newCloseForm.createWindow ();
                    break;
                }

                case ACTION_EXIT:
                {
                    System.out.println ("\n Good bye\n");
                    dispose ();
                    return;
                }
            }
        }
    }
}
