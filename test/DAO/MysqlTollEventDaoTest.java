package DAO;

import DTO.TollEvent;
import Exceptions.DaoException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import main.TollEventsMap;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author andrz
 */
public class MysqlTollEventDaoTest
{

    public MysqlTollEventDaoTest()
    {
    }

    public static void fillTollEventsMap(Map<String, List<TollEvent>> map)
    {
        map.put("152DL345", new ArrayList<>(
                Arrays.asList(
                        TollEvent.createTollEvent("152DL345", 30422, Timestamp.valueOf("2020-02-16 11:16:50"))
                )));
        map.put("161C3457", new ArrayList<>(
                Arrays.asList(
                        TollEvent.createTollEvent("161C3457", 30410, Timestamp.valueOf("2020-02-14 22:15:38"))
                )));
        map.put("181MH3456", new ArrayList<>(
                Arrays.asList(
                        TollEvent.createTollEvent("181MH3456", 30436, Timestamp.valueOf("2020-02-17 17:33:05")),
                        TollEvent.createTollEvent("181MH3456", 30437, Timestamp.valueOf("2020-02-17 18:20:06"))
                )));
        map.put("181MH3458", new ArrayList<>(
                Arrays.asList(
                        TollEvent.createTollEvent("181MH3458", 30438, Timestamp.valueOf("2020-02-17 18:20:07"))
                )));
        map.put("181MH3459", new ArrayList<>(
                Arrays.asList(
                        TollEvent.createTollEvent("181MH3459", 30439, Timestamp.valueOf("2020-02-17 18:58:08"))
                )));
        map.put("181MH3461", new ArrayList<>(
                Arrays.asList(
                        TollEvent.createTollEvent("181MH3461", 30441, Timestamp.valueOf("2020-02-17 23:25:10"))
                )));
        map.put("191LH1111", new ArrayList<>(
                Arrays.asList(
                        TollEvent.createTollEvent("191LH1111", 30402, Timestamp.valueOf("2020-02-14 10:15:30")),
                        TollEvent.createTollEvent("191LH1111", 30411, Timestamp.valueOf("2020-02-14 23:15:39")),
                        TollEvent.createTollEvent("191LH1111", 30421, Timestamp.valueOf("2020-02-16 11:16:49")),
                        TollEvent.createTollEvent("191LH1111", 30432, Timestamp.valueOf("2020-02-17 13:20:01"))
                )));
        map.put("191LH1112", new ArrayList<>(
                Arrays.asList(
                        TollEvent.createTollEvent("191LH1112", 30412, Timestamp.valueOf("2020-02-15 12:15:40"))
                )));
        map.put("191LH1113", new ArrayList<>(
                Arrays.asList(
                        TollEvent.createTollEvent("191LH1113", 30413, Timestamp.valueOf("2020-02-15 12:15:41"))
                )));
        map.put("191LH1114", new ArrayList<>(
                Arrays.asList(
                        TollEvent.createTollEvent("191LH1114", 30414, Timestamp.valueOf("2020-02-15 12:15:42"))
                )));
        map.put("192D33457", new ArrayList<>(
                Arrays.asList(
                        TollEvent.createTollEvent("192D33457", 30409, Timestamp.valueOf("2020-02-14 16:15:37"))
                )));
        map.put("201CN3456", new ArrayList<>(
                Arrays.asList(
                        TollEvent.createTollEvent("201CN3456", 30433, Timestamp.valueOf("2020-02-17 14:25:02"))
                )));
        map.put("201CN3457", new ArrayList<>(
                Arrays.asList(
                        TollEvent.createTollEvent("201CN3457", 30434, Timestamp.valueOf("2020-02-17 16:20:03"))
                )));
        map.put("201LH3025", new ArrayList<>(
                Arrays.asList(
                        TollEvent.createTollEvent("201LH3025", 30408, Timestamp.valueOf("2020-02-14 15:15:36"))
                )));
        map.put("201LH304", new ArrayList<>(
                Arrays.asList(
                        TollEvent.createTollEvent("201LH304", 30405, Timestamp.valueOf("2020-02-14 13:15:33")),
                        TollEvent.createTollEvent("201LH304", 30415, Timestamp.valueOf("2020-02-15 12:15:43")),
                        TollEvent.createTollEvent("201LH304", 30423, Timestamp.valueOf("2020-02-16 11:16:51")),
                        TollEvent.createTollEvent("201LH304", 30435, Timestamp.valueOf("2020-02-17 16:20:04"))
                )));
        map.put("201LH305", new ArrayList<>(
                Arrays.asList(
                        TollEvent.createTollEvent("201LH305", 30416, Timestamp.valueOf("2020-02-15 12:15:44")),
                        TollEvent.createTollEvent("201LH305", 30424, Timestamp.valueOf("2020-02-16 11:16:52"))
                )));
        map.put("201LH306", new ArrayList<>(
                Arrays.asList(
                        TollEvent.createTollEvent("201LH306", 30417, Timestamp.valueOf("2020-02-15 12:15:45"))
                )));
        map.put("201LH3064", new ArrayList<>(
                Arrays.asList(
                        TollEvent.createTollEvent("201LH3064", 30425, Timestamp.valueOf("2020-02-16 11:16:53"))
                )));
        map.put("201LH307", new ArrayList<>(
                Arrays.asList(
                        TollEvent.createTollEvent("201LH307", 30418, Timestamp.valueOf("2020-02-15 12:15:46"))
                )));
        map.put("201LH3076", new ArrayList<>(
                Arrays.asList(
                        TollEvent.createTollEvent("201LH3076", 30426, Timestamp.valueOf("2020-02-16 11:16:54"))
                )));
        map.put("201LH308", new ArrayList<>(
                Arrays.asList(
                        TollEvent.createTollEvent("201LH308", 30419, Timestamp.valueOf("2020-02-15 21:15:47"))
                )));
        map.put("201LH3083", new ArrayList<>(
                Arrays.asList(
                        TollEvent.createTollEvent("201LH3083", 30427, Timestamp.valueOf("2020-02-16 11:16:55"))
                )));
        map.put("201LH309", new ArrayList<>(
                Arrays.asList(
                        TollEvent.createTollEvent("201LH309", 30428, Timestamp.valueOf("2020-02-16 11:16:56"))
                )));
        map.put("201LH310", new ArrayList<>(
                Arrays.asList(
                        TollEvent.createTollEvent("201LH310", 30429, Timestamp.valueOf("2020-02-16 11:16:57"))
                )));
        map.put("201LH311", new ArrayList<>(
                Arrays.asList(
                        TollEvent.createTollEvent("201LH311", 30430, Timestamp.valueOf("2020-02-16 11:16:58"))
                )));
        map.put("201LH312", new ArrayList<>(
                Arrays.asList(
                        TollEvent.createTollEvent("201LH312", 30431, Timestamp.valueOf("2020-02-16 11:16:59"))
                )));
    }

    /**
     * Test of getTollEventsByRegistration method, of class MysqlTollEventDao.
     */
    @Test
    public void test1GetTollEventsByRegistration() throws Exception
    {
        System.out.println("getTollEventsByRegistration");
        String registration = "";
        MysqlTollEventDao instance = new MysqlTollEventDao();
        List<TollEvent> result = instance.getTollEventsByRegistration(registration);
        assertEquals(0, result.size());
    }

    /**
     * Test of getTollEventsByRegistration method, of class MysqlTollEventDao.
     */
    @Test
    public void test2GetTollEventsByRegistration() throws Exception
    {
        System.out.println("getTollEventsByRegistration");
        String registration = null;
        MysqlTollEventDao instance = new MysqlTollEventDao();
        List<TollEvent> result = instance.getTollEventsByRegistration(registration);
        assertEquals(0, result.size());
    }

    /**
     * Test of getTollEventsByRegistration method, of class MysqlTollEventDao.
     */
    @Test
    public void test3GetTollEventsByRegistration() throws Exception
    {
        System.out.println("getTollEventsByRegistration");
        String registration = "191LH1111";
        MysqlTollEventDao instance = new MysqlTollEventDao();
        List<TollEvent> result = instance.getTollEventsByRegistration(registration);
        assertEquals(4, result.size());
        List<TollEvent> expResult = new ArrayList<>(
                Arrays.asList(
                        TollEvent.createTollEvent("191LH1111", 30402, Timestamp.valueOf("2020-02-14 10:15:30")),
                        TollEvent.createTollEvent("191LH1111", 30411, Timestamp.valueOf("2020-02-14 23:15:39")),
                        TollEvent.createTollEvent("191LH1111", 30421, Timestamp.valueOf("2020-02-16 11:16:49")),
                        TollEvent.createTollEvent("191LH1111", 30432, Timestamp.valueOf("2020-02-17 13:20:01"))));
        assertTrue(result.containsAll(expResult));
    }

    /**
     * Test of getTollEventsByRegistration method, of class MysqlTollEventDao.
     */
    @Test
    public void test4etTollEventsByRegistration() throws Exception
    {
        System.out.println("getTollEventsByRegistration");
        String registration = "151DL200";
        MysqlTollEventDao instance = new MysqlTollEventDao();
        List<TollEvent> result = instance.getTollEventsByRegistration(registration);
        assertEquals(0, result.size());
        List<TollEvent> expResult = new ArrayList<>();
        assertTrue(result.containsAll(expResult));
    }

    /**
     * Test of getAllTollEvents method, of class MysqlTollEventDao.
     */
    @Test
    public void test5GetAllTollEvents() throws Exception
    {
        System.out.println("getAllTollEvents");
        MysqlTollEventDao instance = new MysqlTollEventDao();
        Map<String, List<TollEvent>> expResult = new HashMap<>();
        fillTollEventsMap(expResult);
        try
        {
            instance.writeToDatabase(new TollEventsMap(expResult));
        }
        catch (DaoException e)
        {
            //duplicates may happen (just so database isn't empty)
        }
        Map<String, List<TollEvent>> result = instance.getAllTollEvents();
        assertEquals(26, result.size()); //unique registrations
        assertEquals(expResult, result);
    }

    /**
     * Test of getTollEventDetails method, of class MysqlTollEventDao.
     */
    @Test
    public void test6GetTollEventDetails() throws Exception
    {
        System.out.println("getTollEventDetails");
        MysqlTollEventDao instance = new MysqlTollEventDao();
        List<TollEvent> expResult = new ArrayList<>(Arrays.asList(
                TollEvent.createTollEvent("152DL345", 30422, Timestamp.valueOf("2020-02-16 11:16:50")),
                TollEvent.createTollEvent("161C3457", 30410, Timestamp.valueOf("2020-02-14 22:15:38")),
                TollEvent.createTollEvent("181MH3456", 30436, Timestamp.valueOf("2020-02-17 17:33:05")),
                TollEvent.createTollEvent("181MH3456", 30437, Timestamp.valueOf("2020-02-17 18:20:06")),
                TollEvent.createTollEvent("181MH3458", 30438, Timestamp.valueOf("2020-02-17 18:20:07")),
                TollEvent.createTollEvent("181MH3459", 30439, Timestamp.valueOf("2020-02-17 18:58:08")),
                TollEvent.createTollEvent("181MH3461", 30441, Timestamp.valueOf("2020-02-17 23:25:10")),
                TollEvent.createTollEvent("191LH1111", 30402, Timestamp.valueOf("2020-02-14 10:15:30")),
                TollEvent.createTollEvent("191LH1111", 30411, Timestamp.valueOf("2020-02-14 23:15:39")),
                TollEvent.createTollEvent("191LH1111", 30421, Timestamp.valueOf("2020-02-16 11:16:49")),
                TollEvent.createTollEvent("191LH1111", 30432, Timestamp.valueOf("2020-02-17 13:20:01")),
                TollEvent.createTollEvent("191LH1112", 30412, Timestamp.valueOf("2020-02-15 12:15:40")),
                TollEvent.createTollEvent("191LH1113", 30413, Timestamp.valueOf("2020-02-15 12:15:41")),
                TollEvent.createTollEvent("191LH1114", 30414, Timestamp.valueOf("2020-02-15 12:15:42")),
                TollEvent.createTollEvent("192D33457", 30409, Timestamp.valueOf("2020-02-14 16:15:37")),
                TollEvent.createTollEvent("201CN3456", 30433, Timestamp.valueOf("2020-02-17 14:25:02")),
                TollEvent.createTollEvent("201CN3457", 30434, Timestamp.valueOf("2020-02-17 16:20:03")),
                TollEvent.createTollEvent("201LH3025", 30408, Timestamp.valueOf("2020-02-14 15:15:36")),
                TollEvent.createTollEvent("201LH304", 30405, Timestamp.valueOf("2020-02-14 13:15:33")),
                TollEvent.createTollEvent("201LH304", 30415, Timestamp.valueOf("2020-02-15 12:15:43")),
                TollEvent.createTollEvent("201LH304", 30423, Timestamp.valueOf("2020-02-16 11:16:51")),
                TollEvent.createTollEvent("201LH304", 30435, Timestamp.valueOf("2020-02-17 16:20:04")),
                TollEvent.createTollEvent("201LH305", 30416, Timestamp.valueOf("2020-02-15 12:15:44")),
                TollEvent.createTollEvent("201LH305", 30424, Timestamp.valueOf("2020-02-16 11:16:52")),
                TollEvent.createTollEvent("201LH306", 30417, Timestamp.valueOf("2020-02-15 12:15:45")),
                TollEvent.createTollEvent("201LH3064", 30425, Timestamp.valueOf("2020-02-16 11:16:53")),
                TollEvent.createTollEvent("201LH307", 30418, Timestamp.valueOf("2020-02-15 12:15:46")),
                TollEvent.createTollEvent("201LH3076", 30426, Timestamp.valueOf("2020-02-16 11:16:54")),
                TollEvent.createTollEvent("201LH308", 30419, Timestamp.valueOf("2020-02-15 21:15:47")),
                TollEvent.createTollEvent("201LH3083", 30427, Timestamp.valueOf("2020-02-16 11:16:55")),
                TollEvent.createTollEvent("201LH309", 30428, Timestamp.valueOf("2020-02-16 11:16:56")),
                TollEvent.createTollEvent("201LH310", 30429, Timestamp.valueOf("2020-02-16 11:16:57")),
                TollEvent.createTollEvent("201LH311", 30430, Timestamp.valueOf("2020-02-16 11:16:58")),
                TollEvent.createTollEvent("201LH312", 30431, Timestamp.valueOf("2020-02-16 11:16:59"))
        ));
        List<TollEvent> result = instance.getTollEventDetails();
        assertEquals(34, result.size());
        assertEquals(expResult, result);
    }

    /**
     * Test of getAllRegistrationsThroughToll method, of class
     * MysqlTollEventDao.
     */
    @Test
    public void test7GetAllRegistrationsThroughToll() throws Exception
    {
        System.out.println("getAllRegistrationsThroughToll");
        MysqlTollEventDao instance = new MysqlTollEventDao();
        List<String> expResult = new ArrayList<>(Arrays.asList(
                "152DL345",
                "161C3457",
                "181MH3456",
                "181MH3458",
                "181MH3459",
                "181MH3461",
                "191LH1111",
                "191LH1112",
                "191LH1113",
                "191LH1114",
                "192D33457",
                "201CN3456",
                "201CN3457",
                "201LH3025",
                "201LH304",
                "201LH305",
                "201LH306",
                "201LH3064",
                "201LH307",
                "201LH3076",
                "201LH308",
                "201LH3083",
                "201LH309",
                "201LH310",
                "201LH311",
                "201LH312"
        ));
        List<String> result = instance.getAllRegistrationsThroughToll();
        assertEquals(26, result.size());
        assertEquals(expResult, result);
    }

    /**
     * Test of getTollEventsSinceDateTime method, of class MysqlTollEventDao.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test8GetTollEventsSinceDateTime() throws Exception
    {
        System.out.println("getTollEventsSinceDateTime");
        LocalDateTime date = null;
        MysqlTollEventDao instance = new MysqlTollEventDao();
        List<TollEvent> result = instance.getTollEventsSinceDateTime(date);
    }

    @Test
    public void test9GetTollEventsSinceDateTime() throws Exception
    {
        System.out.println("getTollEventsSinceDateTime");
        LocalDateTime date = LocalDateTime.parse("2021-12-12T00:00:00");
        MysqlTollEventDao instance = new MysqlTollEventDao();
        List<TollEvent> result = instance.getTollEventsSinceDateTime(date);
        assertEquals(0, result.size());
    }

    @Test
    public void test10GetTollEventsSinceDateTime() throws Exception
    {
        System.out.println("getTollEventsSinceDateTime");
        LocalDateTime date = LocalDateTime.parse("2020-02-17T18:20:05");
        MysqlTollEventDao instance = new MysqlTollEventDao();
        List<TollEvent> expResult = new ArrayList<>(Arrays.asList(
                TollEvent.createTollEvent("181MH3456", 30437, Timestamp.valueOf("2020-02-17 18:20:06")),
                TollEvent.createTollEvent("181MH3458", 30438, Timestamp.valueOf("2020-02-17 18:20:07")),
                TollEvent.createTollEvent("181MH3459", 30439, Timestamp.valueOf("2020-02-17 18:58:08")),
                TollEvent.createTollEvent("181MH3461", 30441, Timestamp.valueOf("2020-02-17 23:25:10"))
        ));
        List<TollEvent> result = instance.getTollEventsSinceDateTime(date);
        assertEquals(4, result.size());
        assertEquals(expResult, result);
    }

    @Test //border value test for timestamp
    public void test11GetTollEventsSinceDateTime() throws Exception
    {
        System.out.println("getTollEventsSinceDateTime");
        LocalDateTime date = LocalDateTime.parse("2020-02-17T18:20:06");
        MysqlTollEventDao instance = new MysqlTollEventDao();
        List<TollEvent> expResult = new ArrayList<>(Arrays.asList(
                TollEvent.createTollEvent("181MH3458", 30438, Timestamp.valueOf("2020-02-17 18:20:07")),
                TollEvent.createTollEvent("181MH3459", 30439, Timestamp.valueOf("2020-02-17 18:58:08")),
                TollEvent.createTollEvent("181MH3461", 30441, Timestamp.valueOf("2020-02-17 23:25:10"))
        ));
        List<TollEvent> result = instance.getTollEventsSinceDateTime(date);
        assertEquals(3, result.size());
        assertEquals(expResult, result);
    }

    /**
     * Test of getTollEventsBetweenDateTime method, of class MysqlTollEventDao.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test12GetTollEventsBetweenDateTime() throws Exception
    {
        System.out.println("getTollEventsBetweenDateTime");
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        MysqlTollEventDao instance = new MysqlTollEventDao();
        List<TollEvent> result = instance.getTollEventsBetweenDateTime(startDate, endDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test13GetTollEventsBetweenDateTime() throws Exception
    {
        System.out.println("getTollEventsBetweenDateTime");
        LocalDateTime startDate = LocalDateTime.parse("2020-02-17T00:00:00");
        LocalDateTime endDate = null;
        MysqlTollEventDao instance = new MysqlTollEventDao();
        List<TollEvent> result = instance.getTollEventsBetweenDateTime(startDate, endDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test14GetTollEventsBetweenDateTime() throws Exception
    {
        System.out.println("getTollEventsBetweenDateTime");
        LocalDateTime startDate = LocalDateTime.parse("2020-02-17T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2020-01-17T00:00:00");
        MysqlTollEventDao instance = new MysqlTollEventDao();
        List<TollEvent> result = instance.getTollEventsBetweenDateTime(startDate, endDate);
    }

    @Test
    public void test15GetTollEventsBetweenDateTime() throws Exception
    {
        System.out.println("getTollEventsBetweenDateTime");
        LocalDateTime startDate = LocalDateTime.parse("2020-02-17T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2020-02-17T16:20:05");
        MysqlTollEventDao instance = new MysqlTollEventDao();
        List<TollEvent> expResult = new ArrayList<>(Arrays.asList(
                TollEvent.createTollEvent("191LH1111", 30432, Timestamp.valueOf("2020-02-17 13:20:01")),
                TollEvent.createTollEvent("201CN3456", 30433, Timestamp.valueOf("2020-02-17 14:25:02")),
                TollEvent.createTollEvent("201CN3457", 30434, Timestamp.valueOf("2020-02-17 16:20:03")),
                TollEvent.createTollEvent("201LH304", 30435, Timestamp.valueOf("2020-02-17 16:20:04"))
        ));
        List<TollEvent> result = instance.getTollEventsBetweenDateTime(startDate, endDate);
        assertEquals(4, result.size());
        assertEquals(expResult, result);
    }

    @Test//border value test
    public void test16GetTollEventsBetweenDateTime() throws Exception
    {
        System.out.println("getTollEventsBetweenDateTime");
        LocalDateTime startDate = LocalDateTime.parse("2020-02-17T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2020-02-17T16:20:04");
        MysqlTollEventDao instance = new MysqlTollEventDao();
        List<TollEvent> expResult = new ArrayList<>(Arrays.asList(
                TollEvent.createTollEvent("191LH1111", 30432, Timestamp.valueOf("2020-02-17 13:20:01")),
                TollEvent.createTollEvent("201CN3456", 30433, Timestamp.valueOf("2020-02-17 14:25:02")),
                TollEvent.createTollEvent("201CN3457", 30434, Timestamp.valueOf("2020-02-17 16:20:03"))
        ));
        List<TollEvent> result = instance.getTollEventsBetweenDateTime(startDate, endDate);
        assertEquals(3, result.size());
        assertEquals(expResult, result);
    }

    @Test//border value test
    public void test17GetTollEventsBetweenDateTime() throws Exception
    {
        System.out.println("getTollEventsBetweenDateTime");
        LocalDateTime startDate = LocalDateTime.parse("2020-02-17T13:20:00");
        LocalDateTime endDate = LocalDateTime.parse("2020-02-17T16:20:04");
        MysqlTollEventDao instance = new MysqlTollEventDao();
        List<TollEvent> expResult = new ArrayList<>(Arrays.asList(
                TollEvent.createTollEvent("191LH1111", 30432, Timestamp.valueOf("2020-02-17 13:20:01")),
                TollEvent.createTollEvent("201CN3456", 30433, Timestamp.valueOf("2020-02-17 14:25:02")),
                TollEvent.createTollEvent("201CN3457", 30434, Timestamp.valueOf("2020-02-17 16:20:03"))
        ));
        List<TollEvent> result = instance.getTollEventsBetweenDateTime(startDate, endDate);
        assertEquals(3, result.size());
        assertEquals(expResult, result);
    }

    @Test//border value test
    public void test18GetTollEventsBetweenDateTime() throws Exception
    {
        System.out.println("getTollEventsBetweenDateTime");
        LocalDateTime startDate = LocalDateTime.parse("2020-02-17T13:20:01");
        LocalDateTime endDate = LocalDateTime.parse("2020-02-17T16:20:04");
        MysqlTollEventDao instance = new MysqlTollEventDao();
        List<TollEvent> expResult = new ArrayList<>(Arrays.asList(
                TollEvent.createTollEvent("201CN3456", 30433, Timestamp.valueOf("2020-02-17 14:25:02")),
                TollEvent.createTollEvent("201CN3457", 30434, Timestamp.valueOf("2020-02-17 16:20:03"))
        ));
        List<TollEvent> result = instance.getTollEventsBetweenDateTime(startDate, endDate);
        assertEquals(2, result.size());
        assertEquals(expResult, result);
    }

}
