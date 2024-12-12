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

@WebServlet("/departments/*")
public class DepartmenServlet extends HttpServlet {
    private Service departmentService;
    private ObjectMapper objectMapper;

    public DepartmenServlet() {
        this.departmentService = ServiceFactory.getDepartmentService();
        this.objectMapper = new ObjectMapper();
    }

    public void setDepartmentService(Service departmentService) {
        this.departmentService = departmentService;
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
        DepartmentDTO departmentDTO = objectMapper.readValue(req.getReader(), DepartmentDTO.class);
        departmentService.create(departmentDTO);
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

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
        Optional<DepartmentDTO> departmentDTO = departmentService.get(pathValue.substring(1));
        if (departmentDTO.isPresent()) {
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(departmentDTO.get()));
        }
        else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Department not found");
        }
    }
}
