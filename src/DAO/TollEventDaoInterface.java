package DAO;

import DTO.TollEvent;
import Exceptions.DaoException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import main.TollEventsMap;

/**
 *
 * @author Andrej Gorochov D00218937
 */
public interface TollEventDaoInterface
{

    /**
     * Writes specified toll events to the database.
     *
     * @param tollEvents toll events to be written to database.
     * @return true if successfully written to.
     * @throws DaoException DaoException can occur if no database connectivity,
     * table doesn't exist, duplicate entry etc. Needs to be caught and dealt
     * with.
     */
    public boolean writeToDatabase(TollEventsMap tollEvents) throws DaoException;

    /**
     * Gets all toll events from the database.
     *
     * @return a list of toll events from database.
     * @throws DaoException DaoException can occur if no database connectivity,
     * table doesn't exist, duplicate entry etc. Needs to be caught and dealt
     * with.
     */
    public List<TollEvent> getTollEventDetails() throws DaoException;

    /**
     * Gets all toll events associated with the specified registration.
     *
     * @param registration registration number whose toll events to get.
     * @return list of toll events belonging to the specified registration
     * number.
     * @throws DaoException DaoException can occur if no database connectivity,
     * table doesn't exist, duplicate entry etc. Needs to be caught and dealt
     * with.
     */
    public List<TollEvent> getTollEventsByRegistration(String registration) throws DaoException;

    /**
     * Gets a list of toll events since the specified date time.
     *
     * @param date local date time from which to get toll events (exclusive).
     * @return a list of toll events that occurred since the specified date time
     * (exclusive).
     * @throws DaoException DaoException can occur if no database connectivity,
     * table doesn't exist, duplicate entry etc. Needs to be caught and dealt
     * with.
     */
    public List<TollEvent> getTollEventsSinceDateTime(LocalDateTime date) throws DaoException; //-> do we still use timestamp here or can we ues localdatetime?

    /**
     *
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
     * table doesn't exist, duplicate entry etc. Needs to be caught and dealt
     * with.
     */
    public List<TollEvent> getTollEventsBetweenDateTime(LocalDateTime startDate, LocalDateTime endDate) throws DaoException;

    /**
     *
     * Gets a list of all unique registrations that have passed through the toll
     * in alphabetical order.
     *
     * @return a list of string registration numbers in alphabetical order that
     * passed through toll.
     * @throws DaoException DaoException can occur if no database connectivity,
     * table doesn't exist, duplicate entry etc. Needs to be caught and dealt
     * with.
     */
    public List<String> getAllRegistrationsThroughToll() throws DaoException;

    /**
     *
     * Gets all toll events and corresponding registration numbers.
     *
     * @return a map where the key is the registration number and the value is a
     * list of toll events corresponding to that registration number.
     * @throws DaoException DaoException can occur if no database connectivity,
     * table doesn't exist, duplicate entry etc. Needs to be caught and dealt
     * with.
     */
    public Map<String, List<TollEvent>> getAllTollEvents() throws DaoException;

}
