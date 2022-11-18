import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class VazaoClienteSessao implements Runnable {

    private String idCliente;
    private String serverIP = "localhost";
    private int serverPort = 8585;

    private Socket conexao;

    public VazaoClienteSessao(String idCliente, String serverIP, int serverPort) throws IOException {
        this.idCliente = idCliente;
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.conexao = new Socket(serverIP, serverPort);
    }

    @Override
    public void run() {

        DataOutputStream saida;
        DataInputStream entrada;

        try {

            saida = new DataOutputStream( conexao.getOutputStream());
            entrada = new DataInputStream( conexao.getInputStream());// outro

            int tamanhoBufeer = 100;
            byte[] buffer = new byte[tamanhoBufeer];
            int bytesEnviados = 0;
            long tDecorrido;

            long t0 = System.currentTimeMillis();
            do {
                saida.write(buffer);
                bytesEnviados += tamanhoBufeer;
                tDecorrido = System.currentTimeMillis() - t0;
            } while(tDecorrido < 10000);

            float vazao = (bytesEnviados * 8) / (tDecorrido / 1000.0F);

            System.out.println("Vazão (UPLOAD) Cliente: " + vazao + " bit/s");
            saida.writeUTF("A vazão (Download) é de: " + vazao + " bit/s");

            saida.close();
            entrada.close();
            conexao.close();

        } catch (Exception e) {    // CAPTURA algum problema caso ocorra (alguma trap - interrupção de software)
            System.err.println("ERRO: " + e.toString());
        }

    }
}

