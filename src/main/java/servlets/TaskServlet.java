package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.ServiceFactory;
import dto.DepartmentDTO;
import dto.TaskDTO;
import services.Service;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

//@WebServlet("/tasks/*")
public class TaskServlet extends HttpServlet {
    private Service taskService;
    private ObjectMapper objectMapper;

    public TaskServlet() {
        taskService = ServiceFactory.getTaskService();
        objectMapper = new ObjectMapper();
    }

    public void setTaskService(Service taskService) {
        this.taskService = taskService;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathValue = req.getPathInfo();
        if (pathValue == null || pathValue.equals("/")) {
            getAll(resp);
        }
        else {
            getByID(resp, pathValue);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TaskDTO taskDTO = objectMapper.readValue(req.getReader(), TaskDTO.class);
        taskService.create(taskDTO);
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathValue = req.getPathInfo();
        if (pathValue == null || pathValue.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST, "Missing ID task");
            return;
        }
        Long id = Long.parseLong(pathValue.substring(1));
        TaskDTO taskDTO = objectMapper.readValue(req.getReader(), TaskDTO.class);
        taskDTO.setTask_id(id);
        taskService.update(taskDTO);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathValue = req.getPathInfo();
        if (pathValue == null || pathValue.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST, "Missing ID task");
            return;
        }
        Long id = Long.parseLong(pathValue.substring(1));
        taskService.delete(id);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void getAll(HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.getWriter().write(objectMapper.writeValueAsString(taskService.getAll()));
    }

    private void getByID(HttpServletResponse resp, String pathValue) throws IOException {
        Optional<TaskDTO> taskDTO = taskService.get(Long.valueOf(pathValue.substring(1)));
        if (taskDTO.isPresent()) {
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(taskDTO.get()));
        }
        else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Task not found");
        }
    }
}
