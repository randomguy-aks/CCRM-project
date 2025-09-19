package edu.ccrm.domain;

/**
 * Instructor entity extends Person.
 */
public class Instructor extends Person {

    private String department;

    public Instructor(String id, String fullName, String email, String department) {
        super(id, fullName, email);
        this.department = department;
    }

    @Override
    public String getRole() {
        return "Instructor";
    }

    public String getDepartment() { return department; }
    public void setDepartment(String d) { this.department = d; }
}

