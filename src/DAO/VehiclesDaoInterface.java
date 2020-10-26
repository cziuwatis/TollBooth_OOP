package DAO;

import Exceptions.DaoException;
import java.util.Set;

/**
 *
 * @author Andrej Gorochov D00218937
 */
public interface VehiclesDaoInterface
{

    /**
     * Gets all the vehicle registrations present in the database.
     *
     * @return set of all vehicle registrations in database.
     * @throws DaoException DaoException can occur if no database connectivity,
     * table doesn't exist, duplicate entry etc. Needs to be caught and dealt
     * with.
     */
    public Set<String> getAllRegistrations() throws DaoException;
}
