package Engine.models;

import java.util.ArrayList;
import java.util.List;

public class Solution {

    private int days;
    private int hours;
    private List<Lesson> lessons;

    public Solution(int days, int hours) {
        this.days = days;
        this.hours = hours;
        this.lessons = new ArrayList<>();
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public void addLesson(Lesson lesson)
    {
        this.lessons.add(lesson);
    }
}
