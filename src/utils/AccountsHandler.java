package utils;

import java.util.TreeSet;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class AccountsHandler extends DefaultHandler {

    private TreeSet<Account> accounts;

    @Override
    public void startDocument() throws SAXException {
        accounts = new TreeSet<Account>();
    }

    private boolean accountsliststart = false;
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(qName.equals("accounts")){
            accountsliststart = true;
        }else if(qName.equals("account") && accountsliststart){
            Account ac = new Account(attributes.getValue("name"), attributes.getValue("createdon"));
            getAccounts().add(ac);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(qName.equals("accounts")){
            accountsliststart = false;
        }
    }
    
    /**
     * @return the accounts
     */
    public TreeSet<Account> getAccounts() {
        return accounts;
    }
}
