/**
 * Paquete principal
 */
package BaseDeDatos;
/**
 * Librerias a usar
 */
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *-----------------------------------------------------------------------------
 * @author Jeremy Live
 * ----------------------------------------------------------------------------
 */
public class Conexion 
{
    /**
     * Variables globales
     * Establese la conexion entre SQL servers y netBeans
     */
    public static Connection contacto;
    public static String usuario;
    public static String password;
    public static boolean status;
    //Creo conexino a la base de datos
    public static Statement declara;
    public static ResultSet respuesta;
    //Constructor
    public Conexion()
    {
        this.status = false;
    }  
    /**
     * ########################################################################
     * Funciones
     * ########################################################################
     */
    /**
     * Gets and sets
     */
    public static String getName()
    {
        return Conexion.usuario;
    }
    public static String getPass()
    {
        return Conexion.password;
    }
    //public static void setContacto(){        Conexion.contacto = getConexion();}    
    /**
     * Obtengo la conexion del sqlserver con netbeans
     * @return 
     */
     public static void getConexion()
        {
        Conexion.status = false;
        String url = "jdbc:sqlserver://DESKTOP-4P39MH5\\live:1433;databaseName=bdproy1";
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        }catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "No se conecto.."+e.getMessage(),"warnning",JOptionPane.ERROR_MESSAGE);
        }
        
        try{
            Conexion.contacto = DriverManager.getConnection(url,Conexion.usuario, Conexion.password);                                                                      //
            Conexion.status = true;
            //return Conexion.contacto;
        }catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error"+e.getMessage(),"warnning",JOptionPane.ERROR_MESSAGE);
        }
        //return null;
        
    }
    /**
     * Seteo el nuevo usuario y la nueva contraseña a evaluar
     * @param usuario
     * @param password 
     */
    public static void setcuenta(String usuario, String password)
    {
        Conexion.usuario = usuario;
        Conexion.password = password;
    }
    /**
     * Obtengo el estado de la conexion de la BD
     * @return 
     */
    public static boolean getstatus()
    {
        return  status;
    }
    /**
     * Obtengo el resultado del QUERY de SQL SERVER
     * @param consulta
     * @return 
     */
    public static ResultSet consultaSql(String consulta, String name_table)
    {
        //Creo conexino a la base de datos
        try {
            //Obtengo el resultado del a consulta
            declara = contacto.createStatement();
            respuesta = declara.executeQuery(consulta);

            return respuesta;
        } catch (SQLException e) {
            //1-)Primera validación: la tabla debe existir
            JOptionPane.showMessageDialog(null, "ERROR: NO EXISTE LA TABLA -> "+name_table+"\n"+e.getMessage(),"warnning",JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
    
    /**
     * Obtengo la consulta deseada
     * @param sqlQuery
     * @param sqlInsert
     * @param sqlInsertF
     * @return el dato resultset que tiene la vista de la tabla
     */
    public static ResultSet consultaSqlCreate(String sqlQuery, String sqlInsert, String sqlInsertF, String name_table)
    {
        //Creo conexino a la base de datos
        //Connection con = getConexion();
        try {     
            //Obtengo el resultado del a consulta 
            declara = contacto.createStatement();         
            //Creo tabla temporal
            declara.executeUpdate(sqlQuery);
            //Inserto datos en la tabla temporal
            declara.executeUpdate(sqlInsert);
            //Selecciono la tabla temporal
            respuesta = declara.executeQuery(sqlInsertF);
   
            return respuesta;
        } catch (SQLException e) {
            //1-)Primera validación: la tabla debe existir
            JOptionPane.showMessageDialog(null, "ERROR: NO EXISTE LA TABLA -> "+name_table+"\n"+e.getMessage(),"warnning",JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public static ResultSet consultaSql2(String consulta)
    {
        //Creo conexino a la base de datos
        try {
            //Obtengo el resultado del a consulta 
            declara = contacto.createStatement();
            respuesta = declara.executeQuery(consulta);
           
            return respuesta;
        } catch (SQLException e) {
            //1-)Primera validación: la tabla debe existir
            JOptionPane.showMessageDialog(null, "ERROR: NO EXISTE LA TABLA "+e.getMessage(),"warnning",JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
    
    
    /**
     * ########################################################################
     * Procedimientos
     * ########################################################################
     */  
    /**
     * Funcion de insertar en SQL SERVER
     * @param id
     * @param name
     * @param carrer
     * @throws SQLException 
     */
    public static void insertaUser(String id, String name, String carrer) throws SQLException
    {
        CallableStatement input = contacto.prepareCall("{call insertaUser(?,?,?)}");
        input.setString(1, id);
        input.setString(2, name);
        input.setString(3, carrer);
        input.execute();
    }
    /**
     * Funcion de eliminar en SQL SERVER
     * @param id
     * @throws SQLException 
     */
    public static void dropUser(String id) throws SQLException 
    {
        CallableStatement input = contacto.prepareCall("{call dropUser(?)}");
        input.setString(1, id);
        input.execute();
    }
    
    public static void searchUser(String id) throws SQLException
    {
        CallableStatement input = contacto.prepareCall("{call searchUser(?)}");
        input.setString(1, id);
        input.execute();
    }
    

}
