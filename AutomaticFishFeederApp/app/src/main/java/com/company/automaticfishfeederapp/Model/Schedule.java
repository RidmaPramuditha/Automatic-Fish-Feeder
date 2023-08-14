package com.company.automaticfishfeederapp.Model;

public class Schedule {

    private String scheduleId,userId,scheduleTitle,scheduleTime, scheduleTimeHours,scheduleTimeMinutes, scheduleType,isActive;

    public Schedule()
    {

    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getScheduleTitle() {
        return scheduleTitle;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public void setScheduleTitle(String scheduleTitle) {
        this.scheduleTitle = scheduleTitle;
    }


    public String getScheduleTimeHours() {
        return scheduleTimeHours;
    }

    public void setScheduleTimeHours(String scheduleTimeHours) {
        this.scheduleTimeHours = scheduleTimeHours;
    }

    public String getScheduleTimeMinutes() {
        return scheduleTimeMinutes;
    }

    public void setScheduleTimeMinutes(String scheduleTimeMinutes) {
        this.scheduleTimeMinutes = scheduleTimeMinutes;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}