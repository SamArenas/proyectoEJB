package Interfaz_Cliente;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;



public interface ICliente extends Remote {
   
    public LinkedList<String> Obtener_Produtos() throws RemoteException;;
    public void Enviar_Productos_Comprados (LinkedList<String>Productos) throws RemoteException;;
    
}
