package dto;

import java.util.List;

public class TaskDTO {
    private Long task_id;
    private String task_name;

    private DepartmentDTO department;
    private List<UserDTO> userList;

    public TaskDTO(Long task_id, String task_name, List<UserDTO> userList, DepartmentDTO department) {
        this.task_id = task_id;
        this.task_name = task_name;
        this.userList = userList;
        this.department = department;
    }

    public TaskDTO(Long task_id, String task_name, DepartmentDTO department) {
        this.task_id = task_id;
        this.task_name = task_name;
        this.department = department;
    }

    public TaskDTO() {
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

    public DepartmentDTO getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentDTO department) {
        this.department = department;
    }

    public void setUserList(List<UserDTO> userList) {
        this.userList = userList;
    }

    public List<UserDTO> getUserList() {
        return userList;
    }

    @Override
    public String toString() {
        return "Task{" +
                "task_id = " + task_id +
                ", task_name = '" + task_name + '\'' +
                ", department = " + department +
                ", userList = " + userList +
                '}';
    }
}
