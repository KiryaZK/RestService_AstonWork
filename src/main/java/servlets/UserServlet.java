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


public class UserServlet extends HttpServlet {
    private Service userService;
    private ObjectMapper objectMapper;

    public UserServlet() {
        userService = ServiceFactory.getUserService();
        objectMapper = new ObjectMapper();
    }

    public void setUserService(Service userService) {
        this.userService = userService;
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
        UserDTO userDTO = objectMapper.readValue(req.getReader(), UserDTO.class);
        userService.create(userDTO);
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

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
