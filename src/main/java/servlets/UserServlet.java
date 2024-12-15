package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.ServiceFactory;
import dto.TaskDTO;
import dto.UserDTO;
import services.Service;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * The UserServlet class is a servlet that handles HTTP requests related to users.
 * It provides endpoints for retrieving, creating, updating, and deleting users.
 * This servlet uses the UserService to interact with the business logic layer and
 * the ObjectMapper to serialize and deserialize JSON data.
 */
public class UserServlet extends HttpServlet {
    private Service userService;
    private ObjectMapper objectMapper;

    public UserServlet() {
        userService = ServiceFactory.getUserService();
        objectMapper = new ObjectMapper();
    }
    /**
     * Sets the UserService instance to be used by this servlet.
     *
     * @param userService The BookService to be used.
     */
    public void setUserService(Service userService) {
        this.userService = userService;
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
     * Handles GET requests. Retrieves all users or a specific user by ID.
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
     * Handles POST requests. Creates a new user.
     *
     * @param req  The HttpServletRequest object.
     * @param resp The HttpServletResponse object.
     * @throws ServletException If the request for the POST could not be handled.
     * @throws IOException      If an input or output error is detected when the servlet handles the POST request.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserDTO userDTO = objectMapper.readValue(req.getReader(), UserDTO.class);
        userService.create(userDTO);
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }
    /**
     * Handles PUT requests. Updates an existing user.
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
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST, "Missing ID user");
            return;
        }
        Long id = Long.parseLong(pathValue.substring(1));
        UserDTO userDTO = objectMapper.readValue(req.getReader(), UserDTO.class);
        userDTO.setUser_id(id);
        userService.update(userDTO);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
    /**
     * Handles DELETE requests. Deletes a user by ID.
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
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST, "Missing ID user");
            return;
        }
        Long id = Long.parseLong(pathValue.substring(1));
        userService.delete(id);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void getAll(HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.getWriter().write(objectMapper.writeValueAsString(userService.getAll()));
    }

    private void getByID(HttpServletResponse resp, String pathValue) throws IOException {
        Optional<UserDTO> userDTO = userService.get(Long.valueOf(pathValue.substring(1)));
        if (userDTO.isPresent()) {
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(userDTO.get()));
        }
        else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
        }
    }
}
