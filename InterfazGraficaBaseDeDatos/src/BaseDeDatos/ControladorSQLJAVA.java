package BaseDeDatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Bibliotecas a usar
 */


/**
 *  Controlador
 * @author live
 */
public class ControladorSQLJAVA 
{
    //Variables globales
    private ResultSet output;          //Guarda el resultado del query
    private int isCheck;               //Contador para
    private String id;
    private boolean bolean;
    private int cont;
    private String[] name_all_temp;     //Variable con todos los nombres de las tablas temporales
    private String[] name_all_per  = {"sucursal", "cliente", "impositor", "cuenta", "prestatario", "préstamo"};
    private Register register;          //Control con la interfaz
    
    //Constructor
    public ControladorSQLJAVA(Register regis)
    {
      this.name_all_temp = new String[20];
      this.cont = 0;
      this.register = regis; 
    }
    
    //Gets and Sets
    public Register getRegister()
    {
        return this.register;
    }
    //
    
    /**
     * Funcion que carga la tabla de datos con los datos actualizados
     * 
     * 
     * 
     * 
     * DIVIDIR ESTE CODIGO EN:
     * 
     * 1-) Obtengo datos para imprimir la tabla temporal
     * 
     * 2-) Creo tabla temporal y la agrego al diccionario
     * 
     * CON PARAMETRO:
     *  ResultSetMetaData
     * 
     */
    public void cargarTablaSeleccion(String name_tablaInput, String predicado_aux, String name_tablaOutput)
    {
        
        //Creo variables
        String sqlQuery = "CREATE TABLE proy1.#"+name_tablaOutput+" ";
        String sqlQuery2 = "(";
        String sqlQuery3 = "";
        
        //Obtengo la tabla de la Base de datos para poder agregarla
        DefaultTableModel modelo = getRegister().getTablaModel();
        modelo.setRowCount(0);
        
        try{
            //Hago la selecion de la tabla
            output = Conexion.consultaSql("SELECT * FROM proy1."+name_tablaInput+" WHERE "+predicado_aux);
            
            //Obtengo el total de columnas que tiene la tabla
            ResultSetMetaData metaDatos = output.getMetaData();
            
            int index=metaDatos.getColumnCount();
            
            //Inserto datos en la tabla a mostrar
            while(output.next())
            {
                for(int i=1; i<=index; i++)
                {
                    if(i == index)
                    {
                        //
                        sqlQuery3 += metaDatos.getColumnName(i);
                        sqlQuery2 += metaDatos.getColumnName(i) 
                           + " "
                           + metaDatos.getColumnTypeName(i)
                           + "(" + Integer.toString(metaDatos.getPrecision(i))
                           + ")";
                    }else
                    {   
                        //
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
   
            System.out.println("\n" + insertT + "\n" + insertF);
            
            
            
            //Creo la tabla temporal eh Inserto datos en la tabla temporal y accedo a ella
            output = Conexion.consultaSqlCreate(sqlQuery, insertT, insertF); 
                   
            //Imprimo la tabla temporal
            metaDatos = output.getMetaData();
            index = metaDatos.getColumnCount();      
            
            while(output.next()){
                Vector v = new Vector();
                
                for(int i=1;i<=index;i++){
                    //Extraigo tuplas
                    v.add(output.getString(i));      
                    System.out.println("----"+output.getString(i));
                }
                modelo.addRow(v);
                getRegister().setTablaModel(modelo);    //Agrego datos a la tabla
            }
            
            //Aumento contador de nombre de la tabla temporal
            name_all_temp[cont]=name_tablaOutput;
            cont++;
            System.out.println("nameTempe"+name_all_temp[cont-1]);
            
        } catch (SQLException e) {
            //nothing
        }
    }
    //---------
    public void carga1(String name_tablaInput, String predicado_aux)
    {
        DefaultTableModel model = (DefaultTableModel) getRegister().getTablaModel();
        model.setRowCount(0);
        output = Conexion.consultaSql("SELECT * FROM proy1."+name_tablaInput+" WHERE "+predicado_aux);
        try{
            while(output.next()){
                Vector vector = new Vector();
                vector.add(output.getString(1));
                vector.add(output.getString(2));
                vector.add(output.getString(3));
                model.addRow(vector);
                getRegister().setTablaModel(model);
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    //-----------
    
    /**
     * Funcion consulta alternativa
     */
    public void consultAlter(String name_tablaInput, String predicado_aux)
    {
        String url = "jdbc:sqlserver://DESKTOP-4P39MH5\\live:1433;databaseName=bdproy1";
        Connection con;
        Statement stmt;
        String query = "SELECT * FROM proy1."+name_tablaInput+" WHERE "+predicado_aux;
        
        try {
          con = DriverManager.getConnection(url,Conexion.getName(), Conexion.getPass());        
          stmt = con.createStatement();              
          ResultSet rs = stmt.executeQuery(query);
          ResultSetMetaData rsmd = rs.getMetaData();
          
          printColTypes(rsmd);
          System.out.println("");
          
          ResultSetMetaData rsmd1 = rs.getMetaData();
          
          int numberOfColumns = rsmd1.getColumnCount();

          //Recorro la tabla y obtengo el nombre de las columnas
          for (int i = 1; i <= numberOfColumns; i++) {
            if (i > 1) System.out.print(",  ");
            String columnName = rsmd1.getColumnName(i);
            System.out.print(columnName);
          }
          System.out.println("");

          //Recorro la tabla y obtengo el valor del atributo
          while (rs.next()) {
            for (int i = 1; i <= numberOfColumns; i++) {
              if (i > 1) System.out.print(",  ");
              String columnValue = rs.getString(i);
              System.out.print(columnValue);
            }
            System.out.println("");  
          }

          //Cierro el programa
          stmt.close();
          con.close();

        } catch(SQLException ex) {
          //Clausula de error
          System.err.print("SQLException: ");
          System.err.println(ex.getMessage());
        }  
    
    }
    /**
     * Funcion 00#
     */
    public void printColTypes(ResultSetMetaData rsmd) throws SQLException 
    {
        int columns = rsmd.getColumnCount();
    
        for (int i = 1; i <= columns; i++) 
        {
            int jdbcType = rsmd.getColumnType(i);
            String name = rsmd.getColumnTypeName(i);
            System.out.print("Column " + i + " is JDBC type " + jdbcType);
            System.out.println(", which the DBMS calls " + name);
        }
    }
    
    /**
     * Función que imprime todas las tablas temporales
     */
    public void printTablaTemps()
    {
        
        for (int i = 0; i <= cont; i++){
            try {
                
                //ResultSet output1;
                //conexx.getConexion();
                output = Conexion.consultaSql("SELECT * FROM proy1.#"+name_all_temp[i]);
                
                ResultSetMetaData metaDatos = output.getMetaData();
                
                
                //DBTablePrinter.printResultSet(output1);
                
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
        for (int i = 0; i <= 5; i++){
            try {
                output = Conexion.consultaSql("SELECT * FROM proy1."+name_all_per[i]);
                
                ResultSetMetaData metaDatos = output.getMetaData();
                int index=metaDatos.getColumnCount();
                
                System.out.println("->"+name_all_per[i]);
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
     * Función que imprime el output en modelo relacional
     * Parametros:
     *  1. nombre tabla input, 2. predicado, 3. nombre tabla output
     */
    public void modeloRelacional(String operator_aux)
    {
       //Verifica si la informacion esta digitada
       if(getRegister().getInput1().isEmpty() || getRegister().getPredicado().isEmpty())
       {
           JOptionPane.showMessageDialog(null, "¡¡¡Inserte la TABLA INPUT 1 y el PREDICADO correctamente!!!","Information",JOptionPane.INFORMATION_MESSAGE);
           getRegister().cleanText();
       } else {
            switch(operator_aux){           
                      
                case "Selección":
                    //Caso cuando no colocá el nombre de la TABLA OUTPUT
                    if(getRegister().getOutput().isEmpty()){
                        getRegister().setAlgebraR("σ "+getRegister().getPredicado()+"("+getRegister().getInput1()+")");
                        getRegister().setSql("SELECT * FROM "+getRegister().getInput1()+" WHERE "+getRegister().getPredicado());
                        JOptionPane.showMessageDialog(null, "No ingreso el nombre de la tabla resultante:\nNo se muestra la tabla graficamente\nNi se crea la tabla temporal");
                    //Caso en que si digita toda la informacion
                    }else{
                        getRegister().setAlgebraR(getRegister().getOutput()+" <- σ "+getRegister().getPredicado()+"("+getRegister().getInput1()+")");
                        getRegister().setSql("SELECT * FROM "+getRegister().getInput1()+" WHERE "+getRegister().getPredicado());
                        cargarTablaSeleccion(getRegister().getInput1(), getRegister().getPredicado(), getRegister().getOutput());
                        //consultAlter(tabla_input1.getText(), predicado.getText());
                        //carga1(tabla_input1.getText(), predicado.getText());
                    }
                    break;
                
                case "Proyección Generalizada":
                    //Caso cuando no colocá el nombre de la TABLA OUTPUT
                    if(getRegister().getOutput().isEmpty()){
                        getRegister().setAlgebraR(getRegister().getOutput()+" <- π "+getRegister().getPredicado()+"("+getRegister().getInput1()+")");
                        getRegister().setSql("SELECT * FROM "+getRegister().getInput1()+" WHERE "+getRegister().getPredicado());
                        JOptionPane.showMessageDialog(null, "No ingreso el nombre de la tabla resultante:\nNo se muestra la tabla graficamente\nNi se crea la tabla temporal");
                    //Caso en que si digita toda la informacion
                    }else{
                        getRegister().setAlgebraR(getRegister().getOutput()+" <- π "+getRegister().getPredicado()+"("+getRegister().getInput1()+")");
                        getRegister().setSql("SELECT * FROM "+getRegister().getInput1()+" WHERE "+getRegister().getPredicado());
                        cargarTablaSeleccion(getRegister().getInput1(), getRegister().getPredicado(), getRegister().getOutput());
                        //consultAlter(tabla_input1.getText(), predicado.getText());
                        //carga1(tabla_input1.getText(), predicado.getText());
                    }
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
            
            
            getRegister().cleanText();
            JOptionPane.showMessageDialog(null, "The date was successfully correctly");

       }
    }
//Fin del programa controlador del interprete de algebra SQL/JAVA
}
