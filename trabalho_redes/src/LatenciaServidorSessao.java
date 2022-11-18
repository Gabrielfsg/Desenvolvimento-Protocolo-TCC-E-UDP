import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class LatenciaServidorSessao implements Runnable{

    private Socket conexao;

    private String idSessao;

    public LatenciaServidorSessao(Socket conexao, String idSessao){
        this.conexao = conexao;
        this.idSessao = idSessao;
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
            entrada.readByte();
            saida.writeByte(byteEnviado);
            long tDecorrido = System.currentTimeMillis();
            System.out.println("Latência Servidor: " + (tDecorrido  -t0) + "ms");

            saida.close();
            entrada.close();
            conexao.close();

        } catch (Exception e) {
            System.err.println("ERRO: " + e.toString());
        }

    }
}
