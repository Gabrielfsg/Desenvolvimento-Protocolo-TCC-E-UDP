import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class VazaoServidorSessao implements Runnable{

    private Socket controle;

    private Socket dados;

    private String idSessao;

    public VazaoServidorSessao(Socket controle, Socket dados, String idSessao){
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

            saidaControle = new DataOutputStream( controle.getOutputStream());
            entradaControle = new DataInputStream( controle.getInputStream());
            saidaDados = new DataOutputStream( dados.getOutputStream());
            entradaDados = new DataInputStream( dados.getInputStream());

            byte[] buffer = new byte[100];
            int bytesRecebidos = 0;
            long tDecorrido;

            long tInicial = System.currentTimeMillis();
            do {
                 bytesRecebidos += entradaDados.read(buffer);
                 tDecorrido = System.currentTimeMillis() - tInicial;
            } while(tDecorrido < 10000);

            float vazao = (bytesRecebidos * 8) / (tDecorrido / 1000.0F);

            System.out.println("Vazão (Download) Servidor: " + vazao + " bit/s");
            System.out.println(entradaControle.readUTF());
            saidaControle.writeUTF("A vazão (UPLOAD) é de: " + vazao + " bit/s");
            saidaControle.flush();

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


