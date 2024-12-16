package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.ServiceFactory;
import dto.TaskDTO;
import services.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * The TaskServlet class is a servlet that handles HTTP requests related to tasks.
 * It provides endpoints for retrieving, creating, updating, and deleting tasks.
 * This servlet uses the TaskService to interact with the business logic layer and
 * the ObjectMapper to serialize and deserialize JSON data.
 */
public class TaskServlet extends HttpServlet {
    private Service taskService;
    private ObjectMapper objectMapper;

    public TaskServlet() {
        taskService = ServiceFactory.getTaskService();
        objectMapper = new ObjectMapper();
    }
    /**
     * Sets the TaskService instance to be used by this servlet.
     *
     * @param taskService The BookService to be used.
     */
    public void setTaskService(Service taskService) {
        this.taskService = taskService;
    }
    /**
     * Sets the ObjectMapper instance to be used by this servlet.
     *
     * @param objectMapper The ObjectMapper to be used.
     */
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    /**
     * Handles GET requests. Retrieves all tasks or a specific task by ID.
     *
     * @param req  The HttpServletRequest object.
     * @param resp The HttpServletResponse object.
     * @throws ServletException If the request for the GET could not be handled.
     * @throws IOException      If an input or output error is detected when the servlet handles the GET request.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathValue = req.getPathInfo();
        resp.setCharacterEncoding("UTF-8");
        if (pathValue == null || pathValue.equals("/")) {
            getAll(resp);
        }
        else {
            getByID(resp, pathValue);
        }
    }
    /**
     * Handles POST requests. Creates a new task.
     *
     * @param req  The HttpServletRequest object.
     * @param resp The HttpServletResponse object.
     * @throws ServletException If the request for the POST could not be handled.
     * @throws IOException      If an input or output error is detected when the servlet handles the POST request.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        TaskDTO taskDTO = objectMapper.readValue(req.getReader(), TaskDTO.class);
        taskService.create(taskDTO);
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }
    /**
     * Handles PUT requests. Updates an existing task.
     *
     * @param req  The HttpServletRequest object.
     * @param resp The HttpServletResponse object.
     * @throws ServletException If the request for the PUT could not be handled.
     * @throws IOException      If an input or output error is detected when the servlet handles the PUT request.
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathValue = req.getPathInfo();
        if (pathValue == null || pathValue.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST, "Missing ID task");
            return;
        }
        Long id = Long.parseLong(pathValue.substring(1));
        req.setCharacterEncoding("UTF-8");
        TaskDTO taskDTO = objectMapper.readValue(req.getReader(), TaskDTO.class);
        taskDTO.setTask_id(id);
        taskService.update(taskDTO);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
    /**
     * Handles DELETE requests. Deletes a task by ID.
     *
     * @param req  The HttpServletRequest object.
     * @param resp The HttpServletResponse object.
     * @throws ServletException If the request for the DELETE could not be handled.
     * @throws IOException      If an input or output error is detected when the servlet handles the DELETE request.
     */
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
