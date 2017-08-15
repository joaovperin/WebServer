/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package launch;

import java.io.File;
import javax.servlet.DispatcherType;
import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

/**
 * Descrição da classe.
 */
public class SimpleServer implements Server {

    private static final String WEB_APP_DIR_LOCATION = "WEB-APP/";
    private static final String WEB_INF_CLASSES_LOCATION = "/WEB-INF/classes";
    private static final File ADD_INFO_LOCATION = new File("build/classes");

    /** Instância do Tomcat */
    private final Tomcat tomcat;

    /**
     * Prepara a estrutura das pastas
     */
    private static void prepareFolderStructure() {
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

    // Determina o diretório WebApp
    String webappDirLocation = "res/";

    public SimpleServer(int port) {
        this.tomcat = new Tomcat();
        tomcat.setPort(port);
//        tomcat.setBaseDir(webappDirLocation);
    }

    @Override
    @SuppressWarnings("UseSpecificCatch")
    public void start() {
        try {
            // Prepara a estrutura das pastas
            prepareFolderStructure();
            // Determina o diretório WebApp
            StandardContext ctx = (StandardContext) tomcat.addWebapp("/", new File(WEB_APP_DIR_LOCATION).
                    getAbsolutePath());
//            ctx.addApplicationListener(org.jboss.weld.environment.servlet.Listener.class.getName());
//            ctx.addParameter("javax.servlet.jsp.jstl.fmt.locale", "pt_BR");
//            addFilter(ctx, VRaptor.class, "/*", DispatcherType.REQUEST, DispatcherType.FORWARD);
            printConfig();
            // Declare a localização alternativa do diretório de classes ("WEB-INF/classes")
            // Anottations do Servlet 3.0 deve funcionar
            WebResourceRoot resources = new StandardRoot(ctx);
            resources.addPreResources(new DirResourceSet(resources, WEB_INF_CLASSES_LOCATION,
                    ADD_INFO_LOCATION.getAbsolutePath(), "/"));
            ctx.setResources(resources);
            // Inicia o servidor e escuta a porta
            tomcat.start();
        } catch (Exception e) {
            throw new ServerException(e);
        }
    }

    private void addFilter(Context ctx, Class f, String urlPattern, DispatcherType... types) {
        FilterDef def = new FilterDef();
        def.setFilterName(f.getClass().getSimpleName());
        def.setFilterClass(f.getClass().getCanonicalName());
        FilterMap map = new FilterMap();
        map.setFilterName(f.getClass().getSimpleName());
        for (DispatcherType t : types) {
            map.setDispatcher(t.name());
        }
        ctx.addFilterDef(def);
        ctx.addFilterMap(map);
    }

    @Override
    public void await() {
        // Inicia o servidor e escuta a porta
        System.out.println("Servidor iniciando!");
        tomcat.getServer().await();
    }

    private void printConfig() {
        System.out.println("Configurando com diretório base: " + new File("./" + WEB_APP_DIR_LOCATION).
                getAbsolutePath());
    }

}
