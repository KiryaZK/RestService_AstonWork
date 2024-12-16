package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    public int hashCode() {
        return Objects.hash(task_id, task_name, department, userList);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return Objects.equals(task_id, task.task_id) &&
                Objects.equals(task_name, task.task_name)
                && Objects.equals(department, task.department)
                && Objects.equals(userList, task.userList);
    }

    @Override
    public String toString() {
        return "Task{" +
                "task_id = " + task_id +
                ", task_name = '" + task_name + '\'' +
/*                ", department = " + department +*/
                ", userList = " + userList +
                '}';
    }
}
