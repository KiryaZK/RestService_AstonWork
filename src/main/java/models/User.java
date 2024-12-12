package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    public int hashCode() {
        return Objects.hash(user_id, user_firstname, user_lastname, department, taskList);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return Objects.equals(user_id, user.user_id) &&
                Objects.equals(user_firstname, user.user_firstname) &&
                Objects.equals(user_lastname, user.user_lastname) &&
                Objects.equals(department, user.department) &&
                Objects.equals(taskList, user.taskList);
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id = " + user_id +
                ", user_firstname = '" + user_firstname + '\'' +
                ", user_lastname = '" + user_lastname + '\'' +
                ", department = " + department +
                ", taskList = " + taskList +
                '}';
    }
}
