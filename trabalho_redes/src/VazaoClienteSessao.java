import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class VazaoClienteSessao implements Runnable {

    private String idCliente;
    private String serverIP;
    private int serverPort;

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
        Util util = new Util();
        CalculoCliente calculoCliente = new CalculoCliente();

        try {

            saidaControle = new DataOutputStream(controle.getOutputStream());
            entradaControle = new DataInputStream(controle.getInputStream());
            saidaDados = new DataOutputStream(dados.getOutputStream());
            entradaDados = new DataInputStream(dados.getInputStream());
            dados.setSoTimeout(11 * 1000);

            int tamanhoBufeer = 10000;
            byte[] buffer = new byte[tamanhoBufeer];
            long bytesEnviados = 0;
            long tDecorrido = 0;
            long tDecorrido2 = 0;
            long bytesRecebidos = 0;
            long tIEnvio;
            long tFEnvio;
            float avg_rtt = 0;
            float vazaoD = 0,vazaoU = 0;

            long tInicial = System.currentTimeMillis();
            try {
                do {
                    tIEnvio = System.currentTimeMillis();
                    saidaDados.write(buffer);
                    tFEnvio = System.currentTimeMillis();
                    avg_rtt += tFEnvio - tIEnvio;
                    if (avg_rtt > 0.0) {
                        avg_rtt = avg_rtt / 2;
                    }
                    bytesEnviados += tamanhoBufeer;
                    tDecorrido = System.currentTimeMillis() - tInicial;
                } while (tDecorrido < 10000);
                saidaDados.flush();
                vazaoU = util.bytesConvert(bytesEnviados) / (tDecorrido / 1000.0F);
                System.out.println("Vazão (UPLOAD) Cliente: " + vazaoU + " mb/s");
                System.out.println("Latência na Vazão " + avg_rtt + "/ms");
            } catch (Exception e) {
                System.err.println("ERRO: " + e.toString());
            }

            String verificaSePodeReceber = entradaControle.readUTF();
            saidaControle.writeUTF("OKE");
            saidaControle.flush();
            if (verificaSePodeReceber.equals("OKR")) {
                System.out.println("Começou a receber");
                long tInicial2 = System.currentTimeMillis();
                try {
                    do {
                        bytesRecebidos += entradaDados.read(buffer);
                        tDecorrido2 = System.currentTimeMillis() - tInicial2;
                    } while (tDecorrido2 < 10000);
                    vazaoD = util.bytesConvert(bytesRecebidos) / (tDecorrido / 1000.0F);
                    System.out.println("Vazão (Download) Cliente: " + vazaoD + " mb/s");
                    Arquivo.escreveArq("C:\\Users\\T-GAMER\\Pictures\\trab_redes\\trabalho_redes\\src\\arquivo\\vazaoCliente.txt", Float.toString(vazaoU), Float.toString(vazaoD));
                } catch (SocketTimeoutException socketTimeoutException) {
                } catch (Exception e) {
                    System.err.println("ERRO: " + e.toString());
                }
            }

            String verificaSePodeFinalizar = entradaControle.readUTF();
            if (verificaSePodeFinalizar.equals("CF")) {
                calculoCliente.razaoTempoTransferencia();
                saidaControle.close();
                entradaControle.close();
                saidaDados.close();
                entradaDados.close();
                controle.close();
                dados.close();
            }

        } catch (Exception e) {
            System.err.println("ERRO: " + e.toString());
        }

    }
}

// if (vazao >= 1024) { vazao /= 1024 (float) ; uniade="K"}
// if (vazao >= 1024) { vazao /= 1024 (float) ; uniade="M"}
// if (vazao >= 1024) { vazao /= 1024 (float) ; uniade="G"}