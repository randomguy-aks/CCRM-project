package edu.ccrm.util;

import edu.ccrm.domain.Student;

import java.util.Comparator;

/**
 * Example comparator factory using lambda.
 */
public class Comparators {
    public static Comparator<Student> byRegNo() {
        return Comparator.comparing(Student::getRegNo);
    }
}
