package com.dristy.deardiary;

public class Note {
    private String CurrentDate,Note;
    private long Priority;

    public Note() {
    }

    public Note(String currentDate, String note, long priority) {
        CurrentDate = currentDate;
        Note = note;
        Priority = priority;
    }

    public String getCurrentDate() {
        return CurrentDate;
    }

    public void setCurrentDate(String currentDate) {
        CurrentDate = currentDate;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public long getPriority() {
        return Priority;
    }

    public void setPriority(long priority) {
        Priority = priority;
    }

    @Override
    public String toString() {
        return "Note{" +
                "CurrentDate='" + CurrentDate + '\'' +
                ", Note='" + Note + '\'' +
                ", Priority=" + Priority +
                '}';
    }
}
