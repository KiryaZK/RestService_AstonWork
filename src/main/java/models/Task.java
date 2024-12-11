package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Relation:
 * Many To Many: Task <-> User
 */
public class Task {
    private Long task_id;
    private String task_name;
    private Department department;
    private List<User> userList;

    public Task(Long task_id, String task_name, Department department) {
        this.task_id = task_id;
        this.task_name = task_name;
        this.department = department;
        userList = new ArrayList<>();
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Long getTask_id() {
        return task_id;
    }

    public void setTask_id(Long task_id) {
        this.task_id = task_id;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public List<User> getUserList() {
        return userList;
    }
}
