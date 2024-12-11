package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Relation:
 * One To Many: Department <-> User
 * One To Many: Department <-> Task
 */
public class Department {
    private Long department_id;
    private String department_name;
    private List<User> userList;
    private List<Task> taskList;

    public Department(Long department_id, String department_name) {
        this.department_id = department_id;
        this.department_name = department_name;
        userList = new ArrayList<>();
        taskList = new ArrayList<>();
    }

    public Long getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(Long department_id) {
        this.department_id = department_id;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }
}
