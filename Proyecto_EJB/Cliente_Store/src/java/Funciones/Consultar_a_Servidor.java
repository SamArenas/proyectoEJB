package Funciones;


import Interfaz_Cliente.ICliente;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;




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
    
    public ICliente StoreBean; 
    public Consultar_a_Servidor(){
        
    }
      
    public LinkedList<String> Obtner_productos(){
        InitialContext ctx;
        LinkedList<String>Productos= new LinkedList();
        try {
            ctx = new InitialContext();
            StoreBean = (ICliente) ctx.lookup("Interfaz.ICliente");
            
            Productos=StoreBean.Obtener_Produtos();
                  
        } catch (NamingException ex) {
            Logger.getLogger(Consultar_a_Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Productos;
    }
    
    
    public void Comprar(LinkedList<String>Productos_Comprados){
        StoreBean.Enviar_Productos_Comprados(Productos_Comprados);
    }
       
}
