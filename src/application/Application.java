package application;
import admin.Admin;
import bankaccount.Account;
import bankaccount.Account_status;
import card.Card;
import client.Client;
import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.w3c.dom.Element;
import src.Main;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.Type;
import java.sql.SQLSyntaxErrorException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import validatorsax.ValidatorSAX;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Application {
    private static Scanner in = new Scanner(System.in);
    private static Act act;
    private static Logger LOG = Logger.getLogger(Main.class);

    public static Card CreateCard(String clientFio){
        Card newCard = new Card();
        System.out.print("информация о карте:\n\t" +
                "введите время службы: ");
        newCard.setValidity(in.nextInt());
        newCard.setOwner(clientFio);

        System.out.print("\tпридумайте пароль: ");
        newCard.setPassword(in.nextInt());

        System.out.print("\tпридумайте номер акаунта: ");
        newCard.setAccountNumber(in.nextInt());

        in = new Scanner(System.in);
        System.out.print("\tукажите тип карты: ");
        newCard.setKind(in.nextLine());

        System.out.print("\tукажите название банка: ");
        newCard.setBankName(in.nextLine());
        newCard.setIssueDate(new Date());
        newCard.setStatus(Account_status.Opened);
        return newCard;
    }
    public static void AddClient(){
        Client newClient = new Client();

        System.out.print("информация о клиенте:\n\t" +
                "введите фио: ");
        newClient.setFio(in.nextLine());

        newClient.setCard(CreateCard(newClient.getFio()));
    }
    public static void Print(){
        for(Card cl : Card.cardList)
        {
            System.out.println(String.format("\tвладелец: %s\n\tдата выдачи: %s\n\t" +
                            "срок службы в днях: %d\n\tбаланс: %d\n\t" +
                            "банк: %s\n\tномер акаунта: %d\n\t" +
                            "тип карты: %s\n\tстатус карты: %s",
                            cl.getOwner(), cl.getIssue_date(),
                    cl.getValidity(), cl.getBalance(),
                    cl.getBankName(), cl.getAccountNumber(),
                    cl.getKind(), cl.getStatus().toString()));
        }
    }
    public static void AskAdmin(){
        System.out.print("информация для админнистратора:\n\t" +
                "пароль: ");
        int password = in.nextInt();
        System.out.print("");

        System.out.print("\tномер акаунта: ");
        int accountnumber = in.nextInt();

        System.out.print("найти информацию о карте - [f]\n" +
                "заблокирровать - [b]\n" +
                "переиздать - [r]\n" +
                "введите [f|b|r]: ");
        in = new Scanner(System.in);
        String input = in.nextLine();
        if (input.equals("f"))
            Admin.FindInfo(password, accountnumber);
        else if (input.equals("b"))
            Admin.BlockAccount(password, accountnumber);
        else
            Admin.Reissue(password, accountnumber);
    }

    public static void buildCard(NodeList nodeList) throws ParseException {
        Card account = new Card();
        for (int i = 0; i < nodeList.getLength(); i++) {

            if (nodeList.item(i).getNodeName().equals("issueDate")) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                account.setIssueDate(dateFormat.parse(nodeList.item(i).getTextContent()));
            }
            else if (nodeList.item(i).getNodeName().equals("validity")){
                account.setValidity(Integer.parseInt(nodeList.item(i).getTextContent()));
            }
            else if (nodeList.item(i).getNodeName().equals("owner")){
                account.setOwner(nodeList.item(i).getTextContent());
            }
            else if (nodeList.item(i).getNodeName().equals("password")){
                account.setPassword(Integer.parseInt(nodeList.item(i).getTextContent()));
            }
            else if (nodeList.item(i).getNodeName().equals("accountNumber")){
                account.setAccountNumber(Integer.parseInt(nodeList.item(i).getTextContent()));
            }
            else if (nodeList.item(i).getNodeName().equals("balance")){
                account.setBalance(Integer.parseInt(nodeList.item(i).getTextContent()));
            }
            else if (nodeList.item(i).getNodeName().equals("kind")){
                account.setKind(nodeList.item(i).getTextContent());
            }
            else if (nodeList.item(i).getNodeName().equals("status")){
                account.setStatus(nodeList.item(i).getTextContent().equals("opened") ? Account_status.Opened : Account_status.Blocked);
            }
            else if (nodeList.item(i).getNodeName().equals("bankName")){
                account.setBankName(nodeList.item(i).getTextContent());
            }
        }
        //ValidatorSAX.valid();
    }
    public static void buildAccount(int index, NodeList nodeList) throws ParseException {
        for (int i = index; i < nodeList.getLength(); i++){
            if (nodeList.item(i).getNodeName().equals("card"))
            {
                buildCard(nodeList.item(i).getChildNodes());
            }

        }
    }
    public static void getTags(NodeList nodeList) throws ParseException {
        for (int i = 0; i < nodeList.getLength(); i++){
            if (nodeList.item(i).getNodeName().equals("cardList")){
                buildAccount(i, nodeList.item(i).getChildNodes());
            }

            if (nodeList.item(i).hasChildNodes()){
                getTags(nodeList.item(i).getChildNodes());
            }
        }

//        ValidatorSAX validatorSAX = new ValidatorSAX();
//        validatorSAX.valid();
    }
    public static void preCreate() throws ParserConfigurationException, IOException, SAXException, ParseException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File("C:\\Users\\Anton\\source\\repos\\pacei_NV_sovremenueTehnologiyVInternet\\лабораторные\\Lab4(среилилзация)\\javaLabWork3_2curs_2sem\\files\\savedXml.xml"));

        Element element = document.getDocumentElement();
        NodeList nodeList = element.getChildNodes();
        getTags(nodeList);
    }
    public static void saveToJson(){
        try(FileWriter file = new FileWriter("files\\saveToJson.txt", false)){
            Gson gson = new Gson();
            String json = gson.toJson(Card.cardList);

            file.write(json);
            file.flush();

            ArrayList<Card> cardlistfromjson = gson.fromJson(json, Card.cardList.getClass());
            System.out.println("was build from json: " + cardlistfromjson.size());
        }
        catch(IOException e)
        {
            LOG.info(e.getMessage() + "\n");
        }
    }

    public static void Run() throws ParserConfigurationException, IOException, SAXException, ParseException {
        System.out.println("\tздраствуйте");

        System.out.print("fast start? [y,n]: " );
        Scanner in = new Scanner(System.in);
        String fastStart = in.nextLine();

        if (fastStart.equals("y")){
            preCreate();
        }

        Continue();
    }
    private static void Choice(){
        String input = in.nextLine();
        if (input.equals("c"))
            act = Act.AddClient;
        else if (input.equals("p"))
            act = Act.ShowInfo;
        else if (input.equals("a"))
            act = Act.AskAdmin;
        else if (input.equals("b"))
            act = Act.TopupBalanc;
        else if (input.equals("pay"))
            act = Act.Pay;
        else if (input.equals("block"))
            act = Act.Block;
        else if (input.equals("v"))
            act = Act.ViewBalance;
        else if (input.equals("save"))
            act = Act.Save;
        else
            act = Act.Stop;
    }
    public static void Continue(){
        for (boolean work = true; work;)
        {
            try
            {
                System.out.print("введите символ действия:\n\t" +
                        "добавить клиетна - [c]\n\t" +
                        "вывести информацию о клиентах - [p]\n\t" +
                        "обратитья к администратору - [a]\n\t" +
                        "пополнить счёт - [b]\n\t" +
                        "оплатить - [pay]\n\t" +
                        "заблокировать акаунт - [block]\n\t" +
                        "посмотреть баланс - [v]\n\t" +
                        "закончить - [s]\n\t" +
                        "сохранить - [save]" +
                        "ввод [c|p|a|b|pay|block|v|s|save]: ");
                Choice();
                System.out.println("");


                if (act == Act.AddClient)
                    AddClient();
                else if (act == Act.ShowInfo)
                    Print();
                else if (act == Act.AskAdmin)
                    AskAdmin();
                else if (act == Act.Pay)
                {
                    System.out.print("введите fio: ");
                    String fio = in.nextLine();
                    Client.clientList.get(Client.Find(fio)).Pay(fio, 500);
                }
                else if (act == Act.TopupBalanc)
                {
                    System.out.print("введите fio: ");
                    String fio = in.nextLine();
                    Client.clientList.get(Client.Find(fio)).TopUpBalance(fio, 500);
                }
                else if (act == Act.Block)
                {
                    System.out.print("введите fio: ");
                    String fio = in.nextLine();
                    Client.clientList.get(Client.Find(fio)).BlockAccount(fio);
                }
                else if (act == Act.ViewBalance)
                {
                    System.out.print("введите fio: ");
                    String fio = in.nextLine();
                    Client.clientList.get(Client.Find(fio)).GetBalance(fio);
                }
                else if (act == Act.Save)
                    saveToJson();
                else
                {
                    work = false;
                }

                System.out.println("");
            }
            catch(Error e)
            {
                LOG.info(e.getMessage());
            }
        }

    }
}
