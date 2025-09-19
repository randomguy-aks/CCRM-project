package edu.ccrm.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Student entity. Contains inner class Note as an example of non-static inner class.
 */
public class Student extends Person {

    private final String regNo;
    private boolean active;
    private final LocalDateTime registeredOn;
    private final List<Enrollment> enrollments = new ArrayList<>();

    public Student(String id, String fullName, String email, String regNo) {
        super(id, fullName, email);
        this.regNo = regNo;
        this.active = true;
        this.registeredOn = LocalDateTime.now();
    }

    @Override
    public String getRole() {
        return "Student";
    }

    public String getRegNo() { return regNo; }
    public boolean isActive() { return active; }
    public void setActive(boolean a) { this.active = a; }

    public void addEnrollment(Enrollment e) { enrollments.add(e); }
    public void removeEnrollment(Enrollment e) { enrollments.remove(e); }
    public List<Enrollment> getEnrollments() { return enrollments; }

    @Override
    public String toString() {
        return regNo + " - " + fullName + " (" + (active ? "Active" : "Inactive") + ")";
    }

    public String profileString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Student Profile\n");
        sb.append("RegNo: ").append(regNo).append("\n");
        sb.append("Name: ").append(fullName).append("\n");
        sb.append("Email: ").append(email).append("\n");
        sb.append("Status: ").append(active ? "Active" : "Inactive").append("\n");
        sb.append("Enrollments:\n");
        for (Enrollment en : enrollments) {
            sb.append(" - ").append(en.getCourse().getCode()).append(" | Grade: ")
                    .append(en.getGrade() == null ? "N/A" : en.getGrade()).append("\n");
        }
        return sb.toString();
    }

    // inner class example: a quick note tied to a particular student instance
    public class Note {
        private final String text;
        private final LocalDateTime at;
        public Note(String text) {
            this.text = text;
            this.at = LocalDateTime.now();
        }
        @Override
        public String toString() {
            return at + " - " + text;
        }
    }
}
