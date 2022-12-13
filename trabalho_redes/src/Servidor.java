import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main( String args[]  ) {

        // soquete onde o servidor irá ouvir requisições
        ServerSocket serverSocket;
        // conexão a ser estabelecida com um cliente
        Socket controle;
        Socket dados;

        boolean flagContinua = true;

        try {

            serverSocket = new ServerSocket( 8585 );

            int contador = 0;

            while( flagContinua ) {

                try {
                    controle = serverSocket.accept();
                    dados = serverSocket.accept();
                    contador++;

                    System.out.println("+++ cria a sessao nº " + contador + " para atender o novo cliente (" + controle.getInetAddress() + "," + controle.getPort() + ")" );
                    //Runnable sessao = new VazaoServidorSessao(controle, dados,"sessão[" + contador + "]");
                    Runnable sessao = new LatenciaServidorSessao(dados, controle,"sessão[" + contador + "]");
                    Thread t = new Thread( sessao );
                    t.start();

                }
                catch( Exception e) {
                    System.err.println( "ERRO: " + e.toString() );
                }
            }

            System.out.println("=== Servidor finalizado!");
            serverSocket.close();

        }
        catch( Exception e) {
            System.err.println( "ERRO: " + e.toString() );
        }
    }
}