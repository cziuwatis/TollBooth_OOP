package main;

import DAO.MysqlTollEventDao;
import DAO.TollEventDaoInterface;
import DTO.TollEvent;
import Exceptions.DaoException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author Andrej Gorochov D00218937
 */
public class ClientHandler implements Runnable
{

    private BufferedReader socketReader;
    private PrintWriter socketWriter;
    private Socket socket;
    private int clientNumber;
    private Set<String> registrations;
    private TollEventDaoInterface ITollEventDao;
    private List<TollEvent> invalidTollEvents;

    public ClientHandler(Socket clientSocket, int clientNumber, Set<String> registrations, List<TollEvent> invalidTollEvents)
    {
        try
        {
            InputStreamReader isReader = new InputStreamReader(clientSocket.getInputStream());
            this.socketReader = new BufferedReader(isReader);

            OutputStream os = clientSocket.getOutputStream();
            this.socketWriter = new PrintWriter(os, true); // true => auto flush socket buffer

            this.clientNumber = clientNumber;  // ID number that we are assigning to this client

            this.socket = clientSocket;  // store socket ref for closing 
            this.registrations = registrations;
            this.ITollEventDao = new MysqlTollEventDao();
            this.invalidTollEvents = invalidTollEvents;
            JsonBuilderFactory factory = Json.createBuilderFactory(null);
            JsonObjectBuilder objBuilder = factory.createObjectBuilder();
            JsonObject response = objBuilder
                    .add("PacketType", "RegisterTollBooth")
                    .add("message", clientNumber).build();
            socketWriter.println(response);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public void run()
    {
        String message;
        try
        {
            while (!socket.isClosed()
                    && (message = socketReader.readLine()) != null)
            {
                JsonObject messageJson = Json.createReader(new StringReader(message)).readObject();
                System.out.println("Server: (ClientHandler): Read command from client " + clientNumber + ": " + message);
                if (messageJson.getString("PacketType").equalsIgnoreCase("Heartbeat"))
                {
                    heartbeat();
                }
                else if (messageJson.getString("PacketType").equalsIgnoreCase("GetRegisteredVehicles"))
                {
                    getRegisteredVehicles();
                }
                else if (messageJson.getString("PacketType").equalsIgnoreCase("RegisterValidTollEvents"))
                {
                    registerValidTollEvents(messageJson);
                }
                else if (messageJson.getString("PacketType").equalsIgnoreCase("RegisterInvalidTollEvents"))
                {
                    registerInvalidTollEvents(messageJson);
                }
                else if (messageJson.getString("PacketType").equalsIgnoreCase("Close"))
                {
                    socketReader.close();
                    socketWriter.close();
                    socket.close();
                }
                else
                {
                    JsonBuilderFactory factory = Json.createBuilderFactory(null);
                    JsonObjectBuilder objBuilder = factory.createObjectBuilder();
                    JsonObject response = objBuilder
                            .add("PacketType", "Error")
                            .add("message", "Command unrecognized").build();
                    socketWriter.println(response);
                }
            }
            if (!socket.isClosed())
            {
                socket.close();
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        System.out.println("Server: (ClientHandler): Handler for Client " + clientNumber + " is terminating .....");
    }

    /**
     * Writes current available toll events to the database. If successfully
     * written, clears the toll events from memory.
     *
     * @param map map of toll events to be written to database
     * @return true if successfully written to.
     * @throws DaoException if any DAO exception happens e.g a duplicate entry
     * is being written into database. Needs to be caught and dealt with.
     */
    private boolean writeTollEventsToDatabase(TollEventsMap map) throws DaoException
    {
        boolean isSuccess = false;
        if (this.ITollEventDao.writeToDatabase(map))
        {
            //clearTollEvents();
            isSuccess = true;
        }
        return isSuccess;
    }

    /**
     * Server responds to a heartbeat with a heartbeat response.
     */
    private void heartbeat()
    {
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObjectBuilder objBuilder = factory.createObjectBuilder();
        JsonObject response = objBuilder.add("PacketType", "Heartbeat response").build();
        socketWriter.println(response);
    }

    /**
     * Returns the registered vehicle list to client.
     */
    private void getRegisteredVehicles()
    {
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObjectBuilder objBuilder = factory.createObjectBuilder();
        JsonArrayBuilder registrationsBuilder = factory.createArrayBuilder();
        for (String registration : registrations)
        {
            registrationsBuilder.add(registration);
        }
        JsonObject response = objBuilder.add("PacketType", "ReturnRegisteredVehicles")
                .add("Vehicles", registrationsBuilder).build();
        socketWriter.println(response);
    }

    /**
     * Writes to database all valid toll events. If a toll event is invalid, it
     * will be displayed in server console and not be added.
     *
     * @param messageJson JSON message with data of toll events.
     */
    private void registerValidTollEvents(JsonObject messageJson)
    {
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObjectBuilder objBuilder = factory.createObjectBuilder();
        JsonObject response;
        TollEventsMap map = new TollEventsMap();
        map.fromJson(messageJson.getJsonArray("data"));
        TollEventsMap invalidRegistrations = new TollEventsMap();
        TollEventsMap validRegistrations = new TollEventsMap();
        try
        {
            for (String key : map.keySet())
            {
                for (TollEvent t : map.get(key))
                {
                    if (!registrations.contains(key))
                    {
                        addTollEvent(invalidRegistrations, t);
                        this.invalidTollEvents.add(t);
                    }
                    else
                    {
                        addTollEvent(validRegistrations, t);
                    }
                }
            }
            if (writeTollEventsToDatabase(validRegistrations))
            {
                response = objBuilder.add("PacketType", "RegisteredValidTollEvent").build();
                displayTollEventsIfNotEmpty(invalidRegistrations);
            }
            else
            {
                response = objBuilder.add("PacketType", "Error")
                        .add("Message", "Update failed! Possibly no toll events to be added.")
                        .build();
            }
        }
        catch (DaoException e)
        {
            String errorMessage;
            if (e.getMessage().contains("Duplicate entry"))
            {
                errorMessage = "Trying to write to database duplicate entries, aborting transaction.";
            }
            else
            {
                errorMessage = e.getMessage();
            }
            response = objBuilder
                    .add("PacketType", "Error")
                    .add("Message", errorMessage)
                    .build();
        }
        socketWriter.println(response);
    }

    /**
     * Prints out the sent invalid registrations.
     *
     * @param messageJson JSON message with data of toll events.
     */
    private void registerInvalidTollEvents(JsonObject messageJson)
    {
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObjectBuilder objBuilder = factory.createObjectBuilder();
        JsonObject response = objBuilder.add("PacketType", "RegisteredInvalidTollEvent").build();
        TollEventsMap map = new TollEventsMap();
        TollEventsMap invalidRegistrations = new TollEventsMap();
        map.fromJson(messageJson.getJsonArray("data"));
        for (String key : map.keySet())
        {
            for (TollEvent t : map.get(key))
            {
                if (!registrations.contains(key))
                {
                    addTollEvent(invalidRegistrations, t);
                    this.invalidTollEvents.add(t);
                }
            }
        }
        displayTollEventsIfNotEmpty(invalidRegistrations);
        socketWriter.println(response);
    }

    /**
     * Displays toll events if the map is not empty.
     *
     * @param map map of toll events which is to be displayed.
     */
    private void displayTollEventsIfNotEmpty(TollEventsMap map)
    {
        if (!map.isEmpty())
        {
            System.out.println("Server: (ClientHandler): Handler for Client " + clientNumber + ", displaying invalid registrations: ");
            TollEventProcessor.displayTollEvents(map);
        }
    }

    /**
     * Adds a toll even to the specified map.
     *
     * @param map map to which to add the toll event.
     * @param t toll event which is to be added.
     */
    private synchronized static void addTollEvent(TollEventsMap map, TollEvent t)
    {
        List<TollEvent> events = map.get(t.getRegistration());
        if (events == null)
        {
            events = new ArrayList<>();
        }
        events.add(t);
        map.put(t.getRegistration(), events);
    }
}
