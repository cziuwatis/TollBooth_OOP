package DAO;

import DTO.Customer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

/**
 *
 * @author andrz
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MysqlBillingDaoTest
{

    public MysqlBillingDaoTest()
    {
    }

    /**
     * Test of getCustomersAndUnbilledSums method, of class MysqlBillingDao.
     */
    @Test
    public void test1GetCustomersAndUnbilledSums() throws Exception
    {
        System.out.println("getCustomersAndUnbilledSums");
        MysqlBillingDao instance = new MysqlBillingDao();
        List<Customer> expResult = new ArrayList<>(Arrays.asList(
                Customer.createCustomer(2, "Alduin Wicketer", "5th Hate St, Magicland", 25),
                Customer.createCustomer(3, "Polly Murphies", "3rd Face St, Dublin", 15),
                Customer.createCustomer(4, "Tom Parator", "85 Peanut St, Cork", 50),
                Customer.createCustomer(5, "Pat Base", "105 Patrick St, Cork", 20),
                Customer.createCustomer(6, "Cody Progs", "353 Genie St, Limerick", 20),
                Customer.createCustomer(7, "Nate Pad", "133 Write St, Dublin", 20),
                Customer.createCustomer(8, "Nat Benns", "7 Ide St, Newcastle West", 70),
                Customer.createCustomer(9, "Jessica Pooter", "2 What St, Waterford", 10),
                Customer.createCustomer(10, "Abe Sreichsion", "1 Concept St, Dundalk", 45),
                Customer.createCustomer(11, "Kim Plements", "1 Action St, Dundalk", 10),
                Customer.createCustomer(12, "Dee Rives", "15 Derivation St, Dublin", 20),
                Customer.createCustomer(13, "Rex Tends", "4 Marriage St, Dublin", 20),
                Customer.createCustomer(14, "Milly Nilly", "21st Pie St, Dublin", 20),
                Customer.createCustomer(15, "Cass Obschleise", "9 Object St, Dublin", 15),
                Customer.createCustomer(16, "Harry Fashion", "8 Styles St, Dublin", 80)
        ));
        List<Customer> result = instance.getCustomersAndUnbilledSums();
        assertEquals(expResult, result);
    }

    @Test
    public void test2GetCustomersAndUnbilledSums() throws Exception //testing partial update of bill sums
    {
        System.out.println("getCustomersAndUnbilledSums");
        MysqlBillingDao instance = new MysqlBillingDao();
        instance.updateBilledSums(new ArrayList<>(Arrays.asList(
                Customer.createCustomer(2, "Alduin Wicketer", "5th Hate St, Magicland", 25)
        )));
        List<Customer> expResult = new ArrayList<>(Arrays.asList(
                Customer.createCustomer(3, "Polly Murphies", "3rd Face St, Dublin", 15),
                Customer.createCustomer(4, "Tom Parator", "85 Peanut St, Cork", 50),
                Customer.createCustomer(5, "Pat Base", "105 Patrick St, Cork", 20),
                Customer.createCustomer(6, "Cody Progs", "353 Genie St, Limerick", 20),
                Customer.createCustomer(7, "Nate Pad", "133 Write St, Dublin", 20),
                Customer.createCustomer(8, "Nat Benns", "7 Ide St, Newcastle West", 70),
                Customer.createCustomer(9, "Jessica Pooter", "2 What St, Waterford", 10),
                Customer.createCustomer(10, "Abe Sreichsion", "1 Concept St, Dundalk", 45),
                Customer.createCustomer(11, "Kim Plements", "1 Action St, Dundalk", 10),
                Customer.createCustomer(12, "Dee Rives", "15 Derivation St, Dublin", 20),
                Customer.createCustomer(13, "Rex Tends", "4 Marriage St, Dublin", 20),
                Customer.createCustomer(14, "Milly Nilly", "21st Pie St, Dublin", 20),
                Customer.createCustomer(15, "Cass Obschleise", "9 Object St, Dublin", 15),
                Customer.createCustomer(16, "Harry Fashion", "8 Styles St, Dublin", 80)
        ));
        List<Customer> result = instance.getCustomersAndUnbilledSums();
        assertEquals(expResult, result);
    }

    @Test
    public void test3GetCustomersAndUnbilledSums() throws Exception //testing full processing of bill sums
    {
        System.out.println("getCustomersAndUnbilledSums");
        MysqlBillingDao instance = new MysqlBillingDao();
        instance.updateBilledSums(new ArrayList<>(Arrays.asList(
                Customer.createCustomer(2, "Alduin Wicketer", "5th Hate St, Magicland", 25),
                Customer.createCustomer(3, "Polly Murphies", "3rd Face St, Dublin", 15),
                Customer.createCustomer(4, "Tom Parator", "85 Peanut St, Cork", 50),
                Customer.createCustomer(5, "Pat Base", "105 Patrick St, Cork", 20),
                Customer.createCustomer(6, "Cody Progs", "353 Genie St, Limerick", 20),
                Customer.createCustomer(7, "Nate Pad", "133 Write St, Dublin", 20),
                Customer.createCustomer(8, "Nat Benns", "7 Ide St, Newcastle West", 70),
                Customer.createCustomer(9, "Jessica Pooter", "2 What St, Waterford", 10),
                Customer.createCustomer(10, "Abe Sreichsion", "1 Concept St, Dundalk", 45),
                Customer.createCustomer(11, "Kim Plements", "1 Action St, Dundalk", 10),
                Customer.createCustomer(12, "Dee Rives", "15 Derivation St, Dublin", 20),
                Customer.createCustomer(13, "Rex Tends", "4 Marriage St, Dublin", 20),
                Customer.createCustomer(14, "Milly Nilly", "21st Pie St, Dublin", 20),
                Customer.createCustomer(15, "Cass Obschleise", "9 Object St, Dublin", 15),
                Customer.createCustomer(16, "Harry Fashion", "8 Styles St, Dublin", 80)
        )));
        List<Customer> expResult = new ArrayList<>();
        List<Customer> result = instance.getCustomersAndUnbilledSums();
        assertEquals(expResult, result);
    }

    /**
     * Test of updateBilledSums method, of class MysqlBillingDao.
     */
//    @Test
//    public void testUpdateBilledSums() throws Exception
//    {
//        System.out.println("updateBilledSums");
//        List<Customer> customers = null;
//        MysqlBillingDao instance = new MysqlBillingDao();
//        instance.updateBilledSums(customers);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
