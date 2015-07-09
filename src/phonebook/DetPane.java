package phonebook;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.TreeSet;
import javax.swing.JPanel;
import utils.Contact;

public class DetPane extends JPanel {

    private final Phonebook bookinstance;
    private TreeSet<Contact> contacts;
    private final DetInnerPane detInnerPane;
    private Contact selectedContact = null;

    DetPane(Phonebook bookinstance) {
        setPreferredSize(new Dimension(0, 0));
        detInnerPane = new DetInnerPane(bookinstance.maininstance);
        setLayout(new GridLayout(1, 1));
        this.bookinstance = bookinstance;
        bookinstance.addConsumerA(c -> changeContact(c));
        add(detInnerPane);
        reset();
        setVisible(true);
    }

    protected final void reset() {
        contacts = bookinstance.getContacts();
        detInnerPane.setSelectedContact(selectedContact);
        detInnerPane.reset();
    }

    private void changeContact(Contact c) {
        selectedContact = c;
        reset();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(utils.MainUtils.getCroppedPatch(this, getWidth(), getHeight()), 0, 0, null);
    }
}
