package client;

import admin.Admin;
import application.Application;
import bankaccount.Account;
import bankaccount.Account_status;
import card.Card;

import javax.annotation.processing.SupportedSourceVersion;
import java.awt.desktop.AppForegroundListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.io.Serializable;


public class Client implements ClientOpportunties, Serializable{
    public static ArrayList<Client> clientList = new ArrayList();

    private String fio;
    private Card card;

    public String getFio() {
        return fio;
    }
    public void setFio(String fio) {
        this.fio = fio;
    }

    public Client(Card card, String fio) {
        this.fio = fio;
        this.card = card;

        clientList.add(this);
    }
    public Client(){
        clientList.add(this);
    }

    public void setCard(Card card) {
        this.card = card;
    }
    public Card getCard() {
        return card;
    }

    public static int Find(String fio)
    {
        ArrayList<Client> clients = Client.clientList;
        int resCardIndex = -1;
        for (int i = 0; i < clients.size(); i++) {
            Client client = clients.get(i);
            if (client.getFio().equals(fio))
                resCardIndex = i;
        }
        return resCardIndex;
    }

    @Override
    public void GetBalance(String fio) {
        Client client = Client.clientList.get(Client.Find(fio));

        if (card.IsOpen())
            System.out.println(String.format("отсаток на счёте: {0}", client.getCard().getBalance()));
        else
            throw new Error("акаунт заблокирован\n\n");
    }

    @Override
    public void BlockAccount(String fio) {
        Client client = Client.clientList.get(Client.Find(fio));
        client.getCard().setStatus(Account_status.Blocked);
    }

    @Override
    public void Pay(String fio, int prise) {
        Client client = Client.clientList.get(Client.Find(fio));
        if (client.getCard().IsOpen()){
            client.getCard().setBalance(client.getCard().getBalance() - prise);
            System.out.println("платёж одобрен");
        }
    }

    @Override
    public void TopUpBalance(String fio, int money) {
        Client client = Client.clientList.get(Client.Find(fio));
        if(client.getCard().IsOpen()) {
            client.getCard().setBalance(client.getCard().getBalance() + money);
            System.out.println("счёт пополнен");
        }
    }
}
