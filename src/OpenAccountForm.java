import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class OpenAccountForm extends JFrame{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private JLabel accountChoice;
 
    
    private JButton savingButton; 
    private JButton checkingButton; 


    private checkingButtonListner checkingButtonAction; 
    private savingButtonListner savingButtonAction; 
    
    private JPanel newAccountInfo;

    String accountType; 
    String accountNumber; 
    double accountBalance;
    
    private Atm currentAtm;  
    
    // Constructor 
    public OpenAccountForm (Atm thisAtm)
    {
        
        setLayout (new FlowLayout());
        currentAtm = thisAtm; 
        
        accountChoice = new JLabel ("Which account would you like to open?");
        add(accountChoice);

        savingButton = new JButton ("Saving Account");
        add(savingButton);
        savingButtonAction = new savingButtonListner (); 
        savingButton.addActionListener (savingButtonAction);
        
        checkingButton = new JButton ("checking Account");
        add(checkingButton);
        checkingButtonAction = new checkingButtonListner (); 
        checkingButton.addActionListener (checkingButtonAction);
        
        newAccountInfo = (JPanel) getContentPane();
        
 
    }
    
    public class checkingButtonListner implements ActionListener
    {
        
      public void actionPerformed (ActionEvent click)
        {
            accountType = "Checking";
            currentAtm.open_account (accountType);
            showAccountInfo();
                     
        }
    }
    
    public class savingButtonListner implements ActionListener
    {   
        public void actionPerformed (ActionEvent click)
        {
            accountType = "Saving";
            currentAtm.open_account (accountType);
            showAccountInfo();

        }
        
    }
    
    public void showAccountInfo ()
    {
        accountNumber = currentAtm.getThisAccountNum ();
        
        accountBalance = currentAtm.getAccountBalance ();
        NumberFormat dollar = NumberFormat.getCurrencyInstance ();
        
        JOptionPane.showMessageDialog(newAccountInfo, "Account Type        :   " + accountType + "\n" + 
                "Account Number      :   " + accountNumber +"\n" + "Balance             :    " + dollar.format (accountBalance), "New Account Information", JOptionPane.PLAIN_MESSAGE);
    }
    
    public class gotItButtonListner implements ActionListener
    {   
        public void actionPerformed (ActionEvent click)
        {
            dispose(); 

        }
        
    }
   
    
    public void createWindow (){

        setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
        setSize (500,200);
        setVisible (true);
        setTitle ("Open Account Form"); 
        

    }
    

    
}

