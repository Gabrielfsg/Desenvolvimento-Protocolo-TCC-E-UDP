import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class LatenciaClienteSessao implements Runnable{

    private String idCliente;
    private String serverIP;
    private int serverPort;

    private Socket conexao;

    public LatenciaClienteSessao(String idCliente, String serverIP, int serverPort) throws IOException {
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

            saida = new DataOutputStream(conexao.getOutputStream());
            entrada = new DataInputStream(conexao.getInputStream());

            int byteEnviado = 1;
            long t0 = System.currentTimeMillis();
            saida.writeByte(byteEnviado);
            entrada.readByte();
            long tDecorrido = System.currentTimeMillis();
            System.out.println("Latência Cliente: " + (tDecorrido - t0) + "ms");

            saida.close();
            entrada.close();
            conexao.close();

        } catch (Exception e) {
            System.err.println("ERRO: " + e.toString());
        }
    }
}
