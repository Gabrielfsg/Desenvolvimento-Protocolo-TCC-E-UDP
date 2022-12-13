import java.io.IOException;
import java.util.ArrayList;

public class CalculoCliente implements CalculosUteis {

    @Override
    public void vazaoMaxima() throws IOException {
        ArrayList<String> larguras = Arquivo.lerArq("src/larguraCliente.txt");
        ArrayList<String> vazoes = Arquivo.lerArq("src/vazaoCliente.txt");
        if(vazoes.size() == 2 && larguras.size() < 2){
            System.out.println("A largura deve ser calculada para ser mostrada a: (Vazão máxima)");
        } else if (vazoes.size() < 2 && larguras.size() == 2) {
            System.out.println("A vazão deve ser calculada para ser mostrada a: (Vazão máxima)");
        } else if (vazoes.size() == 2 && larguras.size() == 2) {
            float vazaoU = Float.parseFloat(vazoes.get(0));
            float vazaoD = Float.parseFloat(vazoes.get(1));
            float larguraU = Float.parseFloat(larguras.get(0));
            float larguraD = Float.parseFloat(larguras.get(1));
            System.out.println("Vazão máxima (Upload - Cliente): " + ((larguraU/vazaoU)*100) + " %");
            System.out.println("Vazão máxima (Download - Cliente): " + ((larguraD/vazaoD)*100) + " %");
        }
    }

    @Override
    public void razaoTempoTransferencia() throws IOException {
        ArrayList<String> larguras = Arquivo.lerArq("src/larguraClienteTempo.txt");
        ArrayList<String> vazoes = Arquivo.lerArq("src/vazaoClienteTempo.txt");
        if(vazoes.size() == 2 && larguras.size() < 2){
            System.out.println("A largura deve ser calculada para ser mostrada a: (Razão do Tempo de Transferência)");
        } else if (vazoes.size() < 2 && larguras.size() == 2) {
            System.out.println("A vazão deve ser calculada para ser mostrada a: (Razão do Tempo de Transferência)");
        } else if (vazoes.size() == 2 && larguras.size() == 2) {
            float vazaoU = Float.parseFloat(vazoes.get(0));
            float vazaoD = Float.parseFloat(vazoes.get(1));
            float larguraU = Float.parseFloat(larguras.get(0));
            float larguraD = Float.parseFloat(larguras.get(1));
            System.out.println("Razao Tempo Transferencia (Upload - Cliente): " + ((larguraU/vazaoU)*100) + " %");
            System.out.println("Razao Tempo Transferencia (Download - Cliente): " + ((larguraD/vazaoD)*100) + " %");
        }
    }

}
