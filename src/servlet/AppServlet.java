/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

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
@WebServlet(name = "AppServlet", urlPatterns = {"/app"})
public class AppServlet extends HttpServlet {

    /**
     * Tamanho padrão do buffer (em bytes)
     */
    private static final int TAM_BUFFER_PADRAO = 8192;

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
            out.write(buscaHtmls(req).getBytes());
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
    private String buscaHtmls(HttpServletRequest req) throws ServletException, ServletException {
        StringBuilder sb = new StringBuilder(TAM_BUFFER_PADRAO);

        String page = req.getParameter("page");
        if (page == null) {
            page = "erro";
        }

        page = page.concat(".html");
        InputStream resources = req.getServletContext().getResourceAsStream(page);

        String l;
        // Realiza a leitura bufferizada
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(resources))) {
            while ((l = bf.readLine()) != null) {
                sb.append(l);
            }
        } catch (IOException e) {
            e.printStackTrace(System.out);
            sb.append("Falha ap abrir arquivo!").append('\n');
            sb.append(e);
        }

        return sb.toString();

    }
}
