package edu.ccrm.service;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.Instructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * CourseService with Stream filters.
 */
public class CourseService {

    private final List<Course> courses = new ArrayList<>();

    public void addCourse(Course c) {
        courses.add(c);
        System.out.println("Added course: " + c);
    }

    public List<Course> listCourses() { return List.copyOf(courses); }

    public Optional<Course> findByCode(String code) {
        return courses.stream().filter(c -> c.getCode().equalsIgnoreCase(code)).findFirst();
    }

    public List<Course> searchByDepartment(String dept) {
        return courses.stream()
                .filter(c -> c.getDepartment().equalsIgnoreCase(dept))
                .collect(Collectors.toList());
    }

    public List<Course> searchByInstructor(String instrName) {
        return courses.stream()
                .filter(c -> c.getInstructor() != null && c.getInstructor().getFullName().equalsIgnoreCase(instrName))
                .collect(Collectors.toList());
    }

    public void assignInstructor(String courseCode, Instructor instructor) {
        findByCode(courseCode).ifPresentOrElse(c -> {
            c.setInstructor(instructor);
            System.out.println("Assigned instructor to " + courseCode);
        }, () -> System.out.println("Course not found."));
    }
}
