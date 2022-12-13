import java.io.IOException;
import java.util.ArrayList;

public class CalculoServidor implements CalculosUteis{
    @Override
    public void vazaoMaxima() throws IOException {
        ArrayList<String> larguras = Arquivo.lerArq("src/larguraServidor.txt");
        ArrayList<String> vazoes = Arquivo.lerArq("src/vazaoServidor.txt");
        if(vazoes.size() == 2 && larguras.size() < 2){
            System.out.println("A largura deve ser calculada para ser mostrada a: (Vazão máxima)");
        } else if (vazoes.size() < 2 && larguras.size() == 2) {
            System.out.println("A vazão deve ser calculada para ser mostrada a: (Vazão máxima)");
        } else if (vazoes.size() == 2 && larguras.size() == 2) {
            float vazaoU = Float.parseFloat(vazoes.get(0));
            float vazaoD = Float.parseFloat(vazoes.get(1));
            float larguraU = Float.parseFloat(larguras.get(0));
            float larguraD = Float.parseFloat(larguras.get(1));
            System.out.println("Vazão máxima (Upload - Servidor): " + ((larguraU/vazaoU)*100) + " %");
            System.out.println("Vazão máxima (Download - Servidor): " + ((larguraD/vazaoD)*100) + " %");
        }
    }

    @Override
    public void razaoTempoTransferencia() throws IOException {
        ArrayList<String> larguras = Arquivo.lerArq("src/larguraServidorTempo.txt");
        ArrayList<String> vazoes = Arquivo.lerArq("src/vazaoServidorTempo.txt");
        if(vazoes.size() == 2 && larguras.size() < 2){
            System.out.println("A largura deve ser calculada para ser mostrada a: (Razão do Tempo de Transferência)");
        } else if (vazoes.size() < 2 && larguras.size() == 2) {
            System.out.println("A vazão deve ser calculada para ser mostrada a: (Razão do Tempo de Transferência)");
        } else if (vazoes.size() == 2 && larguras.size() == 2) {
            float vazaoU = Float.parseFloat(vazoes.get(0));
            float vazaoD = Float.parseFloat(vazoes.get(1));
            float larguraU = Float.parseFloat(larguras.get(0));
            float larguraD = Float.parseFloat(larguras.get(1));
            System.out.println("Razao Tempo Transferencia (Upload - Servidor): " + ((larguraU/vazaoU)*100) + " %");
            System.out.println("Razao Tempo Transferencia (Download - Servidor): " + ((larguraD/vazaoD)*100) + " %");
        }
    }
}
