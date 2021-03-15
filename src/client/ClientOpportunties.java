package client;

public interface ClientOpportunties {
    public void GetBalance(String fio);
    public void BlockAccount(String fio);
    public void Pay(String fio, int prise);
    public void TopUpBalance(String fio, int money);
}
