package main;

import DAO.MysqlVehiclesDao;
import DAO.VehiclesDaoInterface;
import DTO.TollEvent;
import Exceptions.DaoException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Andrej Gorochov D00218937
 */
public class Server
{

    private Set<String> registrations;
    private List<TollEvent> invalidTollEvents;

    public static void main(String[] args)
    {
        Server server = new Server();
        server.start();
    }

    /**
     * Loads all registrations from the database. Returns true if successfully
     * loaded, false if a DAO exception occurs.
     *
     * @return true if successfully loaded, false if a DAO exception occurs e.g
     * no database connection.
     */
    private boolean loadRegistrations()
    {

        VehiclesDaoInterface IVehiclesDao = new MysqlVehiclesDao();
        try
        {
            this.registrations = IVehiclesDao.getAllRegistrations();
            return true;

        }
        catch (DaoException e)
        {
            return false;
        }
    }

    public void start()
    {
        try
        {
            invalidTollEvents = new ArrayList<>();
            ServerSocket ss = new ServerSocket(8080);  // set up ServerSocket to listen for connections on port 8080

            System.out.println("Server: Server started. Listening for connections on port 8080...");

            int clientNumber = 0;  // a number for clients that the server allocates as clients connect
            if (!loadRegistrations())
            {
                System.out.println("Server: Failed to load vehicle registrations...");
            }
            else
            {
                while (true)    // loop continuously to accept new client connections
                {
                    Socket socket = ss.accept();    // listen (and wait) for a connection, accept the connection, 
                    // and open a new socket to communicate with the client
                    clientNumber++;

                    System.out.println("Server: Client " + clientNumber + " has connected.");

                    System.out.println("Server: Port# of remote client: " + socket.getPort());
                    System.out.println("Server: Port# of this server: " + socket.getLocalPort());

                    Thread t = new Thread(new ClientHandler(socket, clientNumber, this.registrations, this.invalidTollEvents)); // create a new ClientHandler for the client,
                    t.start();                                                  // and run it in its own thread

                    System.out.println("Server: ClientHandler started in thread for client " + clientNumber + ". ");
                    System.out.println("Server: Listening for further connections...");
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("Server: IOException: " + e);
        }
        System.out.println("Server: Server exiting, Goodbye!");
    }
}
