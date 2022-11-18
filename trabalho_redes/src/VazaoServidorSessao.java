import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class VazaoServidorSessao implements Runnable{

    private Socket conexao;

    private String idSessao;

    public VazaoServidorSessao(Socket conexao, String idSessao){
        this.conexao = conexao;
        this.idSessao = idSessao;
    }

    @Override
    public void run() {

        DataOutputStream saida;
        DataInputStream entrada;

        try {

            saida = new DataOutputStream( conexao.getOutputStream());
            entrada = new DataInputStream( conexao.getInputStream());

            byte[] buffer = new byte[100];
            int bytesRecebidos = 0;
            long tDecorrido;

            long t0 = System.currentTimeMillis();
            do {
                 bytesRecebidos += entrada.read(buffer);
                 tDecorrido = System.currentTimeMillis() - t0;
            } while(tDecorrido < 10000);

            float vazao = (bytesRecebidos * 8) / (tDecorrido / 1000.0F);

            System.out.println("Vazão (Download) Servidor: " + vazao + " bit/s");
            saida.writeUTF("A vazão (UPLOAD) é de: " + vazao + " bit/s");

            saida.close();
            entrada.close();
            conexao.close();

        } catch (Exception e) {    // CAPTURA algum problema caso ocorra (alguma trap - interrupção de software)
            System.err.println("ERRO: " + e.toString());
        }
    }

}


