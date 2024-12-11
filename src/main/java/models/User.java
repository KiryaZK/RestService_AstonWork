package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Relation:
 * Many To One: User <-> Department
 * Many to Many: User <-> Task
 */
public class User {
    private Long user_id;
    private String user_firstname;
    private String user_lastname;
    private Department department;
    private List<Task> taskList;

    public User(Long user_id, String user_firstname, String user_lastname, Department department) {
        this.user_id = user_id;
        this.user_firstname = user_firstname;
        this.user_lastname = user_lastname;
        this.department = department;
        taskList = new ArrayList<>();
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }
    public String getUser_firstname() {
        return user_firstname;
    }

    public void setUser_firstname(String user_firstname) {
        this.user_firstname = user_firstname;
    }

    public String getUser_lastname() {
        return user_lastname;
    }

    public void setUser_lastname(String user_lastname) {
        this.user_lastname = user_lastname;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public List<Task> getTaskList() {
        return taskList;
    }
}
