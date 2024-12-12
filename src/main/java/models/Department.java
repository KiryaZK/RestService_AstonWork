package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    public int hashCode() {
        return Objects.hash(department_id, department_name, userList, taskList);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        Department department = (Department) obj;

        return Objects.equals(department_id, department.department_id) &&
                Objects.equals(department_name, department.department_name) &&
                Objects.equals(userList, department.userList) &&
                Objects.equals(taskList, department.taskList);
    }

    @Override
    public String toString() {
        return "Department{" +
                "department_id = " + department_id +
                ", department_name = '" + department_name + '\'' +
                ", userList = " + userList +
                ", taskList = " + taskList +
                '}';
    }
}
