package models;

import repositories.SensorType;

import java.util.ArrayList;
import java.util.List;

public class SensorLog
{
    private SensorType sensorType;
    private List<SensorLogEntry> entries;
    public SensorLog(SensorType sensorType, List<SensorLogEntry> entries)
    {
        this.entries = entries;
        this.sensorType = sensorType;
    }

    public SensorLog(SensorType sensorType, SensorLogEntry entry)
    {
        this.entries = new ArrayList<>();
        this.entries.add(entry);
        this.sensorType = sensorType;
    }

    public List<SensorLogEntry> getEntries()
    {
        return entries;
    }

    public void setEntries(List<SensorLogEntry> entries)
    {
        this.entries = entries;
    }

    public SensorType getSensorType()
    {
        return sensorType;
    }

    public void setSensorType(SensorType sensorType)
    {
        this.sensorType = sensorType;
    }
}
