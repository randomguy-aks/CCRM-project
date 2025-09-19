package edu.ccrm.exception;

/**
 * Exception when exceeding max credits.
 */
public class MaxCreditLimitExceededException extends Exception {
    public MaxCreditLimitExceededException(String message) {
        super(message);
    }
}
