/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phonebook;

/**
 *
 * @author Afzalex
 */
public class DetInnerPaneBlocks extends javax.swing.JPanel {

    /**
     * Creates new form DetInnerPaneBlocks
     */
    public static final int height = 30;
    private final String name;
    private final String value;
    
    public DetInnerPaneBlocks(String name, String value) {
        this.name = name;
        this.value = value;
        initComponents();
        setSize(100, height);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        propName = new javax.swing.JLabel();
        propValue = new javax.swing.JTextField();

        setBackground(new java.awt.Color(213, 213, 213));
        setPreferredSize(new java.awt.Dimension(0, 30));

        propName.setBackground(new java.awt.Color(204, 204, 255));
        propName.setFont(new java.awt.Font("Palatino Linotype", 0, 18)); // NOI18N
        propName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        propName.setText(name);
        propName.setPreferredSize(new java.awt.Dimension(200, 26));

        propValue.setEditable(false);
        propValue.setFont(new java.awt.Font("Mongolian Baiti", 0, 18)); // NOI18N
        propValue.setText(value);
        propValue.setMargin(new java.awt.Insets(2, 10, 2, 2));
        propValue.setSelectionColor(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(propName, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(propValue, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(propValue)
            .addComponent(propName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JLabel propName;
    private javax.swing.JTextField propValue;
    // End of variables declaration//GEN-END:variables
}
