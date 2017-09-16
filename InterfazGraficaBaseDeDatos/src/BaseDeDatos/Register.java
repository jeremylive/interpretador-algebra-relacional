/**
 * Paquete donde esta LA BASE DE DATOS(JAVA)
 */
package BaseDeDatos;
/**
 * Librerias a usar en este programa administrador de una BD
 */
import java.sql.Connection;
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
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 * Menu principal del programa de administrador de bases de datos
 * @author Jeremy Live
 */
public class Register extends javax.swing.JFrame 
{
    /**
     * Variables globales
     */
    private ResultSet output;   //Guarda el resultado del query
    private int isCheck;               //Contador para
    private String id;
    private boolean bolean;
    private int cont=0;
    //Variable con todos los nombres de las tablas temporales
    private String[] name_all_temp = new String[20];
    private String[] name_all_per = {"sucursal", "cliente", "impositor", "cuenta", "prestatario", "préstamo"};
    private Conexion conexx;
    /**
     * Constructor
     */
    public Register(Conexion conex) 
    {
        initComponents();
        conexx = conex;
    }
    
    /**
     * Funcion que carga la tabla de datos con los datos actualizados
     */
    public void cargarTablaSeleccion(String name_tablaInput, String predicado_aux, String name_tablaOutput)
    {
        //Obtengo la tabla de la Base de datos para poder agregarla
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        modelo.setRowCount(0);
        
        try{
            //Hago la selecion de la tabla
            output = conexx.consultaSql("SELECT * FROM proy1."+name_tablaInput+" WHERE "+predicado_aux);
            
            //Obtengo el total de columnas que tiene la tabla
            ResultSetMetaData metaDatos = output.getMetaData();
            int index=metaDatos.getColumnCount();
            
            String sqlQuery = "CREATE TABLE proy1.#"+name_tablaOutput+" ";
            String sqlQuery2 = "(";
            String sqlQuery3 = "";
            //Inserto datos en la tabla a mostrar
            while(output.next()){
                for(int i=1;i<=index;i++){
                    //Obtengo info de la tabla 
                    if(i == index){
                        sqlQuery3 += metaDatos.getColumnName(i);
                        sqlQuery2 += metaDatos.getColumnName(i) 
                           + " "
                           + metaDatos.getColumnTypeName(i)
                           + "(" + Integer.toString(metaDatos.getPrecision(i))
                           + ")";
                    }else{
                        sqlQuery3 += metaDatos.getColumnName(i) + ", ";
                        sqlQuery2 += metaDatos.getColumnName(i) 
                            + " "
                            + metaDatos.getColumnTypeName(i)
                            + "(" + Integer.toString(metaDatos.getPrecision(i))
                            + ")"
                            + ", ";
                    }
                }
            }
            //Datos a utilizar
            sqlQuery= sqlQuery+sqlQuery2+")";
            System.out.println(sqlQuery + "\n" + sqlQuery2 + "\n" + sqlQuery3);
            
            String insertT = "INSERT INTO proy1.#"+name_tablaOutput + " (" + sqlQuery3 + ") " 
                    +"SELECT * FROM proy1."+name_tablaInput+" WHERE "+predicado_aux;
            
            String insertF = "SELECT * FROM proy1.#"+name_tablaOutput;
   
            System.out.println(insertT + "\n" + insertF);
            //Creo la tabla temporal eh Inserto datos en la tabla temporal y accedo a ella
            output = conexx.consultaSqlCreate(sqlQuery, insertT, insertF); 
                   
            //Imprimo la tabla temporal
            metaDatos = output.getMetaData();
            index = metaDatos.getColumnCount();      
            while(output.next()){
                Vector v = new Vector();
                
                for(int i=1;i<=index;i++){
                    //Extraigo tuplas
                    v.add(output.getString(i));      
                }
                modelo.addRow(v);
                jTable1.setModel(modelo);
            }
            
            //Aumento contador de nombre de la tabla temporal
            name_all_temp[cont]=name_tablaOutput;
            cont++;
            System.out.println(name_all_temp[cont-1]);
            
        } catch (SQLException e) {
            //nothing
        }
    }
    
    /**
     * Función que imprime todas las tablas temporales
     */
    public void printTablaTemps()
    {
        for (int i = 0; i <= cont; i++){
            try {
                
                
                output = conexx.consultaSql("SELECT * FROM proy1.#"+name_all_temp[i]);
                
                ResultSetMetaData metaDatos = output.getMetaData();
                int index=metaDatos.getColumnCount();
                
                System.out.println(name_all_temp[i]);
                while(output.next()){
                    for(int x=1;x<=index;x++){
                        //Extraigo tuplas
                        System.out.println(output.getString(x));     
                        
                    }
                }
                
                
            } catch (SQLException ex) {
                Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }
    }
    
    /**
     * Funcion que imprime todas las tablas permanentes
     */
    public void printTablaPermanentes()
    {
        for (int i = 0; i <= cont; i++){
            try {
                output = conexx.consultaSql("SELECT * FROM proy1."+name_all_per[i]);
                
                ResultSetMetaData metaDatos = output.getMetaData();
                int index=metaDatos.getColumnCount();
                
                System.out.println(name_all_per[i]);
                while(output.next()){
                    for(int x=1;x<=index;x++){
                        //Extraigo tuplas
                        System.out.println(output.getString(x));     
                        
                    }
                }
                
                
            } catch (SQLException ex) {
                Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }
    }
    
    
    /**
     * Funcion para limpiar los campos del jTextField
     */
    public void cleanText()
    {
        tabla_input1.setText("");
        predicado.setText("");
        tabla_result.setText("");
        tabla_input1.requestFocus();
        predicado.requestFocus();
        tabla_result.requestFocus();
    }
    
    /**
     * Función que imprime el output en modelo relacional
     * Parametros:
     *  1. nombre tabla input, 2. predicado, 3. nombre tabla output
     */
    public void modeloRelacional(String operator_aux)
    {
       //Verifica si la informacion esta digitada
       if(tabla_input1.getText().isEmpty() || predicado.getText().isEmpty())
       {
           JOptionPane.showMessageDialog(this, "Insert your date correctly","Information",JOptionPane.INFORMATION_MESSAGE);
           cleanText();
       } else {
            switch(operator_aux){
                case "Selección":
                    if(tabla_result.getText().isEmpty()){
                        algebra_relacional.setText("σ "+predicado.getText()+"("+tabla_input1.getText()+")");
                        sql.setText("SELECT * FROM "+tabla_input1.getText()+" WHERE "+predicado.getText());
                        JOptionPane.showMessageDialog(null, "No ingreso el nombre de la tabla resultante:\nNo se muestra la tabla graficamente\nNi se crea la tabla temporal");
                    }else{
                        algebra_relacional.setText(tabla_result.getText()+" <- σ "+predicado.getText()+"("+tabla_input1.getText()+")");
                        sql.setText("SELECT * FROM "+tabla_input1.getText()+" WHERE "+predicado.getText());
                        cargarTablaSeleccion(tabla_input1.getText(), predicado.getText(), tabla_result.getText());
                    }
                    
                    break;
                
                case "Proyección Generalizada":
                    algebra_relacional.setText(tabla_result.getText()+" <- π "+predicado.getText()+"("+tabla_input1.getText()+")");
                    sql.setText("SELECT * FROM "+tabla_input1.getText()+" WHERE "+predicado.getText());
                    break;
                    
                case "Unión":
                    break;
                    
                case "Diferencia de conjuntos":
                    break;
                    
                case "Producto Cartesiano":
                    break;
                    
                case "Intersección":
                    break;
                
                case "División":
                    break;
                 
                case "Renombrar una relación y sus atributos":
                    break;
                    
                case "Concatenación":
                    break;
                    
                case "Concatenación natural":
                    break;
                    
                case "Agregación":
                    break;
                    
                case "Agrupación":
                    break;
                   
                case "Ver referencia cruzada atributos/tablas":
                    break;                    
                default:
                    break;
            };
            
            
            cleanText();
            JOptionPane.showMessageDialog(this, "The date was successfully correctly");

       }
    }
    
    
    /**
     * Funcion imprimir todas las tablas
     */
    
    
    
    /**
     * Funcion imprimir todas las tablas temporales
     */
    
    
    //Codigo sucio de netbeans
    //#########################################################################
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jRadioButtonMenuItem1 = new javax.swing.JRadioButtonMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        tabla_input1 = new javax.swing.JTextField();
        tabla_result = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        algebra_relacional = new javax.swing.JTextField();
        sql = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        tabla_input2 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        nombre_atributos = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        predicado = new javax.swing.JTextField();
        ayuda = new javax.swing.JButton();
        acerca_de = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
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

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Id", "Name", "Carrer"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

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

        jLabel4.setText("Tabla resultante despues de hacer la operación");

        algebra_relacional.setText("Algebra Relacional");

        sql.setText("SQL");
        sql.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sqlActionPerformed(evt);
            }
        });

        jLabel5.setText("Algebra Relacional");

        jLabel6.setText("Instrucción SQL");

        jToggleButton1.setFont(new java.awt.Font("Yu Gothic", 0, 36)); // NOI18N
        jToggleButton1.setText("Salir");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jToggleButton2.setFont(new java.awt.Font("Yu Gothic", 0, 36)); // NOI18N
        jToggleButton2.setText("Ejecutar");
        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton2ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selección", "Proyección Generalizada", "Unión", "Diferencia de conjuntos", "Producto Cartesiano", "Intersección", "División", "Renombrar una relación y sus atributos", "Concatenación", "Concatenación natural", "Agregación", "Agrupación" }));

        jLabel1.setText("Tabla Input 1");

        jLabel2.setText("Predicado");

        jLabel3.setText("Tabla Resultante");

        jLabel7.setText("Tabla Input 2");

        jLabel8.setText("Nombre atributos");

        jLabel9.setText("Operaciones agregación");

        ayuda.setFont(new java.awt.Font("Yu Gothic", 0, 24)); // NOI18N
        ayuda.setText("Ayuda");
        ayuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ayudaActionPerformed(evt);
            }
        });

        acerca_de.setFont(new java.awt.Font("Yu Gothic", 0, 24)); // NOI18N
        acerca_de.setText("Acerca de");
        acerca_de.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acerca_deActionPerformed(evt);
            }
        });

        jLabel11.setText("Operación a realizar");

        jTextField2.setText("⋈, σ, π, Ģ, U, ∩");
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Yu Gothic", 0, 14)); // NOI18N
        jButton1.setText("Ver tablas temporales");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Yu Gothic", 0, 14)); // NOI18N
        jButton2.setText("Ver tablas de la DB");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

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
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jToggleButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(acerca_de, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ayuda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(117, 117, 117)
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(166, 166, 166)
                        .addComponent(jLabel11)
                        .addGap(367, 367, 367)
                        .addComponent(jTextField2))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sql, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(algebra_relacional, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(118, 118, 118)
                                .addComponent(jLabel5))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(124, 124, 124)
                                .addComponent(jLabel6))
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(81, 81, 81)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField1))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel7)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(tabla_input2, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel1)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(tabla_input1, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                            .addGap(2, 2, 2)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jLabel2)
                                                .addComponent(jLabel3))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(tabla_result)
                                                .addComponent(predicado, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel8)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(nombre_atributos, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))))))))
                .addGap(63, 63, 63))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel11))
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(algebra_relacional, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sql, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tabla_input1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(tabla_input2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tabla_result, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(predicado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nombre_atributos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jToggleButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(acerca_de, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ayuda, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(37, Short.MAX_VALUE))
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
     * @param evt 
     */
    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
       //
       //Verifica si la informacion esta digitada
       if(tabla_input1.getText().isEmpty() || predicado.getText().isEmpty() || tabla_input1.getText().isEmpty())
       {
           JOptionPane.showMessageDialog(this, "Insert your date correctly","Information",JOptionPane.INFORMATION_MESSAGE);
           cleanText();
       } else {
           try {
               //Obtengo un 1 o más si el nombre existe en la base de datos
               output = conexx.consultaSql("select COUNT(name) from userProfile where name='"+predicado.getText()+"'");
               try {
                   while(output.next()){
                       isCheck = output.getInt(1);
                   }
               } catch (SQLException e) {
                   //nothing
               }
               //Inserta el usuario si no existe
               if(isCheck >=1)
               {
                   JOptionPane.showMessageDialog(this, "This user is already exists","Information",JOptionPane.INFORMATION_MESSAGE);
               } else {
                   conexx.insertaUser(tabla_input1.getText(), predicado.getText(), tabla_result.getText());
                   cleanText();
                   JOptionPane.showMessageDialog(this, "The date was successfully correctly");
                   //cargarTabla();
               }
           } catch (SQLException e) {
               //nothing
           }   
       }
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
        //
        int row = -1;
        row = jTable1.getSelectedRow();

        
        if(row != -1){
            int opc = JOptionPane.showConfirmDialog(this, "¿Do you want to eliminate this register","Question",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
            if(opc == JOptionPane.YES_OPTION)
            {
                try {
                    conexx.dropUser(jTable1.getValueAt(row, 0).toString());
                    //cargarTabla();
                } catch (SQLException e) {
                    //Nothing
                }
            }
        }
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
        
        if(tabla_input1.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Inserte bien sus datos","Error",JOptionPane.ERROR_MESSAGE);
            tabla_input1.setText("");
            tabla_input1.requestFocus();
        } else {
            try {
                output = conexx.consultaSql("select * from userProfile");
                id = tabla_input1.getText();
                conexx.searchUser(id);
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
    }//GEN-LAST:event_jMenuItem5ActionPerformed
    /**
     * Boton Modificar
     * @param evt 
     */
    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed

        if(tabla_input1.getText().isEmpty() || predicado.getText().isEmpty() || tabla_input2.getText().isEmpty())
       {
           JOptionPane.showMessageDialog(this, "Insert your date correctly","Information",JOptionPane.INFORMATION_MESSAGE);
           cleanText();
       } else {
            try {
                PreparedStatement updateSql = Conexion.contacto.prepareStatement("update userProfile set name='"+predicado.getText()+"', carrer='"+tabla_result.getText()+"' where id='"+tabla_input1.getText()+"'");
                updateSql.executeUpdate();
                JOptionPane.showMessageDialog(null, "Los datos han sido modificados");
                //cargarTabla();
                cleanText();
            } catch(SQLException e) {
                //Nothing
            }
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void sqlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sqlActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sqlActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        //Elimino todas las tablas temporales
        
        //Salgo de la aplcación
        
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton2ActionPerformed
        // TODO add your handling code here:
        String operator = jComboBox1.getSelectedItem().toString();
        JOptionPane.showMessageDialog(null, "Operacion elegida: "+operator);    
        
        modeloRelacional(operator);
    }//GEN-LAST:event_jToggleButton2ActionPerformed

    private void acerca_deActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acerca_deActionPerformed
        
        JOptionPane.showMessageDialog(null, "Nombre de la aplicación: Interprete de Algebra Relacional\nVensión: 01.01.01\nFecha de creación: 13/09/17\nAutores: Jeremy José Live González","Acerca de..",JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_acerca_deActionPerformed

    private void ayudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ayudaActionPerformed
        //Muestro manual del software
        
    }//GEN-LAST:event_ayudaActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
                
        printTablaTemps();
                 

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        //case "Ver tablas de la base de datos":
        //            break;
        
        printTablaPermanentes();
        
    }//GEN-LAST:event_jButton2ActionPerformed


    
    
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acerca_de;
    private javax.swing.JTextField algebra_relacional;
    private javax.swing.JButton ayuda;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
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
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JTextField nombre_atributos;
    private javax.swing.JTextField predicado;
    private javax.swing.JTextField sql;
    private javax.swing.JTextField tabla_input1;
    private javax.swing.JTextField tabla_input2;
    private javax.swing.JTextField tabla_result;
    // End of variables declaration//GEN-END:variables

    private void Swicht() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
