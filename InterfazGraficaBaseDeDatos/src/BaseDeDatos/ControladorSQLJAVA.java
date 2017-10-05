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
    private String insertTemp;
    private String selectTemp;
    private String sqlQuery;            //Temp table
    private String sqlQuery2;           //atri type
    private String sqlQuery3;           //atri
    private int index;
    private Vector v;
    private ResultSetMetaData metaDatos;
    
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
        return 0;
    }
    /**
     * concatenacion join
     */
    public int cargarTablaConcaJoin(String name_tablaInput, String name_tablaInput2, String predicado, String name_tablaOutput)
    {
        //("SELECT * FROM "+getRegister().getInput1()+" INNER JOIN "+getRegister().getInput2()+" ON (proy1."+getRegister().getInput1()+"."+primary_key+" = "+"proy1."+getRegister().getInput2()+"."+primary_key+")");

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
                    +"SELECT * FROM proy1."+name_tablaInput+" INNER JOIN proy1."+name_tablaInput2+" WHERE "+predicado;              //Select
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
     * natural join
     */
    public int cargarTablaNaturalJoin(String name_tablaInput, String name_tablaInput2, String name_tablaOutput)
    {
        restartQuerys();    //Obtengo esqueleto de datos a utilizar en los query
        DefaultTableModel modelo = getRegister().getTablaModel();   //Obtengo la tabla de la Base de datos para poder agregarla
        modelo.setRowCount(0);
        
        try{
            output = Conexion.consultaSql("SELECT * FROM proy1."+name_tablaInput+", proy1."+name_tablaInput2, name_tablaInput);   //Hago la selecion de la tabla, 1*validacion(si existe table)
            metaDatos = output.getMetaData();   //Obtengo el total de columnas que tiene la tabla
            index = metaDatos.getColumnCount();
                       
            ResultSet output2 = Conexion.consultaSql("SELECT * FROM proy1."+name_tablaInput2, name_tablaInput2);
            ResultSetMetaData metaDatos2 = output2.getMetaData();
            int index2 = 0;
            index2 = metaDatos2.getColumnCount();
            
            int contadorAlmenosDos=0;
            String atri1,atri2;
            if(index == index2)
            {
                JOptionPane.showMessageDialog(null, "La aridad de estas dos tablas coinciden Y son de ARIDAD = "+index);
                
                for(int i=1; i<=index; i++)
                {
                    atri1 = metaDatos.getColumnName(i);
                    atri2 = metaDatos2.getColumnName(i);
                    if(atri1.equals(atri2) && contadorAlmenosDos < 2){
                        JOptionPane.showMessageDialog(null, "¡¡¡Los dominios de los atributos son iguales!!!");
                        contadorAlmenosDos++;
                    }else{
                        JOptionPane.showMessageDialog(null, "¡¡¡ERROR: DOMINIOS DIFERENTES. EL ATRIBUTO "+atri1+" TIENE DOMINIO "+atri1+" Y EL ATRIBUTO "+atri2+" TIENE DOMINIO "+atri2+"!!!");
                        return 1;
                    }
                }
                if(contadorAlmenosDos < 2){
                    JOptionPane.showMessageDialog(null, "¡¡¡ERROR: NO HAY ATRIBUTOS COMUNES!!!");
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
            sqlQuery += name_tablaOutput + " " + sqlQuery2 + ")";                               //Create
            insertTemp = "INSERT INTO proy1.#"+name_tablaOutput + " (" + sqlQuery3 + ") "       //Insert
                    +"SELECT * FROM proy1."+name_tablaInput+" NATURAL JOIN proy1."+name_tablaInput2;              //Select
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
;         
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
    /**
     * agrupacion
     */
    public int cargarTablaAgrupacion(String name_TablaInput, String name_atri, String oper_agre, String name_TablaOutput)
    {
        return 0;
    }
    
    /**
     * Funcion retorno string con atributos proy1.tabla 
     */
    public String convertToAtri(String atris, String tabla1, int largo)
    {
        String result = "";
        String[] atributos = atris.split(", ");
        for (int i = 0; i < largo; i++) {
            if(i+1 == largo){
                 result += "proy1."+tabla1+"."+atributos[i];
            }else{
                 result += "proy1."+tabla1+"."+atributos[i]+", ";   
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
            JOptionPane.showMessageDialog(null, "¡¡¡Inserte la TABLA INPUT 1, TABLA INPUT 2 y la TABLA OUTPUT correctamente!!!","Information",JOptionPane.INFORMATION_MESSAGE);
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
            JOptionPane.showMessageDialog(null, "¡¡¡Inserte la TABLA INPUT 1, TABLA INPUT 2 y la TABLA OUTPUT correctamente!!!","Information",JOptionPane.INFORMATION_MESSAGE);
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
            JOptionPane.showMessageDialog(null, "¡¡¡Inserte la TABLA INPUT 1, TABLA INPUT 2 y la TABLA OUTPUT correctamente!!!","Information",JOptionPane.INFORMATION_MESSAGE);
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
            JOptionPane.showMessageDialog(null, "¡¡¡Inserte la TABLA INPUT 1, TABLA INPUT 2 y la TABLA OUTPUT correctamente!!!","Information",JOptionPane.INFORMATION_MESSAGE);
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