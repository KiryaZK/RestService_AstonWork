package dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class UserDTO {
    private Long user_id;
    private String user_firstname;
    private String user_lastname;
    @JsonIgnore
    private DepartmentDTO department;
    private List<TaskDTO> taskList;

    public UserDTO(Long user_id, String user_firstname, String user_lastname, DepartmentDTO department, List<TaskDTO> taskList) {
        this.user_id = user_id;
        this.user_firstname = user_firstname;
        this.user_lastname = user_lastname;
        this.department = department;
        this.taskList = taskList;
    }

    public UserDTO(Long user_id, String user_firstname, String user_lastname, DepartmentDTO department) {
        this.user_id = user_id;
        this.user_firstname = user_firstname;
        this.user_lastname = user_lastname;
        this.department = department;
    }

    public UserDTO() {
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

    public DepartmentDTO getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentDTO department) {
        this.department = department;
    }

    public List<TaskDTO> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<TaskDTO> taskList) {
        this.taskList = taskList;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id = " + user_id +
                ", user_firstname = '" + user_firstname + '\'' +
                ", user_lastname = '" + user_lastname + '\'' +
/*                ", department = " + department +*/
                ", taskList = " + taskList +
                '}';
    }
}
