package main;

import DTO.TollEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author Andrej Gorochov D00218937
 */
public class TollEventsMap
{

    private final Map<String, List<TollEvent>> map;

    /**
     * Default constructor for toll events map encapsulation class.
     */
    public TollEventsMap()
    {
        map = new HashMap<>();
    }

    /**
     * A constructor to convert a map into the encapsulated toll events map
     * class.
     *
     * @param map map to be converted.
     */
    public TollEventsMap(Map<String, List<TollEvent>> map)
    {
        this.map = new HashMap<>();
        for (Map.Entry<String, List<TollEvent>> entry : map.entrySet())
        {
            List<TollEvent> tempList = new ArrayList<>();
            for (TollEvent t : entry.getValue())
            {
                tempList.add(TollEvent.createTollEvent(t));
            }
            this.map.put(entry.getKey(), tempList);
        }
    }

    /**
     * Constructor to make a deep copy of the toll events map object.
     *
     * @param map map to be copied.
     */
    public TollEventsMap(TollEventsMap map)
    {
        this.map = new HashMap<>();
        for (String key : map.keySet())
        {
            List<TollEvent> tempList = new ArrayList<>();
            for (TollEvent t : map.get(key))
            {
                tempList.add(TollEvent.createTollEvent(t));
            }
            this.map.put(key, tempList);
        }
    }

    /**
     * Gets a copy of a list of toll events for the specified registration
     * number.
     *
     * @param key registration number of which toll events are to be gotten of.
     * @return list of toll events of the specified registration number.
     */
    public List<TollEvent> get(String key)
    {
        List<TollEvent> eList = this.map.get(key);
        if (eList == null)
        {
            return null;
        }
        List<TollEvent> tempEList = new ArrayList<>();
        for (TollEvent te : eList)
        {
            tempEList.add(TollEvent.createTollEvent(te));
        }
        return tempEList;
    }

    /**
     * Puts the specified toll events to the specified registration number.
     *
     * @param key registration number to which to put the toll events.
     * @param tollEvents toll events of the specified registration number.
     */
    public void put(String key, List<TollEvent> tollEvents)
    {
        List<TollEvent> tempEList = new ArrayList<>();
        for (TollEvent t : tollEvents)
        {
            tempEList.add(TollEvent.createTollEvent(t));
        }
        map.put(key, tempEList);
    }

    /**
     * Clears the map from memory.
     */
    public void clear()
    {
        map.clear();
    }

    /**
     * Returns a set of all the registration numbers in the map.
     *
     * @return set of all registration numbers in the map.
     */
    public Set<String> keySet()
    {
        return new HashSet<>(map.keySet());
    }

    /**
     * Checks whether the map is empty (if it contains a key-value pair).
     *
     * @return true if it is empty, false if it isn't.
     */
    public boolean isEmpty()
    {
        return map.isEmpty();
    }

    /**
     * Turns the map data into a json array.
     *
     * @return json array of registrations with their toll events.
     */
    public JsonArray toJson()
    {
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObjectBuilder objBuilder = factory.createObjectBuilder();
        JsonArrayBuilder registrationsBuilder = factory.createArrayBuilder();
        for (Map.Entry<String, List<TollEvent>> entry : map.entrySet())
        {
            JsonArrayBuilder tollEventsBuilder = factory.createArrayBuilder();
            for (TollEvent t : entry.getValue())
            {
                tollEventsBuilder.add(
                        factory.createObjectBuilder()
                                .add("registration", t.getRegistration())
                                .add("photoId", t.getPhotoId())
                                .add("timestamp", t.getTimestamp().toString())
                                .build()
                );
            }
            registrationsBuilder.add(
                    factory.createObjectBuilder()
                            .add("registration", entry.getKey())
                            .add("tollEvents", tollEventsBuilder)
                            .build()
            );
        }
        return registrationsBuilder.build();
    }

    /**
     * Fills a map with the json array of toll events.
     *
     * @param arrayJson json array of registrations with their toll events.
     */
    public void fromJson(JsonArray arrayJson)
    {
        for (int i = 0; i < arrayJson.size(); i++)
        {
            JsonObject entry = arrayJson.getJsonObject(i);
            JsonArray tollEventsJson = entry.getJsonArray("tollEvents");
            List<TollEvent> tollEvents = new ArrayList<>();
            for (int j = 0; j < tollEventsJson.size(); j++)
            {
                JsonObject tollEvent = tollEventsJson.getJsonObject(j);
                tollEvents.add(TollEvent.createTollEvent(
                        tollEvent.getString("registration"),
                        tollEvent.getJsonNumber("photoId").longValue(),
                        Timestamp.valueOf(tollEvent.getString("timestamp")))
                );
            }
            map.put(entry.getString("registration"), tollEvents);
        }
    }
}
