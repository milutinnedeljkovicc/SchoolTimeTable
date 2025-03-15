package com.selicapps.schooltimetable.classes;

public class ListModelClass {
    public String subject;
    public String start_time;
    public String stop_time;
    public String classroom;
    public String teacher;

    public ListModelClass(String subject, String start_time, String stop_time, String classroom, String teacher) {
        this.subject = subject;
        this.start_time = start_time;
        this.stop_time = stop_time;
        this.classroom = classroom;
        this.teacher = teacher;
    }
}
