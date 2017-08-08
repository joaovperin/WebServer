/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Map;

/**
 *
 * @author 0199831
 */
public class Processor {

    private static final int TAM_BUFFER = 8192;

    public String readFileNotFound(String fileName, InputStream resource, Map model) {
        try {
            return read(fileName, resource, model);
        } catch (FileNotFoundException ex) {
            return "";
        }
    }

    public String read(String fileName, InputStream resource, Map model) throws FileNotFoundException {
        StringBuilder sb = new StringBuilder(TAM_BUFFER);

        try (Reader r = new InputStreamReader(resource); StringWriter out = new StringWriter()) {
            Template t = new Template(fileName, r, getC());
            t.process(model, out);
            sb.append(out.getBuffer().toString());
            out.flush();
        } catch (TemplateException e) {
            System.out.println("Template....");
            e.printStackTrace(System.out);
        } catch (IOException fe) {
            fe.printStackTrace(System.out);
            if (fe instanceof FileNotFoundException) {
                throw new FileNotFoundException(fe.getMessage());
            }
        }

        return sb.toString();
    }

    private Configuration getC() {
        return new Configuration();
    }

}
