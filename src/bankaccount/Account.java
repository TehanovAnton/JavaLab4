package bankaccount;

import admin.Admin;
import card.Card;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Handler;

public abstract class Account implements Serializable {
    private int password;
    private int accountNumber;
    private int balance;
    private String kind;
    private Account_status status;
    private String bankName;


    public void setPassword(int password) {
        this.password = password;
    }
    public int getPassword() {
        return password;
    }

    public int getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getBalance() {
        return balance;
    }
    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
    public String getKind() {
        return kind;
    }

    public void setStatus(Account_status status) {
        this.status = status;
    }
    public Account_status getStatus() {
        return status;
    }

    public boolean IsOpen(){
        boolean isopened = status == Account_status.Opened;
        if (!isopened)
            System.out.println("акаунт заблокирован");
        return isopened;
    }

    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Account(int password, int accountNumber, String kind, String bankName) {
        this.password = password;
        this.accountNumber = accountNumber;
        this.kind = kind;
        this.status = Account_status.Opened;
        this.bankName = bankName;
        this.balance = 0;
    }
    public Account() {
    }
}
