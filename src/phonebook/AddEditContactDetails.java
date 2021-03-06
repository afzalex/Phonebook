/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phonebook;

import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JComboBox;
import javax.swing.JSeparator;

/**
 *
 * @author Afzalex
 */
public class AddEditContactDetails extends javax.swing.JPanel {

    public static int HEIGHT_EXPECTED = 30;
    private JSeparator seperatorLinked = null;

    /**
     * Creates new form AddEditContactDetails
     */
    public AddEditContactDetails() {
        initComponents();
    }
    
    public AddEditContactDetails(String name, String value){
        initComponents();
        propertyName.setSelectedItem(name);
        propertyValue.setText(value);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        propertyName = new JComboBox(utils.MainUtils.ContactDetailsOptions.ALL_DETAIL_VALUES);
        propertyValue = new javax.swing.JTextField();
        remove = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new Dimension(10, HEIGHT_EXPECTED));

        propertyName.setMaximumSize(new java.awt.Dimension(100, 32767));
        propertyName.setPreferredSize(new java.awt.Dimension(150, 20));

        propertyValue.setBorder(null);

        remove.setText("x");
        remove.setMargin(new java.awt.Insets(2, 2, 2, 2));
        remove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(propertyName, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(propertyValue, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(remove, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(propertyName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(propertyValue, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(remove, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void removeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeActionPerformed
        getParent().remove(seperatorLinked);
        getParent().remove(this);
    }//GEN-LAST:event_removeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JComboBox propertyName;
    protected javax.swing.JTextField propertyValue;
    private javax.swing.JButton remove;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the seperatorLinked
     */
    public JSeparator getSeperatorLinked() {
        return seperatorLinked;
    }

    /**
     * @param seperatorLinked the seperatorLinked to set
     */
    public void setSeperatorLinked(JSeparator seperatorLinked) {
        this.seperatorLinked = seperatorLinked;
    }
}
