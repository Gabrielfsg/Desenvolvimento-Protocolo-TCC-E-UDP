import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class LarguraBandaServidorSessao implements Runnable {


    private Socket conexao;

    private String idSessao;

    public LarguraBandaServidorSessao(Socket conexao, String idSessao) {
        this.conexao = conexao;
        this.idSessao = idSessao;
    }


    @Override
    public void run() {

        DataOutputStream saida;
        DataInputStream entrada;
        int totalBanda = 0;
        int indice = 0;

        try {

            saida = new DataOutputStream(conexao.getOutputStream());
            entrada = new DataInputStream(conexao.getInputStream());
            int b = 0;

            byte[] bytes = new byte[10 * 1024];
            do {
                bytes[b] = 10;
                b++;
            } while (b < bytes.length);

            do {
                saida.write(bytes);
                totalBanda += bytes[indice];
            } while (totalBanda < 400000000);

        } catch (Exception e) {    // CAPTURA algum problema caso ocorra (alguma trap - interrupção de software)
            System.err.println("ERRO: " + e.toString());
        }
    }
}
