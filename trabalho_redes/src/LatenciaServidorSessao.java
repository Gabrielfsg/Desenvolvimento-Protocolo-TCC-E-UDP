import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class LatenciaServidorSessao implements Runnable{

    private Socket dados;

    private Socket controle;

    private String idSessao;

    public LatenciaServidorSessao(Socket dados, Socket controle, String idSessao){
        this.dados = dados;
        this.controle = controle;
        this.idSessao = idSessao;
    }

    @Override
    public void run() {

        DataOutputStream saidaControle;
        DataInputStream entradaControle;
        DataOutputStream saida;
        DataInputStream entrada;

        try {

            long tDecorrido = 0;

            saidaControle = new DataOutputStream(controle.getOutputStream());
            entradaControle = new DataInputStream(controle.getInputStream());
            saida = new DataOutputStream(dados.getOutputStream());
            entrada = new DataInputStream(dados.getInputStream());

            int byteEnviado = 1;
            long t0 = System.currentTimeMillis();
            entrada.readByte();
            saida.writeByte(byteEnviado);
            saida.flush();
            String verificaSePodeEnviar = entradaControle.readUTF();
            if (verificaSePodeEnviar.equals("REC")) {
                tDecorrido = System.currentTimeMillis();
            }
            System.out.println("Latência Servidor: " + (tDecorrido  -t0) + "/ms");

            saida.close();
            entrada.close();
            dados.close();

        } catch (Exception e) {
            System.err.println("ERRO: " + e.toString());
        }

    }
}
