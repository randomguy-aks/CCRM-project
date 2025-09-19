package edu.ccrm.io;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.Grade;
import edu.ccrm.domain.Semester;
import edu.ccrm.domain.Student;
import edu.ccrm.service.CourseService;
import edu.ccrm.service.EnrollmentService;
import edu.ccrm.service.StudentService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Simple CSV-like import/export using NIO.2 and Streams.
 * Students CSV format: regNo,fullName,email
 * Courses CSV format: code,title,credits,department,semester
 */
public class ImportExportService {

    private final StudentService studentService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    public ImportExportService(StudentService ss, CourseService cs, EnrollmentService es) {
        this.studentService = ss;
        this.courseService = cs;
        this.enrollmentService = es;
    }

    public void importStudents(Path csv) throws IOException {
        List<String> lines = Files.readAllLines(csv, StandardCharsets.UTF_8);
        for (String line : lines) {
            String[] parts = line.split(",", -1);
            if (parts.length >= 3) {
                Student s = new Student("imp-" + parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[0].trim());
                studentService.addStudent(s);
            }
        }
    }

    public void importCourses(Path csv) throws IOException {
        List<String> lines = Files.readAllLines(csv, StandardCharsets.UTF_8);
        for (String line : lines) {
            String[] p = line.split(",", -1);
            if (p.length >= 5) {
                Course c = new Course.Builder(p[0].trim(), p[1].trim())
                        .credits(Integer.parseInt(p[2].trim()))
                        .department(p[3].trim())
                        .semester(Semester.valueOf(p[4].trim().toUpperCase()))
                        .build();
                courseService.addCourse(c);
            }
        }
    }

    public void exportAll(Path targetDir) throws IOException {
        if (!Files.exists(targetDir)) Files.createDirectories(targetDir);
        Path studFile = targetDir.resolve("students_export.csv");
        Path courseFile = targetDir.resolve("courses_export.csv");
        Path enrollFile = targetDir.resolve("enrollments_export.csv");

        // students
        List<String> studs = studentService.listStudents().stream()
                .map(s -> String.join(",", s.getRegNo(), s.getFullName(), s.getEmail(), s.isActive() ? "active" : "inactive"))
                .collect(Collectors.toList());
        Files.write(studFile, studs, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        // courses
        List<String> courses = courseService.listCourses().stream()
                .map(c -> String.join(",", c.getCode(), c.getTitle(), String.valueOf(c.getCredits()), c.getDepartment(), c.getSemester().name()))
                .collect(Collectors.toList());
        Files.write(courseFile, courses, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        // enrollments -- simple view
        List<String> enrolls = enrollmentService.topStudents(100).stream() // reusing top list to get students with gpa; for demo only
                .collect(Collectors.toList());
        Files.write(enrollFile, enrolls, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        System.out.println("Exported to: " + targetDir.toAbsolutePath());
    }
}
