package models;

import java.sql.Timestamp;

public class SensorLogEntry
{
    private float value;
    private Timestamp created;
    private String city;

    public SensorLogEntry(float value, Timestamp created, String city)
    {
        this.value = value;
        this.created = created;
        this.city = city;
    }

    public float getValue()
    {
        return value;
    }

    public void setValue(float value)
    {
        this.value = value;
    }

    public Timestamp getCreated()
    {
        return created;
    }

    public void setCreated(Timestamp created)
    {
        this.created = created;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

}
