package DTO;

import java.sql.Timestamp;
import java.util.Objects;

/**
 *
 * @author Andrej Gorochov D00218937
 */
final public class TollEvent
{

    private final String registration;
    private final long photoId;
    private final Timestamp timestamp;

    /**
     * Constructor for creating a toll event.
     *
     * @param registration registration number of vehicle whose toll event it
     * is.
     * @param photoId long photo id of the image of vehicle passing through
     * toll.
     * @param timestamp timestamp of when event occurred.
     */
    private TollEvent(String registration, long photoId, Timestamp timestamp)
    {
        this.registration = registration;
        this.photoId = photoId;
        this.timestamp = timestamp;
    }

    /**
     * Constructor for a copy of the toll event.
     *
     * @param te toll event to be copied.
     */
    private TollEvent(TollEvent te)
    {
        this.registration = te.getRegistration();
        this.photoId = te.getPhotoId();
        this.timestamp = te.getTimestamp();
    }

    /**
     * Gets the registration number of the vehicle whose toll event it is.
     *
     * @return String registration number.
     */
    public String getRegistration()
    {
        return registration;
    }

    /**
     * Gets the photo id of the image of the vehicle passing through toll.
     *
     * @return long photo id
     */
    public long getPhotoId()
    {
        return photoId;
    }

    /**
     * Gets the timestamp of when the event occurred.
     *
     * @return timestamp of when event occurred.
     */
    public Timestamp getTimestamp()
    {
        return timestamp;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.registration);
        hash = 23 * hash + (int) (this.photoId ^ (this.photoId >>> 32));
        hash = 23 * hash + Objects.hashCode(this.timestamp);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final TollEvent other = (TollEvent) obj;
        if (this.photoId != other.photoId)
        {
            return false;
        }
        if (!Objects.equals(this.registration, other.registration))
        {
            return false;
        }
        if (!Objects.equals(this.timestamp, other.timestamp))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "TollEvent{" + "registration=" + registration + ", photoId=" + photoId + ", timestamp=" + timestamp + '}';
    }

    /**
     * Factory design pattern to create a toll event.
     *
     * @param registration registration number of vehicle whose toll event this
     * is.
     * @param photoId photo id of the image of the vehicle passing through toll
     * event.
     * @param timestamp timestamp of when the event occurred.
     * @return a new toll event object with specified values.
     */
    public static TollEvent createTollEvent(String registration, long photoId, Timestamp timestamp)
    {
        return new TollEvent(registration, photoId, timestamp);
    }

    /**
     * Factory design pattern for the copy of the toll event object.
     *
     * @param te toll event object to be copied.
     * @return a new 'deep' copy of the toll event object.
     */
    public static TollEvent createTollEvent(TollEvent te)
    {
        return new TollEvent(te);
    }
}
