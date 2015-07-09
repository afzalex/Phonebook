package utils;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import phonebook.Main;
import resources.ResourceHelper;

public class MainUtils {

    protected static java.util.ResourceBundle RES;
    protected static String BASE;
    public static URL DEFAULT_CONTACT_IMAGE_URL = ResourceHelper.getResourceURL("defaultcontactimage.png");

    static {
        try {
            RES = java.util.ResourceBundle.getBundle("resources");
        } catch (Exception e) {
            System.out.println("Fatal Error 100 : Resource file not found");
            System.exit(0);
        }
        try {
            if (RES.containsKey("base")) {
                BASE = RES.getString("base");
            } else {
                throw new Exception("Property \"base\" not found");
            }
        } catch (Exception e) {
            System.out.println("MainUtils > Error : " + e);
        }
    }

    public static class ContactDetailsOptions {

        public final static String NAME = "Name";
        public final static String SUMMARY = "Summary";
        public final static String CONTACT_NUMBER = "Contact Number";
        public final static String ADDRESS = "Address";
        public final static String EMAIL_ID = "Email ID";
        public final static String FAX = "Fax";
        public final static String OCCUPATION = "Occupation";
        public final static String COLLEGE_OFFICE = "College / Office";
        public final static String ZIP_CODE = "ZIP / PIN Code";
        public final static String OTHER_DETAILS = "Other detail";

        public static String[] ALL_DETAIL_VALUES = {
            CONTACT_NUMBER,
            ADDRESS,
            EMAIL_ID,
            FAX,
            OCCUPATION,
            COLLEGE_OFFICE,
            ZIP_CODE,
            OTHER_DETAILS
        };

        public static boolean isValidContactDetail(Object name, Object value) {
            boolean isValid = false;
            if ((name instanceof String) && (value instanceof String)) {
                String n = (String) name;
                String v = (String) value;
                switch (n) {
                    case CONTACT_NUMBER:
                        isValid = v.matches("^[+0]?\\d{8,15}$");
                        break;
                    case ADDRESS:
                        isValid = v.matches("^[\\w,. ]{3,50}$");
                        break;
                    case EMAIL_ID:
                        isValid = v.matches("^(\\w+\\.?){1,10}@(\\w{1,20}\\.){1,5}\\w{2,5}$");
                        break;
                    case FAX:
                        isValid = v.matches("^\\d{8,15}$");
                        break;
                    case OCCUPATION:
                        isValid = v.matches("^[\\w,. ]{1,50}$");
                        break;
                    case COLLEGE_OFFICE:
                        isValid = v.matches("^[\\w,. ]{1,50}$");
                        break;
                    case ZIP_CODE:
                        isValid = v.matches("^\\d{5,12}$");
                        break;
                    case NAME:
                        isValid = v.matches("^[\\w,. ]{1,50}$");
                        break;
                    case SUMMARY:
                        isValid = v.matches("^.{1,70}");
                        break;
                    case OTHER_DETAILS:
                        isValid = v.matches("^.{1, 70}$");
                        break;
                }
            }
            return isValid;
        }

    }

    /**
     * It will read the accounts.xml file and Information of accounts in a
     * <i>TreeSet</i> of <i>Account</i>'s
     *
     * @return TreeSet<Account> containing all the accounts present in the base
     * folder as specified by the <i>accounts.xml</i> file.
     */
    public TreeSet<Account> readAccounts() {
        TreeSet<Account> accounts = new TreeSet<>();
        File acfile = new File(BASE, "accounts.xml");
        try {
            try {
                javax.xml.parsers.SAXParserFactory spf = javax.xml.parsers.SAXParserFactory.newInstance();
                spf.setNamespaceAware(false);
                javax.xml.parsers.SAXParser parser = spf.newSAXParser();
                org.xml.sax.XMLReader reader = parser.getXMLReader();
                AccountsHandler handler = new AccountsHandler();
                reader.setContentHandler(handler);
                reader.parse(new org.xml.sax.InputSource(new java.io.FileInputStream(acfile)));
                accounts = handler.getAccounts();
            } catch (FileNotFoundException ffe) {
                acfile.createNewFile();
            }
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            System.out.println("MainUtils > readAccounts() > Error in reading Accounts : " + ex);
        }
        return accounts;
    }

    public void storeAccounts(TreeSet<Account> accounts) {
        try {
            File acfile = new File(BASE, "accounts.xml");
            javax.xml.stream.XMLOutputFactory xof = javax.xml.stream.XMLOutputFactory.newInstance();
            javax.xml.stream.XMLStreamWriter writer = xof.createXMLStreamWriter(
                    new java.io.FileOutputStream(acfile));
            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeStartElement("accounts");
            for (Account ac : accounts) {
                writer.writeEmptyElement("account");
                writer.writeAttribute("name", ac.getName());
                writer.writeAttribute("createdon", String.valueOf(ac.getCreated().getTime()));
            }
            writer.writeEndElement();
            writer.writeEndDocument();
        } catch (Exception ex) {
            System.out.println("MainUtils > addAccount(...) > Error in Creating Account : " + ex);
        }
    }

    public void storeState(Main main) {
        try {
            java.io.ObjectOutputStream oos = null;
            try {
                File stfile = new File(BASE, "state.dat");
                oos = new java.io.ObjectOutputStream(new java.io.FileOutputStream(stfile));
                State state = main.getMainstate();
                state.setArea(main.getBounds());
                state.setSelectedac(main.getSelectedaccount());
                state.setLastopened();
                oos.writeObject(state);
            } finally {
                if (oos != null) {
                    oos.close();
                }
            }
        } catch (Exception ex) {
            System.out.println("MainUtils > storeState(...) > Error in storing state of application : " + ex);
        }
    }

    public void restoreState(Main main) {
        try {
            java.io.ObjectInputStream ois = null;
            State state;
            try {
                File stfile = new File(BASE, "state.dat");
                if (!stfile.exists()) {
                    throw new Exception("MainUtils > State File not found");
                }
                ois = new java.io.ObjectInputStream(new java.io.FileInputStream(stfile));
                state = (State) ois.readObject();
            } catch (Exception ex) {
                java.awt.Rectangle rect;
                java.awt.Toolkit kit = java.awt.Toolkit.getDefaultToolkit();
                java.awt.Dimension dim = kit.getScreenSize();
                java.awt.Dimension siz = new java.awt.Dimension(700, 500);
                int x = (dim.width - siz.width) / 2;
                int y = (dim.height - siz.height) / 2;
                rect = new java.awt.Rectangle(x, y, siz.width, siz.height);
                state = new State(rect, main.DEFAULT_ACCOUNT);
            } finally {
                if (ois != null) {
                    ois.close();
                }
            }
            main.setLocation(state.getArea().getLocation());
            main.setSize(state.getArea().getSize());
            main.setMainstate(state);
        } catch (Exception ex) {
            System.out.println("MainUtils > restoreState(...) > Error during restoring application");
        }
    }

    public static void getPictureList() {
        try {
            URL url = ResourceHelper.class.getResource("contactimages");
            File file = new File(url.toURI());
            for (File f : file.listFiles()) {
                System.out.println("\"contactimages/" + f.getName() + "\",");
            }
        } catch (URISyntaxException ex) {
            Logger.getLogger(MainUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static Image commonPatch;

    static {
        commonPatch = createPatch(2000, 1300);
    }

    private static Image createPatch(int width, int height) {
        Image patch = null;
        Image img = null;
        try {
            img = ResourceHelper.getImageObject("patch.jpg");
        } catch (IOException ex) {
            System.out.println("DetPane > Patch image not loaded successfully.");
        }
        int imgh = img.getHeight(null);
        int imgw = img.getWidth(null);
        int ixl = (width / imgw + 1);
        int iyl = (height / imgh + 1);
        int pw = ixl * imgw;
        int ph = iyl * imgh;
        patch = new BufferedImage(pw, ph, BufferedImage.TYPE_INT_RGB);
        Graphics g = patch.getGraphics();
        for (int i = 0; i < ixl; i++) {
            for (int j = 0; j < iyl; j++) {
                g.drawImage(img, i * imgw, j * imgh, null);
            }
        }
        return patch;
    }

    public static Image getCroppedPatch(Component comp, int width, int height) {
        CropImageFilter cif = new CropImageFilter(0, 0, width, height);
        FilteredImageSource fis = new FilteredImageSource(commonPatch.getSource(), cif);
        return comp.createImage(fis);
    }
}
