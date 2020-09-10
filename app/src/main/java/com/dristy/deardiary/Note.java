package com.dristy.deardiary;

public class Note {
    private String date,message;
    private long priortity;

    public Note(String date, String message, long priortity) {
        this.date = date;
        this.message = message;
        this.priortity = priortity;
    }

    public Note() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getPriortity() {
        return priortity;
    }

    public void setPriortity(long priortity) {
        this.priortity = priortity;
    }

    @Override
    public String toString() {
        return "Note{" +
                "date='" + date + '\'' +
                ", message='" + message + '\'' +
                ", priortity=" + priortity +
                '}';
    }
}
