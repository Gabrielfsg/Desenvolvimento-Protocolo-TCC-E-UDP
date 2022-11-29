import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class VazaoServidorSessao implements Runnable {

    private Socket controle;

    private Socket dados;

    private String idSessao;

    public VazaoServidorSessao(Socket controle, Socket dados, String idSessao) {
        this.controle = controle;
        this.dados = dados;
        this.idSessao = idSessao;
    }

    @Override
    public void run() {

        DataOutputStream saidaControle;
        DataInputStream entradaControle;
        DataOutputStream saidaDados;
        DataInputStream entradaDados;

        try {

            saidaControle = new DataOutputStream(controle.getOutputStream());
            entradaControle = new DataInputStream(controle.getInputStream());
            saidaDados = new DataOutputStream(dados.getOutputStream());
            entradaDados = new DataInputStream(dados.getInputStream());
            dados.setSoTimeout(11 * 1000);

            byte[] buffer = new byte[100];
            long bytesRecebidos = 0;
            long tDecorrido = 0;
            long tDecorrido2 = 0;
            long tIEnvio;
            long tFEnvio;
            float avg_rtt = 0;
            long bytesEnviados = 0;
            int tamanhoBufeer = 100;

            long tInicial = System.currentTimeMillis();
            try {
                do {
                    bytesRecebidos += entradaDados.read(buffer);
                    tDecorrido = System.currentTimeMillis() - tInicial;
                } while (tDecorrido < 10000);
            } catch (SocketTimeoutException socketTimeoutException) {
            } catch (Exception e) {
                System.err.println("ERRO: " + e.toString());
            }

            float vazao = (bytesRecebidos * 8) / (tDecorrido / 1000.0F);
            System.out.println("Vazão (Download) Servidor: " + vazao + " bit/s");

            saidaControle.writeUTF("OKR");
            saidaControle.flush();
            String verificaSePodeEnviar = entradaControle.readUTF();
            if (verificaSePodeEnviar.equals("OKE")) {
                System.out.println("Começou a enviar");
                try {
                    long tInicial2 = System.currentTimeMillis();
                    do {
                        tIEnvio = System.currentTimeMillis();
                        saidaDados.write(buffer);
                        tFEnvio = System.currentTimeMillis();
                        avg_rtt += tFEnvio - tIEnvio;
                        if (avg_rtt > 0.0) {
                            avg_rtt = avg_rtt / 2;
                        }
                        bytesEnviados += tamanhoBufeer;
                        tDecorrido2 = System.currentTimeMillis() - tInicial2;
                    } while (tDecorrido2 < 10000);
                    saidaDados.flush();
                    vazao = (bytesEnviados * 8) / (tDecorrido2 / 1000.0F);
                    System.out.println("Vazão (UPLOAD) Servidor: " + vazao + " bit/s");
                    saidaControle.writeUTF("CF");
                    saidaControle.flush();
                } catch (Exception e) {
                    System.err.println("ERRO: " + e.toString());
                }
            }

            saidaControle.close();
            entradaControle.close();
            saidaDados.close();
            entradaDados.close();
            controle.close();
            dados.close();

        } catch (Exception e) {
            System.err.println("ERRO: " + e.toString());
        }
    }

}


