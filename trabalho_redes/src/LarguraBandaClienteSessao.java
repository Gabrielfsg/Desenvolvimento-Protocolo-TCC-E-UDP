import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class LarguraBandaClienteSessao implements Runnable{

    private String idCliente;
    private String serverIP = "localhost";
    private int serverPort = 8585;

    private Socket conexao;

    public LarguraBandaClienteSessao(String idCliente, String serverIP, int serverPort) throws IOException {
        this.idCliente = idCliente;
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.conexao = new Socket(serverIP, serverPort);
    }

    @Override
    public void run() {

        DataOutputStream saida;
        DataInputStream entrada;

        int totalBanda = 0;
        long inicio = System.currentTimeMillis();
        byte[] bytes = new byte[10240];

        try {

            saida = new DataOutputStream( conexao.getOutputStream());
            entrada = new DataInputStream( conexao.getInputStream());

            while (true) {

                int bytesLidos = entrada.read();
                totalBanda += bytesLidos;
                long tempo = System.currentTimeMillis() - inicio;
                if (tempo > 0 && System.currentTimeMillis() % 10 == 0) {
                    System.out.println("Leu " + totalBanda + " bytes, Velocidade: " + ((totalBanda / tempo)/ 1024) + "MB/s");
                }
            }


        } catch (Exception e) {    // CAPTURA algum problema caso ocorra (alguma trap - interrupção de software)
            System.err.println("ERRO: " + e.toString());
        }

    }
}
