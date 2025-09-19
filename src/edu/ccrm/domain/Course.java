package edu.ccrm.domain;

/**
 * Course class with Builder pattern and a static nested class Syllabus.
 */
public class Course {

    private final String code;
    private final String title;
    private final int credits;
    private Instructor instructor;
    private final Semester semester;
    private final String department;
    private boolean active = true;

    private Course(Builder b) {
        this.code = b.code;
        this.title = b.title;
        this.credits = b.credits;
        this.instructor = b.instructor;
        this.semester = b.semester;
        this.department = b.department;
    }

    public String getCode() { return code; }
    public String getTitle() { return title; }
    public int getCredits() { return credits; }
    public Instructor getInstructor() { return instructor; }
    public void setInstructor(Instructor i) { this.instructor = i; }
    public Semester getSemester() { return semester; }
    public String getDepartment() { return department; }
    public boolean isActive() { return active; }
    public void setActive(boolean a) { this.active = a; }

    @Override
    public String toString() {
        return code + " - " + title + " (" + credits + " cr) [" + department + ", " + semester + "]";
    }

    // static nested class
    public static class Syllabus {
        private final String outline;
        public Syllabus(String outline) {
            this.outline = outline;
        }
        @Override
        public String toString() {
            return "Syllabus: " + outline;
        }
    }

    // Builder
    public static class Builder {
        private final String code;
        private final String title;
        private int credits = 3;
        private Instructor instructor = null;
        private Semester semester = Semester.FALL;
        private String department = "General";

        public Builder(String code, String title) {
            this.code = code;
            this.title = title;
        }
        public Builder credits(int c) { this.credits = c; return this; }
        public Builder instructor(Instructor i) { this.instructor = i; return this; }
        public Builder semester(Semester s) { this.semester = s; return this; }
        public Builder department(String d) { this.department = d; return this; }
        public Course build() { return new Course(this); }
    }
}
