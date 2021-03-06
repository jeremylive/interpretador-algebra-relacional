/**
 * Paquete donde esta LA BASE DE DATOS(JAVA)
 */
package BaseDeDatos;
/**
 * Librerias a usar en este programa administrador de una BD
 */
import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
/**
 * Menu principal del programa de administrador de bases de datos
 * @author Jeremy Live
 */
public class Register extends javax.swing.JFrame 
{
    //Variables globales
    private ControladorSQLJAVA control;
    private DbaInterfaz interfaz_operacion;
    
    /**
     * Constructor
     */
    public Register(DbaInterfaz oper) 
    {
        initComponents();
        this.control = new ControladorSQLJAVA(this);
        this.interfaz_operacion = oper;
        
        backGro back = new backGro();
        this.add(back, BorderLayout.CENTER);
        this.pack();
    }
    /**
     * Funciones get and set
     */
    //obtengo jlabel
    public JLabel jltabla1()
    {
        return input2;
    }
    public JLabel jltabla2()
    {
        return input2;
    }
    public JLabel jlout()
    {
        return outputRelt;
    }
    public JLabel jlPre()
    {
        return pre;
    }
    public JLabel jlNameA()
    {
        return nameAtri;
    }
    public JLabel jlOper()
    {
        return OperAgre;
    }
    public JLabel jlExpres()
    {
        return expree;
    }
    //obtengo tabla input 1
    public String getInput1()
    {
        return tabla_input1.getText(); 
    }
    public JTextField getTabla1()
    {
        return tabla_input1;
    }
    //obtengo tabla input 2
    public String getInput2()
    {
        return tabla_input2.getText();
    }
    public JTextField getTabla2()
    {
        return tabla_input2;
    }
    //obtengo tabla output
    public String getOutput()
    {
        return tabla_result.getText();
    }
    public JTextField getTablaOut()
    {
        return tabla_result;
    }
    //obtengo el predicado
    public String getPredicado()
    {
        return predicado.getText();
    }
    public JTextField getPredicadoField()
    {
        return predicado;
    }
    //obtengo nombre de atributos
    public String getNombreA()
    {
        return nombre_atributos.getText();
    }
    public JTextField getNombreAField()
    {
        return nombre_atributos;
    }
    //obtengo operacion de agregacion
    public String getOperAgre()
    {
        return oper_agre.getText();
    }  
    public JTextField getOperAgreField()
    {
        return oper_agre;
    } 
    //
    public DefaultTableModel getTablaModel()
    {
        return (DefaultTableModel) tabla0.getModel();
    }
    //
    public JTable getTable0()
    {
        return tabla0;
    }
    //
    public void setTablaModel(DefaultTableModel model0)
    {
        tabla0.setModel(model0);
    }
    //
    public void setAlgebraR(String query)
    {
        algebra_relacional.setText(query);
    }
    //
    public void setSql(String query)
    {
        sql.setText(query);
    }
    //
    public ControladorSQLJAVA getControl()
    {
        return control;
    }
    //
    public JTextField getAlgebra()
    {
        return algebra_relacional;
    }
    //
    public JTextField getSql()
    {
        return sql;
    }
    //
    public JTextField getExpres()
    {
        return expres;
    }
    //
    public String getExpress()
    {
        return expres.getText();
    }
    //
    public void setOperacion0(String x)
    {
        operacion0.setText(x);
    }
    /**
     * Funcion para limpiar los campos del jTextField
     */
    public void cleanText()
    {
        tabla_input1.setText("");
        predicado.setText("");
        tabla_result.setText("");
        expres.setText("");
        tabla_input2.setText("");
        nombre_atributos.setText("");
        oper_agre.setText("");      
        /*
        tabla_input1.requestFocus();
        predicado.requestFocus();
        tabla_result.requestFocus();
        expres.requestFocus();
        tabla_input2.requestFocus();
        nombre_atributos.requestFocus();
        oper_agre.requestFocus();
        */
    }
    
    
    
    
    
    
    //Codigo sucio de netbeans
    //#########################################################################
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jRadioButtonMenuItem1 = new javax.swing.JRadioButtonMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla0 = new javax.swing.JTable();
        tabla_input1 = new javax.swing.JTextField();
        tabla_result = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        algebra_relacional = new javax.swing.JTextField();
        sql = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();
        pre = new javax.swing.JLabel();
        outputRelt = new javax.swing.JLabel();
        input2 = new javax.swing.JLabel();
        nameAtri = new javax.swing.JLabel();
        nombre_atributos = new javax.swing.JTextField();
        OperAgre = new javax.swing.JLabel();
        oper_agre = new javax.swing.JTextField();
        predicado = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        expree = new javax.swing.JLabel();
        expres = new javax.swing.JTextField();
        tabla_input2 = new javax.swing.JTextField();
        operacion0 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();

        jRadioButtonMenuItem1.setSelected(true);
        jRadioButtonMenuItem1.setText("jRadioButtonMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tabla0.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "", "", "", "", "", "", ""
            }
        ));
        jScrollPane1.setViewportView(tabla0);

        tabla_input1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tabla_input1ActionPerformed(evt);
            }
        });

        tabla_result.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tabla_resultActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Tabla resultante despues de hacer la operación");

        algebra_relacional.setText("Algebra Relacional");
        algebra_relacional.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                algebra_relacionalActionPerformed(evt);
            }
        });

        sql.setText("SQL");
        sql.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sqlActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Algebra Relacional");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Instrucción SQL");

        jToggleButton1.setBackground(new java.awt.Color(255, 51, 51));
        jToggleButton1.setFont(new java.awt.Font("Stencil", 1, 36)); // NOI18N
        jToggleButton1.setText("Salir");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jToggleButton2.setBackground(new java.awt.Color(51, 51, 255));
        jToggleButton2.setFont(new java.awt.Font("Stencil", 1, 36)); // NOI18N
        jToggleButton2.setForeground(new java.awt.Color(0, 204, 0));
        jToggleButton2.setText("Ejecutar");
        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton2ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Tabla Input 1");

        pre.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        pre.setForeground(new java.awt.Color(255, 255, 255));
        pre.setText("Predicado");

        outputRelt.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        outputRelt.setForeground(new java.awt.Color(255, 255, 255));
        outputRelt.setText("Tabla Resultante");

        input2.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        input2.setForeground(new java.awt.Color(255, 255, 255));
        input2.setText("Tabla Input 2");

        nameAtri.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        nameAtri.setForeground(new java.awt.Color(255, 255, 255));
        nameAtri.setText("Nombre atributos");

        OperAgre.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        OperAgre.setForeground(new java.awt.Color(255, 255, 255));
        OperAgre.setText("Operaciones agregación");

        oper_agre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oper_agreActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(51, 51, 255));
        jButton1.setFont(new java.awt.Font("Yu Gothic", 1, 24)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Ver tablas temporales");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(51, 51, 255));
        jButton2.setFont(new java.awt.Font("Yu Gothic", 1, 24)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Ver tablas de la DB");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(51, 51, 255));
        jButton4.setFont(new java.awt.Font("Yu Gothic", 1, 24)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Ver referencia cruzada / tablas");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        expree.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        expree.setForeground(new java.awt.Color(255, 255, 255));
        expree.setText("Expresión de la proyeción generalizada");

        expres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expresActionPerformed(evt);
            }
        });

        tabla_input2.setRequestFocusEnabled(false);

        operacion0.setFont(new java.awt.Font("Papyrus", 3, 14)); // NOI18N
        operacion0.setForeground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Papyrus", 3, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Informacion necesaria para realizar la operación:");

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Operaciones disponibles: ⋈, σ, π, Ģ, U, ∩");

        jMenu1.setText("INICIO");

        jMenuItem2.setText("Regresar ");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("CARGAR");

        jMenuItem1.setText("Cargar Datos");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuItem3.setText("Insertar Datos");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuItem4.setText("Eliminar");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuItem5.setText("Buscar un usuario");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem5);

        jMenuItem6.setText("Modificar");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem6);

        jMenuBar1.add(jMenu2);
        jMenuBar1.add(jMenu3);
        jMenuBar1.add(jMenu4);
        jMenuBar1.add(jMenu5);
        jMenuBar1.add(jMenu6);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 255, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sql, javax.swing.GroupLayout.PREFERRED_SIZE, 756, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))))
                .addContainerGap())
            .addComponent(jToggleButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(140, 140, 140)
                        .addComponent(expres))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(input2)
                            .addComponent(outputRelt)
                            .addComponent(pre)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tabla_input2)
                            .addComponent(tabla_input1)
                            .addComponent(tabla_result)
                            .addComponent(predicado)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(nameAtri)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(oper_agre, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(nombre_atributos)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(OperAgre))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(57, 57, 57)
                                .addComponent(jLabel2))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(expree)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(186, 186, 186)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(63, 63, 63)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(operacion0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jToggleButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(183, 183, 183))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(algebra_relacional, javax.swing.GroupLayout.PREFERRED_SIZE, 595, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tabla_input1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(input2)
                            .addComponent(tabla_input2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tabla_result, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(outputRelt))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pre)
                            .addComponent(predicado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(expree)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(expres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(OperAgre)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(oper_agre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nombre_atributos, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nameAtri))
                        .addGap(63, 63, 63)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(operacion0, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jToggleButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(algebra_relacional, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sql, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * Boton cargar datos
     * @param evt 
     */
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        //
        //cargarTabla();
    }//GEN-LAST:event_jMenuItem1ActionPerformed
    /**
     * Boton insertar datos en la tabla 
     *
     * No importa el dominio de la tabla, se adactara la tabla automaticamente
     *
     * Respuesta TABLA OUTPUT ........
     * 
     * @param evt 
     */
    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
       /*
       //Verifica si la informacion esta digitada
       if(tabla_input1.getText().isEmpty() || predicado.getText().isEmpty() || tabla_input1.getText().isEmpty())
       {
           JOptionPane.showMessageDialog(this, "¡¡¡Inserte la TABLA INPUT 1 y el PREDICADO correctamente!!!","Information",JOptionPane.INFORMATION_MESSAGE);
           cleanText();
       } else {
           try {
               //Obtengo un 1 o más si el nombre existe en la base de datos
               output = Conexion.consultaSql("select COUNT(name) from userProfile where name='"+predicado.getText()+"'");
               try {
                   while(output.next()){
                       isCheck = output.getInt(1);
                   }
               } catch (SQLException e) {
                   //nothing
               }
               
               //LOGICA A USAR PARA LA IMPRESION EN LA TABLA
               
               //Inserta el usuario si no existe
               if(isCheck >=1)
               {
                   JOptionPane.showMessageDialog(this, "¡¡¡El usuario digitado ya existe!!!","Information",JOptionPane.INFORMATION_MESSAGE);
               } else {
                   Conexion.insertaUser(tabla_input1.getText(), predicado.getText(), tabla_result.getText());
                   cleanText();
                   JOptionPane.showMessageDialog(this, "¡¡¡Los datos fueron satisfactoriamente correctos!!!");
                   //cargarTabla();
               }
           } catch (SQLException e) {
               //nothing
           }   
       }
        */
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void tabla_input1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tabla_input1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tabla_input1ActionPerformed

    private void tabla_resultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tabla_resultActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tabla_resultActionPerformed
    /**
     * Botom eliminar de la tabla
     * @param evt 
     */
    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        /*
        int row = -1;
        row = tabla0.getSelectedRow();

        if(row != -1){
            int opc = JOptionPane.showConfirmDialog(this, "¿Do you want to eliminate this register","Question",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
            if(opc == JOptionPane.YES_OPTION)
            {
                try {
                    Conexion.dropUser(tabla0.getValueAt(row, 0).toString());
                    //cargarTabla();
                } catch (SQLException e) {
                    //Nothing
                }
            }
        }
        */
    }//GEN-LAST:event_jMenuItem4ActionPerformed
    /**
     * Boton regresar
     * @param evt 
     */
    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jMenuItem2ActionPerformed
    /**
     * Boton buscar usuario
     * @param evt 
     */
    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        /*
        if(tabla_input1.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Inserte bien sus datos","Error",JOptionPane.ERROR_MESSAGE);
            tabla_input1.setText("");
            tabla_input1.requestFocus();
        } else {
            try {
                output = Conexion.consultaSql("select * from userProfile");
                id = tabla_input1.getText();
                Conexion.searchUser(id);
                cleanText();
                
                while(output.next())
                {
                    if(output.getString(1).equals(id))
                    {
                        bolean = true;
                        JOptionPane.showMessageDialog(null, "Datos encontrados!");
                        tabla_input1.setText(output.getString(1));
                        predicado.setText(output.getString(2));
                        tabla_result.setText(output.getString(3));
                        //cargarTabla();
                        break;
                    } 
                }
                
                if(!bolean){
                    JOptionPane.showMessageDialog(null, "Datos no encontrados");
                }
            } catch (SQLException e) {
                //Nothing
            }
        }
        */
    }//GEN-LAST:event_jMenuItem5ActionPerformed
    /**
     * Boton Modificar
     * @param evt 
     */
    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        /*
        if(tabla_input1.getText().isEmpty() || predicado.getText().isEmpty() || tabla_input2.getText().isEmpty())
       {
           JOptionPane.showMessageDialog(this, "¡¡¡Inserte sus datos correctamente!!!","Information",JOptionPane.INFORMATION_MESSAGE);
           cleanText();
       } else {
            try {
                PreparedStatement updateSql = Conexion.contacto.prepareStatement("update userProfile set name='"+predicado.getText()+"', carrer='"+tabla_result.getText()+"' where id='"+tabla_input1.getText()+"'");
                updateSql.executeUpdate();
                JOptionPane.showMessageDialog(null, "¡¡¡Los datos han sido modificados!!!");
                //cargarTabla();
                cleanText();
            } catch(SQLException e) {
                //Nothing
            }
        }
        */
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void sqlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sqlActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sqlActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        //Elimino todas las tablas temporales
        //Salgo de la aplcación
        //dispose();
        this.setVisible(false);
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    /**
     * Boton EJECUTAR
     * @param evt 
     */
    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton2ActionPerformed
        //
        String operator = interfaz_operacion.getOper().getSelectedItem().toString();
        JOptionPane.showMessageDialog(null, "¡¡¡La operacion elegida: "+operator+"!!!");    
        //
        getControl().modeloRelacional(operator);
    }//GEN-LAST:event_jToggleButton2ActionPerformed

    /**
     * Imprime todas las tablas temporales que eh creado desde que inicie la APP
     * @param evt 
     */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
                
        getControl().printTablaTemps();
                 

    }//GEN-LAST:event_jButton1ActionPerformed
    /**
     * Imprime todas las tablas permanentes de la Base de datos 
     * @param evt 
     */
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        //case "Ver tablas de la base de datos":
        //            break;
        
        getControl().printTablaPermanentes();
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void oper_agreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oper_agreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_oper_agreActionPerformed

    private void algebra_relacionalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_algebra_relacionalActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_algebra_relacionalActionPerformed

    private void expresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_expresActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_expresActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed
   
    /**
     * -------------------------------------------------------------------------
     * -------------------------------------------------------------------------
     * -------------------------------------------------------------------------
     * ..........................CODIGO_BASUTA..................................
     * -------------------------------------------------------------------------
     * -------------------------------------------------------------------------
     * -------------------------------------------------------------------------
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel OperAgre;
    private javax.swing.JTextField algebra_relacional;
    private javax.swing.JLabel expree;
    private javax.swing.JTextField expres;
    private javax.swing.JLabel input2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JLabel nameAtri;
    private javax.swing.JTextField nombre_atributos;
    private javax.swing.JTextField oper_agre;
    private javax.swing.JLabel operacion0;
    private javax.swing.JLabel outputRelt;
    private javax.swing.JLabel pre;
    private javax.swing.JTextField predicado;
    private javax.swing.JTextField sql;
    private javax.swing.JTable tabla0;
    private javax.swing.JTextField tabla_input1;
    private javax.swing.JTextField tabla_input2;
    private javax.swing.JTextField tabla_result;
    // End of variables declaration//GEN-END:variables

    private void Swicht() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
