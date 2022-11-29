import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class LatenciaClienteSessao implements Runnable{

    private String idCliente;
    private String serverIP;
    private int serverPort;

    private Socket dados;

    private Socket controle;

    public LatenciaClienteSessao(String idCliente, String serverIP, int serverPort) throws IOException {
        this.idCliente = idCliente;
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.controle = new Socket(serverIP, serverPort);
        this.dados = new Socket(serverIP, serverPort);
    }

    @Override
    public void run() {


        DataOutputStream saida;
        DataInputStream entrada;
        DataOutputStream saidaControle;
        DataInputStream entradaControle;

        try {

            saidaControle = new DataOutputStream(controle.getOutputStream());
            entradaControle = new DataInputStream(controle.getInputStream());
            saida = new DataOutputStream(dados.getOutputStream());
            entrada = new DataInputStream(dados.getInputStream());

            int byteEnviado = 1;
            long t0 = System.currentTimeMillis();
            saida.writeByte(byteEnviado);
            entrada.readByte();
            long tDecorrido = System.currentTimeMillis();
            saidaControle.writeUTF("REC");
            saidaControle.flush();
            System.out.println("Latência Cliente: " + (tDecorrido - t0) + "ms");

            saida.close();
            entrada.close();
            dados.close();

        } catch (Exception e) {
            System.err.println("ERRO: " + e.toString());
        }
    }
}
