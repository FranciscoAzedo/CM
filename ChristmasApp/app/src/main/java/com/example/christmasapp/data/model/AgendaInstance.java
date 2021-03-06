package com.example.christmasapp.data.model;

/**
 * The class AgendaInstance represents a single instance on an event's agenda. It contains the
 * required data to represents each of those instances of an event's agenda and the correspondent
 * methods to data manipulation.
 */
public class AgendaInstance {

    /**
     * Represents the agenda instance's title
     */
    private String title;
    /**
     * Represents the agenda instance's opening time
     */
    private String startTime;
    /**
     * Represents the agenda instance's closing time
     */
    private String endTime;
    /**
     * Represents the agenda instance's date
     */
    private String date;

    /**
     * Constructor
     *
     * @param title     the title to be assigned to the agenda's instance
     * @param startTime the start time to be assigned to the agenda's instance
     * @param endTime   the end time to be assigned to the agenda's instance
     * @param date      the date to be assigned to the agenda's instance
     */
    public AgendaInstance(String title, String startTime, String endTime, String date) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
    }

    /**
     * Gets agenda's instance title
     *
     * @return agenda's instance title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets agenda's instance title
     *
     * @param title the title to be assigned to the agenda's instance
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets agenda's instance start time
     *
     * @return agenda's instance start time
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Sets agenda's instance start time
     *
     * @param startTime the start time to be assigned to the agenda's instance
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets agenda's instance end time
     *
     * @return agenda's instance end time
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Sets agenda's instance end time
     *
     * @param endTime the end time to be assigned to the agenda's instance
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets agenda's instance date
     *
     * @return agenda's instance date
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets agenda's instance date
     *
     * @param date the date to be assigned to the agenda's instance
     */
    public void setDate(String date) {
        this.date = date;
    }
}
