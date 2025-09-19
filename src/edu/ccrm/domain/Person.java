package edu.ccrm.domain;

import java.time.LocalDateTime;

/**
 * Abstract person base class.
 */
public abstract class Person {
    protected final String id;
    protected String fullName;
    protected String email;
    protected final LocalDateTime createdAt;

    public Person(String id, String fullName, String email) {
        assert id != null && !id.isBlank();
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.createdAt = LocalDateTime.now();
    }

    public abstract String getRole();

    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return String.format("%s [%s] <%s>", fullName, getRole(), email);
    }
}
