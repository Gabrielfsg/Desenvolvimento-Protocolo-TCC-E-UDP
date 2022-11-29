import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class LatenciaServidorSessao implements Runnable{

    private Socket dados;

    private String idSessao;

    public LatenciaServidorSessao(Socket dados, String idSessao){
        this.dados = dados;
        this.idSessao = idSessao;
    }

    @Override
    public void run() {


        DataOutputStream saida;
        DataInputStream entrada;

        try {

            saida = new DataOutputStream(dados.getOutputStream());
            entrada = new DataInputStream(dados.getInputStream());

            int byteEnviado = 1;
            long t0 = System.currentTimeMillis();
            entrada.readByte();
            saida.writeByte(byteEnviado);
            long tDecorrido = System.currentTimeMillis();
            System.out.println("Latência Servidor: " + (tDecorrido  -t0) + "ms");

            saida.close();
            entrada.close();
            dados.close();

        } catch (Exception e) {
            System.err.println("ERRO: " + e.toString());
        }

    }
}
