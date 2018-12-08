// Sav_Acct.java :

public class Sav_Acct extends Account
{

    private double interest_rate;

    // public Sav_Acct (0)
    {
        interest_rate = 5;
    }

    public double calculate_interest (int login_count)
    {
        double interest = 0;

        if (login_count % 5 == 0)
        {
            interest = balance * (interest_rate / 100);
            balance += interest;
            // balance = balance * (1 + interest_rate / 100);
        }

        return interest;
    }
}
