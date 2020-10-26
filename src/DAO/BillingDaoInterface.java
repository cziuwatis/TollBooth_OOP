package DAO;

import DTO.Customer;
import Exceptions.DaoException;
import java.util.List;

/**
 *
 * @author Andrej Gorochov D00218937
 */
public interface BillingDaoInterface
{

    /**
     * Gets customers who have an unbilled sum remaining.
     *
     * @return a list of customers who have some kind of bill amount to pay.
     * @throws DaoException throws exception if any sql exception occurs in the
     * dao.
     */
    public List<Customer> getCustomersAndUnbilledSums() throws DaoException;

    /**
     * Sets the specified customers toll events as billed.
     *
     * @param customers customers whose toll events to set as billed.
     * @throws DaoException if any sql exception occurs in the dao.
     */
    public void updateBilledSums(List<Customer> customers) throws DaoException;
}
