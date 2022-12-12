import java.io.IOException;

public class Cliente {
    public static void main( String args[]  ) {
        Runnable cliente;
        Thread t;
        String serverIP = "lar-linc-pc19.local";
        int serverPort = 8585;
        try {
            cliente = new VazaoClienteSessao("cliente[1]", serverIP, serverPort);
            //cliente = new LatenciaClienteSessao("cliente[1]", serverIP, serverPort);
            t = new Thread( cliente );
            t.start();
        } catch (IOException ioException){

        }
    }
}
