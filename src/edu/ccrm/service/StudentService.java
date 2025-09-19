package edu.ccrm.service;

import edu.ccrm.domain.Student;
import edu.ccrm.util.Validators;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * StudentService demonstrates CRUD operations and Stream usage.
 */
public class StudentService {

    private final List<Student> students = new ArrayList<>();

    public void addStudent(Student s) {
        if (!Validators.isValidEmail(s.getEmail())) {
            System.out.println("Invalid email format, student not added.");
            return;
        }
        students.add(s);
        System.out.println("Added: " + s);
    }

    public List<Student> listStudents() {
        return students.stream()
                .sorted(Comparator.comparing(Student::getRegNo))
                .collect(Collectors.toList());
    }

    public Optional<Student> findByRegNo(String regNo) {
        return students.stream().filter(s -> s.getRegNo().equalsIgnoreCase(regNo)).findFirst();
    }

    public void updateEmail(String regNo, String newEmail) {
        Optional<Student> opt = findByRegNo(regNo);
        if (opt.isPresent()) {
            if (!Validators.isValidEmail(newEmail)) {
                System.out.println("Invalid email format.");
                return;
            }
            opt.get().setEmail(newEmail);
            System.out.println("Email updated.");
        } else {
            System.out.println("Student not found.");
        }
    }

    public void deactivateStudent(String regNo) {
        findByRegNo(regNo).ifPresentOrElse(s -> {
            s.setActive(false);
            System.out.println("Deactivated: " + regNo);
        }, () -> System.out.println("Student not found."));
    }

    // return top N students by count of enrollments (simple example)
    public List<Student> topStudentsByEnrollments(int n) {
        return students.stream()
                .sorted((a, b) -> Integer.compare(b.getEnrollments().size(), a.getEnrollments().size()))
                .limit(n)
                .collect(Collectors.toList());
    }
}
