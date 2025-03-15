package com.selicapps.schooltimetable.classes;

public class RCTaskModelClass {
    String clicked_day;
    String date;
    String subject;
    String task_kind;

    public RCTaskModelClass(String clicked_day, String date, String subject, String task_kind){
        this.clicked_day = clicked_day;
        this.date = date;
        this.subject = subject;
        this.task_kind = task_kind;
    }

    public String getClicked_day() {return clicked_day;}

    public String getDate() {return date;}

    public String getSubject() {return subject;}

    public String getTask_kind() {return task_kind;}
}
