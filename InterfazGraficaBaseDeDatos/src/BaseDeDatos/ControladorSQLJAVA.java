package BaseDeDatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
    private String insertTemp;
    private String selectTemp;
    private String sqlQuery;            //Temp table
    private String sqlQuery2;           //atri type
    private String sqlQuery3;           //atri
    private int index;
    private Vector v;
    private ResultSetMetaData metaDatos;
    private ArrayList<String> atriConcatena1 = new ArrayList<String>();
    private ArrayList<String> atriConcatena2 = new ArrayList<String>();
    private ArrayList<String> atriConcatena3 = new ArrayList<String>();
    private int largo_global = 0;
    
    //Constructor
    public ControladorSQLJAVA(Register regis)
    {
      this.name_all_temp = new String[20];
      this.cont = 0;
      this.register = regis; 
    }
    
    //Gets and Sets
    public void setLargo()
    {
        largo_global++;
    }
    public Register getRegister()
    {
        return this.register;
    }
    //
    public void setTemp(String table, int cont)
    {
        this.name_all_temp[cont] = table;
    }
    //
    public String getTemp(int cont)
    {
        String x = name_all_temp[cont];
        return x;
    }
    //
    public void restartQuerys()
    {
        sqlQuery = "";  //Tabla temporal
        sqlQuery2 = "";                    //atributos con tipo
        sqlQuery3 = "";                     //atributos
        
        sqlQuery = "CREATE TABLE proy1.#";  
        sqlQuery2 = "(";                    
    }
    //
    public String getQuery1()
    {
        return sqlQuery;
    }
    //
    public String getQuery2()
    {
        return sqlQuery2;
    }
    //
    public String getQuery3()
    {
        return sqlQuery3;
    }
    /**
     * Funcion valido que la tabla output no exista en las tablas permanentes
     */
    public boolean isExistsTableOut(String table_out)
    {
        int i,largo;
        i = 0;
        largo = name_all_per.length;
        while(i < largo){
            if(name_all_per[i]==table_out){
                JOptionPane.showMessageDialog(null, "¡¡¡ERROR NO SE PUEDE DEJAR EL RESULTADO EN UNA TABLA PERMANENTE DE LA BASE DE DATOS!!!");
                return true;
            }
            i++;
        }
        return false;
    }
    /**
     * Funcion fijo etiquetas tabla
     */
    public Object[] putEtiq(int largo, ResultSetMetaData meta)
    {
        
        Object[] etiquetas = new Object[largo];
        for (int i = 0; i < largo; i++) {
            try {
                etiquetas[i] = meta.getColumnLabel(i+1);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return etiquetas;
    }
    /**
     * Funcion seleccion
     */
    public void cargarTablaSeleccion(String name_tablaInput, String predicado_aux, String name_tablaOutput)
    {
        
        restartQuerys();    //Obtengo esqueleto de datos a utilizar en los query
        DefaultTableModel modelo = getRegister().getTablaModel();   //Obtengo la tabla de la Base de datos para poder agregarla
        modelo.setRowCount(0);
        
        try{
            output = Conexion.consultaSql("SELECT * FROM proy1."+name_tablaInput+" WHERE "+predicado_aux, name_tablaInput);   //Hago la selecion de la tabla, 1*validacion(si existe table)
            metaDatos = output.getMetaData();   //Obtengo el total de columnas que tiene la tabla
            index = metaDatos.getColumnCount();
                       
            //Recorro la tabla y obtengo el nombre de las columnas
            for (int i = 1; i <= index; i++) {
              if (i == index){
                sqlQuery3 += metaDatos.getColumnName(i);
                sqlQuery2 += metaDatos.getColumnName(i) 
                     + " "
                     + metaDatos.getColumnTypeName(i)
                     + "(" + Integer.toString(metaDatos.getPrecision(i))
                     + ")";
                String columnName = metaDatos.getColumnName(i);
                System.out.print(columnName);
              }else{
                sqlQuery3 += metaDatos.getColumnName(i)+ ", ";
                sqlQuery2 += metaDatos.getColumnName(i) 
                     + " "
                     + metaDatos.getColumnTypeName(i)
                     + "(" + Integer.toString(metaDatos.getPrecision(i))
                     + ")"
                     + ", ";
                String columnName = metaDatos.getColumnName(i);
                System.out.print(columnName);
                System.out.print(",  ");
                
              }
            }
            System.out.println("");
            
            //Recorro la tabla y obtengo el valor del atributo
            while (output.next()) {
              for (int i = 1; i <= index; i++) {
                if (i > 1){
                    System.out.print(",  ");
                }
                
                String columnValue = output.getString(i);
                System.out.print(columnValue);
              }
              System.out.println("");  
            }         
            
            //Datos a utilizar
            sqlQuery += name_tablaOutput + " " + sqlQuery2 + ")";                                                      //Create
            insertTemp = "INSERT INTO proy1.#"+name_tablaOutput + " (" + sqlQuery3 + ") "          //Insert
                    +"SELECT * FROM proy1."+name_tablaInput+" WHERE "+predicado_aux;               //Select
            selectTemp = "SELECT * FROM proy1.#"+name_tablaOutput;
            
            System.out.println(sqlQuery + "\n" + sqlQuery2 + "\n" + sqlQuery3);
            System.out.println("\n" + insertTemp + "\n" + selectTemp);
            
            //-----Creo la tabla temporal eh Inserto datos en la tabla temporal y accedo a ella
            output = Conexion.consultaSqlCreate(sqlQuery, insertTemp, selectTemp, name_tablaInput);          //Lo accede el usuario usproy1
            metaDatos = output.getMetaData();
            index = metaDatos.getColumnCount();      
            
            while(output.next())
            {
                v = new Vector();
                for(int i=1;i<=index;i++)
                {
                    //Extraigo tupla. Imprimo la tabla temporal
                    v.add(output.getString(i));      
                    //System.out.println("----"+output.getString(i)); 
                }
                modelo.addRow(v);
            }
            modelo.setColumnIdentifiers(putEtiq(index, metaDatos));     //Agego etiquetas a la tabla
            getRegister().setTablaModel(modelo);    //Agrego datos a la tabla
            setTemp(name_tablaOutput, cont);        //Aumento contador de nombre de la tabla temporal
            this.cont++;
            
            /*
            System.out.println(sqlQuery + "\n" + sqlQuery2 + "\n" + sqlQuery3);
            System.out.println("\n" + insertTemp + "\n" + selectTemp);
            System.out.println("\n ....."+name_tablaOutput);
            
            System.out.println("nameTempe "+getTemp(cont-1));
            */
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * Funcion generalizacion
     */
    public void cargarTablaGene(String name_tablaInput, String gene_aux, String name_tablaOutput)
    {
        System.out.println(name_tablaInput+"\n"+gene_aux+"\n"+name_tablaOutput);
        restartQuerys();    //Obtengo esqueleto de datos a utilizar en los query
        DefaultTableModel modelo = getRegister().getTablaModel();   //Obtengo la tabla de la Base de datos para poder agregarla
        modelo.setRowCount(0);
        
        try{
            output = Conexion.consultaSql("SELECT "+gene_aux+" FROM proy1."+name_tablaInput, name_tablaInput);   //Hago la selecion de la tabla, 1*validacion(si existe table)
            metaDatos = output.getMetaData();   //Obtengo el total de columnas que tiene la tabla
            index = metaDatos.getColumnCount();
            
            //Recorro la tabla y obtengo el nombre de las columnas
            for (int i = 1; i <= index; i++) {
              if (i == index){
                sqlQuery2 += metaDatos.getColumnName(i) 
                     + " "
                     + metaDatos.getColumnTypeName(i)
                     + "(" + Integer.toString(metaDatos.getPrecision(i))
                     + ")";
              }else{
                sqlQuery2 += metaDatos.getColumnName(i) 
                     + " "
                     + metaDatos.getColumnTypeName(i)
                     + "(" + Integer.toString(metaDatos.getPrecision(i))
                     + ")"
                     + ", ";
              }
            }
           
            //Datos a utilizar
            sqlQuery += name_tablaOutput + " "+ sqlQuery2 + ")";                                                      //Create
            insertTemp = "INSERT INTO proy1.#"+name_tablaOutput + " (" + gene_aux+ ") " 
                    +"SELECT "+gene_aux+" FROM proy1."+name_tablaInput;               //Select
            selectTemp = "SELECT "+gene_aux+" FROM proy1.#"+name_tablaOutput;
         
            System.out.println(sqlQuery + "\n" + sqlQuery2);
            System.out.println("\n" + insertTemp + "\n" + selectTemp);
            
            //-----Creo la tabla temporal eh Inserto datos en la tabla temporal y accedo a ella
            output = Conexion.consultaSqlCreate(sqlQuery, insertTemp, selectTemp, name_tablaInput);          //Lo accede el usuario usproy1
            metaDatos = output.getMetaData();
            index = metaDatos.getColumnCount();      
            
            while(output.next())
            {
                v = new Vector();
                for(int i=1;i<=index;i++)
                {
                    //Extraigo tupla. Imprimo la tabla temporal
                    v.add(output.getString(i));      
                    //System.out.println("----"+output.getString(i)); 
                }
                modelo.addRow(v);
            }
            modelo.setColumnIdentifiers(putEtiq(index, metaDatos));     //Agego etiquetas a la tabla
            getRegister().setTablaModel(modelo);    //Agrego datos a la tabla
            setTemp(name_tablaOutput, cont);        //Aumento contador de nombre de la tabla temporal
            this.cont++;
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }    
 
    /**
     * Funcion UNION
     * valida si tienes dos tablas la misma aridad(total de columnas)
     */
    public int cargarTablaUnion(String name_tablaInput, String name_tablaInput2, String name_tablaOutput)
    {
        restartQuerys();    //Obtengo esqueleto de datos a utilizar en los query
        DefaultTableModel modelo = getRegister().getTablaModel();   //Obtengo la tabla de la Base de datos para poder agregarla
        modelo.setRowCount(0);

        try{
            output = Conexion.consultaSql("SELECT * FROM proy1."+name_tablaInput, name_tablaInput);   //Hago la selecion de la tabla, 1*validacion(si existe table)
            metaDatos = output.getMetaData();   //Obtengo el total de columnas que tiene la tabla
            index = metaDatos.getColumnCount();
            
            ResultSet output2 = Conexion.consultaSql("SELECT * FROM proy1."+name_tablaInput2, name_tablaInput);
            ResultSetMetaData metaDatos2 = output2.getMetaData();
            int index2 = 0;
            index2 = metaDatos2.getColumnCount();
            
            String atri1,atri2;
            if(index == index2)
            {
                JOptionPane.showMessageDialog(null, "La aridad de estas dos tablas coinciden Y son de ARIDAD = "+index);
                
                for(int i=1; i<=index; i++)
                {
                    atri1 = metaDatos.getColumnName(i);
                    atri2 = metaDatos2.getColumnName(i);
                    if(atri1.equals(atri2)){
                        JOptionPane.showMessageDialog(null, "¡¡¡Los dominios de los atributos son iguales!!!");
                    }else{
                        JOptionPane.showMessageDialog(null, "¡¡¡ERROR: DOMINIOS DIFERENTES. EL ATRIBUTO "+atri1+" TIENE DOMINIO "+atri1+" Y EL ATRIBUTO "+atri2+" TIENE DOMINIO "+atri2+"!!!");
                        return 1;
                    }
                }
                
            }else
            {
                JOptionPane.showMessageDialog(null, "¡¡¡ERROR: TABLAS CON DIFERENTE ARIDAD. LA TABLA 1 TIENE ARIDAD "+index+" Y LA TABLA 2 TIENE ARIDAD "+index2+"!!!");
                return 1;
            }
            
            //Recorro la tabla y obtengo el nombre de las columnas
            for (int i = 1; i <= index; i++) {
              if (i == index){
                sqlQuery3 += metaDatos.getColumnName(i);
                sqlQuery2 += metaDatos.getColumnName(i) 
                     + " "
                     + metaDatos.getColumnTypeName(i)
                     + "(" + Integer.toString(metaDatos.getPrecision(i))
                     + ")";
              }else{
                sqlQuery3 += metaDatos.getColumnName(i)+ ", ";
                sqlQuery2 += metaDatos.getColumnName(i) 
                     + " "
                     + metaDatos.getColumnTypeName(i)
                     + "(" + Integer.toString(metaDatos.getPrecision(i))
                     + ")"
                     + ", ";   
              }
            }
            //Datos a utilizar
            sqlQuery += name_tablaOutput + " " + sqlQuery2 + ")";                                                      //Create
            insertTemp = "INSERT INTO proy1.#"+name_tablaOutput + " (" + sqlQuery3 + ") "        
                    +"SELECT * FROM proy1."+name_tablaInput+" UNION "+"SELECT * FROM proy1."+name_tablaInput2;         //Select
            selectTemp = "SELECT * FROM proy1.#"+name_tablaOutput;
            System.out.println("\n"+insertTemp+"\n"+sqlQuery3);
            //-----Creo la tabla temporal eh Inserto datos en la tabla temporal y accedo a ella
            output = Conexion.consultaSqlCreate(sqlQuery, insertTemp, selectTemp, name_tablaInput);          //Lo accede el usuario usproy1
            metaDatos = output.getMetaData();
            index = metaDatos.getColumnCount();      
            
            while(output.next())
            {
                v = new Vector();
                for(int i=1;i<=index;i++)
                {
                    //Extraigo tupla. Imprimo la tabla temporal
                    v.add(output.getString(i));      
                    //System.out.println("----"+output.getString(i)); 
                }
                modelo.addRow(v);
            }
            modelo.setColumnIdentifiers(putEtiq(index, metaDatos));     //Agego etiquetas a la tabla
            getRegister().setTablaModel(modelo);    //Agrego datos a la tabla
            setTemp(name_tablaOutput, cont);        //Aumento contador de nombre de la tabla temporal
            this.cont++;
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
    /**
     * Funcion diferencia de conjuntos
     */
    public int cargarTablaDif(String name_tablaInput, String name_tablaInput2, String name_tablaOutput)
    {
        restartQuerys();    //Obtengo esqueleto de datos a utilizar en los query
        DefaultTableModel modelo = getRegister().getTablaModel();   //Obtengo la tabla de la Base de datos para poder agregarla
        modelo.setRowCount(0);

        try{
            output = Conexion.consultaSql("SELECT * FROM proy1."+name_tablaInput, name_tablaInput);   //Hago la selecion de la tabla, 1*validacion(si existe table)
            metaDatos = output.getMetaData();   //Obtengo el total de columnas que tiene la tabla
            index = metaDatos.getColumnCount();
            
            ResultSet output2 = Conexion.consultaSql("SELECT * FROM proy1."+name_tablaInput2, name_tablaInput);
            ResultSetMetaData metaDatos2 = output2.getMetaData();
            int index2 = 0;
            index2 = metaDatos2.getColumnCount();
            
            if(index == index2)
            {
                JOptionPane.showMessageDialog(null, "La aridad de estas dos tablas coinciden Y son de ARIDAD = "+index);
            }else
            {
                JOptionPane.showMessageDialog(null, "¡¡¡ERROR: TABLAS CON DIFERENTE ARIDAD. LA TABLA 1 TIENE ARIDAD "+index+" Y LA TABLA 2 TIENE ARIDAD "+index2+"!!!");
                return 1;
            }
            
            //-----
            output = Conexion.consultaSql("SELECT * FROM proy1."+name_tablaInput, name_tablaInput);   //Hago la selecion de la tabla, 1*validacion(si existe table)
            metaDatos = output.getMetaData();   //Obtengo el total de columnas que tiene la tabla
            index = metaDatos.getColumnCount();
            
            //Recorro la tabla y obtengo el nombre de las columnas
            for (int i = 1; i <= index; i++) {
              if (i == index){
                sqlQuery3 += metaDatos.getColumnName(i);
                sqlQuery2 += metaDatos.getColumnName(i) 
                     + " "
                     + metaDatos.getColumnTypeName(i)
                     + "(" + Integer.toString(metaDatos.getPrecision(i))
                     + ")";
              }else{
                sqlQuery3 += metaDatos.getColumnName(i)+ ", ";
                sqlQuery2 += metaDatos.getColumnName(i) 
                     + " "
                     + metaDatos.getColumnTypeName(i)
                     + "(" + Integer.toString(metaDatos.getPrecision(i))
                     + ")"
                     + ", ";   
              }
            }
            //Obtengo la llave principal
            String primary_key=obtengoKey(sqlQuery3);
            //Datos a utilizar
            sqlQuery += name_tablaOutput + " " + sqlQuery2 + ")";                 //Create
            insertTemp = "INSERT INTO proy1.#"+name_tablaOutput + " (" + sqlQuery3 + ") "          
                    +"SELECT * FROM proy1."+name_tablaInput+" WHERE NOT EXISTS "+"(SELECT * FROM proy1."+name_tablaInput2
                    +" WHERE proy1."+name_tablaInput+"."+primary_key+" = proy1."+name_tablaInput2+"."+primary_key
                    +")";                                                         //Select
            selectTemp = "SELECT * FROM proy1.#"+name_tablaOutput;
            System.out.println("\n"+insertTemp+"\n"+sqlQuery3);
            //-----Creo la tabla temporal eh Inserto datos en la tabla temporal y accedo a ella
            output = Conexion.consultaSqlCreate(sqlQuery, insertTemp, selectTemp, name_tablaInput);          //Lo accede el usuario usproy1
            metaDatos = output.getMetaData();
            index = metaDatos.getColumnCount();      
            
            while(output.next())
            {
                v = new Vector();
                for(int i=1;i<=index;i++)
                {
                    //Extraigo tupla. Imprimo la tabla temporal
                    v.add(output.getString(i));      
                    //System.out.println("----"+output.getString(i)); 
                }
                modelo.addRow(v);
            }
            modelo.setColumnIdentifiers(putEtiq(index, metaDatos));     //Agego etiquetas a la tabla
            getRegister().setTablaModel(modelo);    //Agrego datos a la tabla
            setTemp(name_tablaOutput, cont);        //Aumento contador de nombre de la tabla temporal
            this.cont++;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
    public String obtengoKey(String sqlQue)
    {
        String primary_key9="";
        for (int x=0;x<sqlQue.length();x++){
            if(sqlQue.charAt(x) == ','){
                return primary_key9;
            }else{
                primary_key9 += sqlQuery3.charAt(x);
            }
        }
        return "";
    }
    /**
     * producto carteciano
     */
    public void cargarTablaProCar(String name_tablaInput, String name_tablaInput2, String name_tablaOutput)
    {
        restartQuerys();    //Obtengo esqueleto de datos a utilizar en los query
        DefaultTableModel modelo = getRegister().getTablaModel();   //Obtengo la tabla de la Base de datos para poder agregarla
        modelo.setRowCount(0);
        
        try{
            output = Conexion.consultaSql("SELECT * FROM proy1."+name_tablaInput+", proy1."+name_tablaInput2, name_tablaInput);   //Hago la selecion de la tabla, 1*validacion(si existe table)
            metaDatos = output.getMetaData();   //Obtengo el total de columnas que tiene la tabla
            index = metaDatos.getColumnCount();
                       
            //Recorro la tabla y obtengo el nombre de las columnas
            for (int i = 1; i <= index; i++) {
              if (i == index){
                sqlQuery3 += metaDatos.getColumnName(i);
                sqlQuery2 += metaDatos.getColumnName(i) 
                     + " "
                     + metaDatos.getColumnTypeName(i)
                     + "(" + Integer.toString(metaDatos.getPrecision(i))
                     + ")";
              }else{
                sqlQuery3 += metaDatos.getColumnName(i)+ ", ";
                sqlQuery2 += metaDatos.getColumnName(i) 
                     + " "
                     + metaDatos.getColumnTypeName(i)
                     + "(" + Integer.toString(metaDatos.getPrecision(i))
                     + ")"
                     + ", ";    
              }
            }
            
            //Datos a utilizar
            sqlQuery += name_tablaOutput + " " + sqlQuery2 + ")";                               //Create
            insertTemp = "INSERT INTO proy1.#"+name_tablaOutput + " (" + sqlQuery3 + ") "       //Insert
                    +"SELECT * FROM proy1."+name_tablaInput+", proy1."+name_tablaInput2;              //Select
            selectTemp = "SELECT * FROM proy1.#"+name_tablaOutput;
            
            System.out.println(sqlQuery + "\n" + sqlQuery2 + "\n" + sqlQuery3);
            System.out.println("\n" + insertTemp + "\n" + selectTemp);
            
            //-----Creo la tabla temporal eh Inserto datos en la tabla temporal y accedo a ella
            output = Conexion.consultaSqlCreate(sqlQuery, insertTemp, selectTemp, name_tablaInput);          //Lo accede el usuario usproy1
            metaDatos = output.getMetaData();
            index = metaDatos.getColumnCount();      
            
            while(output.next())
            {
                v = new Vector();
                for(int i=1;i<=index;i++)
                {
                    //Extraigo tupla. Imprimo la tabla temporal
                    v.add(output.getString(i));      
                    //System.out.println("----"+output.getString(i)); 
                }
                modelo.addRow(v);
            }
            modelo.setColumnIdentifiers(putEtiq(index, metaDatos));     //Agego etiquetas a la tabla
            getRegister().setTablaModel(modelo);    //Agrego datos a la tabla
            setTemp(name_tablaOutput, cont);        //Aumento contador de nombre de la tabla temporal
            this.cont++;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * interseccion
     */
    public int cargarTablaInter(String name_tablaInput, String name_tablaInput2, String name_tablaOutput)
    {
        restartQuerys();    //Obtengo esqueleto de datos a utilizar en los query
        DefaultTableModel modelo = getRegister().getTablaModel();   //Obtengo la tabla de la Base de datos para poder agregarla
        modelo.setRowCount(0);
        
        try{
            output = Conexion.consultaSql("SELECT * FROM proy1."+name_tablaInput, name_tablaInput);   //Hago la selecion de la tabla, 1*validacion(si existe table)
            metaDatos = output.getMetaData();   //Obtengo el total de columnas que tiene la tabla
            index = metaDatos.getColumnCount();
             
            
            ResultSet output2 = Conexion.consultaSql("SELECT * FROM proy1."+name_tablaInput2, name_tablaInput2);
            ResultSetMetaData metaDatos2 = output2.getMetaData();
            int index2 = 0;
            index2 = metaDatos2.getColumnCount();
            
            String atri1,atri2;
            if(index == index2)
            {
                JOptionPane.showMessageDialog(null, "La aridad de estas dos tablas coinciden Y son de ARIDAD = "+index);
                
                for(int i=1; i<=index; i++)
                {
                    atri1 = metaDatos.getColumnName(i);
                    atri2 = metaDatos2.getColumnName(i);
                    if(atri1.equals(atri2)){
                        JOptionPane.showMessageDialog(null, "¡¡¡Los dominios de los atributos son iguales!!!");
                    }else{
                        JOptionPane.showMessageDialog(null, "¡¡¡ERROR: DOMINIOS DIFERENTES. EL ATRIBUTO "+atri1+" TIENE DOMINIO "+atri1+" Y EL ATRIBUTO "+atri2+" TIENE DOMINIO "+atri2+"!!!");
                        return 1;
                    }
                }
            }else
            {
                JOptionPane.showMessageDialog(null, "¡¡¡ERROR: TABLAS CON DIFERENTE ARIDAD. LA TABLA 1 TIENE ARIDAD "+index+" Y LA TABLA 2 TIENE ARIDAD "+index2+"!!!");
                return 1;
            }
            
            //Recorro la tabla y obtengo el nombre de las columnas
            for (int i = 1; i <= index; i++) {
              if (i == index){
                sqlQuery3 += metaDatos.getColumnName(i);
                sqlQuery2 += metaDatos.getColumnName(i) 
                     + " "
                     + metaDatos.getColumnTypeName(i)
                     + "(" + Integer.toString(metaDatos.getPrecision(i))
                     + ")";
              }else{
                sqlQuery3 += metaDatos.getColumnName(i)+ ", ";
                sqlQuery2 += metaDatos.getColumnName(i) 
                     + " "
                     + metaDatos.getColumnTypeName(i)
                     + "(" + Integer.toString(metaDatos.getPrecision(i))
                     + ")"
                     + ", ";   
              }
            }      
            
            //Datos a utilizar
            sqlQuery += name_tablaOutput + " " + sqlQuery2 + ")";                                        //Create
            insertTemp = "INSERT INTO proy1.#"+name_tablaOutput + " (" + sqlQuery3 + ") "                //Insert
                    +"SELECT * FROM proy1."+name_tablaInput+" INTERSECT SELECT * FROM proy1."+name_tablaInput2;//Select
            selectTemp = "SELECT * FROM proy1.#"+name_tablaOutput;
            
            System.out.println(sqlQuery + "\n" + sqlQuery2 + "\n" + sqlQuery3);
            System.out.println("\n" + insertTemp + "\n" + selectTemp);
            
            //-----Creo la tabla temporal eh Inserto datos en la tabla temporal y accedo a ella
            output = Conexion.consultaSqlCreate(sqlQuery, insertTemp, selectTemp, name_tablaInput);          //Lo accede el usuario usproy1
            metaDatos = output.getMetaData();
            index = metaDatos.getColumnCount();      
            
            while(output.next())
            {
                v = new Vector();
                for(int i=1;i<=index;i++)
                {
                    //Extraigo tupla. Imprimo la tabla temporal
                    v.add(output.getString(i));      
                    //System.out.println("----"+output.getString(i)); 
                }
                modelo.addRow(v);
            }
            modelo.setColumnIdentifiers(putEtiq(index, metaDatos));     //Agego etiquetas a la tabla
            getRegister().setTablaModel(modelo);    //Agrego datos a la tabla
            setTemp(name_tablaOutput, cont);        //Aumento contador de nombre de la tabla temporal
            this.cont++;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
    /**
     * division
     */
    public int cargarTablaDiv(String name_tablaInput, String name_tablaInput2, String name_tablaOutput)
    {
        restartQuerys();    //Obtengo esqueleto de datos a utilizar en los query
        DefaultTableModel modelo = getRegister().getTablaModel();   //Obtengo la tabla de la Base de datos para poder agregarla
        modelo.setRowCount(0);
        
        try{
            output = Conexion.consultaSql("SELECT * FROM proy1."+name_tablaInput, name_tablaInput);   //Hago la selecion de la tabla, 1*validacion(si existe table)
            metaDatos = output.getMetaData();   //Obtengo el total de columnas que tiene la tabla
            index = metaDatos.getColumnCount();
             
            
            ResultSet output2 = Conexion.consultaSql("SELECT * FROM proy1."+name_tablaInput2, name_tablaInput2);
            ResultSetMetaData metaDatos2 = output2.getMetaData();
            int index2 = 0;
            index2 = metaDatos2.getColumnCount();
            
            if(index == index2)
            {
                JOptionPane.showMessageDialog(null, "La aridad de estas dos tablas coinciden Y son de ARIDAD = "+index);
            }else
            {
                JOptionPane.showMessageDialog(null, "¡¡¡ERROR: TABLAS CON DIFERENTE ARIDAD. LA TABLA 1 TIENE ARIDAD "+index+" Y LA TABLA 2 TIENE ARIDAD "+index2+"!!!");
                return 1;
            }
            
            String sqlQuery4 = "";
            //Recorro la tabla y obtengo el nombre de las columnas
            for (int i = 1; i <= index; i++) {
              if (i == index){
                sqlQuery4 += metaDatos2.getColumnName(i);
                sqlQuery3 += metaDatos.getColumnName(i);
                sqlQuery2 += metaDatos.getColumnName(i) 
                     + " "
                     + metaDatos.getColumnTypeName(i)
                     + "(" + Integer.toString(metaDatos.getPrecision(i))
                     + ")";
              }else{
                sqlQuery4 += metaDatos2.getColumnName(i)+ ", ";
                sqlQuery3 += metaDatos.getColumnName(i)+ ", ";
                sqlQuery2 += metaDatos.getColumnName(i) 
                     + " "
                     + metaDatos.getColumnTypeName(i)
                     + "(" + Integer.toString(metaDatos.getPrecision(i))
                     + ")"
                     + ", ";  
              }
            }
            //Obtengo la llave principal y
            //Valido que los atributos de tabla2 esten todos en tabla1
            String primary_key0="", primary_key="", primary_key2="";
            int x=0;
            int status=0;
            for (x=0;x<sqlQuery.length();x++){
                if(sqlQuery3.charAt(x) == ',' || sqlQuery4.charAt(x) == ','){
                    //x = sqlQuery.length();
                    status = 1;
                    primary_key = "";
                    primary_key2 = "";
                }else{
                    if(status != 1){
                        primary_key0 += sqlQuery3.charAt(x);   
                    }
                    primary_key += sqlQuery3.charAt(x);
                    primary_key2 += sqlQuery4.charAt(x);
                    if(!(primary_key.equals(primary_key2))){
                        JOptionPane.showMessageDialog(null, "¡¡¡ERROR: EL ATRIBUTO “"+primary_key+"” DE LA TABLA "+name_tablaInput+" NO ESTA EN LA TABLA "+name_tablaInput2+"!!!");
                        return 1;
                    }
                }
            }
            //
            String atributos=convertToAtri(sqlQuery3, name_tablaInput, index); 
            System.out.println("------ "+atributos);
            //Datos a utilizar
            sqlQuery += name_tablaOutput + " " + sqlQuery2 + ")";                                        //Create
            insertTemp = "INSERT INTO proy1.#"+name_tablaOutput + " (" + sqlQuery3 + ") "                //Insert
                    +"SELECT DISTINCT "+atributos+" FROM proy1."+name_tablaInput+" LEFT JOIN "+" proy1."+name_tablaInput2
                    +" ON proy1."+name_tablaInput+"."+primary_key0+" = proy1."+name_tablaInput2+"."+primary_key0
                    +" WHERE proy1."+name_tablaInput+"."+primary_key0+" IS NOT NULL";                                 //Select
            selectTemp = "SELECT * FROM proy1.#"+name_tablaOutput;
            
            System.out.println(sqlQuery + "\n" + sqlQuery2 + "\n" + sqlQuery3);
            System.out.println("\n" + insertTemp + "\n" + selectTemp);
            
            //-----Creo la tabla temporal eh Inserto datos en la tabla temporal y accedo a ella
            output = Conexion.consultaSqlCreate(sqlQuery, insertTemp, selectTemp, name_tablaInput);          //Lo accede el usuario usproy1
            metaDatos = output.getMetaData();
            index = metaDatos.getColumnCount();      
            
            while(output.next())
            {
                v = new Vector();
                for(int i=1;i<=index;i++)
                {
                    //Extraigo tupla. Imprimo la tabla temporal
                    v.add(output.getString(i));      
                    //System.out.println("----"+output.getString(i)); 
                }
                modelo.addRow(v);
            }
            modelo.setColumnIdentifiers(putEtiq(index, metaDatos));     //Agego etiquetas a la tabla
            getRegister().setTablaModel(modelo);    //Agrego datos a la tabla
            setTemp(name_tablaOutput, cont);        //Aumento contador de nombre de la tabla temporal
            this.cont++;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
    /**
     * Renombrar atributos
     */
    public int cargarTablaRenomR(String name_tablaInput, String name_atributos, String name_tablaOutput)
    {
        restartQuerys();    //Obtengo esqueleto de datos a utilizar en los query
        DefaultTableModel modelo = getRegister().getTablaModel();   //Obtengo la tabla de la Base de datos para poder agregarla
        modelo.setRowCount(0);
        
        try{
            output = Conexion.consultaSql("SELECT * FROM proy1."+name_tablaInput, name_tablaInput);   //Hago la selecion de la tabla, 1*validacion(si existe table)
            metaDatos = output.getMetaData();   //Obtengo el total de columnas que tiene la tabla
            index = metaDatos.getColumnCount();
            
            //Recorro la tabla y obtengo el nombre de las columnas
            for (int i = 1; i <= index; i++) {
              if (i == index){
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
           
            //Datos a utilizar
            sqlQuery += name_tablaOutput + " "+ sqlQuery2 + ")";                                                      //Create
            insertTemp = "INSERT INTO proy1.#"+name_tablaOutput + " (" + sqlQuery3 + ") " 
                    +"SELECT * FROM proy1."+name_tablaInput;               //Select
            selectTemp = "SELECT * FROM proy1.#"+name_tablaOutput;
         
            System.out.println(sqlQuery + "\n" + sqlQuery2);
            System.out.println("\n" + insertTemp + "\n" + selectTemp);
            
            //Creo nombre de atributos de la nueva tabla
            String[] nuevos_atri = {};
            Object[] nuevos_atriN222 = nuevos_atri;
            Object[] nuevos_atriN = new Object[index];
            int largoAtriN = 0;
            boolean status = false;
            //obtengo nombrees de los nuevos atrbutos
            if(index == 1){
                status = true;
            }else{
                nuevos_atri = name_atributos.split(", ");
            }
            largoAtriN = nuevos_atri.length;
            System.out.println("\nlargoNUEVOSATRI "+largoAtriN);
            
            if(largoAtriN == index){
                JOptionPane.showMessageDialog(null, "Felicidades: los atributos tienen la misma aridad");
                
                //Los agrego al arraylist
                if(status == true){
                    nuevos_atriN[0] = name_atributos;
                }else{
                    //entra si tienen misma aridad
                    for (int i = 0; i < index; i++) {
                        nuevos_atriN[i] = nuevos_atri[i];
                    }
                }
                
            }else{
                JOptionPane.showMessageDialog(null, "Error: atributos con distinta aridad");
            }

            //-----Creo la tabla temporal eh Inserto datos en la tabla temporal y accedo a ella
            output = Conexion.consultaSqlCreate(sqlQuery, insertTemp, selectTemp, name_tablaInput);          //Lo accede el usuario usproy1
            metaDatos = output.getMetaData();
            index = metaDatos.getColumnCount();      
            
            while(output.next())
            {
                v = new Vector();
                for(int i=1;i<=index;i++)
                {
                    //Extraigo tupla. Imprimo la tabla temporal
                    v.add(output.getString(i));      
                    //System.out.println("----"+output.getString(i)); 
                }
                modelo.addRow(v);
            }
            
            
            modelo.setColumnIdentifiers(nuevos_atriN);     //Agego etiquetas a la tabla
            
            
            getRegister().setTablaModel(modelo);    //Agrego datos a la tabla
            setTemp(name_tablaOutput, cont);        //Aumento contador de nombre de la tabla temporal
            this.cont++;
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
    //
    public String getAllAtri(ArrayList<String> atriCo)
    {
        String result = "";
        int largo = atriCo.size();
        for (int i = 1; i <= largo; i++) {
                if(i == largo){
                     result += atriCo.get(i-1);
                }else{
                     result += atriCo.get(i-1)
                            + ", ";   
                }
        }     
        
        return result;
    }
    //
    public void setAtriConca(String atributo)
    {
        int largo = 0;
        int status = 0;
        ArrayList<String> atri2 = atriConcatena1;
        largo = atriConcatena1.size();

        if(largo == 0){
            atriConcatena1.add(atributo);
        }else{
            for (int i = 0; i < largo; i++) {
                if(atri2.get(i).equals(atributo)){
                    status = 1;
                }
            }   
            if(status == 0){
                atriConcatena1.add(atributo);
            }
        }

    }
    //
    public void getAllTable(ResultSetMetaData rTabla0, ResultSetMetaData rTabla)
    {
        try{
            String atri_comparo = "";
            String tabla0 = "";
            String result = "";
            boolean boleano = false;
            index = rTabla.getColumnCount();
            int index0 = rTabla0.getColumnCount();
            System.out.println("index0 y 1"+index0+" "+index);
            for (int i = 1; i <= index0; i++) {
               String atri0 = rTabla0.getColumnName(i);
               atriConcatena2.add(atri0);
            }
            //Recorro la tabla y obtengo el nombre de las columnas TABLA2
            for (int i = 1; i <= index; i++) {
                String atri = rTabla.getColumnName(i);
                if(atriConcatena2.contains(atri)){      //comparo atri1 con atri2
                    atriConcatena3.add(atri);
                    //setLargo();
                    if(index==i){
                        result += atri;                  
                    }else {
                        result += atri + ", ";
                    }
                }
            }
            //return result;    
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        //return "";
    }
    //
    public String getAtri2()
    {
        String result = "";
        int largo = atriConcatena2.size();
        for (int i = 0; i < largo; i++) { 
            if(i == largo-1){
                result += atriConcatena2.get(i);
            }else{
                result += atriConcatena2.get(i)+", ";
            }
                         
        }
        if(largo == 0){
             result += atriConcatena3.get(0) + ", ";
        }
        
        return result;
    }
    //
    public void comparaString(){
        int largo1 = atriConcatena1.size();
        int largo2 = atriConcatena3.size();
        atriConcatena2.clear();
        boolean status = false;
        //String result ="";
        //System.out.println("largo1: "+largo1+"  largo2: "+largo2);
        for (int i = 0; i < largo1; i++) {
            for (int j = 0; j < largo2; j++) {
                //System.out.println(atriConcatena1.get(i) + "\n- " +  atriConcatena3.get(j));
                if(atriConcatena1.get(i).equals(atriConcatena3.get(j))){
                    status = true;
                    //result += atriConcatena1.get(i);
                }
            }
            //
            if(status == true){
                atriConcatena2.add(atriConcatena1.get(i));
                status = false;
            }
        }
        //return result;
    }
    /**
     * concatenacion join
     */
    public void cargarTablaConcaJoin(String name_tablaInput, String name_tablaInput2, String predicado, String name_tablaOutput)
    {
        String all_atri = "";
        String atributos99="";
        String all_atri_tabla2 = "";
        int largo_columnas = 0;
        restartQuerys();    //Obtengo esqueleto de datos a utilizar en los query
        DefaultTableModel modelo = getRegister().getTablaModel();   //Obtengo la tabla de la Base de datos para poder agregarla
        modelo.setRowCount(0);
        try{
            //Recorro la tabla y obtengo el nombre de las columnas
            output = Conexion.consultaSql("SELECT *  FROM proy1."+name_tablaInput+", proy1."+name_tablaInput2+" WHERE "+predicado, name_tablaInput);   //Hago la selecion de la tabla, 1*validacion(si existe table)
            metaDatos = output.getMetaData();   //Obtengo el total de columnas que tiene la tabla
            index = metaDatos.getColumnCount();
            for (int i = 1; i <= index; i++) {
                setAtriConca(metaDatos.getColumnName(i));
            }
            //Inserto datos bien...
            output = Conexion.consultaSql("SELECT *  FROM proy1."+name_tablaInput2, name_tablaInput2);   //Hago la selecion de la tabla, 1*validacion(si existe table)
            ResultSet output2 = Conexion.consultaSql("SELECT *  FROM proy1."+name_tablaInput, name_tablaInput);   //Hago la selecion de la tabla, 1*validacion(si existe table)         
            ResultSetMetaData metaDatos99 = output.getMetaData();
            ResultSetMetaData metaDatos992 = output2.getMetaData();
            //Obtengo todos los atributos del nuevo JOIN
            //all_atri = getAllAtri();
            //obtengo atributos que estan en las dos tablas a hacer JOIN
            getAllTable(metaDatos992, metaDatos99);
            String prueba=getAtri2();
            System.out.println("\n tabla 1: "+prueba);
            //          
            String comparo="";
            comparaString();
            comparo = getAtri2();
            //Largo del all_Atri_tabla2
            largo_columnas = atriConcatena2.size();
            System.out.println("\n.."+comparo+"\n largo.."+largo_columnas);
            //Convierto el string a la syntaxis de SQL
            atributos99 = convertToAtri(comparo, name_tablaInput, largo_columnas);          
            //Obtengo tipos de los atributos para que calse con SQL
            sqlQuery2 = getTypeAtri(metaDatos, comparo);
            //Datos a utilizar
            sqlQuery += name_tablaOutput + " (" + sqlQuery2+")";                               //Create
            insertTemp = "INSERT INTO proy1.#"+name_tablaOutput + " (" +comparo+ ") "       //Insert
                    +"SELECT "+atributos99+" FROM proy1."+name_tablaInput+" JOIN proy1."+name_tablaInput2+" ON "+predicado;              //Select
            selectTemp = "SELECT * FROM proy1.#"+name_tablaOutput;
           
            
            System.out.println("\n comparo: "+comparo + "\n largo_colum: " + largo_columnas);            
            System.out.println("\n sqlQuery: "+sqlQuery + "\n sqlQuery2: " + sqlQuery2 );
            System.out.println("\n insertTemp: " + insertTemp + "\n selectTemp: " + selectTemp);
           
            //-----Creo la tabla temporal eh Inserto datos en la tabla temporal y accedo a ella
            output = Conexion.consultaSqlCreate(sqlQuery, insertTemp, selectTemp, name_tablaInput);          //Lo accede el usuario usproy1
            metaDatos = output.getMetaData();
            index = metaDatos.getColumnCount();      
            
            while(output.next())
            {
                v = new Vector();
                for(int i=1;i<=index;i++)
                {
                    //Extraigo tupla. Imprimo la tabla temporal
                    v.add(output.getString(i));      
                    //System.out.println("----"+output.getString(i)); 
                }
                modelo.addRow(v);
            }
            modelo.setColumnIdentifiers(putEtiq(index, metaDatos));     //Agego etiquetas a la tabla
            getRegister().setTablaModel(modelo);    //Agrego datos a la tabla
            setTemp(name_tablaOutput, cont);        //Aumento contador de nombre de la tabla temporal
            this.cont++;
            atriConcatena1.clear();
            atriConcatena2.clear();
            atriConcatena3.clear();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (NullPointerException x) {
            System.out.println(x.getMessage());
        }

    }
    /**
     * natural join
     */
    public int cargarTablaNaturalJoin(String name_tablaInput, String name_tablaInput2, String name_tablaOutput)
    {
        String all_atri = "";
        String atributos99="";
        String all_atri_tabla2 = "";
        int largo_columnas = 0;
        restartQuerys();    //Obtengo esqueleto de datos a utilizar en los query
        DefaultTableModel modelo = getRegister().getTablaModel();   //Obtengo la tabla de la Base de datos para poder agregarla
        modelo.setRowCount(0);
        try{
            //Recorro la tabla y obtengo el nombre de las columnas
            output = Conexion.consultaSql("SELECT *  FROM proy1."+name_tablaInput+", proy1."+name_tablaInput2, name_tablaInput);   //Hago la selecion de la tabla, 1*validacion(si existe table)
            metaDatos = output.getMetaData();   //Obtengo el total de columnas que tiene la tabla
            index = metaDatos.getColumnCount();
            for (int i = 1; i <= index; i++) {
                setAtriConca(metaDatos.getColumnName(i));
            }
            //Inserto datos bien...
            output = Conexion.consultaSql("SELECT *  FROM proy1."+name_tablaInput2, name_tablaInput2);   //Hago la selecion de la tabla, 1*validacion(si existe table)
            ResultSet output2 = Conexion.consultaSql("SELECT *  FROM proy1."+name_tablaInput, name_tablaInput);   //Hago la selecion de la tabla, 1*validacion(si existe table)         
            ResultSetMetaData metaDatos99 = output.getMetaData();
            ResultSetMetaData metaDatos992 = output2.getMetaData();
            
            String atri1,atri2;
            int index2 = 0;
            int contadorAlmenosDos=0;
            index = metaDatos992.getColumnCount();
            index2 = metaDatos99.getColumnCount();            
            
            for(int i=1; i<=index; i++)
            {
                atri1 = metaDatos992.getColumnName(i);
                atri2 = metaDatos99.getColumnName(i);
                int isExists=getIfExists(atri1, metaDatos99);
                if(isExists == 1 && contadorAlmenosDos < 2){
                    JOptionPane.showMessageDialog(null, "¡¡¡Los dominios de los atributos son iguales!!!");
                    contadorAlmenosDos++;
                }else if(contadorAlmenosDos < 2){
                    JOptionPane.showMessageDialog(null, "¡¡¡ERROR: NO HAY ATRIBUTOS COMUNES!!!");
                    return 1;
                }
            }
            if(contadorAlmenosDos < 2){
                JOptionPane.showMessageDialog(null, "¡¡¡ERROR: NO HAY ATRIBUTOS COMUNES!!!");
                return 1;
            }
           
            //Obtengo todos los atributos del nuevo JOIN
            //all_atri = getAllAtri();
            //obtengo atributos que estan en las dos tablas a hacer JOIN
            getAllTable(metaDatos992, metaDatos99);
            String prueba=getAtri2();
            System.out.println("\n tabla 1: "+prueba);
            //          
            String comparo="";
            comparaString();
            comparo = getAtri2();
            //Largo del all_Atri_tabla2
            largo_columnas = atriConcatena2.size();
            System.out.println("\n.."+comparo+"\n largo.."+largo_columnas);
            //Convierto el string a la syntaxis de SQL
            atributos99 = convertToAtri(comparo, name_tablaInput, largo_columnas);   
            //Obtengo predicado
            String foreKey = "";
            String[] key = {};
            if(largo_columnas == 1){
                foreKey = comparo;
            }else{
                key = comparo.split(", ");               
            }
            String predicado  = "proy1."+name_tablaInput+"."+key[0]+" = "+"proy1."+name_tablaInput2+"."+key[0];
            System.out.println("\n predicado"+predicado+"\n");
            //Obtengo tipos de los atributos para que calse con SQL
            sqlQuery2 = getTypeAtri(metaDatos, comparo);
            //Datos a utilizar
            sqlQuery += name_tablaOutput + " (" + sqlQuery2+")";                               //Create
            insertTemp = "INSERT INTO proy1.#"+name_tablaOutput + " (" +comparo+ ") "       //Insert
                    +"SELECT "+atributos99+" FROM proy1."+name_tablaInput+" JOIN proy1."+name_tablaInput2+" ON "+predicado;              //Select
            selectTemp = "SELECT * FROM proy1.#"+name_tablaOutput;
           
            System.out.println("\n comparo: "+comparo + "\n largo_colum: " + largo_columnas);            
            System.out.println("\n sqlQuery: "+sqlQuery + "\n sqlQuery2: " + sqlQuery2 );
            System.out.println("\n insertTemp: " + insertTemp + "\n selectTemp: " + selectTemp);
           
            //-----Creo la tabla temporal eh Inserto datos en la tabla temporal y accedo a ella
            output = Conexion.consultaSqlCreate(sqlQuery, insertTemp, selectTemp, name_tablaInput);          //Lo accede el usuario usproy1
            metaDatos = output.getMetaData();
            index = metaDatos.getColumnCount();      
            
            while(output.next())
            {
                v = new Vector();
                for(int i=1;i<=index;i++)
                {
                    //Extraigo tupla. Imprimo la tabla temporal
                    v.add(output.getString(i));      
                    //System.out.println("----"+output.getString(i)); 
                }
                modelo.addRow(v);
            }
            modelo.setColumnIdentifiers(putEtiq(index, metaDatos));     //Agego etiquetas a la tabla
            getRegister().setTablaModel(modelo);    //Agrego datos a la tabla
            setTemp(name_tablaOutput, cont);        //Aumento contador de nombre de la tabla temporal
            this.cont++;
            atriConcatena1.clear();
            atriConcatena2.clear();
            atriConcatena3.clear();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (NullPointerException x) {
            System.out.println(x.getMessage());
        }



/*
        restartQuerys();    //Obtengo esqueleto de datos a utilizar en los query
        DefaultTableModel modelo = getRegister().getTablaModel();   //Obtengo la tabla de la Base de datos para poder agregarla
        modelo.setRowCount(0);
        
        try{
            output = Conexion.consultaSql("SELECT * FROM proy1."+name_tablaInput, name_tablaInput);   //Hago la selecion de la tabla, 1*validacion(si existe table)
            metaDatos = output.getMetaData();   //Obtengo el total de columnas que tiene la tabla
            index = metaDatos.getColumnCount();
                       
            ResultSet output2 = Conexion.consultaSql("SELECT * FROM proy1."+name_tablaInput2, name_tablaInput2);
            ResultSetMetaData metaDatos2 = output2.getMetaData();
            
            int index2 = 0;
            index2 = metaDatos2.getColumnCount();            
            int contadorAlmenosDos=0;
            String atri1,atri2;
            for(int i=1; i<=index; i++)
            {
                atri1 = metaDatos.getColumnName(i);
                atri2 = metaDatos2.getColumnName(i);
                int isExists=getIfExists(atri1, metaDatos2);
                if(isExists == 1 && contadorAlmenosDos < 2){
                    JOptionPane.showMessageDialog(null, "¡¡¡Los dominios de los atributos son iguales!!!");
                    contadorAlmenosDos++;
                }else if(contadorAlmenosDos < 2){
                    JOptionPane.showMessageDialog(null, "¡¡¡ERROR: NO HAY ATRIBUTOS COMUNES!!!");
                    return 1;
                }
            }
            if(contadorAlmenosDos < 2){
                JOptionPane.showMessageDialog(null, "¡¡¡ERROR: NO HAY ATRIBUTOS COMUNES!!!");
                return 1;
            }
           
            
            output = Conexion.consultaSql("SELECT *  FROM proy1."+name_tablaInput+", proy1."+name_tablaInput2, name_tablaInput);   //Hago la selecion de la tabla, 1*validacion(si existe table)
            metaDatos = output.getMetaData();   //Obtengo el total de columnas que tiene la tabla
            index = metaDatos.getColumnCount();
                       
            //Recorro la tabla y obtengo el nombre de las columnas
            for (int i = 1; i <= index; i++) {
                setAtriConca(metaDatos.getColumnName(i));
            }
            //Inserto datos bien...
            output = Conexion.consultaSql("SELECT *  FROM proy1."+name_tablaInput2, name_tablaInput2);   //Hago la selecion de la tabla, 1*validacion(si existe table)
            ResultSetMetaData metaDatos99 = output.getMetaData();
            output2 = Conexion.consultaSql("SELECT *  FROM proy1."+name_tablaInput, name_tablaInput);   //Hago la selecion de la tabla, 1*validacion(si existe table)
            ResultSetMetaData metaDatos992 = output2.getMetaData();
            
            String all_atri = "";
            all_atri = getAllAtri();
            String all_atri_tabla2 = getAllTable(metaDatos992, metaDatos99);
            int largo_columnas = atriConcatena3.size();
            atriConcatena3.clear();
            System.out.println(all_atri + "\n" + largo_columnas + "\n" + all_atri_tabla2);

            
            String atributos99 = convertToAtri(all_atri_tabla2, name_tablaInput, largo_columnas);
            
            sqlQuery2 = getTypeAtri(metaDatos, all_atri_tabla2);
            //Datos a utilizar
            sqlQuery += name_tablaOutput + " (" + sqlQuery2+")";                               //Create
            insertTemp = "INSERT INTO proy1.#"+name_tablaOutput + " (" +all_atri_tabla2+ ") "       //Insert
                    +"SELECT * FROM proy1."+name_tablaInput+" NATURAL JOIN proy1."+name_tablaInput2;              //Select
            selectTemp = "SELECT * FROM proy1.#"+name_tablaOutput;
            
            System.out.println(sqlQuery + "\n" + sqlQuery2 );
            System.out.println("\n" + insertTemp + "\n" + selectTemp);
            
            //-----Creo la tabla temporal eh Inserto datos en la tabla temporal y accedo a ella
            output = Conexion.consultaSqlCreate(sqlQuery, insertTemp, selectTemp, name_tablaInput);          //Lo accede el usuario usproy1
            metaDatos = output.getMetaData();
            index = metaDatos.getColumnCount();      
            
            while(output.next())
            {
                v = new Vector();
                for(int i=1;i<=index;i++)
                {
                    //Extraigo tupla. Imprimo la tabla temporal
                    v.add(output.getString(i));      
                    //System.out.println("----"+output.getString(i)); 
                }
                modelo.addRow(v);
            }
            modelo.setColumnIdentifiers(putEtiq(index, metaDatos));     //Agego etiquetas a la tabla
            getRegister().setTablaModel(modelo);    //Agrego datos a la tabla
            setTemp(name_tablaOutput, cont);        //Aumento contador de nombre de la tabla temporal
            this.cont++;
            atriConcatena1.clear();
            atriConcatena2.clear();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (NullPointerException x) {
            System.out.println(x.getMessage());
        }
        */
        return 0;
    }
    public int getIfExists(String atri1, ResultSetMetaData metaDatos)
    {
        try{
            int index = metaDatos.getColumnCount();
            for (int i = 1; i <= index; i++) {
                String atri0 = metaDatos.getColumnName(i);
                if(atri1.equals(atri0)){
                    return 1;
                }
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
    /**
     * agregacion
     */
    public int cargarTablaAgregacion(String name_TablaInput, String oper_agre, String name_TablaOutput)
    {
        //Ģ
        restartQuerys();    //Obtengo esqueleto de datos a utilizar en los query
        DefaultTableModel modelo = getRegister().getTablaModel();   //Obtengo la tabla de la Base de datos para poder agregarla
        modelo.setRowCount(0);
        
        try{
            output = Conexion.consultaSql("SELECT "+oper_agre+" FROM proy1."+name_TablaInput, name_TablaInput);   //Hago la selecion de la tabla, 1*validacion(si existe table)
            metaDatos = output.getMetaData();   //Obtengo el total de columnas que tiene la tabla
            index = metaDatos.getColumnCount();
            
            //Recorro la tabla y obtengo el nombre de las columnas
            for (int i = 1; i <= index; i++) {
              if (i == index){
                sqlQuery3 += metaDatos.getColumnName(i);
                sqlQuery2 += metaDatos.getColumnName(i) 
                     + " "
                     + metaDatos.getColumnTypeName(i)
                     + "(" + Integer.toString(metaDatos.getPrecision(i))
                     + ")";
              }else{
                sqlQuery3 += metaDatos.getColumnName(i)+ ", ";
                sqlQuery2 += metaDatos.getColumnName(i) 
                     + " "
                     + metaDatos.getColumnTypeName(i)
                     + "(" + Integer.toString(metaDatos.getPrecision(i))
                     + ")"
                     + ", ";
              }
            }
           
            //Datos a utilizar
            sqlQuery += name_TablaOutput + " "+ sqlQuery2 + ")";                                                      //Create
            insertTemp = "INSERT INTO proy1.#"+name_TablaOutput + " (" + sqlQuery3+ ") " 
                    +"SELECT "+oper_agre+" FROM proy1."+name_TablaInput;               //Select
            selectTemp = "SELECT * FROM proy1.#"+name_TablaOutput;
     
            System.out.println(sqlQuery + "\n" + sqlQuery2);
            System.out.println("\n" + insertTemp + "\n" + selectTemp);
            
            //-----Creo la tabla temporal eh Inserto datos en la tabla temporal y accedo a ella
            output = Conexion.consultaSqlCreate(sqlQuery, insertTemp, selectTemp, name_TablaInput);          //Lo accede el usuario usproy1
            metaDatos = output.getMetaData();
            index = metaDatos.getColumnCount();      
            
            while(output.next())
            {
                v = new Vector();
                for(int i=1;i<=index;i++)
                {
                    //Extraigo tupla. Imprimo la tabla temporal
                    v.add(output.getString(i));      
                    //System.out.println("----"+output.getString(i)); 
                }
                modelo.addRow(v);
            }
            modelo.setColumnIdentifiers(putEtiq(index, metaDatos));     //Agego etiquetas a la tabla
            getRegister().setTablaModel(modelo);    //Agrego datos a la tabla
            setTemp(name_TablaOutput, cont);        //Aumento contador de nombre de la tabla temporal
            this.cont++;
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
    public String getTypeAtri(ResultSetMetaData data, String name_atri)
    {
        try {
            int largo = 0;

            String result = "";
            String[] atributos = name_atri.split(", ");
            largo = atributos.length;
            for (int i = 1; i <= largo; i++) {
                if(i == largo){
                     result += atributos[i-1];
                     if(atributos[i-1].equals(data.getColumnName(i))){
                         result += " "
                            + data.getColumnTypeName(i)
                            + "(" + Integer.toString(data.getPrecision(i))
                            + ")"; 
                     }
                }else{
                     result += atributos[i-1];
                     if(atributos[i-1].equals(data.getColumnName(i))){
                         result += " "
                            + data.getColumnTypeName(i)
                            + "(" + Integer.toString(data.getPrecision(i))
                            + "), "; 
                     } 
                }
            }        
            return result;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    /**
     * agrupacion
     */
    public int cargarTablaAgrupacion(String name_TablaInput, String name_atri, String oper_agre, String name_TablaOutput)
    {
        restartQuerys();    //Obtengo esqueleto de datos a utilizar en los query
        DefaultTableModel modelo = getRegister().getTablaModel();   //Obtengo la tabla de la Base de datos para poder agregarla
        modelo.setRowCount(0);
        
        try{
            output = Conexion.consultaSql("SELECT "+name_atri+", "+oper_agre+" FROM proy1."+name_TablaInput+" GROUP BY "+name_atri, name_TablaInput);   //Hago la selecion de la tabla, 1*validacion(si existe table)
            metaDatos = output.getMetaData();   //Obtengo el total de columnas que tiene la tabla
            index = metaDatos.getColumnCount();
            
            String sqlQuery22 = "";
            //String sql00 = getTypeAtri(metaDatos, name_atri);
            //Recorro la tabla y obtengo el nombre de las columnas
            for (int i = 1; i <= index; i++) {
              if (i == index){
                sqlQuery3 += metaDatos.getColumnName(i);
                sqlQuery22 += metaDatos.getColumnName(i) 
                     + " "
                     + metaDatos.getColumnTypeName(i)
                     + "(" + Integer.toString(metaDatos.getPrecision(i))
                     + ")";
              }else{
                sqlQuery3 += metaDatos.getColumnName(i)+ ", ";
                sqlQuery22 += metaDatos.getColumnName(i) 
                     + " "
                     + metaDatos.getColumnTypeName(i)
                     + "(" + Integer.toString(metaDatos.getPrecision(i))
                     + ")"
                     + ", ";
              }
            }
           
            //Datos a utilizar
            sqlQuery += name_TablaOutput + " ("+ sqlQuery22 + ")";                                                      //Create
            insertTemp = "INSERT INTO proy1.#"+name_TablaOutput + " (" + sqlQuery3 + ") " 
                    +"SELECT "+name_atri+", "+oper_agre+" FROM proy1."+name_TablaInput
                    +" GROUP BY "+name_atri;               //Select
            selectTemp = "SELECT * FROM proy1.#"+name_TablaOutput;
     
            System.out.println(sqlQuery + "\n" + sqlQuery22);
            System.out.println("\n" + insertTemp + "\n" + selectTemp);
            
            //-----Creo la tabla temporal eh Inserto datos en la tabla temporal y accedo a ella
            output = Conexion.consultaSqlCreate(sqlQuery, insertTemp, selectTemp, name_TablaInput);          //Lo accede el usuario usproy1
            metaDatos = output.getMetaData();
            index = metaDatos.getColumnCount();      
            
            while(output.next())
            {
                v = new Vector();
                for(int i=1;i<=index;i++)
                {
                    //Extraigo tupla. Imprimo la tabla temporal
                    v.add(output.getString(i));      
                    //System.out.println("----"+output.getString(i)); 
                }
                modelo.addRow(v);
            }
            modelo.setColumnIdentifiers(putEtiq(index, metaDatos));     //Agego etiquetas a la tabla
            getRegister().setTablaModel(modelo);    //Agrego datos a la tabla
            setTemp(name_TablaOutput, cont);        //Aumento contador de nombre de la tabla temporal
            this.cont++;
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
    
    /**
     * Funcion retorno string con atributos proy1.tabla 
     */
    public String convertToAtri(String atris, String tabla1, int largo)
    {
        String result = "";
        String[] atributos = atris.split(", ");
        
        for (int i = 1; i <= largo; i++) {
          if(i == largo){
               result += "proy1."+tabla1+"."+atributos[i-1];
          }else{
               result += "proy1."+tabla1+"."+atributos[i-1]+", "; 
          }
        }   
        return result;
    }
   
    /**
     * Función que imprime todas las tablas temporales
     */
    public void printTablaTemps()
    {
        for (int i = 0; i < cont; i++){
            try {
                output = Conexion.consultaSql2("SELECT * FROM proy1.#"+getTemp(i));
                metaDatos = output.getMetaData();
                index=metaDatos.getColumnCount();
                
                System.out.println("contador="+cont+" name: "+getTemp(i));
                                
                while(output.next())
                {
                    for(int x=1;x<=index;x++)
                    {
                        //Extraigo tuplas
                        System.out.println(output.getString(x));     
                        
                    }
                }
                
            } catch (SQLException e) {
                System.out.println(e.getMessage());
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
                output = Conexion.consultaSql2("SELECT * FROM proy1."+name_all_per[i]);
                metaDatos = output.getMetaData();
                index=metaDatos.getColumnCount();
                
                System.out.println("->"+name_all_per[i]);
                
                while(output.next())
                {
                    for(int x=1;x<=index;x++)
                    {
                        //Extraigo tuplas
                        System.out.println(output.getString(x));                         
                    }
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    /**
     * Funcion de SELECCION
     */
    public void ejecSeleccion()
    {
         //Verifica si la informacion esta digitada
        if(getRegister().getInput1().isEmpty() || getRegister().getPredicado().isEmpty() || getRegister().getOutput().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "¡¡¡Inserte la TABLA INPUT 1, TABLA OUTPUT Y el PREDICADO correctamente!!!","Information",JOptionPane.INFORMATION_MESSAGE);
            getRegister().cleanText();
        } else {
            //Caso cuando no colocá el nombre de la TABLA OUTPUT
            if(getRegister().getOutput().isEmpty()){
                getRegister().setAlgebraR("σ "+getRegister().getPredicado()+"("+getRegister().getInput1()+")");
                getRegister().setSql("SELECT * FROM "+getRegister().getInput1()+" WHERE "+getRegister().getPredicado());
                JOptionPane.showMessageDialog(null, "No ingreso el nombre de la tabla resultante:\nNo se muestra la tabla graficamente\nNi se crea la tabla temporal");
            //Caso en que si digita toda la informacion
            }else{
                if(!isExistsTableOut(getRegister().getOutput())){
                    getRegister().setAlgebraR(getRegister().getOutput()+" <- σ "+getRegister().getPredicado()+"("+getRegister().getInput1()+")");
                    getRegister().setSql("SELECT * FROM "+getRegister().getInput1()+" WHERE "+getRegister().getPredicado());
                    cargarTablaSeleccion(getRegister().getInput1(), getRegister().getPredicado(), getRegister().getOutput());
                }
            }
        }
    }
    /**
     * Funcion Generalizada
     */
    public void ejecGeneralizada()
    {
         //Verifica si la informacion esta digitada
        if(getRegister().getInput1().isEmpty() || getRegister().getExpress().isEmpty() || getRegister().getOutput().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "¡¡¡Inserte la TABLA INPUT 1, TABLA OUTPUT Y la EXPRECION DE PROYECCION GENERALIZADA correctamente!!!","Information",JOptionPane.INFORMATION_MESSAGE);
            getRegister().cleanText();
        } else {
            //Caso cuando no colocá el nombre de la TABLA OUTPUT
            if(getRegister().getOutput().isEmpty()){
                getRegister().setAlgebraR(getRegister().getOutput()+" <- π "+getRegister().getExpress()+"("+getRegister().getInput1()+")");
                getRegister().setSql("SELECT DISTINCT "+getRegister().getExpress()+" FROM "+getRegister().getInput1());
                JOptionPane.showMessageDialog(null, "No ingreso el nombre de la tabla resultante:\nNo se muestra la tabla graficamente\nNi se crea la tabla temporal");
            //Caso en que si digita toda la informacion
            }else{
                if(!isExistsTableOut(getRegister().getOutput())){
                    getRegister().setAlgebraR(getRegister().getOutput()+" <- π "+getRegister().getExpress()+"("+getRegister().getInput1()+")");
                    getRegister().setSql("SELECT DISTINCT "+getRegister().getExpress()+" FROM "+getRegister().getInput1());
                    cargarTablaGene(getRegister().getInput1(), getRegister().getExpress(), getRegister().getOutput());
                }
            }
        }
    }
    /**
     * Funcion Union
     */
    public void ejecUnion()
    {
        //Verifica si la informacion esta digitada
        if(getRegister().getInput1().isEmpty() || getRegister().getInput2().isEmpty() || getRegister().getOutput().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "¡¡¡Inserte la TABLA INPUT 1, TABLA INPUT 2 y la TABLA OUTPUT correctamente!!!","Information",JOptionPane.INFORMATION_MESSAGE);
            getRegister().cleanText();
        } else {
            String atributos="";
            atributos=obtenerAtributos(getRegister().getInput1());
            String atributos2="";
            atributos2=obtenerAtributos(getRegister().getInput2());   
            //Caso cuando no colocá el nombre de la TABLA OUTPUT
            if(getRegister().getOutput().isEmpty()){
                getRegister().setAlgebraR(getRegister().getOutput()+" <- π "+atributos+" ("+getRegister().getInput1()+") U π "+atributos2+" "+"("+getRegister().getInput2()+")");
                getRegister().setSql("SELECT * FROM "+getRegister().getInput1()+" UNION "+"SELECT * FROM "+getRegister().getInput2());
                JOptionPane.showMessageDialog(null, "No ingreso el nombre de la tabla resultante:\nNo se muestra la tabla graficamente\nNi se crea la tabla temporal");
            //Caso en que si digita toda la informacion
            }else{
                if(!isExistsTableOut(getRegister().getOutput())){
                    getRegister().setAlgebraR(getRegister().getOutput()+" <- π "+atributos+" ("+getRegister().getInput1()+") U π "+atributos+" ("+getRegister().getInput2()+")");
                    getRegister().setSql("SELECT * FROM "+getRegister().getInput1()+" UNION "+"SELECT * FROM "+getRegister().getInput2());
                    cargarTablaUnion(getRegister().getInput1(), getRegister().getInput2(), getRegister().getOutput());
                }
            }
        }
    }
    public String obtenerAtributos(String table1)
    {
        try {
            output = Conexion.consultaSql("SELECT * FROM proy1."+table1, table1);   //Hago la selecion de la tabla, 1*validacion(si existe table)
            metaDatos = output.getMetaData();   //Obtengo el total de columnas que tiene la tabla
            index = metaDatos.getColumnCount();
            sqlQuery3="";
            //Recorro la tabla y obtengo el nombre de las columnas
            for (int i = 1; i <= index; i++) {
              if (i == index){
                sqlQuery3 += metaDatos.getColumnName(i);
              }else{
                sqlQuery3 += metaDatos.getColumnName(i)+ ", ";
              }
            }
            return sqlQuery3;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }
    /**
     * Funcion direcia de conjuntos
     */
    public void ejecDifConjuntos()
    {
        //Verifica si la informacion esta digitada
        if(getRegister().getInput1().isEmpty() || getRegister().getInput2().isEmpty() || getRegister().getOutput().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "¡¡¡Inserte la TABLA INPUT 1, TABLA INPUT 2 y la TABLA OUTPUT correctamente!!!","Information",JOptionPane.INFORMATION_MESSAGE);
            getRegister().cleanText();
        } else {
            String atributos="";
            atributos=obtenerAtributos(getRegister().getInput1());
            String atributos2="";
            atributos2=obtenerAtributos(getRegister().getInput2());        
            
            String primary_key=obtengoKey(atributos);
            //Caso cuando no colocá el nombre de la TABLA OUTPUT
            if(getRegister().getOutput().isEmpty()){
                getRegister().setAlgebraR(getRegister().getOutput()+" <- π "+atributos+" ("+getRegister().getInput1()+")"+" - π "+atributos2+" "+"("+getRegister().getInput2()+")");
                getRegister().setSql("SELECT * FROM "+getRegister().getInput1()+" WHERE NOT EXISTS "+"(SELECT * FROM "+getRegister().getInput2()+"WHERE proy1."+getRegister().getInput1()+"."+primary_key+" = proy1."+getRegister().getInput2()+"."+primary_key+")");
                JOptionPane.showMessageDialog(null, "No ingreso el nombre de la tabla resultante:\nNo se muestra la tabla graficamente\nNi se crea la tabla temporal");
            //Caso en que si digita toda la informacion
            }else{
                if(!isExistsTableOut(getRegister().getOutput())){
                    getRegister().setAlgebraR(getRegister().getOutput()+" <- π "+atributos+" ("+getRegister().getInput1()+")"+" - π "+atributos2+" ("+getRegister().getInput2()+")");
                    getRegister().setSql("SELECT * FROM "+getRegister().getInput1()+" WHERE NOT EXISTS "+"(SELECT * FROM "+getRegister().getInput2()+"WHERE proy1."+getRegister().getInput1()+"."+primary_key+" = proy1."+getRegister().getInput2()+"."+primary_key+")");
                    cargarTablaDif(getRegister().getInput1(), getRegister().getInput2(), getRegister().getOutput());
                }
            }
        }
    }
    /**
     * Funcion ejecutar producto cartesiano
     */
    public void ejecProCar()
    {
        //Verifica si la informacion esta digitada
        if(getRegister().getInput1().isEmpty() || getRegister().getInput2().isEmpty() || getRegister().getOutput().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "¡¡¡Inserte la TABLA INPUT 1, TABLA INPUT 2 y la TABLA OUTPUT correctamente!!!","Information",JOptionPane.INFORMATION_MESSAGE);
            getRegister().cleanText();
        } else {
            //Caso cuando no colocá el nombre de la TABLA OUTPUT
            if(getRegister().getOutput().isEmpty()){
                getRegister().setAlgebraR(getRegister().getOutput()+" <- ("+getRegister().getInput1()+" X "+getRegister().getInput2()+")");
                getRegister().setSql("SELECT * FROM "+getRegister().getInput1()+", "+getRegister().getInput2());
                JOptionPane.showMessageDialog(null, "No ingreso el nombre de la tabla resultante:\nNo se muestra la tabla graficamente\nNi se crea la tabla temporal");
            //Caso en que si digita toda la informacion
            }else{
                if(!isExistsTableOut(getRegister().getOutput())){
                    getRegister().setAlgebraR(getRegister().getOutput()+" <- ("+getRegister().getInput1()+" X "+getRegister().getInput2()+")");
                    getRegister().setSql("SELECT * FROM "+getRegister().getInput1()+", "+getRegister().getInput2());
                    cargarTablaProCar(getRegister().getInput1(), getRegister().getInput2(), getRegister().getOutput());
                }
            }
        }
    }
    /**
     * Funcion interseccion
     */
    public void ejecIntersec()
    {
        //Verifica si la informacion esta digitada
        if(getRegister().getInput1().isEmpty() || getRegister().getInput2().isEmpty() || getRegister().getOutput().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "¡¡¡Inserte la TABLA INPUT 1, TABLA INPUT 2 y la TABLA OUTPUT correctamente!!!","Information",JOptionPane.INFORMATION_MESSAGE);
            getRegister().cleanText();
        } else {
            String atributos="";
            atributos=obtenerAtributos(getRegister().getInput1());
            String atributos2="";
            atributos2=obtenerAtributos(getRegister().getInput2());  
            
            //Caso cuando no colocá el nombre de la TABLA OUTPUT
            if(getRegister().getOutput().isEmpty()){
                getRegister().setAlgebraR(getRegister().getOutput()+" <- π "+atributos+" ("+getRegister().getInput1()+") ∩  π "+atributos2+" ("+getRegister().getInput2()+")");
                getRegister().setSql("SELECT * FROM "+getRegister().getInput1()+" INTERSECT SELECT * FROM "+getRegister().getInput2());
                JOptionPane.showMessageDialog(null, "No ingreso el nombre de la tabla resultante:\nNo se muestra la tabla graficamente\nNi se crea la tabla temporal");
            //Caso en que si digita toda la informacion
            }else{
                if(!isExistsTableOut(getRegister().getOutput())){
                    getRegister().setAlgebraR(getRegister().getOutput()+" <- π "+atributos+" ("+getRegister().getInput1()+") ∩  π "+atributos2+" ("+getRegister().getInput2()+")");
                    getRegister().setSql("SELECT * FROM "+getRegister().getInput1()+" INTERSECT SELECT * FROM "+getRegister().getInput2());
                    cargarTablaInter(getRegister().getInput1(), getRegister().getInput2(), getRegister().getOutput());
                }
            }
        }
    }
    /**
     * Funcion division
     */
    public void ejecDiv()
    {
        //Verifica si la informacion esta digitada
        if(getRegister().getInput1().isEmpty() || getRegister().getInput2().isEmpty() || getRegister().getOutput().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "¡¡¡Inserte la TABLA INPUT 1, TABLA INPUT 2 y la TABLA OUTPUT correctamente!!!","Information",JOptionPane.INFORMATION_MESSAGE);
            getRegister().cleanText();
        } else {
            String atributos="";
            atributos=obtenerAtributos(getRegister().getInput1());
            String atributos2="";
            atributos2=obtenerAtributos(getRegister().getInput2());  
            
            //Caso cuando no colocá el nombre de la TABLA OUTPUT
            if(getRegister().getOutput().isEmpty()){
                getRegister().setAlgebraR(getRegister().getOutput()+" <- π "+atributos+" ("+getRegister().getInput1()+") ÷ π "+atributos2+" ("+getRegister().getInput2()+")");
                getRegister().setSql("SELECT * FROM "+getRegister().getInput1()+" EXCEPT SELECT * FROM "+getRegister().getInput2());
                JOptionPane.showMessageDialog(null, "No ingreso el nombre de la tabla resultante:\nNo se muestra la tabla graficamente\nNi se crea la tabla temporal");
            //Caso en que si digita toda la informacion
            }else{
                if(!isExistsTableOut(getRegister().getOutput())){
                    getRegister().setAlgebraR(getRegister().getOutput()+" <- π "+atributos+" ("+getRegister().getInput1()+") ÷ π "+atributos2+" ("+getRegister().getInput2()+")");
                    getRegister().setSql("SELECT * FROM "+getRegister().getInput1()+" EXCEPT SELECT * FROM "+getRegister().getInput2());
                    cargarTablaDiv(getRegister().getInput1(), getRegister().getInput2(), getRegister().getOutput());
                }
            }
        }        
    }
    /**
     * Funcion interseccion
     */
    public void ejecRenomR()
    {
        //Verifica si la informacion esta digitada
        if(getRegister().getInput1().isEmpty() || getRegister().getNombreA().isEmpty() || getRegister().getOutput().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "¡¡¡Inserte la TABLA INPUT 1, NOMBRE DE ATRIBUTOS y la TABLA OUTPUT correctamente!!!","Information",JOptionPane.INFORMATION_MESSAGE);
            getRegister().cleanText();
        } else {
            //Caso cuando no colocá el nombre de la TABLA OUTPUT
            if(getRegister().getOutput().isEmpty()){
                getRegister().setAlgebraR(getRegister().getOutput()+" <- π * ("+getRegister().getInput1()+") ÷ π * ("+getRegister().getNombreA()+")");
                getRegister().setSql("SELECT * FROM "+getRegister().getInput1()+" EXCEPT SELECT * FROM "+getRegister().getInput2());
                JOptionPane.showMessageDialog(null, "No ingreso el nombre de la tabla resultante:\nNo se muestra la tabla graficamente\nNi se crea la tabla temporal");
            //Caso en que si digita toda la informacion
            }else{
                if(!isExistsTableOut(getRegister().getOutput())){
                    getRegister().setAlgebraR(getRegister().getOutput()+" <- π * ("+getRegister().getInput1()+") ÷ π * ("+getRegister().getInput2()+")");
                    getRegister().setSql("SELECT * FROM "+getRegister().getInput1()+" EXCEPT SELECT * FROM "+getRegister().getInput2());
                    cargarTablaRenomR(getRegister().getInput1(), getRegister().getNombreA(), getRegister().getOutput());
                }
            }
        } 
    }
    /**
     * concatenacion join
     */
    public void ejecConcaJoin()
    {
        //Verifica si la informacion esta digitada
        if(getRegister().getInput1().isEmpty() || getRegister().getInput2().isEmpty() || getRegister().getPredicado().isEmpty() || getRegister().getOutput().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "¡¡¡Inserte la TABLA INPUT 1, TABLA INPUT 2, PREDICADO y la TABLA OUTPUT correctamente!!!","Information",JOptionPane.INFORMATION_MESSAGE);
            getRegister().cleanText();
        } else {
            //Caso cuando no colocá el nombre de la TABLA OUTPUT
            if(getRegister().getOutput().isEmpty()){
                getRegister().setAlgebraR(getRegister().getOutput()+" <- ("+getRegister().getInput1()+" ⋈ "+getRegister().getPredicado()+" "+getRegister().getInput2()+")");
                getRegister().setSql("SELECT * FROM proy1."+getRegister().getInput1()+" INNER JOIN proy1."+getRegister().getInput2()+" ON "+getRegister().getPredicado());
                //(proy1."+getRegister().getInput1()+"."+primary_key+" = "+"proy1."+getRegister().getInput2()+"."+primary_key+")"
                JOptionPane.showMessageDialog(null, "No ingreso el nombre de la tabla resultante:\nNo se muestra la tabla graficamente\nNi se crea la tabla temporal");
            //Caso en que si digita toda la informacion
            }else{
                if(!isExistsTableOut(getRegister().getOutput())){
                    getRegister().setAlgebraR(getRegister().getOutput()+" <- ("+getRegister().getInput1()+" ⋈ "+getRegister().getPredicado()+" "+getRegister().getInput2()+")");
                    getRegister().setSql("SELECT * FROM proy1."+getRegister().getInput1()+" INNER JOIN proy1."+getRegister().getInput2()+" ON "+getRegister().getPredicado());
                    cargarTablaConcaJoin(getRegister().getInput1(), getRegister().getInput2(), getRegister().getPredicado(), getRegister().getOutput());
                }
            }
        }
    }
    /**
     * Funcion natural join
     */
    public void ejecNaturalJoin()
    {
        //Verifica si la informacion esta digitada
        if(getRegister().getInput1().isEmpty() || getRegister().getInput2().isEmpty() || getRegister().getOutput().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "¡¡¡Inserte la TABLA INPUT 1, TABLA INPUT 2 y la TABLA OUTPUT correctamente!!!","Information",JOptionPane.INFORMATION_MESSAGE);
            getRegister().cleanText();
        } else {
            //Caso cuando no colocá el nombre de la TABLA OUTPUT
            if(getRegister().getOutput().isEmpty()){
                getRegister().setAlgebraR(getRegister().getOutput()+" <- ("+getRegister().getInput1()+" ⋈ "+getRegister().getInput2()+")");
                getRegister().setSql("SELECT * FROM proy1."+getRegister().getInput1()+" NATURAL JOIN proy1."+getRegister().getInput2());
                //(proy1."+getRegister().getInput1()+"."+primary_key+" = "+"proy1."+getRegister().getInput2()+"."+primary_key+")"
                JOptionPane.showMessageDialog(null, "No ingreso el nombre de la tabla resultante:\nNo se muestra la tabla graficamente\nNi se crea la tabla temporal");
            //Caso en que si digita toda la informacion
            }else{
                if(!isExistsTableOut(getRegister().getOutput())){
                    getRegister().setAlgebraR(getRegister().getOutput()+" <- ("+getRegister().getInput1()+" ⋈ "+getRegister().getInput2()+")");
                    getRegister().setSql("SELECT * FROM proy1."+getRegister().getInput1()+" NATURAL JOIN proy1."+getRegister().getInput2());
                    cargarTablaNaturalJoin(getRegister().getInput1(), getRegister().getInput2(), getRegister().getOutput());
                }
            }
        }  
    }
    /**
     * Funcion agragacion
     */
    public void ejecAgregacion()
    {
        //Verifica si la informacion esta digitada
        if(getRegister().getInput1().isEmpty() || getRegister().getOperAgre().isEmpty() || getRegister().getOutput().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "¡¡¡Inserte la TABLA INPUT 1, OPERACIONES DE AGREGACIONES y la TABLA OUTPUT correctamente!!!","Information",JOptionPane.INFORMATION_MESSAGE);
            getRegister().cleanText();
        } else {
            //Caso cuando no colocá el nombre de la TABLA OUTPUT
            if(getRegister().getOutput().isEmpty()){
                getRegister().setAlgebraR(getRegister().getOutput()+" <- Ģ "+getRegister().getOperAgre()+" ("+getRegister().getInput1()+")");
                getRegister().setSql("SELECT "+getRegister().getOperAgre()+" FROM "+getRegister().getInput1());
                JOptionPane.showMessageDialog(null, "No ingreso el nombre de la tabla resultante:\nNo se muestra la tabla graficamente\nNi se crea la tabla temporal");
            //Caso en que si digita toda la informacion
            }else{
                if(!isExistsTableOut(getRegister().getOutput())){
                    getRegister().setAlgebraR(getRegister().getOutput()+" <- Ģ "+getRegister().getOperAgre()+" ("+getRegister().getInput1()+")");
                    getRegister().setSql("SELECT "+getRegister().getOperAgre()+" FROM "+getRegister().getInput1());
                    cargarTablaAgregacion(getRegister().getInput1(), getRegister().getOperAgre(), getRegister().getOutput());
                }
            }
        }
    }
    /**
     * Funcion agrupacion
     */
    public void ejecAgrupacion()
    {
        //Verifica si la informacion esta digitada
        if(getRegister().getInput1().isEmpty() || getRegister().getNombreA().isEmpty() || getRegister().getOperAgre().isEmpty() || getRegister().getOutput().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "¡¡¡Inserte la TABLA INPUT 1, NOMBRE ATRIBUTOS, OPERACIONES DE AGREGACION y la TABLA OUTPUT correctamente!!!","Information",JOptionPane.INFORMATION_MESSAGE);
            getRegister().cleanText();
        } else {
            //Caso cuando no colocá el nombre de la TABLA OUTPUT
            if(getRegister().getOutput().isEmpty()){
                getRegister().setAlgebraR(getRegister().getOutput()+" <- "+getRegister().getNombreA()+" Ģ "+getRegister().getOperAgre()+" ("+getRegister().getInput1()+")");
                getRegister().setSql("SELECT "+getRegister().getNombreA()+", "+getRegister().getOperAgre()+" FROM "+getRegister().getInput1());
                JOptionPane.showMessageDialog(null, "No ingreso el nombre de la tabla resultante:\nNo se muestra la tabla graficamente\nNi se crea la tabla temporal");
            //Caso en que si digita toda la informacion
            }else{
                if(!isExistsTableOut(getRegister().getOutput())){
                    getRegister().setAlgebraR(getRegister().getOutput()+" <- "+getRegister().getNombreA()+" Ģ "+getRegister().getOperAgre()+" ("+getRegister().getInput1()+")");
                    getRegister().setSql("SELECT "+getRegister().getNombreA()+", "+getRegister().getOperAgre()+" FROM "+getRegister().getInput1());
                    cargarTablaAgrupacion(getRegister().getInput1(), getRegister().getNombreA(), getRegister().getOperAgre(), getRegister().getOutput());
                }
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
        switch(operator_aux){           

            case "Selección":
                ejecSeleccion();
                break;

            case "Proyección Generalizada":
                ejecGeneralizada();
                break;

            case "Unión":
                ejecUnion();
                break;

            case "Diferencia de conjuntos":
                ejecDifConjuntos();
                break;

            case "Producto Cartesiano":
                ejecProCar();
                break;
                
            case "Intersección":
                ejecIntersec();
                break;

            case "División":
                ejecDiv();
                break;

            case "Renombrar una relación y sus atributos":
                ejecRenomR();
                break;

            case "Concatenación":
                ejecConcaJoin();
                break;

            case "Concatenación natural":
                ejecNaturalJoin();
                break;

            case "Agregación":
                ejecAgregacion();
                break;

            case "Agrupación":
                ejecAgrupacion();
                break;

            default:
                break;
        };
        //getRegister().cleanText();
    }
//Fin del programa controlador del interprete de algebra SQL/JAVA
}






            /*
            //Comparo y retorno
            for (int x=0;x<rTotal.length();x++){
                if(rTotal.charAt(x) == ','){
                    if(atri_comparo.equals(rTotal.charAt(x))){
                        
                    }
                    atri_comparo = "";
                    
                }else if(rTotal.charAt(x) == ' '){
                    continue;
                }else{
                    atri_comparo += sqlQuery3.charAt(x);
                }
            }
            */



        
        
        /*
        String primary_key="";
        int x = 0;
        int largo = sqlQuery3.length();
        String atribu0 = "";
        String atribu = "proy1."+tabla1+".";
        
        for (x=0;x<largo;x++){
            if(sqlQuery3.charAt(x) == ','){
                atribu = atribu + primary_key; 
                atribu0 = atribu0+atribu;
                atribu0 = atribu0+", proy1."+tabla1+".";   
                primary_key = "";
                atribu = "";
            }else{
                if(sqlQuery3.charAt(index) == ' '){
                    break;
                }else{
                    primary_key += sqlQuery3.charAt(x);      
                }
            }
        }
        return atribu0;
         */




/*
           while(output.next())    //Obtengo datos de la tabla input
            {
                for(int i=1; i<=index; i++)
                {
                    if(i == index)
                    {
                        sqlQuery3 += metaDatos.getColumnName(i);
                        sqlQuery2 += metaDatos.getColumnName(i) 
                           + " "
                           + metaDatos.getColumnTypeName(i)
                           + "(" + Integer.toString(metaDatos.getPrecision(i))
                           + ")";
                    }else
                    {   
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
*/

/*

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
    
*/


    
    /*
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
    */


/*
public class DatabaseSQLiteConnection 
{
    Connection conn = null;
    PreparedStatement statement = null;
    ResultSet res = null;

    public DatabaseSQLiteConnection()
    {
        try{
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:test.sqlite");
            statement = conn.prepareStatement("SELECT * from product_info;");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }   

    public void getnPrintAllData()
    {
        String name, supplier, id;
        DefaultTableModel dtm = new DefaultTableModel();
        Window gui = new Window(); //My JPanel class        
        try{
            res = statement.executeQuery();
            testResultSet(res);
            ResultSetMetaData meta = res.getMetaData();
            int numberOfColumns = meta.getColumnCount();
            while (res.next())
            {
                Object [] rowData = new Object[numberOfColumns];
                for (int i = 0; i < rowData.length; ++i)
                {
                    rowData[i] = res.getObject(i+1);
                }
                dtm.addRow(rowData);
            }
            gui.jTable1.setModel(dtm);
            dtm.fireTableDataChanged();
            //////////////////////////

        }
        catch(Exception e){
            System.err.println(e);
            e.printStackTrace();
        }
        finally{
            try{
                res.close();
                statement.close();
                conn.close();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }    

    public void testResultSet(ResultSet res)
    {
        try{
            while(res.next()){
                System.out.println("Product ID: "+ res.getInt("product_id"));
                System.out.println("Product name: "+ res.getString("product_name"));
                System.out.println("Supplier: "+ res.getString("supplier"));
            }        
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}



*/