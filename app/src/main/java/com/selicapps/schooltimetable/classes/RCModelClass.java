package com.selicapps.schooltimetable.classes;

public class RCModelClass {
    String subject_text;
    String time_text;
    String classroom_text;
    String teacher_text;

    public RCModelClass(String subject_text, String time_text, String classroom_text, String teacher_text){
        this.subject_text = subject_text;
        this.time_text = time_text;
        this.classroom_text = classroom_text;
        this.teacher_text = teacher_text;
    }

    public String getSubject_text() {
        return subject_text;
    }

    public String getTime_text() {
        return time_text;
    }

    public String getClassroom_text() {
        return classroom_text;
    }

    public String getTeacher_text() {
        return teacher_text;
    }
}
