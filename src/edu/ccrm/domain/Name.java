package edu.ccrm.domain;

/**
 * An immutable Name value object.
 */
public final class Name {
    private final String first;
    private final String last;

    public Name(String first, String last) {
        this.first = first == null ? "" : first;
        this.last = last == null ? "" : last;
    }

    public String getFirst() { return first; }
    public String getLast() { return last; }

    @Override
    public String toString() { return first + (last.isEmpty() ? "" : " " + last); }
}
