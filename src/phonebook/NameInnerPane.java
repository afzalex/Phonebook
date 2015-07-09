package phonebook;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import resources.ResourceHelper;
import utils.Contact;

public class NameInnerPane extends JPanel {

    private static final Color CONTACT_IMAGE_BG = new Color(100, 100, 100);
    private final Contact contact;
    private final JLabel namelbl;
    private JLabel imglbl;
    static final Dimension NAMEINNERPANE_SIZE = new Dimension(0, 25);
    private final NameInnerPane instance = this;
    private final Color transparent = new Color(0, 0, 0, 0);
    private final NamePane namePaneInstance;

    NameInnerPane(NamePane namePaneInstance, Contact contact) {
        this.contact = contact;
        this.namePaneInstance = namePaneInstance;
        setSize(NAMEINNERPANE_SIZE);
        setPreferredSize(NAMEINNERPANE_SIZE);
        namelbl = new JLabel(contact.getName()) {

            @Override
            protected void paintComponent(Graphics g) {
                Color c = g.getColor();
                {
                    Color col1 = new Color(36, 104, 179);
                    Color col2 = new Color(204, 225, 237);
                    Point topleft = new Point(0, 0);
                    Point bottomright = new Point(getWidth(), getHeight());
                    Graphics2D g2 = (Graphics2D) g;
                    GradientPaint gp = new GradientPaint(topleft, col1, bottomright, col2);
                    g2.setPaint(gp);
                    Rectangle2D rect = new Rectangle2D.Float(topleft.x, topleft.y,
                            bottomright.x, bottomright.y);
                    g2.fill(rect);
                }
                g.setColor(c);
                super.paintComponent(g);
            }
        };
        namelbl.setOpaque(true);
        namelbl.setBackground(transparent);//Color.RED);
        ImageIcon icon;
        try {
            icon = new ImageIcon((ResourceHelper.getImageObject(contact.getImageLoc()))
                    .getScaledInstance(NAMEINNERPANE_SIZE.height, NAMEINNERPANE_SIZE.height, Image.SCALE_FAST));
            imglbl = new JLabel(icon);
        } catch (IOException ex) {
            imglbl = new JLabel("N/A");
        }
        imglbl.setOpaque(true);
        imglbl.setBackground(CONTACT_IMAGE_BG);
        imglbl.setPreferredSize(new Dimension(NAMEINNERPANE_SIZE.height, NAMEINNERPANE_SIZE.height));
        setLayout(new GridBagLayout());
        init();
        setVisible(true);
    }

    MouseAdapter mouseadapter = new MouseAdapter() {

        @Override
        public void mousePressed(MouseEvent e) {
            instance.getParent().dispatchEvent(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            instance.getParent().dispatchEvent(e);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            instance.getParent().dispatchEvent(e);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            namePaneInstance.contactSelected(contact);
        }
    };

    private void init() {
        GridBagConstraints con = new GridBagConstraints();
        con.anchor = GridBagConstraints.WEST;
        con.gridx = 0;
        con.gridy = 0;
        con.fill = GridBagConstraints.BOTH;
        con.weightx = 1;
        con.weighty = 1;
        add(namelbl, con);
        con.weightx = 0;
        con.anchor = GridBagConstraints.EAST;
        con.gridx = 1;
        add(imglbl, con);
        addMouseListener(mouseadapter);
        addMouseMotionListener(mouseadapter);
    }
}
