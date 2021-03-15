package card;

import admin.Admin;
import bankaccount.Account;
import bankaccount.Account_status;

import java.util.ArrayList;
import java.util.Date;
import java.io.Serializable;

public class Card extends Account implements Serializable {
    public static ArrayList<Card> cardList = new ArrayList<Card>();

    private Date issueDate;
    private int validity; //срок действия карты в днях
    private String owner;

    public Date getIssue_date() {
        return issueDate;
    }
    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public void setValidity(int validity) {
        this.validity = validity;
    }
    public int getValidity() {
        return validity;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
    public String getOwner() {
        return owner;
    }

    public Card(Date issue_date, int validity, String owner,
                int password, int accountNumber, String kind, String bankName) {
        super(password, accountNumber, kind, bankName);
        this.issueDate = issue_date;
        this.validity = validity;
        this.owner = owner;

        cardList.add(this);
    }
    public Card() {cardList.add(this);}
}
