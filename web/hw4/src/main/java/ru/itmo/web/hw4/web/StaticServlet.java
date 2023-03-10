package ru.itmo.web.hw4.web;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


public class StaticServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        File file = new File(getServletContext().getRealPath(request.getRequestURI()));
        File file1 = new File("/Users/arturuzeev/Desktop/web/hw4/src/main/webapp" + request.getRequestURI());
        if (file1.isFile()) {
            response.setContentType(getServletContext().getMimeType(file1.getName()));
            Files.copy(file1.toPath(), response.getOutputStream());
        } else if (file.isFile()) {
            response.setContentType(getServletContext().getMimeType(file.getName()));
            Files.copy(file.toPath(), response.getOutputStream());
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
