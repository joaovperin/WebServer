/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Classe Servlet
 *
 * @author Joaov
 */
@WebServlet(name = "MyServlet", urlPatterns = {"/hello"})
public class HelloServlet extends HttpServlet {

    /**
     * Responde a requisições GET
     *
     * @param req Request
     * @param resp Response
     * @throws javax.servlet.ServletException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {
        // Envia uma mensagem
        try (ServletOutputStream out = resp.getOutputStream()) {
            out.write(monta(req).getBytes());
            out.flush();
        } catch (IOException e) {
            throw new ServletException(e);
        }
    }

    /**
     * Monta Html para devolver ao Client
     *
     * @param req Request recebido pelo Servlet
     * @return String
     */
    private String monta(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<title>Hello</title>");
        sb.append("</head>");
        sb.append("<body style='background-color: #4286f4'>");
        sb.append("<div style='color: #FEFFEA;height: 30px;background-color: #6241f4;width: 100%;text-align: center;font-size: 172px;'>");
        String name = req.getParameter("nome");
        sb.append(name != null ? name : "SEU BURRO QUE NAO SABE MANDAR O NOME VIA GET.");
        sb.append("</div>");
        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }

}
