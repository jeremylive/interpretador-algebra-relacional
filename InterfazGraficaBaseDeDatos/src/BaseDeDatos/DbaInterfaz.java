package BaseDeDatos;
//Bibliotecas a usar
import java.awt.BorderLayout;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
/**
 *
 * @author live
 */
public class DbaInterfaz extends javax.swing.JFrame 
{
    //Variables globales
    private Register interfaz3;
    private String operator;
    //Constructor
    public DbaInterfaz() {
        initComponents();
        interfaz3 = new Register(this);
        backGro back = new backGro();
        this.add(back, BorderLayout.CENTER);
        this.pack();
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
        Salir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Eliga la operación a realizar:");

        operacion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selección", "Proyección Generalizada", "Unión", "Diferencia de conjuntos", "Producto Cartesiano", "Intersección", "División", "Renombrar una relación y sus atributos", "Concatenación", "Concatenación natural", "Agregación", "Agrupación" }));
        operacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                operacionActionPerformed(evt);
            }
        });

        acerca_de.setBackground(new java.awt.Color(51, 51, 255));
        acerca_de.setFont(new java.awt.Font("Yu Gothic", 1, 24)); // NOI18N
        acerca_de.setForeground(new java.awt.Color(255, 255, 255));
        acerca_de.setText("Acerca de");
        acerca_de.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acerca_deActionPerformed(evt);
            }
        });

        ayuda.setBackground(new java.awt.Color(51, 51, 255));
        ayuda.setFont(new java.awt.Font("Yu Gothic", 1, 24)); // NOI18N
        ayuda.setForeground(new java.awt.Color(255, 255, 255));
        ayuda.setText("Ayuda");
        ayuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ayudaActionPerformed(evt);
            }
        });

        ejecutarOper.setBackground(new java.awt.Color(51, 51, 255));
        ejecutarOper.setFont(new java.awt.Font("Stencil", 1, 24)); // NOI18N
        ejecutarOper.setForeground(new java.awt.Color(0, 204, 0));
        ejecutarOper.setText("Ejercutar operación");
        ejecutarOper.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ejecutarOperActionPerformed(evt);
            }
        });

        Salir.setBackground(new java.awt.Color(255, 51, 51));
        Salir.setFont(new java.awt.Font("Stencil", 1, 36)); // NOI18N
        Salir.setText("Salir");
        Salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(89, 89, 89)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ejecutarOper, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(acerca_de, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                                    .addComponent(ayuda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Salir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(operacion, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(operacion, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ejecutarOper, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(acerca_de, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ayuda, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(Salir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(20, Short.MAX_VALUE))
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
        
        operator = operacion.getSelectedItem().toString();
        interfaz3.setOperacion0(operator);
        
        interfaz3.getAlgebra().setEditable(false);
        interfaz3.getSql().setEditable(false);
        interfaz3.getTable0().setEnabled(false);
        
        switch(operator){
            case "Selección":
                interfaz3.getTabla1().setVisible(true);
                interfaz3.getPredicadoField().setVisible(true);
                interfaz3.getTablaOut().setVisible(true);
                interfaz3.getTabla2().setVisible(false);
                interfaz3.getNombreAField().setVisible(false);
                interfaz3.getOperAgreField().setVisible(false);
                interfaz3.getExpres().setVisible(false);
                
                interfaz3.jltabla1().setVisible(true);
                interfaz3.jlPre().setVisible(true);
                interfaz3.jlout().setVisible(true);
                interfaz3.jlExpres().setVisible(false);
                interfaz3.jlNameA().setVisible(false);
                interfaz3.jlOper().setVisible(false);
                interfaz3.jltabla2().setVisible(false);
                break;

            case "Proyección Generalizada":
                interfaz3.getTabla1().setVisible(true);
                interfaz3.getExpres().setVisible(true);
                interfaz3.getTablaOut().setVisible(true);
                interfaz3.getTabla2().setVisible(false);
                interfaz3.getNombreAField().setVisible(false);
                interfaz3.getOperAgreField().setVisible(false);
                interfaz3.getPredicadoField().setVisible(false);
                
                interfaz3.jltabla1().setVisible(true);
                interfaz3.jlExpres().setVisible(true);
                interfaz3.jlout().setVisible(true);
                interfaz3.jlPre().setVisible(false);
                interfaz3.jlNameA().setVisible(false);
                interfaz3.jlOper().setVisible(false);
                interfaz3.jltabla2().setVisible(false);
                break;
            case "Unión":
                interfaz3.getTabla1().setVisible(true);
                interfaz3.getTabla2().setVisible(true);
                interfaz3.getTablaOut().setVisible(true);
                interfaz3.getPredicadoField().setVisible(false);
                interfaz3.getNombreAField().setVisible(false);
                interfaz3.getOperAgreField().setVisible(false);
                interfaz3.getExpres().setVisible(false);
                
                interfaz3.jltabla1().setVisible(true);
                interfaz3.jltabla2().setVisible(true);
                interfaz3.jlout().setVisible(true);
                interfaz3.jlPre().setVisible(false);
                interfaz3.jlExpres().setVisible(false);
                interfaz3.jlNameA().setVisible(false);
                interfaz3.jlOper().setVisible(false);
                break;

            case "Diferencia de conjuntos":
                interfaz3.getTabla1().setVisible(true);
                interfaz3.getTabla2().setVisible(true);
                interfaz3.getTablaOut().setVisible(true);
                interfaz3.getPredicadoField().setVisible(false);
                interfaz3.getNombreAField().setVisible(false);
                interfaz3.getOperAgreField().setVisible(false);    
                interfaz3.getExpres().setVisible(false);
                
                interfaz3.jltabla1().setVisible(true);
                interfaz3.jltabla2().setVisible(true);
                interfaz3.jlout().setVisible(true);
                interfaz3.jlPre().setVisible(false);
                interfaz3.jlExpres().setVisible(false);
                interfaz3.jlNameA().setVisible(false);
                interfaz3.jlOper().setVisible(false);
                break;

            case "Producto Cartesiano":
                interfaz3.getTabla1().setVisible(true);
                interfaz3.getTabla2().setVisible(true);
                interfaz3.getTablaOut().setVisible(true);
                interfaz3.getPredicadoField().setVisible(false);
                interfaz3.getNombreAField().setVisible(false);
                interfaz3.getOperAgreField().setVisible(false);    
                interfaz3.getExpres().setVisible(false);
                
                interfaz3.jltabla1().setVisible(true);
                interfaz3.jltabla2().setVisible(true);
                interfaz3.jlout().setVisible(true);
                interfaz3.jlPre().setVisible(false);
                interfaz3.jlExpres().setVisible(false);
                interfaz3.jlNameA().setVisible(false);
                interfaz3.jlOper().setVisible(false);
                break;

            case "Intersección":
                interfaz3.getTabla1().setVisible(true);
                interfaz3.getTabla2().setVisible(true);
                interfaz3.getTablaOut().setVisible(true);
                interfaz3.getPredicadoField().setVisible(false);
                interfaz3.getNombreAField().setVisible(false);
                interfaz3.getOperAgreField().setVisible(false);  
                interfaz3.getExpres().setVisible(false);
                
                interfaz3.jltabla1().setVisible(true);
                interfaz3.jltabla2().setVisible(true);
                interfaz3.jlout().setVisible(true);
                interfaz3.jlPre().setVisible(false);
                interfaz3.jlExpres().setVisible(false);
                interfaz3.jlNameA().setVisible(false);
                interfaz3.jlOper().setVisible(false);
                break;

            case "División":
                interfaz3.getTabla1().setVisible(true);
                interfaz3.getTabla2().setVisible(true);
                interfaz3.getTablaOut().setVisible(true);
                interfaz3.getPredicadoField().setVisible(false);
                interfaz3.getNombreAField().setVisible(false);
                interfaz3.getOperAgreField().setVisible(false);    
                interfaz3.getExpres().setVisible(false);
                
                interfaz3.jltabla1().setVisible(true);
                interfaz3.jltabla2().setVisible(true);
                interfaz3.jlout().setVisible(true);
                interfaz3.jlPre().setVisible(false);
                interfaz3.jlExpres().setVisible(false);
                interfaz3.jlNameA().setVisible(false);
                interfaz3.jlOper().setVisible(false);
                break;

            case "Renombrar una relación y sus atributos":
                interfaz3.getTabla1().setVisible(true);
                interfaz3.getNombreAField().setVisible(true);
                interfaz3.getTablaOut().setVisible(true);
                interfaz3.getPredicadoField().setVisible(false);
                interfaz3.getTabla2().setVisible(false);
                interfaz3.getOperAgreField().setVisible(false);    
                interfaz3.getExpres().setVisible(false);
                
                interfaz3.jltabla1().setVisible(true);
                interfaz3.jlNameA().setVisible(true);
                interfaz3.jlout().setVisible(true);
                interfaz3.jlPre().setVisible(false);
                interfaz3.jlExpres().setVisible(false);
                interfaz3.jlOper().setVisible(false);
                interfaz3.jltabla2().setVisible(false);
                break;

            case "Concatenación":
                interfaz3.getTabla1().setVisible(true);
                interfaz3.getTabla2().setVisible(true);
                interfaz3.getPredicadoField().setVisible(true);
                interfaz3.getTablaOut().setVisible(true);
                interfaz3.getNombreAField().setVisible(false);
                interfaz3.getOperAgreField().setVisible(false);    
                interfaz3.getExpres().setVisible(false);
                
                interfaz3.jltabla1().setVisible(true);
                interfaz3.jltabla2().setVisible(true);
                interfaz3.jlout().setVisible(true);
                interfaz3.jlPre().setVisible(true);
                interfaz3.jlExpres().setVisible(false);
                interfaz3.jlNameA().setVisible(false);
                interfaz3.jlOper().setVisible(false);
                break;

            case "Concatenación natural":
                interfaz3.getTabla1().setVisible(true);
                interfaz3.getTabla2().setVisible(true);
                interfaz3.getTablaOut().setVisible(true);
                interfaz3.getPredicadoField().setVisible(false);
                interfaz3.getNombreAField().setVisible(false);
                interfaz3.getExpres().setVisible(false);
                interfaz3.getOperAgreField().setVisible(false);
                
                interfaz3.jltabla1().setVisible(true);
                interfaz3.jltabla2().setVisible(true);
                interfaz3.jlout().setVisible(true);
                interfaz3.jlPre().setVisible(false);
                interfaz3.jlExpres().setVisible(false);
                interfaz3.jlNameA().setVisible(false);
                interfaz3.jlOper().setVisible(false);
                break;

            case "Agregación":
                interfaz3.getTabla1().setVisible(true);
                interfaz3.getOperAgreField().setVisible(true);
                interfaz3.getTablaOut().setVisible(true);
                interfaz3.getPredicadoField().setVisible(false);
                interfaz3.getTabla2().setVisible(false);
                interfaz3.getExpres().setVisible(false);
                interfaz3.getNombreAField().setVisible(false);
                
                interfaz3.jltabla1().setVisible(true);
                interfaz3.jlOper().setVisible(true);
                interfaz3.jlout().setVisible(true);
                interfaz3.jltabla2().setVisible(false);
                interfaz3.jlPre().setVisible(false);
                interfaz3.jlExpres().setVisible(false);
                interfaz3.jlNameA().setVisible(false);
                break;

            case "Agrupación":
                interfaz3.getTabla1().setVisible(true);
                interfaz3.getNombreAField().setVisible(true);
                interfaz3.getOperAgreField().setVisible(true);
                interfaz3.getTablaOut().setVisible(true);
                interfaz3.getPredicadoField().setVisible(false);
                interfaz3.getTabla2().setVisible(false);  
                interfaz3.getExpres().setVisible(false);
                
                interfaz3.jltabla1().setVisible(true);
                interfaz3.jlNameA().setVisible(true);
                interfaz3.jlOper().setVisible(true);
                interfaz3.jlout().setVisible(true);
                interfaz3.jltabla2().setVisible(false);
                interfaz3.jlPre().setVisible(false);
                interfaz3.jlExpres().setVisible(false);
                break;
                
            default:
                break;
         };
        
        
        interfaz3.setLocationRelativeTo(null);
        interfaz3.setVisible(true);
        
    }//GEN-LAST:event_ejecutarOperActionPerformed

    private void SalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SalirActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_SalirActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Salir;
    private javax.swing.JButton acerca_de;
    private javax.swing.JButton ayuda;
    private javax.swing.JButton ejecutarOper;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JComboBox<String> operacion;
    // End of variables declaration//GEN-END:variables

    void setLocationRelativeTo(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
