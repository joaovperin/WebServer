/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package launch;

import java.io.File;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

/**
 * Classe principal de inicialização da aplicação
 *
 * @author Joaov
 */
public class WebServer {

    /**
     * EntryPoint principal
     *
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        // Pega a porta da variável de ambiente assumindo default 8080.
        String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }
        System.out.println("Porta: " + webPort);
        // Instancia o Tomcat e define a porta
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(Integer.valueOf(webPort));
        // Determina o diretório WebApp
        String webappDirLocation = "WEB-APP/";
        StandardContext ctx = (StandardContext) tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
        System.out.println("Configurando com diretório base: " + new File("./" + webappDirLocation).getAbsolutePath());
        // Declare a localização alternativa do diretório de classes ("WEB-INF/classes")
        // Anottations do Servlet 3.0 deve funcionar
        File additionWebInfClasses = new File("build/classes");
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                additionWebInfClasses.getAbsolutePath(), "/"));
        ctx.setResources(resources);
        // Inicia o servidor e escuta a porta
        tomcat.start();
        tomcat.getServer().await();
    }

}
