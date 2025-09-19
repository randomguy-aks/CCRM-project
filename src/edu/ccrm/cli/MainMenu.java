package edu.ccrm.cli;

import edu.ccrm.config.AppConfig;
import edu.ccrm.domain.*;
import edu.ccrm.io.BackupService;
import edu.ccrm.io.ImportExportService;
import edu.ccrm.service.CourseService;
import edu.ccrm.service.EnrollmentService;
import edu.ccrm.service.StudentService;
import edu.ccrm.service.TranscriptService;

import java.nio.file.Paths;
import java.util.Scanner;
import java.util.UUID;

/**
 * Console menu for CCRM
 * Demonstrates loops, switch, labeled break, services integration
 */
public class MainMenu {

    private static final StudentService studentService = new StudentService();
    private static final CourseService courseService = new CourseService();
    private static final EnrollmentService enrollmentService = new EnrollmentService(studentService, courseService);
    private static final TranscriptService transcriptService = new TranscriptService();
    private static final ImportExportService importExportService = new ImportExportService(studentService, courseService, enrollmentService);
    private static final BackupService backupService = new BackupService();

    public static void main(String[] args) {
        AppConfig cfg = AppConfig.getInstance(); // Singleton usage
        System.out.println("Data folder: " + cfg.getDataFolder());

        // demo data
        seedDemoData();

        Scanner scanner = new Scanner(System.in);
        mainLoop:
        while (true) {
            System.out.println("\n=== Campus Course & Records Manager (CCRM) ===");
            System.out.println("1. Manage Students");
            System.out.println("2. Manage Courses");
            System.out.println("3. Enrollment & Grades");
            System.out.println("4. Import/Export Data");
            System.out.println("5. Backup & Utilities");
            System.out.println("6. Reports");
            System.out.println("0. Exit");

            System.out.print("Choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> studentMenu(scanner);
                case "2" -> courseMenu(scanner);
                case "3" -> enrollmentMenu(scanner);
                case "4" -> importExportMenu(scanner);
                case "5" -> backupMenu(scanner);
                case "6" -> reportsMenu(scanner);
                case "0" -> {
                    System.out.println("Goodbye.");
                    break mainLoop; // labeled break demo
                }
                default -> System.out.println("Invalid choice, try again.");
            }
        }

        scanner.close();
    }

    // ---------------- STUDENT MENU ----------------
    // This is the student menu system
    private static void studentMenu(Scanner scanner) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Student Management ---");
            System.out.println("1. Add Student");
            System.out.println("2. List Students");
            System.out.println("3. Update Student Email");
            System.out.println("4. Deactivate Student");
            System.out.println("5. Print Student Profile");
            System.out.println("0. Back");
            System.out.print("Choice: ");
            String c = scanner.nextLine().trim();

            switch (c) {
                case "1" -> {
                    System.out.print("Full name: ");
                    String name = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("RegNo: ");
                    String reg = scanner.nextLine();
                    Student s = new Student(UUID.randomUUID().toString(), name, email, reg);
                    studentService.addStudent(s);
                }
                case "2" -> studentService.listStudents().forEach(System.out::println);
                case "3" -> {
                    System.out.print("RegNo: ");
                    String r = scanner.nextLine();
                    System.out.print("New email: ");
                    String e = scanner.nextLine();
                    studentService.updateEmail(r, e);
                }
                case "4" -> {
                    System.out.print("RegNo: ");
                    String r = scanner.nextLine();
                    studentService.deactivateStudent(r);
                }
                case "5" -> {
                    System.out.print("RegNo: ");
                    String r = scanner.nextLine();
                    studentService.findByRegNo(r).ifPresentOrElse(
                            st -> System.out.println(st.profileString()),
                            () -> System.out.println("Student not found.")
                    );
                }
                case "0" -> back = true;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // ---------------- COURSE MENU ----------------
    // This is the course menu system
    private static void courseMenu(Scanner scanner) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Course Management ---");
            System.out.println("1. Add Course");
            System.out.println("2. List Courses");
            System.out.println("3. Search Courses by Department");
            System.out.println("4. Assign Instructor to Course");
            System.out.println("0. Back");
            System.out.print("Choice: ");
            String c = scanner.nextLine().trim();

            switch (c) {
                case "1" -> {
                    System.out.print("Course code: ");
                    String code = scanner.nextLine();
                    System.out.print("Title: ");
                    String title = scanner.nextLine();
                    System.out.print("Credits: ");
                    int credits = Integer.parseInt(scanner.nextLine());
                    System.out.print("Department: ");
                    String dept = scanner.nextLine();
                    System.out.print("Semester (SPRING,SUMMER,FALL,WINTER): ");
                    Semester sem = Semester.valueOf(scanner.nextLine().trim().toUpperCase());
                    Course course = new Course.Builder(code, title)
                            .credits(credits)
                            .department(dept)
                            .semester(sem)
                            .build();
                    courseService.addCourse(course);
                }
                case "2" -> courseService.listCourses().forEach(System.out::println);
                case "3" -> {
                    System.out.print("Department: ");
                    String d = scanner.nextLine();
                    courseService.searchByDepartment(d).forEach(System.out::println);
                }
                case "4" -> {
                    System.out.print("Course code: ");
                    String code = scanner.nextLine();
                    System.out.print("Instructor full name: ");
                    String name = scanner.nextLine();
                    System.out.print("Instructor email: ");
                    String email = scanner.nextLine();
                    System.out.print("Department: ");
                    String d = scanner.nextLine();
                    Instructor ins = new Instructor(UUID.randomUUID().toString(), name, email, d);
                    courseService.assignInstructor(code, ins);
                }
                case "0" -> back = true;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // ---------------- ENROLLMENT MENU ----------------
    // This is the enrollment menu service
    private static void enrollmentMenu(Scanner scanner) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Enrollment & Grades ---");
            System.out.println("1. Enroll Student in Course");
            System.out.println("2. Unenroll Student");
            System.out.println("3. Record Grade");
            System.out.println("4. Print Transcript (student)");
            System.out.println("0. Back");
            System.out.print("Choice: ");
            String c = scanner.nextLine().trim();

            switch (c) {
                case "1" -> {
                    System.out.print("Student RegNo: ");
                    String reg = scanner.nextLine();
                    System.out.print("Course code: ");
                    String code = scanner.nextLine();
                    try {
                        enrollmentService.enrollByRegNo(reg, code);
                    } catch (Exception ex) {
                        System.out.println("Error: " + ex.getMessage());
                    }
                }
                case "2" -> {
                    System.out.print("Student RegNo: ");
                    String reg = scanner.nextLine();
                    System.out.print("Course code: ");
                    String code = scanner.nextLine();
                    enrollmentService.unenrollByRegNo(reg, code);
                }
                case "3" -> {
                    System.out.print("Student RegNo: ");
                    String reg = scanner.nextLine();
                    System.out.print("Course code: ");
                    String code = scanner.nextLine();
                    System.out.print("Grade (S,A,B,C,D,E,F): ");
                    Grade g = Grade.valueOf(scanner.nextLine().trim().toUpperCase());
                    enrollmentService.recordGrade(reg, code, g);
                }
                case "4" -> {
                    System.out.print("Student RegNo: ");
                    String reg = scanner.nextLine();
                    transcriptService.printTranscriptByRegNo(reg); 
                }
                case "0" -> back = true;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // ---------------- IMPORT/EXPORT MENU ----------------
    // This is the import/export menu
    private static void importExportMenu(Scanner scanner) {
        System.out.println("\n--- Import / Export ---");
        System.out.println("1. Import students from CSV");
        System.out.println("2. Import courses from CSV");
        System.out.println("3. Export all data");
        System.out.println("0. Back");
        System.out.print("Choice: ");
        String c = scanner.nextLine().trim();

        try {
            switch (c) {
                case "1" -> {
                    System.out.print("Path to students CSV: ");
                    String p = scanner.nextLine();
                    importExportService.importStudents(Paths.get(p));
                }
                case "2" -> {
                    System.out.print("Path to courses CSV: ");
                    String p = scanner.nextLine();
                    importExportService.importCourses(Paths.get(p));
                }
                case "3" -> {
                    System.out.print("Export directory path: ");
                    String p = scanner.nextLine();
                    importExportService.exportAll(Paths.get(p));
                }
                case "0" -> {}
                default -> System.out.println("Invalid option.");
            }
        } catch (Exception ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }

    // ---------------- BACKUP MENU ----------------
    // This is the backup menu
    private static void backupMenu(Scanner scanner) {
        System.out.print("Enter source export folder path: ");
        String src = scanner.nextLine();
        System.out.print("Enter backup base folder path: ");
        String base = scanner.nextLine();

        try {
            var backupDir = backupService.backup(Paths.get(src), Paths.get(base));
            System.out.println("Backup created at: " + backupDir);
            long size = backupService.calculateBackupSize(backupDir);
            System.out.println("Backup size (bytes): " + size);
        } catch (Exception e) {
            System.out.println("Backup failed: " + e.getMessage());
        }
    }

    // ---------------- REPORTS MENU ----------------
    // This is where the reports of a student will be shown
    private static void reportsMenu(Scanner scanner) {
        System.out.println("\n--- Reports ---");
        System.out.println("1. GPA distribution");
        System.out.println("2. Top students by GPA");
        System.out.println("3. All student transcript summaries");
        System.out.println("0. Back");
        System.out.print("Choice: ");
        String c = scanner.nextLine().trim();
        switch (c) {
            case "1" -> enrollmentService.gpaDistribution().forEach((k, v) -> System.out.println(k + " -> " + v));
            case "2" -> enrollmentService.topStudents(5).forEach(System.out::println);
            case "3" -> studentService.listStudents().forEach(s -> System.out.println(transcriptService.transcriptSummary(s)));
            case "0" -> {}
            default -> System.out.println("Invalid");
        }
    }

    // ---------------- DEMO DATA ----------------
    // A demo data to show how the program would work
    private static void seedDemoData() {
        Student s1 = new Student("s-1", "Akshat Aditya", "akshat.24bey10140@vitbhopal.ac.in", "24BEY10140");
        Student s2 = new Student("s-2", "Devansh Mishra", "devansh.24bey10154@vitbhopal.ac.in", "23BEY10154");
        studentService.addStudent(s1);
        studentService.addStudent(s2);

        Course c1 = new Course.Builder("CSE2006", "Programming in Java").credits(4).department("CSE").semester(Semester.FALL).build();
        Course c2 = new Course.Builder("MAT1003", "Calculus ").credits(3).department("Mathematics").semester(Semester.FALL).build();
        courseService.addCourse(c1);
        courseService.addCourse(c2);
    }
}
