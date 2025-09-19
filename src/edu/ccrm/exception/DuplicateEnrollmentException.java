package edu.ccrm.exception;

/**
 * Checked exception thrown when registering duplicate enrollment.
 */
public class DuplicateEnrollmentException extends Exception {
    public DuplicateEnrollmentException(String message) {
        super(message);
    }
}
