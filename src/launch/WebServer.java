/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package launch;

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
        // Pega a porta da variável de ambiente assumindo default 8082.
        String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = "8082";
        }
        // Instancia e roda o server
        Server sv = new SimpleServer(Integer.valueOf(webPort));
        sv.start();
        sv.await();
    }

}
