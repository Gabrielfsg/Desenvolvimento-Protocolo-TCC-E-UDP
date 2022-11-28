import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class VazaoClienteSessao implements Runnable {

    private String idCliente;
    private String serverIP = "localhost";
    private int serverPort = 8585;

    private Socket controle;

    private Socket dados;

    public VazaoClienteSessao(String idCliente, String serverIP, int serverPort) throws IOException {
        this.idCliente = idCliente;
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.controle = new Socket(serverIP, serverPort);
        this.dados = new Socket(serverIP, serverPort);
    }

    @Override
    public void run() {

        DataOutputStream saidaControle;
        DataInputStream entradaControle;
        DataOutputStream saidaDados;
        DataInputStream entradaDados;

        try {

            saidaControle = new DataOutputStream( controle.getOutputStream());
            entradaControle = new DataInputStream( controle.getInputStream());
            saidaDados = new DataOutputStream( dados.getOutputStream());
            entradaDados = new DataInputStream( dados.getInputStream());

            int tamanhoBufeer = 100;
            byte[] buffer = new byte[tamanhoBufeer];
            int bytesEnviados = 0;
            long tDecorrido;

            long tIEnvio;
            long tFEnvio;
            float avg_rtt = 0;

            long tInicial = System.currentTimeMillis();
            do {
                tIEnvio = System.currentTimeMillis();
                saidaDados.write(buffer);
                tFEnvio = System.currentTimeMillis();
                avg_rtt += tFEnvio - tIEnvio;
                if( avg_rtt > 0.0){
                    avg_rtt = avg_rtt/2;
                }
                bytesEnviados += tamanhoBufeer;
                tDecorrido = System.currentTimeMillis() - tInicial;
            } while(tDecorrido < 10000);
            saidaDados.flush();

            float vazao = (bytesEnviados * 8) / (tDecorrido / 1000.0F);

            System.out.println("Vazão (UPLOAD) Cliente: " + vazao + " bit/s");
            saidaControle.writeUTF("A vazão (Download) é de: " + vazao + " bit/s");
            saidaControle.flush();
            System.out.println(entradaControle.readUTF());

            saidaControle.close();
            entradaControle.close();
            saidaDados.close();
            entradaDados.close();
            controle.close();
            dados.close();

        } catch (Exception e) {    // CAPTURA algum problema caso ocorra (alguma trap - interrupção de software)
            System.err.println("ERRO: " + e.toString());
        }

    }
}

