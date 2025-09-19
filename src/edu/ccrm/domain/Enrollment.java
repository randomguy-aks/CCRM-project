package edu.ccrm.domain;

import java.time.LocalDateTime;

/**
 * Enrollment ties a student and course and stores grade.
 */
public class Enrollment {
    private final Student student;
    private final Course course;
    private Grade grade;
    private final LocalDateTime enrolledOn;

    public Enrollment(Student s, Course c) {
        this.student = s;
        this.course = c;
        this.enrolledOn = LocalDateTime.now();
    }

    public Student getStudent() { return student; }
    public Course getCourse() { return course; }
    public Grade getGrade() { return grade; }
    public void setGrade(Grade g) { this.grade = g; }
    public LocalDateTime getEnrolledOn() { return enrolledOn; }

    @Override
    public String toString() {
        return student.getRegNo() + " -> " + course.getCode() + " : " + (grade == null ? "N/A" : grade);
    }
}
