package com.selicapps.schooltimetable.classes;

public class TaskModelClass {
    public String subject;
    public String date;
    public String clicked_day;
    public String task_kind;

    public TaskModelClass(String subject, String date, String clicked_day, String task_kind) {
        this.subject = subject;
        this.date = date;
        this.clicked_day = clicked_day;
        this.task_kind = task_kind;
    }
}
