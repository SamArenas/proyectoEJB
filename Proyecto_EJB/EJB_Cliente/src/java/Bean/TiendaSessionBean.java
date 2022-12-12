package Bean;

import Interfaz_Cliente.ICliente;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.TextMessage;

@Stateless
public class TiendaSessionBean implements ICliente {
    
    
    //Recursos en los cuales se acumularan los pedidos
    
    @Resource(lookup = "jms/Queue")
    private Queue queue;
    @Resource(lookup = "jms/ConnectionFactory")
    private QueueConnectionFactory connectionFactory;
    
    Session session;
    Connection connection;
    
    @PostConstruct
    public void Hacer_la_conexion(){
        try {
            connection = connectionFactory.createConnection();
        } catch (JMSException ex) {
            Logger.getLogger(TiendaSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    
    @PreDestroy
    public void Temina_la_conexion() {
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException ex) {
                Logger.getLogger(TiendaSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
   
    
    

    @Override
    public LinkedList<String> Obtener_Produtos() {
        LinkedList<String>Productos= new LinkedList();
        
        File r= new File("Productos.txt");
        
        try {
            FileReader k = new FileReader(r);
            BufferedReader l = new BufferedReader(k);
            
            String linea=l.readLine();
            
            while(linea!=null){
                Productos.add(linea);
                linea=l.readLine();
            }
            
            l.close();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TiendaSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TiendaSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       return Productos;
    }

   
    
    @Override
    public void Enviar_Productos_Comprados(LinkedList<String>Productos) {
           
          String producto;
          int i=0;
          while(i<Productos.size()){
              producto=Productos.get(i);
              enviarMensajeJMS(producto);
              i++;
          }
               
    }

    public TextMessage crear_mensaje(Session sesion,String mensaje){
          TextMessage message = null;
        try {

            message = session.createTextMessage();
            message.setText(mensaje);

        } catch (JMSException ex) {
            Logger.getLogger(TiendaSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return message;
    }
    
     public void enviarMensajeJMS(String mensajeTexto) {
        try {
            MessageProducer messageProducer;
            TextMessage message;

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            messageProducer = session.createProducer(queue);

            System.out.println("Enviando producto: " + mensajeTexto);
            message = crear_mensaje(session, mensajeTexto);
            messageProducer.send(message);
        } catch (JMSException ex) {
            Logger.getLogger(TiendaSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (JMSException ex) {
                    Logger.getLogger(TiendaSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }
    
    
    
    
}