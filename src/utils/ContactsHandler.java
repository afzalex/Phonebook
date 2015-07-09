package utils;

import java.net.URL;
import java.util.Date;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import resources.ResourceHelper;

public class ContactsHandler extends DefaultHandler {

    private java.util.TreeSet<Contact> contacts;

    @Override
    public void startDocument() throws SAXException {
        contacts = new java.util.TreeSet<Contact>();
    }

    private boolean contactliststart = false;
    private String currcontact;
    private String currcontactcreated;
    private java.util.ArrayList<String[]> currprops;
    private java.util.TreeSet<String> currsummary;
    private String currimageloc;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("property")) {
            if (currprops == null) {
                currprops = new java.util.ArrayList<>();
            }
            currprops.add(new String[]{attributes.getValue("name"), attributes.getValue("value")});
        } else if (qName.equals("summary")) {
            if (currsummary == null) {
                currsummary = new java.util.TreeSet<>();
            }
            currsummary.add(attributes.getValue("value"));
        } else if (qName.equals("imageloc")) {
            currimageloc = attributes.getValue("value");
        } else if (qName.equals("contact") && contactliststart) {
            currcontact = attributes.getValue("name");
            currcontactcreated = attributes.getValue("createdon");
        } else if (qName.equals("contacts")) {
            contactliststart = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("contact")) {
            String[][] props = null;
            String[] summary = null;
            Date created;
            URL imageloc;
            if (currprops != null) {
                props = new String[currprops.size()][2];
                for(int i = 0; i< currprops.size(); i++){
                    props[i][0]=currprops.get(i)[0];
                    props[i][1]=currprops.get(i)[1];
                }
            }
            if (currsummary != null) {
                summary = new String[currsummary.size()];
                int i = 0;
                for (String smry : currsummary) {
                    summary[i++] = smry;
                }
            }

            if (currcontactcreated == null) {
                created = new Date();
            } else {
                created = new Date(new Long(currcontactcreated));
            }
            try {
                imageloc = ResourceHelper.getResourceURL(currimageloc);
            } catch (Exception ex) {
                System.out.println("ContactsHandler > endElement(...) > Error in getting image location : "+ex);
                imageloc = null;
            }
            Contact contact = new Contact(currcontact, imageloc, created, summary, props);
            getContacts().add(contact);
            currcontact = null;
            currcontactcreated = null;
            currprops = null;
            currsummary = null;
        } else if (qName.equals("contacts")) {
            contactliststart = false;
        }
    }

    /**
     * @return the contacts
     */
    public java.util.TreeSet<Contact> getContacts() {
        return contacts;
    }
}
