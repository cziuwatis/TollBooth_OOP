package main;

import DAO.BillingDaoInterface;
import DAO.MysqlBillingDao;
import DTO.Customer;
import DTO.TollEvent;
import Exceptions.DaoException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Andrej Gorochov D00218937
 */
public class TollEventProcessor
{

    final public static String DEFAULT_TOLL_EVENT_DISPLAY_FORMAT = "%-15s %-20s %s\n";
    final public static String DEFAULT_BILLING_DISPLAY_FORMAT = "%-7s | %-30s | %-10s | %s\n";
    final public static int MENU_BORDER_LENGTH = 20;
    final public static String MENU_BORDER = "=||=";
    final public static String MAIN_MENU_OPTIONS = "\n 1. Load registration numbers\n 2. Process toll events\n 3. Write toll events to database\n 4. TollEventDao methods\n 5. Reset toll events\n 6. Send heartbeat\n 7. Do billing\n 0. Exit";
    final public static String DAO_METHODS_MENU_OPTIONS = "\n 1. Get toll events by registration from DB\n 2. Get toll events since a certain date time from DB\n 3. Get toll events between two dates from DB\n 4. Get all unique registrations that passed through the toll from DB\n 5. Get all toll events from DB\n 6. Get toll event details list\n 0. Exit";
    final private TollBooth tollBooth;
    private boolean running;

    /**
     * Default constructor for the TollEventProcessor application.
     */
    public TollEventProcessor()
    {
        tollBooth = new TollBooth();
        running = false;
    }

    /**
     * Method through which the application is run.
     */
    public void run()
    {
        this.running = true;
        Utilities.printMenu(MENU_BORDER, MENU_BORDER_LENGTH, MAIN_MENU_OPTIONS, "Main Menu");
        while (this.running)
        {
            int choice = Utilities.getInt("Enter option (8 to print menu): ", 0, 8);
            switch (choice)
            {
                case 0:
                    System.out.println("\n\nEnding...\n\n");
                    this.tollBooth.closeConnection();
                    this.running = false;
                    break;
                case 1:
                    loadRegistrations();
                    break;
                case 2:
                    processTollEvents();
                    break;
                case 3:
                    writeTollEventsToDatabase();
                    break;
                case 4:
                    tollEventDaoMethodsMenu();
                    break;
                case 5:
                    resetTollEvents();
                    System.out.println("Toll events reset. Programme back to initial state.");
                    break;
                case 6:
                    tollBooth.sendHeartbeat();
                    break;
                case 7:
                    doBilling();
                    break;
                case 8:
                    Utilities.printMenu(MENU_BORDER, MENU_BORDER_LENGTH, MAIN_MENU_OPTIONS, "Main Menu");
                    break;
                default:
                    System.err.println("Incorrect option entered! Try again.");
                    break;
            }
        }
    }

    /**
     * Displays all customer information and their amount due. If selected to
     * process, bills will be processed (set to billed in db)
     */
    private void doBilling()
    {
        try
        {
            BillingDaoInterface IBillingDao = new MysqlBillingDao();
            List<Customer> customers = IBillingDao.getCustomersAndUnbilledSums();
            System.out.println("");
            Utilities.printString(MENU_BORDER, MENU_BORDER_LENGTH);
            System.out.println("\n\tDISPLAYING BILLS");
            Utilities.printString(MENU_BORDER, MENU_BORDER_LENGTH);
            System.out.println("\n");
            displayBills(customers);
            Utilities.printString(MENU_BORDER, MENU_BORDER_LENGTH);
            System.out.println("");
            if (!customers.isEmpty() && Utilities.getYesNoAnswer("Do you want to process the bills?(y/n) "))
            {
                System.out.println("Chosen to process bills!");
                IBillingDao.updateBilledSums(customers);
                System.out.println("Bills have been processed");
            }
        }
        catch (DaoException e)
        {
            System.err.println("An error occurred while trying to do billing. Try again later.");
        }
    }

    /**
     * Displays customer bills neatly in a table.
     *
     * @param customers customers whose bills to display.
     */
    private void displayBills(List<Customer> customers)
    {
        if (customers.isEmpty())
        {
            System.out.println("\n***NO BILLS TO DISPLAY***\n");
        }
        else
        {
            System.out.printf(DEFAULT_BILLING_DISPLAY_FORMAT, "ID", "Name", "€ Amount", "Address");
            for (Customer c : customers)
            {
                System.out.printf(DEFAULT_BILLING_DISPLAY_FORMAT, c.getId(), Utilities.cutAndAppendString(c.getName(), 27, "..."), c.getBillAmount(), c.getAddress());
            }
            System.out.println("");
        }
    }

    /**
     * Resets all toll event variables for easier demonstration purposes.
     */
    private void resetTollEvents()
    {
        this.tollBooth.resetTollEvents();
    }

    /**
     * Attempts to write all present toll events to database.
     */
    private void writeTollEventsToDatabase()
    {
        if (this.tollBooth.writeTollEventsToDatabase())
        {
            System.out.println("\nSuccessfully written to database!\n");
        }
    }

    /**
     * Reads toll events from toll events csv and displays all of them,
     * including invalid registrations.
     */
    private void processTollEvents()
    {
        if (this.tollBooth.getRegistrations().size() <= 0)
        {
            System.err.println("There are no registrations loaded, all toll events will be invalid!");
        }
        if (!this.tollBooth.getTollEvents().isEmpty() || !this.tollBooth.getInvalidRegistrations().isEmpty())
        {
            System.err.println("Due to the nature of the application (same "
                    + "events, same file), you are required to write to the database "
                    + "first before processing toll events another time.");
        }
        else
        {
            System.out.println("Processing toll events...");
            this.tollBooth.readInTollEvents("Toll-Events.csv");
            displayTollEvents(this.tollBooth.getTollEvents());
            System.out.println("\nInvalid toll event registrations: ");
            displayTollEvents(this.tollBooth.getInvalidRegistrations());
            System.out.println("\t***END OF INVALID***\t");
//            displayStrings("Invalid toll event registrations: ", this.tollBooth.getInvalidRegistrations());
            //    this.tollBooth.clearInvalidRegistrations();//QUESTION AROUND HERE, WHAT TO DO? -> If I run this method before loading it will have 
            //the invalid registrations which are actually valid. Do I allow to run this without loading?
        }
    }

    /**
     * Loads all vehicle registrations in.
     */
    private void loadRegistrations()
    {
        if (tollBooth.loadRegistrations())//if loaded success and display the regs
        {
            System.out.println("Successfully loaded vehicles");
            displayStrings("REGISTRATIONS", tollBooth.getRegistrations());
        }
        else
        {
            System.out.println("Failed to load vehicles.");
        }
    }

    /**
     * All toll event dao methods are run from this menu.
     */
    private void tollEventDaoMethodsMenu()
    {
        int choice = 1;
        while (choice != 0)
        {
            Utilities.printMenu(MENU_BORDER, MENU_BORDER_LENGTH, DAO_METHODS_MENU_OPTIONS, "Main Menu");
            choice = Utilities.getInt("Enter option: ", 0, 6);
            try
            {
                switch (choice)
                {
                    case 1:
                        System.out.println("Getting toll events by registration\n\n");
                        displayTollEvents(this.tollBooth.getTollEventsByRegistration(Utilities.getStringNotEmpty("Enter registration: ")));
                        break;
                    case 2:
                        System.out.println("Getting toll events since a certain datetime\n\n");
                        displayTollEvents(this.tollBooth.getTollEventsSinceDateTime(Utilities.getLocalDateTimeFromUser("Enter a date and optionally a time (yyyy-mm-dd hh:MM:ss): ")));
                        break;
                    case 3:
                        System.out.println("Getting toll events since a certain datetime\n\n");
                        LocalDateTime startDate = Utilities.getLocalDateTimeFromUser("Enter a start date and time (yyyy-mm-dd hh:MM:ss): ");
                        LocalDateTime endDate = Utilities.getLocalDateTimeFromUser("Enter an end date and time (yyyy-mm-dd hh:MM:ss): ");
                        while (startDate.isAfter(endDate))
                        {
                            System.out.println("End date cannot be earlier than mentioned start date! Start date entered: " + startDate);
                            endDate = Utilities.getLocalDateTimeFromUser("Enter an end date and time (yyyy-mm-dd hh:MM:ss): ");
                        }
                        displayTollEvents(this.tollBooth.getTollEventsBetweenDateTime(Utilities.getLocalDateTimeFromUser("Enter a start date and time (yyyy-mm-dd hh:MM:ss): "), Utilities.getLocalDateTimeFromUser("Enter an end date and time (yyyy-mm-dd hh:MM:ss): ")));
                        break;
                    case 4:
                        System.out.println("Getting all registrations that passed through the toll\n\n");
                        displayStrings("REGISTRATIONS", this.tollBooth.getAllRegistrationsThroughToll());
                        break;
                    case 5:
                        System.out.println("Getting all toll events");
                        displayTollEvents(this.tollBooth.getAllTollEvents());
                        break;
                    case 6:
                        System.out.println("Getting all toll events in a list");
                        displayTollEvents(this.tollBooth.getTollEventDetails());
                        break;
                    default:
                        break;
                }
                Utilities.promptEnterToContinue();
            }
            catch (DaoException e)
            {
                System.out.println("There has been an issue with the request!\n"
                        + e.getMessage() + "\n\n");
                Utilities.promptEnterToContinue();
            }
        }
    }

    /**
     * Displays toll events neatly in a table.
     *
     * @param map map which contains the toll events.
     */
    public static void displayTollEvents(TollEventsMap map)
    {
        if (map.isEmpty())
        {
            System.out.println("\n**** NO TOLL EVENTS TO DISPLAY ****\n");
        }
        else
        {
            System.out.printf(DEFAULT_TOLL_EVENT_DISPLAY_FORMAT, "Registration", "PhotoId", "Timestamp");
            for (String key : map.keySet())
            {
                for (TollEvent t : map.get(key))
                {
                    System.out.printf(DEFAULT_TOLL_EVENT_DISPLAY_FORMAT, t.getRegistration(), t.getPhotoId(), t.getTimestamp());
                }
            }
            System.out.println("");
        }
    }

    /**
     * Displays toll events neatly in a table.
     *
     * @param map map which contains the toll events.
     */
    private static void displayTollEvents(Map<String, List<TollEvent>> map)
    {
        if (map.isEmpty())
        {
            System.out.println("\n**** NO TOLL EVENTS TO DISPLAY ****\n");
        }
        else
        {
            System.out.printf(DEFAULT_TOLL_EVENT_DISPLAY_FORMAT, "Registration", "PhotoId", "Timestamp");
            for (Map.Entry<String, List<TollEvent>> entry : map.entrySet())
            {
                for (TollEvent t : entry.getValue())
                {
                    System.out.printf(DEFAULT_TOLL_EVENT_DISPLAY_FORMAT, t.getRegistration(), t.getPhotoId(), t.getTimestamp());
                }
            }
            System.out.println("");
        }
    }

    /**
     * Displays the collection of strings in a column with the specified header.
     *
     * @param header the header of the table which is displayed first before any
     * string.
     * @param strings strings which are to be displayed.
     */
    private static void displayStrings(String header, Collection<String> strings)
    {
        System.out.println(header);
        for (String s : strings)
        {
            System.out.println(s);
        }
        System.out.println("");
    }

    /**
     * Displays toll events neatly in a table.
     *
     * @param eventList toll events which are to be displayed.
     */
    private static void displayTollEvents(List<TollEvent> eventList)
    {
        if (eventList.isEmpty())
        {
            System.out.println("\n**** NO TOLL EVENTS TO DISPLAY ****\n");
        }
        else
        {
            System.out.printf(DEFAULT_TOLL_EVENT_DISPLAY_FORMAT, "Registration", "PhotoId", "Timestamp");
            for (TollEvent t : eventList)
            {
                System.out.printf(DEFAULT_TOLL_EVENT_DISPLAY_FORMAT, t.getRegistration(), t.getPhotoId(), t.getTimestamp());
            }
            System.out.println("");
        }
    }
}
