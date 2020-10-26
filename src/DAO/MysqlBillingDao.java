package DAO;

import DTO.Customer;
import Exceptions.DaoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andrej Gorochov D00218937
 */
public class MysqlBillingDao extends MysqlDao implements BillingDaoInterface
{

    @Override
    public List<Customer> getCustomersAndUnbilledSums() throws DaoException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Customer> customers = new ArrayList<>();
        String query;

        try
        {
            //Get connection object using the methods in the super class (MySqlDao.java)...
            con = this.getConnection();
            query = "SELECT c.customerid, c.customer_name, c.customer_address, SUM(vt.cost) AS bill_amount"
                    + "	FROM customers c, customer_vehicles cv, vehicles v, vehicle_type_cost vt, tollevents t"
                    + "	WHERE c.customerid = cv.customerid"
                    + "	AND v.id = cv.vehicleid"
                    + "	AND v.vehicle_type = vt.vehicle_type"
                    + "	AND v.id = t.registrationId"
                    + "	AND t.billed = 0"
                    + "	GROUP BY c.customerid";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next())
            {
                customers.add(Customer.createCustomer(rs.getInt("customerid"), rs.getString("customer_name"), rs.getString("customer_address"), rs.getDouble("bill_amount")));
            }
        }
        catch (SQLException e)
        {
            throw new DaoException("getCustomersAndUnbilledSums() " + e.getMessage());
        }
        finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (con != null)
                {
                    freeConnection(con);
                }
            }
            catch (SQLException e)
            {
                throw new DaoException("getCustomersAndUnbilledSums() " + e.getMessage());
            }
        }

        return customers;
    }

    @Override
    public void updateBilledSums(List<Customer> customers) throws DaoException
    {
        Connection con = null;
        PreparedStatement ps = null;
        String query;
        try
        {
            //Get connection object using the methods in the super class (MySqlDao.java)...
            con = this.getConnection();
            query = "UPDATE tollevents"
                    + " SET billed = 1"
                    + " WHERE photoId IN("
                    + " SELECT t.photoId"
                    + " FROM tollevents t, customer_vehicles cv"
                    + " WHERE t.registrationId = cv.vehicleid"
                    + " AND (cv.customerid = -1";
            for (Customer c : customers)
            {
                query += " OR cv.customerid = " + c.getId();
            }
            query += "))";
            //  + "OR cv.customerid = 1 OR cv.customerid = 2 OR cv.customerid = 4)"
            //  + "		)";
            ps = con.prepareStatement(query);
            int result = ps.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new DaoException("updateBilledSums() " + e.getMessage());
        }
        finally
        {
            try
            {
                if (ps != null)
                {
                    ps.close();
                }
                if (con != null)
                {
                    freeConnection(con);
                }
            }
            catch (SQLException e)
            {
                throw new DaoException("updateBilledSums() " + e.getMessage());
            }
        }
    }

}
