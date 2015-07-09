package utils;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.TreeSet;
import resources.ResourceHelper;
import static utils.MainUtils.BASE;

public class Account implements Comparable, Serializable {

    private final String name;
    private final Date created;
    private final Date modified;
    private java.util.TreeSet<Contact> contacts;

    public Account(String name) {
        this(name, new Date());
    }

    public Account(String name, Date created) {
        this.name = name;
        this.created = created;
        this.modified = new Date();
    }

    public Account(String name, String longtime) {
        this.name = name;
        this.created = new Date(new Long(longtime));
        this.modified = new Date();
    }

    /**
     * @return the name of account
     */
    public String getName() {
        return name;
    }

    /**
     * @return the date when account is created
     */
    public Date getCreated() {
        return created;
    }

    /**
     * @return the date when account is last accessed
     */
    public Date getModified() {
        return modified;
    }

    public static void createAccountBase(String name) {
        try {
            File acbase = new File(BASE, name);
            if (!acbase.exists()) {
                if (!acbase.mkdirs()) {
                    throw new Exception("Base directory cannot be created here");
                }
                File imgbase = new File(acbase, "images");
                if (!imgbase.mkdirs()) {
                    throw new Exception("images directory cannot be created for Account '" + name + "' here");
                }
            }
        } catch (Exception ex) {
            System.out.println("Account > addAccount() > Error in creating Account : " + ex);
        }
    }

    public TreeSet<Contact> loadContacts() {
        try {
            File acbase = new File(BASE, name);
            if (!acbase.exists()) {
                System.out.println("Account > loadContacts() > Error : Contact location not found");
                System.out.println("Account > loadContacts() > creating account ...");
                createAccountBase(name);
            } else {
                File contactfile = new File(acbase, "contacts.xml");
                if (!contactfile.exists()) {
                    System.out.println("Account > loadContacts() > Error : Contact file 'contacts.xml' not found");
                    storeContacts();
                }
                javax.xml.parsers.SAXParserFactory spf = javax.xml.parsers.SAXParserFactory.newInstance();
                spf.setNamespaceAware(false);
                javax.xml.parsers.SAXParser parser = spf.newSAXParser();
                org.xml.sax.XMLReader reader = parser.getXMLReader();
                ContactsHandler handler = new ContactsHandler();
                reader.setContentHandler(handler);
                reader.parse(new org.xml.sax.InputSource(new java.io.FileInputStream(contactfile)));
                contacts = handler.getContacts();
            }
        } catch (Exception ex) {
            System.out.println("Account > loadContacts() > Error in loading contacts : " + ex);
        }
        return contacts;
    }

    public void storeContacts() {
        try {
            File acbase = new File(BASE, name);
            if (!acbase.exists()) {
                System.out.println("Account > storeContacts() > Error : Contact location not found");
                System.out.println("Account > storeContacts() > creating account ...");
                createAccountBase(name);
            }
            File contactsfile = new File(acbase, "contacts.xml");
            javax.xml.stream.XMLOutputFactory xof = javax.xml.stream.XMLOutputFactory.newInstance();
            javax.xml.stream.XMLStreamWriter writer = xof.createXMLStreamWriter(
                    new java.io.FileOutputStream(contactsfile));
            writer.writeStartDocument("UTF-8", "1.0");                  // <?xml version="1.0" encoding="UTF-8"?>
            writer.writeStartElement("contacts");                       // <contacts
            writer.writeAttribute("account", name);                     //           account=[name]>
            if (getContacts() != null) {
                for (Contact c : getContacts()) {
                    writer.writeStartElement("contact");               //    <contact
                    writer.writeAttribute("name", c.getName());        //             name=[c.getName]
                    writer.writeAttribute("createdon",
                            Long.toString(c.getCreatedon().getTime()));//             createdon=[...]>
                    if (c.getImageLoc() != null) {
                        String home = ResourceHelper.getResourceURL("").toString();
                        String cont = c.getImageLoc().toString();
                        if (cont.startsWith(home)) {
                            cont = cont.substring(home.length());
                        }
                        writer.writeEmptyElement("imageloc");         //       <imageloc
                        writer.writeAttribute("value", cont);         //                 value=[cont] />
                    }
                    if (c.getSummary() != null) {
                        for (String smry : c.getSummary()) {
                            writer.writeEmptyElement("summary");      //       <summary
                            writer.writeAttribute("value", smry);     //                 value=[smry] />
                        }
                    }
                    String[][] map = c.getProperties();
                    if (map != null) {
                        for (String[] prop : map) {
                            writer.writeEmptyElement("property");     //       <property  
                            writer.writeAttribute("name", prop[0]);   //                 name=[..]
                            writer.writeAttribute("value", prop[1]);  //                           value=[..] />
                        }
                    }
                    writer.writeEndElement();                         //     </contact>
                }
            }
            writer.writeEndElement();                                 //   </contacts>
            writer.writeEndDocument();
        } catch (Exception ex) {
            System.out.println("Account > storeContacts() > Error in storing contacts : " + ex);
        }
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Account) {
            return -((Account) o).getName().compareTo(name);
        } else {
            return 1;
        }
    }

    @Override
    public String toString() {
        return "Account[Name:'" + name + "' | CreatedOn:'" + created + "']";
    }

    /**
     * @return the contacts
     */
    public java.util.TreeSet<Contact> getContacts() {
        return contacts;
    }
}
