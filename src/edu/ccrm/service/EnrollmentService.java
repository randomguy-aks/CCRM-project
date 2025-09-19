package edu.ccrm.service;

import edu.ccrm.domain.*;
import edu.ccrm.exception.DuplicateEnrollmentException;
import edu.ccrm.exception.MaxCreditLimitExceededException;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * EnrollmentService handles enroll/unenroll, grade recording, GPA reports.
 * Demonstrates custom exceptions, streams, lambdas and maps.
 */
public class EnrollmentService {

    private final List<Enrollment> enrollments = new ArrayList<>();
    private final StudentService studentService;
    private final CourseService courseService;
    private final int MAX_CREDITS_PER_SEMESTER = 20;

    public EnrollmentService(StudentService ss, CourseService cs) {
        this.studentService = ss;
        this.courseService = cs;
    }

    public void enrollByRegNo(String regNo, String courseCode) throws Exception {
        var stuOpt = studentService.findByRegNo(regNo);
        var courseOpt = courseService.findByCode(courseCode);
        if (stuOpt.isEmpty()) throw new Exception("Student not found.");
        if (courseOpt.isEmpty()) throw new Exception("Course not found.");

        Student s = stuOpt.get();
        Course c = courseOpt.get();

        // check duplicate
        boolean already = enrollments.stream().anyMatch(e -> e.getStudent() == s && e.getCourse() == c);
        if (already) throw new DuplicateEnrollmentException("Already enrolled.");

        // check max credits
        int currentCredits = enrollments.stream()
                .filter(e -> e.getStudent() == s)
                .mapToInt(e -> e.getCourse().getCredits())
                .sum();
        if (currentCredits + c.getCredits() > MAX_CREDITS_PER_SEMESTER) {
            throw new MaxCreditLimitExceededException("Would exceed max credits (" + MAX_CREDITS_PER_SEMESTER + ").");
        }

        Enrollment en = new Enrollment(s, c);
        enrollments.add(en);
        s.addEnrollment(en);
        System.out.println("Enrolled " + s.getRegNo() + " into " + c.getCode());
    }

    public void unenrollByRegNo(String regNo, String courseCode) {
        Optional<Enrollment> opt = enrollments.stream()
                .filter(e -> e.getStudent().getRegNo().equalsIgnoreCase(regNo) &&
                        e.getCourse().getCode().equalsIgnoreCase(courseCode))
                .findFirst();
        opt.ifPresent(e -> {
            e.getStudent().removeEnrollment(e);
            enrollments.remove(e);
            System.out.println("Unenrolled.");
        });
        if (opt.isEmpty()) System.out.println("Enrollment not found.");
    }

    public void recordGrade(String regNo, String courseCode, Grade grade) {
        Optional<Enrollment> opt = enrollments.stream()
                .filter(e -> e.getStudent().getRegNo().equalsIgnoreCase(regNo) &&
                        e.getCourse().getCode().equalsIgnoreCase(courseCode))
                .findFirst();
        if (opt.isEmpty()) {
            System.out.println("Enrollment not found.");
            return;
        }
        opt.get().setGrade(grade);
        System.out.println("Recorded grade " + grade + " for " + regNo);
    }

    public void printTranscript(String regNo) {
        Optional<Student> sOpt = studentService.findByRegNo(regNo);
        if (sOpt.isEmpty()) {
            System.out.println("Student not found.");
            return;
        }
        Student s = sOpt.get();
        List<Enrollment> studsEnroll = enrollments.stream().filter(e -> e.getStudent() == s).collect(Collectors.toList());
        System.out.println("Transcript for " + s.getFullName());
        int totalCredits = 0;
        int totalPoints = 0;
        for (Enrollment e : studsEnroll) {
            int credits = e.getCourse().getCredits();
            Grade g = e.getGrade();
            System.out.printf("%s | %s cr | %s\n", e.getCourse().getCode(), credits, (g == null ? "N/A" : g));
            if (g != null) {
                totalCredits += credits;
                totalPoints += g.getPoints() * credits;
            }
        }
        double gpa = totalCredits == 0 ? 0.0 : (double) totalPoints / totalCredits;
        System.out.printf("GPA: %.2f\n", gpa);
    }

    // Simple GPA distribution computed across all students
    public Map<String, Long> gpaDistribution() {
        // For each student compute GPA and bucket it
        Map<Student, Double> gpaMap = studentsGPA();
        return gpaMap.values().stream()
                .map(gpa -> {
                    if (gpa >= 9) return "9-10";
                    if (gpa >= 8) return "8-9";
                    if (gpa >= 7) return "7-8";
                    if (gpa >= 6) return "6-7";
                    if (gpa > 0) return "0-6";
                    return "No Grades";
                })
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    // returns map student->gpa
    private Map<Student, Double> studentsGPA() {
        return enrollments.stream()
                .collect(Collectors.groupingBy(Enrollment::getStudent))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> computeGPA(e.getValue())));
    }

    private double computeGPA(List<Enrollment> list) {
        int tcr = 0; int tpt = 0;
        for (Enrollment en : list) {
            if (en.getGrade() != null) {
                tcr += en.getCourse().getCredits();
                tpt += en.getGrade().getPoints() * en.getCourse().getCredits();
            }
        }
        return tcr == 0 ? 0.0 : (double) tpt / tcr;
    }

    public List<String> topStudents(int n) {
        var map = studentsGPA();
        return map.entrySet().stream()
                .sorted((a,b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(n)
                .map(e -> e.getKey().getRegNo() + " : " + e.getKey().getFullName() + " -> " + String.format("%.2f", e.getValue()))
                .collect(Collectors.toList());
    }
}
