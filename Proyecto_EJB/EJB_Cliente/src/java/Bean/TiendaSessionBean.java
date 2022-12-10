/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;


import Interfaz_Cliente.ICliente;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.*;
import javax.annotation.*;
import javax.ejb.*;
import javax.jms.*;
import objeto.Producto;

/**
 *
 * @author beatriz
 */
@Stateless
public class TiendaSessionBean implements ICliente {
    
    ArrayList<Producto> catalogoDeProductos;
    Producto unProducto;
    
    @Resource(lookup = "jms/Queue")
    private Queue queue;
    @Resource(lookup = "jms/ConnectionFactory")
    private QueueConnectionFactory connectionFactory;
    
    Session session;
    Connection connection;

    @PostConstruct
    public void makeConnection() {
        try {
            connection = connectionFactory.createConnection();
        } catch (JMSException ex) {
            Logger.getLogger(TiendaSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @PreDestroy
    public void endConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException ex) {
                Logger.getLogger(TiendaSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public TiendaSessionBean() {
        catalogoDeProductos = new ArrayList();
    }

    @Override
    public void Agregar_Carrito(String res) {

        unProducto = new Producto();
        unProducto.setNombre(res);
        catalogoDeProductos.add(unProducto);
        
        enviarMensajeJMS(res);

    }

    @Override
    public List<String> obtenerProdcutos() {
        
        List<String> listaProductos;
        listaProductos = new ArrayList();
        for (Producto unProducto : catalogoDeProductos) {
            String nombreProducto = unProducto.getNombre();
            listaProductos.add(nombreProducto);
        }
        return listaProductos;
    }

    private TextMessage crearMensajeJMS(Session session, String mensajeTexto) {
        TextMessage message = null;
        try {

            message = session.createTextMessage();
            message.setText(mensajeTexto);

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

            System.out.println("Sending message: " + mensajeTexto);
            message = crearMensajeJMS(session, mensajeTexto);
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

    @Override
    public List consultar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
