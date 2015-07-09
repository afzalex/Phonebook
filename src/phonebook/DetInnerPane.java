/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phonebook;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import resources.ResourceHelper;
import utils.Contact;

public class DetInnerPane extends javax.swing.JPanel {

    private final Color transparent = new Color(0, 0, 0, 0);
    private final Main maininstance;
    Contact selectedContact = null;
    JPanel detailPane = new JPanel();
    JScrollPane detPaneHolder = new JScrollPane(detailPane);

    /**
     * Creates new form DetInnerPane
     *
     * @param maininstance
     */
    public DetInnerPane(Main maininstance) {
        this.maininstance = maininstance;
        initComponents();
        postInitComponents();
    }

    private void postInitComponents() {
        setButtonIcon("addicon.png", add);
        setButtonIcon("editicon.png", edit);
        setButtonIcon("deleteicon.jpg", delete);
        setButtonIcon("deleteaccounticon.jpg", deleteAccount);
        detPanePlatform.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        detailPane.setLayout(new BoxLayout(detailPane, BoxLayout.PAGE_AXIS));
        detPanePlatform.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                detPanePlatform.updateUI();
                resetDetPaneHolderSize();
            }
        });
        detPanePlatform.add(detPaneHolder);
        detPanePlatform.setVisible(true);
    }

    private void setButtonIcon(String res, JButton button) {
        try {
            Image image = ResourceHelper.getImageObject(res);
            image = image.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            ImageIcon editicon = new ImageIcon(image);
            button.setIcon(editicon);
        } catch (IOException ex) {
            System.out.println("DetInnerPane > setButtonIcon(...) > Error : " + ex);
        }
    }

    public void resetDetPaneHolderSize() {
        try {
            Dimension toSetSize = new Dimension();
            toSetSize.width = detPanePlatform.getWidth();
            if (detPanePlatform.getHeight() > detailPane.getHeight()) {
                toSetSize.height = detailPane.getHeight() + 3;
            } else {
                toSetSize.height = detPanePlatform.getHeight();
            }
            detPaneHolder.setPreferredSize(toSetSize);
        } catch (Exception ex) {
            System.out.println("DetInnerPane > resetDetPaneHolderSize() > Error : " + ex);
        }
    }

    public void resetDetailPane() {
        detailPane.removeAll();
        Dimension d = new Dimension(0, 3);
        detailPane.setOpaque(false);
        detPaneHolder.setOpaque(false);
        if (selectedContact != null && selectedContact.getProperties() != null) {
            String[][] properties = selectedContact.getProperties();
            for (String[] property : properties) {
                javax.swing.JSeparator space = new JSeparator(SwingConstants.HORIZONTAL);
                space.setSize(d);
                detailPane.add(space);
                DetInnerPaneBlocks dipb = new DetInnerPaneBlocks(property[0], property[1]);
                detailPane.add(dipb);
            }
            detailPane.add(Box.createGlue());
            detailPane.setSize(0, (DetInnerPaneBlocks.height + d.height) * properties.length);
        } else {
            detailPane.setSize(0, 0);
        }
        resetDetPaneHolderSize();
    }

    protected void setSelectedContact(Contact c) {
        this.selectedContact = c;
    }

    public void reset() {
        Contact c = selectedContact;
        if (c != null) {
            name.setText(c.getName());
            try {
                ImageIcon icon = new ImageIcon((ResourceHelper.getImageObject(c.getImageLoc()))
                        .getScaledInstance(110, 100, Image.SCALE_DEFAULT));
                picture.setIcon(icon);
            } catch (Exception ex) {
                picture.setIcon(null);
                System.out.println("DetPane > changeContact(...) > Error in setting image : " + ex);
            }
            detail1.setText("");
            detail2.setText("");
            if (c.getSummary() != null) {
                String[] smry = c.getSummary();
                if (smry.length >= 1) {
                    detail1.setText(smry[0]);
                } else {
                    detail1.setText("");
                }
                if (smry.length >= 2) {
                    detail2.setText(smry[1]);
                } else {
                    detail2.setText("");
                }
            }
        }
        resetDetailPane();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        header = new javax.swing.JPanel(){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                Point tl = new Point(0, 0);
                Point br = new Point(0, getHeight());
                Point med = new Point(0, getHeight() * 3 / 8);
                Color col1 = new Color(220, 220, 220);
                Color col2 = new Color(100, 100, 100);
                java.awt.GradientPaint paint1 =
                new java.awt.GradientPaint(tl, col1, med, col2);
                java.awt.GradientPaint paint2 =
                new java.awt.GradientPaint(med, col2, br, col1);
                java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
                g2.setPaint(paint1);
                g2.fillRect(0, 0, getWidth(), med.y);
                g2.setPaint(paint2);
                g2.fillRect(0, med.y, getWidth(), getHeight());
            }
        };
        picture = new javax.swing.JLabel();
        name = new javax.swing.JLabel();
        detail1 = new javax.swing.JLabel();
        detail2 = new javax.swing.JLabel();
        detPanePlatform = new javax.swing.JPanel();
        edit = new javax.swing.JButton();
        add = new javax.swing.JButton();
        delete = new javax.swing.JButton();
        deleteAccount = new javax.swing.JButton();

        setOpaque(false);

        header.setBackground(new java.awt.Color(233, 223, 223));

        picture.setBackground(new java.awt.Color(204, 204, 204));
        picture.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        picture.setText("Picture N/A");
        picture.setOpaque(true);

        name.setFont(new java.awt.Font("Mongolian Baiti", 0, 24)); // NOI18N
        name.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        name.setText("N / A");
        name.setToolTipText("Name");

        detail1.setFont(new java.awt.Font("Mongolian Baiti", 0, 14)); // NOI18N
        detail1.setToolTipText("summary");

        detail2.setFont(new java.awt.Font("Mongolian Baiti", 0, 14)); // NOI18N
        detail2.setToolTipText("summary");

        javax.swing.GroupLayout headerLayout = new javax.swing.GroupLayout(header);
        header.setLayout(headerLayout);
        headerLayout.setHorizontalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerLayout.createSequentialGroup()
                .addGroup(headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(headerLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(detail2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(detail1, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)))
                    .addGroup(headerLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(picture, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        headerLayout.setVerticalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerLayout.createSequentialGroup()
                .addGroup(headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(headerLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(picture, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(headerLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(detail1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(detail2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(43, 43, 43))
        );

        detPanePlatform.setOpaque(false);

        javax.swing.GroupLayout detPanePlatformLayout = new javax.swing.GroupLayout(detPanePlatform);
        detPanePlatform.setLayout(detPanePlatformLayout);
        detPanePlatformLayout.setHorizontalGroup(
            detPanePlatformLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 366, Short.MAX_VALUE)
        );
        detPanePlatformLayout.setVerticalGroup(
            detPanePlatformLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 84, Short.MAX_VALUE)
        );

        edit.setText("Edit");
        edit.setMargin(new java.awt.Insets(2, 8, 2, 8));
        edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editActionPerformed(evt);
            }
        });

        add.setText("Add");
        add.setMargin(new java.awt.Insets(2, 8, 2, 8));
        add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addActionPerformed(evt);
            }
        });

        delete.setText("Delete");
        delete.setMargin(new java.awt.Insets(2, 8, 2, 8));
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });

        deleteAccount.setText("Delete Account");
        deleteAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteAccountActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(detPanePlatform, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(header, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(deleteAccount)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(add)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(delete)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(header, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(detPanePlatform, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edit)
                    .addComponent(add)
                    .addComponent(delete)
                    .addComponent(deleteAccount))
                .addGap(5, 5, 5))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editActionPerformed
        maininstance.addEditContact(selectedContact);
    }//GEN-LAST:event_editActionPerformed

    private void addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addActionPerformed
        maininstance.addEditContact(null);
    }//GEN-LAST:event_addActionPerformed

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
        maininstance.deleteContact(selectedContact);
    }//GEN-LAST:event_deleteActionPerformed

    private void deleteAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteAccountActionPerformed
        maininstance.deleteAccount();
    }//GEN-LAST:event_deleteAccountActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton add;
    private javax.swing.JButton delete;
    private javax.swing.JButton deleteAccount;
    private javax.swing.JPanel detPanePlatform;
    protected javax.swing.JLabel detail1;
    protected javax.swing.JLabel detail2;
    private javax.swing.JButton edit;
    private javax.swing.JPanel header;
    protected javax.swing.JLabel name;
    protected javax.swing.JLabel picture;
    // End of variables declaration//GEN-END:variables
}
