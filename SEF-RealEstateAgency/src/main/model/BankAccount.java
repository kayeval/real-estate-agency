package main.model;

public class BankAccount {
    private String bankAccountNo;
    private double balance;

    public BankAccount(double balance) {
        setBalance(balance);
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public boolean setBalance(double balance) {
        boolean valid = true;

        if (balance >= 0)
            this.balance = balance;

        else valid = false;

        return valid;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public double getBalance() {
        return balance;
    }
}
