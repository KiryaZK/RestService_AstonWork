package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.ServiceFactory;
import dto.DepartmentDTO;
import services.Service;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * The DepartmentServlet class is a servlet that handles HTTP requests related to departments.
 * It provides endpoints for retrieving, creating, updating, and deleting departments.
 * This servlet uses the DepartmentService to interact with the business logic layer and
 * the ObjectMapper to serialize and deserialize JSON data.
 */
public class DepartmenServlet extends HttpServlet {
    private Service departmentService;
    private ObjectMapper objectMapper;

    public DepartmenServlet() {
        this.departmentService = ServiceFactory.getDepartmentService();
        this.objectMapper = new ObjectMapper();
    }
    /**
     * Sets the DepartmentService instance to be used by this servlet.
     *
     * @param departmentService The BookService to be used.
     */
    public void setDepartmentService(Service departmentService) {
        this.departmentService = departmentService;
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
     * Handles GET requests. Retrieves all departments or a specific department by ID.
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
     * Handles POST requests. Creates a new department.
     *
     * @param req  The HttpServletRequest object.
     * @param resp The HttpServletResponse object.
     * @throws ServletException If the request for the POST could not be handled.
     * @throws IOException      If an input or output error is detected when the servlet handles the POST request.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DepartmentDTO departmentDTO = objectMapper.readValue(req.getReader(), DepartmentDTO.class);
        departmentService.create(departmentDTO);
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }
    /**
     * Handles PUT requests. Updates an existing department.
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
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST, "Missing ID department");
            return;
        }
        Long id = Long.parseLong(pathValue.substring(1));
        DepartmentDTO departmentDTO = objectMapper.readValue(req.getReader(), DepartmentDTO.class);
        departmentDTO.setDepartment_id(id);
        departmentService.update(departmentDTO);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
    /**
     * Handles DELETE requests. Deletes a department by ID.
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
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST, "Missing ID department");
            return;
        }
        Long id = Long.parseLong(pathValue.substring(1));
        departmentService.delete(id);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void getAll(HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.getWriter().write(objectMapper.writeValueAsString(departmentService.getAll()));
    }

    private void getByID(HttpServletResponse resp, String pathValue) throws IOException {
        Optional<DepartmentDTO> departmentDTO = departmentService.get(Long.valueOf(pathValue.substring(1)));
        if (departmentDTO.isPresent()) {
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(departmentDTO.get()));
        }
        else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Department not found");
        }
    }
}
