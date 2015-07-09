package phonebook;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.TreeSet;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import utils.Contact;
import utils.MainUtils;

public class NamePane extends JPanel {

    private final Color transparent = new Color(0, 0, 0, 0);
    private final JPanel stage = new JPanel();
    private final Phonebook bookinstance;
    private TreeSet<Contact> contacts;
    private JScrollPane pane = new JScrollPane(stage);
    private final Contact DEMO_CONTACT = new Contact("Demo Contact");

    NamePane(Phonebook bookinstance) {
        this.bookinstance = bookinstance;
        pane.setOpaque(false);
        stage.setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        add(pane);
        setBackground(transparent);
        stage.setLayout(new BoxLayout(stage, BoxLayout.PAGE_AXIS));
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateUI();
                resetPaneSize();
            }
        });
        setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(MainUtils.getCroppedPatch(this, getWidth(), getHeight()), 0, 0, null);
        super.paintComponent(g);
    }

    public void resetPaneSize() {
        try {
            Dimension toSetSize = new Dimension();
            toSetSize.width = getWidth();
            if (getHeight() > stage.getHeight()) {
                toSetSize.height = stage.getHeight() + 3;
            } else {
                toSetSize.height = getHeight();
            }
            pane.setPreferredSize(toSetSize);
        } catch (Exception ex) {

        }
    }

    private void init() {
        Dimension d = new Dimension(0, 3);
        pane.setOpaque(false);
        stage.setOpaque(false);
        pane.setBackground(transparent);
        stage.setBackground(transparent);
        for (Contact c : contacts) {
            JComponent space = new JComponent() {
            };
            space.setOpaque(false);
            space.setPreferredSize(d);
            stage.add(space);
            NameInnerPane nip = new NameInnerPane(this, c);
            stage.add(nip);
        }
        stage.add(Box.createGlue());
        stage.setSize(0, (NameInnerPane.NAMEINNERPANE_SIZE.height + d.height) * contacts.size());
    }

    protected void contactSelected(Contact c) {
        bookinstance.callConsumersA(c);
    }

    protected final void reset() {
        stage.removeAll();
        contacts = bookinstance.getContacts();
        if (contacts == null || contacts.size() < 1) {
            contacts = new TreeSet<>();
            contactSelected(DEMO_CONTACT);
        } else {
            contactSelected(contacts.first());
        }
        init();
        resetPaneSize();
    }

}
