package BaseDeDatos;
//Bibliotecas a usar
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
/**
 *
 * @author live
 */
public class DbaInterfaz extends javax.swing.JFrame 
{
    //Constructor
    public DbaInterfaz() {
        initComponents();
    }
    /**
     * FUNCIONES..............................
     *  
     */
    public JComboBox getOper()
    {
        return operacion;
    }
    

    //Condigo basura
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        operacion = new javax.swing.JComboBox<>();
        acerca_de = new javax.swing.JButton();
        ayuda = new javax.swing.JButton();
        ejecutarOper = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Yu Gothic", 1, 24)); // NOI18N
        jLabel1.setText("Eliga la operación a realizar:");

        operacion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selección", "Proyección Generalizada", "Unión", "Diferencia de conjuntos", "Producto Cartesiano", "Intersección", "División", "Renombrar una relación y sus atributos", "Concatenación", "Concatenación natural", "Agregación", "Agrupación" }));
        operacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                operacionActionPerformed(evt);
            }
        });

        acerca_de.setFont(new java.awt.Font("Yu Gothic", 0, 24)); // NOI18N
        acerca_de.setText("Acerca de");
        acerca_de.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acerca_deActionPerformed(evt);
            }
        });

        ayuda.setFont(new java.awt.Font("Yu Gothic", 0, 24)); // NOI18N
        ayuda.setText("Ayuda");
        ayuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ayudaActionPerformed(evt);
            }
        });

        ejecutarOper.setFont(new java.awt.Font("Yu Gothic", 1, 24)); // NOI18N
        ejecutarOper.setText("Ejercutar operación");
        ejecutarOper.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ejecutarOperActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(operacion, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ayuda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(acerca_de, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ejecutarOper))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(operacion, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(ejecutarOper, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(acerca_de, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ayuda, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void operacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_operacionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_operacionActionPerformed

    private void acerca_deActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acerca_deActionPerformed
        //
        JOptionPane.showMessageDialog(null, "Nombre de la aplicación: Interprete de Algebra Relacional\nVensión: 01.01.01\nFecha de creación: 13/09/17\nAutores: Jeremy José Live González","Acerca de..",JOptionPane.INFORMATION_MESSAGE);

    }//GEN-LAST:event_acerca_deActionPerformed

    private void ayudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ayudaActionPerformed
        //Muestro manual del software
        JOptionPane.showMessageDialog(null, "MANUAL","MANUAL....",3);
    }//GEN-LAST:event_ayudaActionPerformed

    private void ejecutarOperActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ejecutarOperActionPerformed
        
        Register interfaz3 = new Register(this);
        String operator = operacion.getSelectedItem().toString();
        
        switch(operator){
            case "Selección":
                interfaz3.getTabla2().setEditable(false);
                interfaz3.getNombreAField().setEditable(false);
                interfaz3.getOperAgreField().setEditable(false);
                break;

            case "Proyección Generalizada":
                interfaz3.getTabla2().setEditable(false);
                interfaz3.getNombreAField().setEditable(false);
                interfaz3.getOperAgreField().setEditable(false);
                break;
            case "Unión":
                interfaz3.getPredicadoField().setEditable(false);
                interfaz3.getNombreAField().setEditable(false);
                interfaz3.getOperAgreField().setEditable(false);    
                break;

            case "Diferencia de conjuntos":
                interfaz3.getPredicadoField().setEditable(false);
                interfaz3.getNombreAField().setEditable(false);
                interfaz3.getOperAgreField().setEditable(false);    
                break;

            case "Producto Cartesiano":
                interfaz3.getPredicadoField().setEditable(false);
                interfaz3.getNombreAField().setEditable(false);
                interfaz3.getOperAgreField().setEditable(false);    
                break;

            case "Intersección":
                interfaz3.getPredicadoField().setEditable(false);
                interfaz3.getNombreAField().setEditable(false);
                interfaz3.getOperAgreField().setEditable(false);    
                break;

            case "División":
                interfaz3.getPredicadoField().setEditable(false);
                interfaz3.getNombreAField().setEditable(false);
                interfaz3.getOperAgreField().setEditable(false);    
                break;

            case "Renombrar una relación y sus atributos":
                interfaz3.getPredicadoField().setEditable(false);
                interfaz3.getTabla2().setEditable(false);
                interfaz3.getOperAgreField().setEditable(false);    
                break;

            case "Concatenación":
                interfaz3.getNombreAField().setEditable(false);
                interfaz3.getOperAgreField().setEditable(false);    
                break;

            case "Concatenación natural":
                interfaz3.getPredicadoField().setEditable(false);
                interfaz3.getNombreAField().setEditable(false);
                interfaz3.getTabla2().setEditable(false);    
                break;

            case "Agregación":
                interfaz3.getPredicadoField().setEditable(false);
                interfaz3.getTabla2().setEditable(false);
                interfaz3.getOperAgreField().setEditable(false);    
                break;

            case "Agrupación":
                interfaz3.getPredicadoField().setEditable(false);
                interfaz3.getTabla2().setEditable(false);  
                break;
                
            default:
                break;
         };
        
        
        interfaz3.setLocationRelativeTo(null);
        interfaz3.setVisible(true);
        //interfaz2.cargarTabla();
        
    }//GEN-LAST:event_ejecutarOperActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acerca_de;
    private javax.swing.JButton ayuda;
    private javax.swing.JButton ejecutarOper;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JComboBox<String> operacion;
    // End of variables declaration//GEN-END:variables
}
