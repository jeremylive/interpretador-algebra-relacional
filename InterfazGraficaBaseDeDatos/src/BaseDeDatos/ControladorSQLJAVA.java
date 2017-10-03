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
        return this.name_all_temp[cont];
    }
    //
    public void restartQuerys()
    {
        sqlQuery = "CREATE TABLE proy1.#";  //Tabla temporal
        sqlQuery2 = "(";                    //atributos con tipo
        sqlQuery3 = "";                     //atributos
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
            //Datos a utilizar
            sqlQuery += name_tablaOutput + " ";
            sqlQuery = sqlQuery + sqlQuery2 + ")";                                                      //Create
            insertTemp = "INSERT INTO proy1.#"+name_tablaOutput + " (" + sqlQuery3 + ") "          //Insert
                    +"SELECT * FROM proy1."+name_tablaInput+" WHERE "+predicado_aux;               //Select
            selectTemp = "SELECT * FROM proy1.#"+name_tablaOutput;
            
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
        
        restartQuerys();    //Obtengo esqueleto de datos a utilizar en los query
        DefaultTableModel modelo = getRegister().getTablaModel();   //Obtengo la tabla de la Base de datos para poder agregarla
        modelo.setRowCount(0);
        
        try{
            output = Conexion.consultaSql("SELECT * FROM proy1."+name_tablaInput, name_tablaInput);   //Hago la selecion de la tabla, 1*validacion(si existe table)
            metaDatos = output.getMetaData();   //Obtengo el total de columnas que tiene la tabla
            index = metaDatos.getColumnCount();
            
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
            //Datos a utilizar
            System.out.println(sqlQuery3);
            sqlQuery += name_tablaOutput + " ";
            sqlQuery = sqlQuery + sqlQuery2 + ")";                                                      //Create
            insertTemp = "INSERT INTO proy1.#"+name_tablaOutput + " (" + gene_aux+ ") "          //Insert
                    +"SELECT "+gene_aux+" FROM proy1."+name_tablaInput;               //Select
            selectTemp = "SELECT * FROM proy1.#"+name_tablaOutput;
            
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
            
            System.out.println(sqlQuery + "\n" + sqlQuery2 + "\n" + sqlQuery3);
            System.out.println("\n" + insertTemp + "\n" + selectTemp);
            System.out.println("\n ....."+name_tablaOutput);
            System.out.println("nameTempe "+getTemp(cont-1));
            
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
            //System.out.println("///"+index+"  "+index2);
            if(index == index2)
            {
                JOptionPane.showMessageDialog(null, "La aridad de estas dos tablas coincide.. son de aridad = "+index);
                while(output.next())    //Obtengo datos de la tabla input
                {
                    for(int i=1; i<=index; i++)
                    {
                        atri1 = metaDatos.getColumnName(i);
                        atri2 = metaDatos2.getColumnName(i);
                        if(atri1 != atri2){
                            JOptionPane.showMessageDialog(null, "¡¡¡ERROR: DOMINIOS DIFERENTES. EL ATRIBUTO "+atri1+" TIENE DOMINIO "+atri1+" Y EL ATRIBUTO "+atri2+" TIENE DOMINIO "+atri2+"!!!");
                            return 1;
                        }
                    }
                }
            }else
            {
                JOptionPane.showMessageDialog(null, "¡¡¡ERROR: TABLAS CON DIFERENTE ARIDAD. LA TABLA 1 TIENE ARIDAD "+index+" Y LA TABLA 2 TIENE ARIDAD "+index2+"!!!");
                return 1;
            }
            
            //----U
            output = Conexion.consultaSql("SELECT * FROM proy1."+name_tablaInput, name_tablaInput);   //Hago la selecion de la tabla, 1*validacion(si existe table)
            metaDatos = output.getMetaData();   //Obtengo el total de columnas que tiene la tabla
            index = metaDatos.getColumnCount();
            
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
            //Datos a utilizar
            sqlQuery += name_tablaOutput + " ";
            sqlQuery = sqlQuery + sqlQuery2 + ")";                                                      //Create
            insertTemp = "INSERT INTO proy1.#"+name_tablaOutput + " (" + sqlQuery3 + ") "          //Insert
                    +"SELECT * FROM proy1."+name_tablaInput+" UNION "+"SELECT * FROM proy1."+name_tablaInput2; ;               //Select
            selectTemp = "SELECT * FROM proy1.#"+name_tablaOutput;
            
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
            
            String atri1,atri2;
            //System.out.println("///"+index+"  "+index2);
            if(index == index2)
            {
                JOptionPane.showMessageDialog(null, "La aridad de estas dos tablas coincide.. son de aridad = "+index);
                while(output.next())    //Obtengo datos de la tabla input
                {
                    for(int i=1; i<=index; i++)
                    {
                        atri1 = metaDatos.getColumnName(i);
                        atri2 = metaDatos2.getColumnName(i);
                        if(atri1 != atri2){
                            JOptionPane.showMessageDialog(null, "¡¡¡ERROR: DOMINIOS DIFERENTES. EL ATRIBUTO "+atri1+" TIENE DOMINIO "+atri1+" Y EL ATRIBUTO "+atri2+" TIENE DOMINIO "+atri2+"!!!");
                            return 1;
                        }
                    }
                }
            }else
            {
                JOptionPane.showMessageDialog(null, "¡¡¡ERROR: TABLAS CON DIFERENTE ARIDAD. LA TABLA 1 TIENE ARIDAD "+index+" Y LA TABLA 2 TIENE ARIDAD "+index2+"!!!");
                return 1;
            }
            
            //-----
            output = Conexion.consultaSql("SELECT * FROM proy1."+name_tablaInput, name_tablaInput);   //Hago la selecion de la tabla, 1*validacion(si existe table)
            metaDatos = output.getMetaData();   //Obtengo el total de columnas que tiene la tabla
            index = metaDatos.getColumnCount();
            
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
            //Datos a utilizar
            sqlQuery += name_tablaOutput + " ";
            sqlQuery = sqlQuery + sqlQuery2 + ")";                                                      //Create
            insertTemp = "INSERT INTO proy1.#"+name_tablaOutput + " (" + sqlQuery3 + ") "          //Insert
                    +"SELECT * FROM proy1."+name_tablaInput+" WHERE NOT EXIST "+"SELECT * FROM proy1."+name_tablaInput2; ;               //Select
            selectTemp = "SELECT * FROM proy1.#"+name_tablaOutput;
            
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
        return 0;
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
            //Caso cuando no colocá el nombre de la TABLA OUTPUT
            if(getRegister().getOutput().isEmpty()){
                getRegister().setAlgebraR(getRegister().getOutput()+" <- π * ("+getRegister().getInput1()+")"+" U π * "+"("+getRegister().getInput2()+")");
                getRegister().setSql("SELECT * FROM "+getRegister().getInput1()+" UNION "+"SELECT * FROM "+getRegister().getInput2());
                JOptionPane.showMessageDialog(null, "No ingreso el nombre de la tabla resultante:\nNo se muestra la tabla graficamente\nNi se crea la tabla temporal");
            //Caso en que si digita toda la informacion
            }else{
                if(!isExistsTableOut(getRegister().getOutput())){
                    getRegister().setAlgebraR(getRegister().getOutput()+" <- π * ("+getRegister().getInput1()+" U π * ("+getRegister().getInput2()+")");
                    getRegister().setSql("SELECT * FROM "+getRegister().getInput1()+" UNION "+"SELECT * FROM "+getRegister().getInput2());
                    cargarTablaUnion(getRegister().getInput1(), getRegister().getInput2(), getRegister().getOutput());
                }
            }
        }
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
            //Caso cuando no colocá el nombre de la TABLA OUTPUT
            if(getRegister().getOutput().isEmpty()){
                getRegister().setAlgebraR(getRegister().getOutput()+" <- π * ("+getRegister().getInput1()+")"+" - π * "+"("+getRegister().getInput2()+")");
                getRegister().setSql("SELECT * FROM "+getRegister().getInput1()+" WHERE NOT EXISTS "+"(SELECT * FROM "+getRegister().getInput2()+")");
                JOptionPane.showMessageDialog(null, "No ingreso el nombre de la tabla resultante:\nNo se muestra la tabla graficamente\nNi se crea la tabla temporal");
            //Caso en que si digita toda la informacion
            }else{
                if(!isExistsTableOut(getRegister().getOutput())){
                    getRegister().setAlgebraR(getRegister().getOutput()+" <- π * ("+getRegister().getInput1()+" U π * ("+getRegister().getInput2()+")");
                    getRegister().setSql("SELECT * FROM "+getRegister().getInput1()+" WHERE NOT EXISTS "+"(SELECT * FROM "+getRegister().getInput2()+")");
                    cargarTablaDif(getRegister().getInput1(), getRegister().getInput2(), getRegister().getOutput());
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

            default:
                break;
        };


        getRegister().cleanText();
        JOptionPane.showMessageDialog(null, "The date was successfully correctly");
    }
//Fin del programa controlador del interprete de algebra SQL/JAVA
}



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