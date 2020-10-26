package main;

import DAO.MysqlTollEventDao;
import DAO.TollEventDaoInterface;
import DTO.TollEvent;
import Exceptions.DaoException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author Andrej Gorochov D00218937
 */
public class TollBooth
{

    // private Set<String> invalidRegistrations;
    private TollEventsMap invalidTollEvents;
    private TollEventsMap tollEvents;
    private Set<String> registrations;
    private TollEventDaoInterface ITollEventDao;
    private Socket socket;
    private PrintWriter socketWriter;
    private Scanner socketReader;
    private int id;

    /**
     * Default constructor for TollBooth.
     */
    public TollBooth()
    {
        //    invalidRegistrations = new HashSet<>();
        invalidTollEvents = new TollEventsMap();
        tollEvents = new TollEventsMap();
        registrations = new HashSet<>();
        ITollEventDao = new MysqlTollEventDao();
        this.connectToServer();
    }

    /**
     * Connects to the server.
     */
    private void connectToServer()
    {
        try
        {
            socket = new Socket("localhost", 8080);
            OutputStream os = socket.getOutputStream();
            socketWriter = new PrintWriter(os, true);
            socketReader = new Scanner(socket.getInputStream());

            String response = socketReader.nextLine();
            JsonObject responseJson = Json.createReader(new StringReader(response)).readObject();
            if (responseJson.getString("PacketType").equalsIgnoreCase("RegisterTollBooth"))
            {
                id = responseJson.getInt("message");
            }
            else
            {
                id = 0;
            }
        }
        catch (IOException e)
        {
            System.err.println("Client message: IOException: " + e);
        }
    }

    /**
     * Gets the id of the tollbooth.
     *
     * @return integer id of tollbooth.
     */
    public int getId()
    {
        return id;
    }

    /**
     * Gets a copy of the registrations.
     *
     * @return a copy of the registrations set.
     */
    public Set<String> getRegistrations()
    {
        return new HashSet<>(this.registrations);
    }

    /**
     * Gets a deep copy of the toll events map.
     *
     * @return a deep copy of the toll events map.
     */
    public TollEventsMap getTollEvents()
    {
        return new TollEventsMap(this.tollEvents);
    }

    /**
     * Gets the copy of the set of invalid registrations.
     *
     * @return a copy of the set of invalid registrations.
     */
//    public Set<String> getInvalidRegistrations()
//    {
//        return new HashSet<>(this.invalidRegistrations);
//    }
    public TollEventsMap getInvalidRegistrations()
    {
        return new TollEventsMap(this.invalidTollEvents);
    }

    /**
     * Writes current available toll events to the database. If successfully
     * written, clears the toll events from memory.
     *
     * @return true if successfully written to.
     * @throws DaoException if any DAO exception happens e.g a duplicate entry
     * is being written into database. Needs to be caught and dealt with.
     */
    public boolean writeTollEventsToDatabase()
    {
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObjectBuilder objBuilder = factory.createObjectBuilder();
        JsonObject request = objBuilder
                .add("PacketType", "RegisterValidTollEvents")
                .add("TollBoothId", this.id)
                .add("data", this.tollEvents.toJson())
                .build();
        try
        {
            socketWriter.println(request);
            String response = socketReader.nextLine();
            JsonObject responseJson = Json.createReader(new StringReader(response)).readObject();
            if (responseJson.getString("PacketType").equalsIgnoreCase("RegisteredValidTollEvent"))
            {
                registerInvalidTollEvents();
                clearTollEvents();
                return true;
            }
            else
            {
                System.err.println("\n\n" + responseJson.getString("Message") + "\n\n");
                return false;
            }
        }
        catch (NullPointerException | NoSuchElementException e)
        {
            System.err.println("Client message: Couldn't establish a connection with the server");
            connectToServer();
            return false;
        }
//        if (this.ITollEventDao.writeToDatabase(this.tollEvents))
//        {
//            clearTollEvents();
//            return true;
//        }
//        return false;
    }

    /**
     * Registers the invalid toll events to the server.
     *
     * @return true if successfully registered, false if not.
     */
    public boolean registerInvalidTollEvents()
    {
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObjectBuilder objBuilder = factory.createObjectBuilder();
        JsonObject request = objBuilder
                .add("PacketType", "RegisterInvalidTollEvents")
                .add("TollBoothId", this.id)
                .add("data", this.invalidTollEvents.toJson())
                .build();
        try
        {
            socketWriter.println(request);
            String response = socketReader.nextLine();
            JsonObject responseJson = Json.createReader(new StringReader(response)).readObject();
            if (responseJson.getString("PacketType").equalsIgnoreCase("RegisteredInvalidTollEvent"))
            {
                clearInvalidRegistrations();
                return true;
            }
            else
            {
                System.err.println("\n\n" + responseJson.getString("Message") + "\n\n");
                return false;
            }
        }
        catch (NullPointerException | NoSuchElementException e)
        {
            System.err.println("Client message: Couldn't establish a connection with the server");
            connectToServer();
            return false;
        }
    }

    /**
     * Reads in toll events from the specified file.
     *
     * @param filePath file path of the file from which to read in toll events.
     */
    public void readInTollEvents(String filePath)
    {
        try (Scanner in = new Scanner(new File(filePath + this.id)))
        {
            in.useDelimiter("[\n\r;]+");
            while (in.hasNextLine())
            {
                String[] tollDetails = in.nextLine().split("[\n\r;]+");
                if (tollDetails.length == 3)
                {
                    String registration = tollDetails[0];
                    try
                    {
                        if (this.registrations.contains(registration))
                        {
                            long photoId = Long.parseLong(tollDetails[1]);
                            Timestamp timestamp = Timestamp.from(Instant.parse(tollDetails[2]));
                            addTollEvent(TollEvent.createTollEvent(registration, photoId, timestamp));
                        }
                        else
                        {
                            //          this.invalidRegistrations.add(registration);
                            this.addInvalidTollEvent(TollEvent.createTollEvent(registration,
                                    Long.parseLong(tollDetails[1]),
                                    Timestamp.from(Instant.parse(tollDetails[2]))));
                        }
                    }
                    catch (NumberFormatException | DateTimeParseException e)
                    {
                        //skip this toll event record and add to invalid
                        //       this.invalidRegistrations.add(registration);
                        this.addInvalidTollEvent(TollEvent.createTollEvent(registration, Integer.MAX_VALUE, Timestamp.valueOf("9999-12-30 00:00")));
                    }
                }
            }
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Toll event file not found!");
        }
    }

    /**
     * Loads all registrations from the database. Returns true if successfully
     * loaded, false if any error or connection error to server occurs.
     *
     * @return true if successfully loaded, false if any error or connection
     * error to server occurs.
     */
    public boolean loadRegistrations()
    {
        boolean isSuccess = false;
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObjectBuilder objBuilder = factory.createObjectBuilder();
        JsonObject request = objBuilder
                .add("PacketType", "GetRegisteredVehicles").build();
        try
        {
            socketWriter.println(request);
            String response = socketReader.nextLine();
            JsonObject responseJson = Json.createReader(new StringReader(response)).readObject();
            if (responseJson.containsKey("PacketType") && responseJson.getString("PacketType").equals("ReturnRegisteredVehicles"))
            {
                JsonArray registrationsArray = responseJson.getJsonArray("Vehicles");
                for (int i = 0; i < registrationsArray.size(); i++)
                {
                    registrations.add(registrationsArray.getString(i));
                }
                isSuccess = true;
            }
            else
            {
                System.err.println("Failed to get a list of registered vehicles");
            }
        }
        catch (NullPointerException | NoSuchElementException e)
        {
            System.err.println("Client message: Couldn't establish a connection with the server");
            connectToServer();
        }
        return isSuccess;
    }

//    /**
//     * Loads all registrations from the database. Returns true if successfully
//     * loaded, false if a DAO exception occurs.
//     *
//     * @return true if successfully loaded, false if a DAO exception occurs e.g
//     * no database connection.
//     */
//    public boolean loadRegistrations2()
//    {
//
//        VehiclesDaoInterface IVehiclesDao = new MysqlVehiclesDao();
//        try
//        {
//            this.registrations = IVehiclesDao.getAllRegistrations();
//            return true;
////            readInTollEvents("Toll-Events.csv");
////            System.out.println(invalidRegistrations);
////            System.out.println(tollEvents);
////            ITollEventDao.writeToDatabase(tollEvents);
//
//        }
//        catch (DaoException e)
//        {
//            return false;
//        }
//    }
    /**
     * Adds toll event to the toll event list in memory.
     *
     * @param tollEvent toll event to be added.
     */
    private void addTollEvent(TollEvent tollEvent)
    {
        List<TollEvent> events = this.tollEvents.get(tollEvent.getRegistration());
        if (events == null)
        {
            events = new ArrayList<>();
        }
        events.add(tollEvent);
        this.tollEvents.put(tollEvent.getRegistration(), events);
    }

    /**
     * Adds invalid toll event to the invalid toll event list in memory.
     *
     * @param tollEvent toll event to be added.
     */
    private void addInvalidTollEvent(TollEvent tollEvent)
    {
        List<TollEvent> events = this.invalidTollEvents.get(tollEvent.getRegistration());
        if (events == null)
        {
            events = new ArrayList<>();
        }
        events.add(tollEvent);
        this.invalidTollEvents.put(tollEvent.getRegistration(), events);
    }

    /**
     * Gets all toll events associated with the specified registration. Throws a
     * DaoException which needs to be caught and dealt with. Can occur if for
     * example can't connect to the database or table doesn't exist etc.
     *
     * @param registration registration of which to get toll events.
     * @return a list of toll events that belong to the specified registration
     * number
     * @throws DaoException DaoException which needs to be caught and dealt
     * with, can occur if no database connectivity or if table is missing.
     */
    public List<TollEvent> getTollEventsByRegistration(String registration) throws DaoException
    {
        return this.ITollEventDao.getTollEventsByRegistration(registration);
    }

    /**
     * Gets a list of toll events since the specified date time.
     *
     * @param date local date time from which to get toll events (exclusive).
     * @return a list of toll events that occurred since the specified date time
     * (exclusive).
     * @throws DaoException DaoException can occur if no database connectivity,
     * table doesn't exist etc. Needs to be caught and dealt with.
     */
    public List<TollEvent> getTollEventsSinceDateTime(LocalDateTime date) throws DaoException
    {
        return this.ITollEventDao.getTollEventsSinceDateTime(date);
    }

    /**
     * Gets a list of toll events that happened between the specified start date
     * time and end date time. Throws an illegal argument exception if the start
     * date is after the end date.
     *
     * @param startDate local date time from which to get toll events
     * (exclusive).
     * @param endDate local date time to which to get toll events (exclusive).
     * @return list of toll events that happened between the specified times
     * (exclusive).
     * @throws DaoException DaoException can occur if no database connectivity,
     * table doesn't exist etc. Needs to be caught and dealt with.
     */
    public List<TollEvent> getTollEventsBetweenDateTime(LocalDateTime startDate, LocalDateTime endDate) throws DaoException
    {
        if (startDate.isAfter(endDate))
        {
            throw new IllegalArgumentException("Start date cannot appear after end date!");
        }
        return this.ITollEventDao.getTollEventsBetweenDateTime(startDate, endDate);
    }

    /**
     * Gets a list of all unique registrations that have passed through the toll
     * in alphabetical order.
     *
     * @return a list of string registration numbers in alphabetical order that
     * passed through toll.
     * @throws DaoException DaoException can occur if no database connectivity,
     * table doesn't exist etc. Needs to be caught and dealt with.
     */
    public List<String> getAllRegistrationsThroughToll() throws DaoException
    {
        return this.ITollEventDao.getAllRegistrationsThroughToll();
    }

    /**
     * Gets all toll events and corresponding registration numbers.
     *
     * @return a map where the key is the registration number and the value is a
     * list of toll events corresponding to that registration number.
     * @throws DaoException DaoException can occur if no database connectivity,
     * table doesn't exist etc. Needs to be caught and dealt with.
     */
    public Map<String, List<TollEvent>> getAllTollEvents() throws DaoException
    {
        return this.ITollEventDao.getAllTollEvents();
    }

    /**
     * Gets all toll events in a list.
     *
     * @return a list of toll events
     * @throws DaoException DaoException can occur if no database connectivity,
     * table doesn't exist etc. Needs to be caught and dealt with.
     */
    public List<TollEvent> getTollEventDetails() throws DaoException
    {
        return this.ITollEventDao.getTollEventDetails();
    }

    /**
     * Clears all invalid registrations. (Invalid registrations are removed from
     * memory).
     */
    public void clearInvalidRegistrations()
    {
//        this.invalidRegistrations.clear();
        this.invalidTollEvents.clear();
    }

    /**
     * Resets all variables to empty.
     */
    public void resetTollEvents()
    {
        clearInvalidRegistrations();
        clearTollEvents();
        this.registrations.clear();
    }

    /**
     * Clears all key value pairs in the toll events. (Toll events are removed
     * from memory).
     */
    private void clearTollEvents()
    {
        this.tollEvents.clear();
    }

    /**
     * Sends a heartbeat to a server and awaits for a response from server.
     *
     * @return true if heartbeat response received, false if not.
     */
    public boolean sendHeartbeat()
    {
        boolean isHeartbeat = false;
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObjectBuilder objBuilder = factory.createObjectBuilder();
        JsonObject request = objBuilder
                .add("PacketType", "Heartbeat").build();
        try
        {
            socketWriter.println(request);
            String response = socketReader.nextLine();
            JsonObject responseJson = Json.createReader(new StringReader(response)).readObject();
            if (responseJson.containsKey("PacketType") && responseJson.getString("PacketType").equals("Heartbeat response"))
            {
                System.out.println("\n\nHeartbeat successful!\n\n");
                isHeartbeat = true;
            }
            else
            {
                System.out.println("\n\nHeartbeat NOT successful\n\n");
            }
        }
        catch (NullPointerException | NoSuchElementException e)
        {
            System.err.println("Client message: Couldn't establish a connection with the server");
            connectToServer();
        }
        return isHeartbeat;
    }

    /**
     * Closes connection with server.
     */
    public void closeConnection()
    {
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObjectBuilder objBuilder = factory.createObjectBuilder();
        JsonObject request = objBuilder
                .add("PacketType", "Close").build();
        try
        {
            socketWriter.println(request);
        }
        catch (NullPointerException | NoSuchElementException e)
        {
            //do nothing
        }
    }
}
