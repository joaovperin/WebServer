/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import core.Processor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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
    private String buscaHtmls(HttpServletRequest req) throws ServletException {
        String page = "notFound";
        // Busca a página nos atributos do request
        Map<String, String[]> parameterMap = Collections.synchronizedMap(req.getParameterMap());

        String[] pageParam = parameterMap.get("page");
        if (pageParam != null && pageParam.length == 1) {
            page = pageParam[0];
        }
        // Converte o mapa e põe no novo Map
        Map model = new HashMap<>();
        req.getParameterMap().forEach((k, v) -> {
            if (v.length == 1) {
                model.put(k, v[0]);
            } else {
                model.put(k, v);
            }
        });
        // Busca a página, o template, realiza o processamento e devolve ao cliente
        String xxx = page.concat(".ftl");
        try {
            InputStream res = getS(req, xxx);
            if (res == null) {
                throw new FileNotFoundException(xxx);
            }
            return new Processor().read(xxx, res, model);
        } catch (FileNotFoundException ex) {
            String fileNoutFohund = "fileNotFound.ftl";
            return new Processor().readFileNotFound(fileNoutFohund, getS(req, fileNoutFohund), model);
        }
    }

    private InputStream getS(HttpServletRequest req, String xxx) {
        return req.getServletContext().getResourceAsStream(xxx);
    }

}
