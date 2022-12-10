package Funciones;


import Interfaz_Cliente.ICliente;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import objeto.Producto;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author CIIC
 */
public class Consultar_a_Servidor {
    
    public Consultar_a_Servidor(){
        
    }
    
    public void f(){
    Consultar_a_Servidor nuevo= new Consultar_a_Servidor();
         nuevo.testStatelessEjb();
    }
    
     public void testStatelessEjb(Producto a) {
         
        try {
            InitialContext ctx = new InitialContext();
                       
            ICliente libraryBean = (ICliente) ctx.lookup("Interfaz.ICliente");
            
            
            try {
                
                int choice = 1;
                while (choice != 2) {
                    String bookName;
                  
                    String strChoice;
                  
                    if (choice == 1) {
                        int id=a.getId();
                        float precio=a.getPrecio();
                        String nomb=a.getNombre();
                        String tipe=a.getTipo();
                        
                        
                        System.out.print("Nombre del libro: ");
                        
                        libraryBean.Agregar_Carrito(nomb);
                        
                    } else if (choice == 2) {
                        break;
                    }
                }

                List<String> booksList = libraryBean.obtenerProdcutos();
                System.out.println("Lista de libros: " + booksList.size());
                for (int i = 0; i < booksList.size(); ++i) {
                    System.out.println((i + 1) + ". " + booksList.get(i));
                }

                ICliente libraryBean1;
                libraryBean1 = (ICliente) ctx.lookup("interfaz.ILibreria");
                List<String> booksList1 = libraryBean1.obtenerProdcutos();
                System.out.println(
                        "*** Realizando otra búsqueda y obteniendo otro bean ***");
                System.out.println(
                        "Lista de libros: " + booksList1.size());
                for (int i = 0; i < booksList1.size(); ++i) {
                    System.out.println((i + 1) + ". " + booksList1.get(i));
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            } finally {
                
               
            }
        } catch (NamingException ex) {
            Logger.getLogger(Consultar_a_Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void testStatelessEjb() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
}
