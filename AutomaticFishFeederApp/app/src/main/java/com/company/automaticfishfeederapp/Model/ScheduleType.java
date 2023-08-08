package com.company.automaticfishfeederapp.Model;

public class ScheduleType {

    private int id;
    private String scheduleType;
    private int scheduleTypeColor;

    public ScheduleType(int id,String scheduleType, int scheduleTypeColor)
    {
        this.id=id;
        this.scheduleType = scheduleType;
        this.scheduleTypeColor = scheduleTypeColor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public int getScheduleTypeColor() {
        return scheduleTypeColor;
    }

    public void setScheduleTypeColor(int scheduleTypeColor) {
        this.scheduleTypeColor = scheduleTypeColor;
    }
}
