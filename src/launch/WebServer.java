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

    private static final String WEB_APP_DIR_LOCATION = "WEB-APP/";
    private static final String WEB_INF_CLASSES_LOCATION = "/WEB-INF/classes";
    private static final File ADD_INFO_LOCATION = new File("build/classes");

    private static void prepare() {
        createDirIfNotExists(new File(WEB_APP_DIR_LOCATION));
        createDirIfNotExists(new File(WEB_INF_CLASSES_LOCATION));
        createDirIfNotExists(ADD_INFO_LOCATION);
    }

    private static void createDirIfNotExists(File f) {
        if (!f.exists() && f.isDirectory()) {
            System.out.println("Criando diretório".concat(f.getName()).concat(" ..."));
            f.mkdir();
        }
    }

    /**
     * EntryPoint principal
     *
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        // Prepara
        prepare();
        // Pega a porta da variável de ambiente assumindo default 8080.
        String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }
        // Instancia o Tomcat e define a porta
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(Integer.valueOf(webPort));
        // Determina o diretório WebApp
        StandardContext ctx = (StandardContext) tomcat.addWebapp("/", new File(WEB_APP_DIR_LOCATION).getAbsolutePath());
        System.out.println("Configurando com diretório base: " + new File("./" + WEB_APP_DIR_LOCATION).getAbsolutePath());
        // Declare a localização alternativa do diretório de classes ("WEB-INF/classes")
        // Anottations do Servlet 3.0 deve funcionar
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, WEB_INF_CLASSES_LOCATION,
                ADD_INFO_LOCATION.getAbsolutePath(), "/"));
        ctx.setResources(resources);
        // Inicia o servidor e escuta a porta
        System.out.println("Servidor rodando na Porta " + webPort + ".f");
        tomcat.start();
        tomcat.getServer().await();
    }

}
