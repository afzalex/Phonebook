package phonebook;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import resources.ResourceHelper;
import utils.Contact;
import utils.MainUtils;

/**
 *
 * @author Afzalex
 */
public abstract class AddEditContact extends javax.swing.JPanel {

    private final Color transparent = new Color(0, 0, 0, 0);
    private Contact contact = null;
    private Contact receivedContact = null;
    private JPanel detArea = new JPanel();
    private JScrollPane detPane = new JScrollPane(detArea);
    private URL contactImageURL = null;
    private JDialog changeContactImageDialog;
    private boolean isToEdit = false;

    /**
     * @return the receivedContact
     */
    public Contact getReceivedContact() {
        return receivedContact;
    }

    private abstract class PictureOptions extends JPanel {

        public static final int GAP_WIDTH = 5;

        PictureOptions() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
            for (String str : ResourceHelper.CONTACT_IMAGE_LIST) {
                try {
                    URL url = ResourceHelper.getResourceURL(str);
                    Image image = ResourceHelper.getImageObject(url)
                            .getScaledInstance(80, 80, Image.SCALE_FAST);
                    ImageIcon icon = new ImageIcon(image);
                    JButton button = new JButton(icon);
                    button.setSize(100, 100);
                    button.setPreferredSize(new Dimension(100, 100));
                    button.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            selected(url);
                        }
                    });
                    add(button);
                } catch (IOException ex) {
                    Logger.getLogger(AddEditContact.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        public abstract void selected(URL url);
    }

    /**
     * Creates new form AddContact
     */
    public AddEditContact() {
        initComponents();
        postInitComponents();
        reset();
    }

    private void postInitComponents() {
        setBackground(transparent);
        detPlatform.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        detPlatform.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resetDetPaneSize();
            }
        });
        detArea.addContainerListener(new ContainerAdapter() {
            @Override
            public void componentRemoved(ContainerEvent e) {
                resetDetAreaSize();
                resetDetPaneSize();
            }
        });
        detArea.setLayout(new BoxLayout(detArea, BoxLayout.PAGE_AXIS));
        detPlatform.add(detPane);
        //setting dialog which will be shown to set image
        changeContactImageDialog = new JDialog(getContainingDialog());
        PictureOptions panel = new PictureOptions() {
            @Override
            public void selected(URL url) {
                setContactImage(url);
                changeContactImageDialog.dispose();
            }
        };
        JScrollPane pane = new JScrollPane(panel);
        changeContactImageDialog.setLayout(new GridLayout(1, 1));
        changeContactImageDialog.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension dim = changeContactImageDialog.getContentPane().getSize();
                Dimension toSet = new Dimension();
                int unit = 105;
                int neg = 20;
                int totalWidth = panel.getComponentCount() * (unit + neg);
                int height = (int) Math.floor(totalWidth / (float) dim.width) * unit;
                toSet.width = dim.width - neg;
                toSet.height = height;
                panel.setPreferredSize(toSet);

            }
        });
        changeContactImageDialog.add(pane);
    }

    private void setContactImage(URL url) {
        contactImageURL = url;
        try {
            Image image = ResourceHelper.getImageObject(url);
            image = image.getScaledInstance(110, 105, Image.SCALE_AREA_AVERAGING);
            ImageIcon icon = new ImageIcon(image);
            picture.setIcon(icon);
        } catch (IOException ex) {
            System.out.println("AddEditContact > postInitComponents() > Error in reading Image : " + ex);
        }
    }

    private void resetDetPaneSize() {
        detPlatform.updateUI();
        Dimension toSetSize = new Dimension(detPlatform.getWidth(), 0);//detArea.getHeight());
        if (detArea.getHeight() > detPlatform.getHeight()) {
            toSetSize.height = detPlatform.getHeight();
        } else {
            toSetSize.height = detArea.getHeight() + 3;
        }
        detPane.setPreferredSize(toSetSize);
        detPane.setSize(toSetSize);
    }

    private final Dimension DETAREA_SEPERATOR_SIZE = new Dimension(0, 3);

    private void addDetailInDetArea(AddEditContactDetails addEditContactDet) {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setPreferredSize(DETAREA_SEPERATOR_SIZE);
        addEditContactDet.setSeperatorLinked(separator);
        detArea.add(addEditContactDet);
        detArea.add(separator);
    }

    private void resetDetAreaSize() {
        Dimension toSetDetAreaSize = new Dimension(0, detArea.getComponents().length / 2
                * (AddEditContactDetails.HEIGHT_EXPECTED + DETAREA_SEPERATOR_SIZE.height));
        detArea.setSize(toSetDetAreaSize);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Dimension dim = getSize();
        g.drawImage(MainUtils.getCroppedPatch(this, dim.width, dim.height), 0, 0, null);
        super.paintComponent(g);
    }

    private ArrayList<AddEditContactDetails> filterDetAreaComponents() {
        ArrayList<AddEditContactDetails> al = new ArrayList<>();
        for (Component comp : detArea.getComponents()) {
            if (comp instanceof AddEditContactDetails) {
                al.add((AddEditContactDetails) comp);
            }
        }
        return al;
    }

    private boolean checkDetailsValidity() {
        boolean flag = true;
        for (AddEditContactDetails det : filterDetAreaComponents()) {
            Object name = det.propertyName.getSelectedItem();
            Object value = det.propertyValue.getText();
            if (!(flag = MainUtils.ContactDetailsOptions.isValidContactDetail(name, value))) {
                break;
            }
        }
        return flag;
    }

    /**
     * @return the contact
     */
    public Contact getContact() {
        String name = this.name.getText();
        String[] summary = null;
        String[][] details = null;

        //filling summary fields 
        {
            String det1 = detail1.getText();
            String det2 = detail2.getText();
            ArrayList<String> al = new ArrayList<>();
            if (MainUtils.ContactDetailsOptions.isValidContactDetail(
                    MainUtils.ContactDetailsOptions.SUMMARY, det1)) {
                al.add(det1);
            }
            if (MainUtils.ContactDetailsOptions.isValidContactDetail(
                    MainUtils.ContactDetailsOptions.SUMMARY, det2)) {
                al.add(det2);
            }
            if (al.size() > 0) {
                summary = new String[al.size()];
                for (int i = 0; i < al.size(); i++) {
                    summary[i] = al.get(i);
                }
            }
        }

        //filling details
        {
            ArrayList<String[]> map = new ArrayList<>();
            //TreeMap<String, String> map = new TreeMap<>();
            for (AddEditContactDetails det : filterDetAreaComponents()) {
                Object n = det.propertyName.getSelectedItem();
                Object v = det.propertyValue.getText();
                if (MainUtils.ContactDetailsOptions.isValidContactDetail(n, v)) {
                    map.add(new String[]{(String) n, (String) v});
                }
            }
            if (map.size() > 0) {
                details = new String[map.size()][2];
                for (int i = 0; i < map.size(); i++) {
                    details[i][0] = map.get(i)[0];
                    details[i][1] = map.get(i)[1];
                }
            }
        }

        if (contact != null) {
            contact = new Contact(name, contactImageURL, contact.getCreatedon(), summary, details);
        } else {
            contact = new Contact(name, contactImageURL, summary, details);
        }
        return contact;
    }

    public void reset() {
        reset(null);
    }

    public void reset(Contact contact) {
        receivedContact = contact;
        name.setText("");
        detail1.setText("");
        detail2.setText("");
        setContactImage(MainUtils.DEFAULT_CONTACT_IMAGE_URL);
        detArea.removeAll();
        if (contact != null) {
            create.setText("Edit");
            isToEdit = true;
            name.setText(contact.getName());
            if (contact.getSummary() != null) {
                String[] smry = contact.getSummary();
                if (smry.length >= 1) {
                    detail1.setText(smry[0]);
                }
                if (smry.length >= 2) {
                    detail2.setText(smry[1]);
                }
            }
            setContactImage(contact.getImageLoc());
            if (contact.getProperties() != null) {
                String[][] prps = contact.getProperties();
                for (String[] prpty : prps) {
                    addDetailInDetArea(new AddEditContactDetails(prpty[0], prpty[1]));
                }
            }
            resetDetAreaSize();
            resetDetPaneSize();
        } else {
            create.setText("Create");
            isToEdit = false;
        }
    }

    public abstract void close();

    public abstract JDialog getContainingDialog();

    public abstract void onCreate(Contact contact);

    public abstract void onEdit(Contact contact);

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        detPlatform = new javax.swing.JPanel();
        create = new javax.swing.JButton();
        cancel = new javax.swing.JButton();
        addDetail = new javax.swing.JButton();
        headHolder = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        detail1Label = new javax.swing.JLabel();
        detail2Label = new javax.swing.JLabel();
        detail2 = new javax.swing.JTextField();
        detail1 = new javax.swing.JTextField();
        name = new javax.swing.JTextField();
        picture = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(632, 350));

        detPlatform.setOpaque(false);

        javax.swing.GroupLayout detPlatformLayout = new javax.swing.GroupLayout(detPlatform);
        detPlatform.setLayout(detPlatformLayout);
        detPlatformLayout.setHorizontalGroup(
            detPlatformLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        detPlatformLayout.setVerticalGroup(
            detPlatformLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 167, Short.MAX_VALUE)
        );

        create.setText("Create");
        create.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createActionPerformed(evt);
            }
        });

        cancel.setText("Cancel");
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });

        addDetail.setText("Add Contact Details");
        addDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDetailActionPerformed(evt);
            }
        });

        nameLabel.setText("Name *");

        detail1Label.setText("Detail");

        detail2Label.setText("Detail (More)");

        picture.setText("N / A");
        picture.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pictureActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout headHolderLayout = new javax.swing.GroupLayout(headHolder);
        headHolder.setLayout(headHolderLayout);
        headHolderLayout.setHorizontalGroup(
            headHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headHolderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(headHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(detail2Label, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(detail1Label, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(headHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(detail1)
                    .addComponent(detail2)
                    .addComponent(name))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(picture, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );
        headHolderLayout.setVerticalGroup(
            headHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headHolderLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(headHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(picture, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(headHolderLayout.createSequentialGroup()
                        .addGroup(headHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nameLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(name, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(headHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(detail1Label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(detail1, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(headHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(detail2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(detail2Label, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(81, 81, 81))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(detPlatform, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(addDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 154, Short.MAX_VALUE)
                        .addComponent(cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(create, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(headHolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(headHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(detPlatform, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(create)
                    .addComponent(cancel)
                    .addComponent(addDetail))
                .addGap(10, 10, 10))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDetailActionPerformed
        if (checkDetailsValidity()) {
            addDetailInDetArea(new AddEditContactDetails());
            resetDetAreaSize();
            resetDetPaneSize();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please fill valid entries",
                    "invalid entry found",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_addDetailActionPerformed

    private void createActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createActionPerformed
        if (MainUtils.ContactDetailsOptions.isValidContactDetail(
                MainUtils.ContactDetailsOptions.NAME, name.getText())) {
            Contact contact = getContact();
            if (isToEdit) {
                onEdit(contact);
            } else {
                onCreate(contact);
            }
            close();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid name for contact",
                    "empty / invalid contact name",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_createActionPerformed

    private void pictureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pictureActionPerformed
        changeContactImageDialog.setLocation(getLocationOnScreen());
        changeContactImageDialog.setSize(getSize());
        changeContactImageDialog.setVisible(true);
    }//GEN-LAST:event_pictureActionPerformed

    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        close();
    }//GEN-LAST:event_cancelActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addDetail;
    private javax.swing.JButton cancel;
    private javax.swing.JButton create;
    private javax.swing.JPanel detPlatform;
    private javax.swing.JTextField detail1;
    private javax.swing.JLabel detail1Label;
    private javax.swing.JTextField detail2;
    private javax.swing.JLabel detail2Label;
    private javax.swing.JPanel headHolder;
    private javax.swing.JTextField name;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JButton picture;
    // End of variables declaration//GEN-END:variables

    public static void main(String... args) {
    }

}
