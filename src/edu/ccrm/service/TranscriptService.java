package edu.ccrm.service;

import edu.ccrm.domain.Enrollment;
import edu.ccrm.domain.Grade;
import edu.ccrm.domain.Person;
import edu.ccrm.domain.Student;

import java.util.List;

/**
 * Service to generate transcripts and compute GPA.
 * Demonstrates polymorphism (Person reference to Student).
 */
public class TranscriptService {

    /**
     * Compute GPA for given enrollments.
     */
    public double computeGPA(List<Enrollment> enrollments) {
        int totalCredits = 0;
        int totalPoints = 0;

        for (Enrollment e : enrollments) {
            Grade g = e.getGrade();
            if (g != null) {
                int credits = e.getCourse().getCredits();
                totalCredits += credits;
                totalPoints += g.getPoints() * credits;
            }
        }

        return totalCredits == 0 ? 0.0 : (double) totalPoints / totalCredits;
    }

    /**
     * Print transcript for a student.
     */
    public void printTranscript(Student student) {
        System.out.println("=== Transcript ===");
        // Polymorphism: Person reference pointing to Student
        Person p = student;
        System.out.println("Student: " + p);

        List<Enrollment> enrolls = student.getEnrollments();
        if (enrolls.isEmpty()) {
            System.out.println("No courses enrolled.");
            return;
        }

        int totalCredits = 0;
        int totalPoints = 0;

        for (Enrollment e : enrolls) {
            Grade g = e.getGrade();
            int credits = e.getCourse().getCredits();
            System.out.printf("%s | %s (%d cr) | Grade: %s\n",
                    e.getCourse().getCode(),
                    e.getCourse().getTitle(),
                    credits,
                    (g == null ? "N/A" : g));
            if (g != null) {
                totalCredits += credits;
                totalPoints += g.getPoints() * credits;
            }
        }

        double gpa = totalCredits == 0 ? 0.0 : (double) totalPoints / totalCredits;
        System.out.printf("Overall GPA: %.2f\n", gpa);
    }

    /**
     * Generate a string summary of a student's transcript.
     */
    public String transcriptSummary(Student student) {
        double gpa = computeGPA(student.getEnrollments());
        return student.getRegNo() + " - " + student.getFullName() + " | GPA: " + String.format("%.2f", gpa);
    }

    @Override
    public String toString() {
        return "TranscriptService: computes GPA and prints transcripts";
    }
}
