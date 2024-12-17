package dto;

import java.util.List;

public class DepartmentDTO {
    private Long department_id;
    private String department_name;

    private List<UserDTO> userList;

    private List<TaskDTO> taskList;

    public DepartmentDTO(Long department_id, String department_name, List<UserDTO> userList, List<TaskDTO> taskList) {
        this.department_id = department_id;
        this.department_name = department_name;
        this.userList = userList;
        this.taskList = taskList;
    }

    public DepartmentDTO(Long department_id, String department_name) {
        this.department_id = department_id;
        this.department_name = department_name;
    }

    public DepartmentDTO() {
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

    public List<UserDTO> getUserList() {
        return userList;
    }

    public List<TaskDTO> getTaskList() {
        return taskList;
    }

    public void setUserList(List<UserDTO> userList) {
        this.userList = userList;
    }

    public void setTaskList(List<TaskDTO> taskList) {
        this.taskList = taskList;
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
