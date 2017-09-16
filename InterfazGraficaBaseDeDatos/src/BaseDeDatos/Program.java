
package BaseDeDatos;

/**
 *
 * @author Jeremy Live
 */
public class Program 
{
    /**
     * Funcion Main
     * @param args 
     */
    public static void main(String[] args) 
    {
        /**
         * Comienza el programa con la interfaz visible
         */
                /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Interface().setVisible(true);
            }
        });
    }
    
}
