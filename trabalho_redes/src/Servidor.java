import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main( String args[]  ) {

        // soquete onde o servidor irá ouvir requisições
        ServerSocket serverSocket;
        // conexão a ser estabelecida com um cliente
        Socket conexao;

        boolean flagContinua = true;

        try {

            serverSocket = new ServerSocket( 8585 );

            int contador = 0;

            while( flagContinua ) {

                try {
                    conexao = serverSocket.accept();
                    contador++;

                    System.out.println("+++ cria a sessao nº " + contador + " para atender o novo cliente (" + conexao.getInetAddress() + "," + conexao.getPort() + ")" );
                    Runnable sessao = new VazaoServidorSessao(conexao,"sessão[" + contador + "]");
                    //Runnable sessao = new LatenciaServidorSessao(conexao,"sessão[" + contador + "]");
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