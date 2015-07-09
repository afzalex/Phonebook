package utils;

import java.io.Serializable;
import java.net.URL;
import java.util.Date;

public class Contact implements Comparable, Serializable {

    private final String name;
    private final Date createdon;
    private final String[][] properties;
    private final String[] summary;
    private final URL imageLoc;

    public Contact(String name) {
        this(name, new Date(), null);
    }

    public Contact(String name, String[][] properties) {
        this(name, new Date(), properties);
    }

    public Contact(String name, Date createdon) {
        this(name, createdon, null);
    }

    public Contact(String name, Date createdon, String[][] properties) {
        this(name, createdon, null, properties);
    }

    public Contact(String name, Date createdon, String[] summary, String[][] properties) {
        this(name, null, createdon, summary, properties);
    }

    public Contact(String name, String[] summary, String[][] properties) {
        this(name, null, new Date(), summary, properties);
    }

    public Contact(String name, URL imageLoc, String[] summary, String[][] properties) {
        this(name, imageLoc, new Date(), summary, properties);
    }

    public Contact(String name, URL imageLoc, Date createdon, String[] summary, String[][] properties) {
        this.name = name;
        this.properties = properties;
        this.createdon = createdon;
        this.summary = summary;
        this.imageLoc = (imageLoc == null) ? MainUtils.DEFAULT_CONTACT_IMAGE_URL : imageLoc;
    }

    /**
     * @return The name of the contact
     */
    public String getName() {
        return name;
    }

    /**
     * @return The date on which contact is created
     */
    public Date getCreatedon() {
        return createdon;
    }

    /**
     * @return Other properties of the contact
     */
    public String[][] getProperties() {
        return properties;
    }

    public String[] getSummary() {
        return summary;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Contact) {
            return -((Contact) o).getName().compareTo(name);
        } else {
            return 1;
        }
    }

    @Override
    public String toString() {
        return "Contact[" + name + "]";
    }

    /**
     * @return the imageLoc
     */
    public URL getImageLoc() {
        return imageLoc;
    }

}
