import java.io.IOException;
import java.util.ArrayList;

public class CalculoCliente implements CalculosUteis {

    @Override
    public void razaoTempoTransferencia() throws IOException {
        ArrayList<String> larguras = Arquivo.lerArq("C:\\Users\\T-GAMER\\Pictures\\trab_redes\\trabalho_redes\\src\\arquivo\\larguraCliente.txt");
        ArrayList<String> vazoes = Arquivo.lerArq("C:\\Users\\T-GAMER\\Pictures\\trab_redes\\trabalho_redes\\src\\arquivo\\vazaoCliente.txt");
        if(vazoes.size() == 2 && larguras.size() < 2){
            System.out.println("A largura deve ser calculada para ser mostrada a: (Razão do Tempo de Transferência)");
        } else if (vazoes.size() < 2 && larguras.size() == 2) {
            System.out.println("A vazão deve ser calculada para ser mostrada a: (Razão do Tempo de Transferência)");
        } else if (vazoes.size() == 2 && larguras.size() == 2) {
            float vazaoU = Float.parseFloat(vazoes.get(0));
            float vazaoD = Float.parseFloat(vazoes.get(1));
            float larguraU = Float.parseFloat(larguras.get(0));
            float larguraD = Float.parseFloat(larguras.get(1));
            System.out.println("Razão do Tempo de Transferência (Upload - Cliente): " + (vazaoU/larguraU) + "mb/s");
            System.out.println("Razão do Tempo de Transferência (Download - Cliente): " + (vazaoD/larguraD) + "mb/s");
        }
    }

}
