package staxparser;

import bankaccount.Account_status;
import card.Card;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

public  class StaxParser {
    private HashSet<Card> cards = new HashSet<>();
    private XMLInputFactory inputFactory;
    public void CardStaxParser() {
        inputFactory = XMLInputFactory.newInstance();
    }
    public Set<Card> getCards(){
        return cards;
    }
    public void buildSetCards(String fileName) throws IOException {
        FileInputStream inputStream = null;
        XMLStreamReader reader = null;
        String name;

        try{
            inputStream = new FileInputStream(new File(fileName));
            reader = inputFactory.createXMLStreamReader(inputStream);
            while(reader.hasNext()){
                int type = reader.next();
                if (type == XMLStreamConstants.START_ELEMENT){
                    name = reader.getLocalName(); if (name.equals("card")){
                        Card st = buildCard(reader);
                        cards.add(st);
                    }
                }
            }
        }
        catch(XMLStreamException ex){
        }
        catch(FileNotFoundException ex){
        } catch (ParseException e) {
            e.printStackTrace();
        } finally{
            inputStream.close();
        }
    }
    private String getXMLText(XMLStreamReader reader) throws XMLStreamException{
        String text = null;
        if (reader.hasNext()){
            reader.next();
            text  =  reader.getText();
        }
        return text;
    }

    public Card buildCard(XMLStreamReader reader) throws XMLStreamException, ParseException {
        Card st = new Card();
        String name;
        int type;
        while (reader.hasNext()){
            type = reader.next();
            switch (type){
                case XMLStreamConstants.START_ELEMENT: name = reader.getLocalName();
                    if (name.equals("password"))
                        st.setPassword(Integer.parseInt(getXMLText(reader)));
                    if (name.equals("accountNumber"))
                        st.setAccountNumber(Integer.parseInt(getXMLText(reader)));
                    if (name.equals("balance"))
                        st.setBalance(Integer.parseInt(getXMLText(reader)));
                    if (name.equals("status"))
                        st.setStatus(getXMLText(reader).equals("Opened") ? Account_status.Opened : Account_status.Blocked);
                    if (name.equals("bankName"))
                        st.setBankName(getXMLText(reader));
                    if (name.equals("issueDate")) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                        st.setIssueDate(dateFormat.parse(getXMLText(reader)));
                    }
                    if (name.equals("validity"))
                        st.setValidity(Integer.parseInt(getXMLText(reader)));
                    if (name.equals("owner"))
                        st.setOwner(getXMLText(reader));

                break;
                case XMLStreamConstants.END_ELEMENT: name = reader.getLocalName();
                if (name.equals("card")) return st;
            }
        }
        throw new XMLStreamException("Unknown element in card");

    }
}
