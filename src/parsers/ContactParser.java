package parsers;

import java.util.TreeSet;
import utils.Contact;

public interface ContactParser {
    public boolean isFormatSupported();
    public TreeSet<Contact> parseFile();
}
