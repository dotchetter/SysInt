package models;

import java.sql.Timestamp;

public class SensorLogEntry
{
    private float value;
    private Timestamp created;

    public SensorLogEntry(final float temperature, Timestamp created)
    {
        this.value = temperature;
        this.created = created;
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
}
