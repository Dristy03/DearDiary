package com.dristy.deardiary;

public class Note {
    private String CurrentDate,Note,Font,Color;
    private long Priority;
    private int Type;

    public Note() {
    }

    public Note(String currentDate, String note, String font, String color, long priority, int type) {
        CurrentDate = currentDate;
        Note = note;
        Font = font;
        Color = color;
        Priority = priority;
        Type = type;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getFont() {
        return Font;
    }

    public void setFont(String font) {
        Font = font;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
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
                ", Font='" + Font + '\'' +
                ", Color='" + Color + '\'' +
                ", Priority=" + Priority +
                ", Type=" + Type +
                '}';
    }
}
