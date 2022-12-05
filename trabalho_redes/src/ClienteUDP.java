import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

public class ClienteUDP {
    public static void main(String[] args) {
        try {

            DatagramSocket socket = new DatagramSocket();
            System.out.println("=== Cliente iniciado!");


            String endCliente = InetAddress.getLocalHost().getHostName();
            int portoCliente = socket.getLocalPort();

            System.out.println("Endereço do cliente: \t" + endCliente);
            System.out.println("Porto do cliente: \t" + portoCliente);
            Util util = new Util();
            InetAddress endServidor = InetAddress.getByName(endCliente);
            int portoServidor = Integer.parseInt("8587");
            int portoServidorTCP = Integer.parseInt("8586");
            CalculoCliente calculoCliente = new CalculoCliente();
            byte[] bytesEntrada = new byte[10000];
            byte[] bytesSaida = new byte[10000];
            long tDecorrido = 0;
            long tDecorrido2 = 0;
            long bytesEnviados = 0;
            long bytesLidos = 0;
            float vazaoD = 0,vazaoU = 0;
            Socket controle = new Socket(endServidor, portoServidorTCP);
            DataOutputStream saidaControle = new DataOutputStream(controle.getOutputStream());
            DataInputStream entradaControle = new DataInputStream(controle.getInputStream());

            DatagramPacket envia;

            for (int i = 0; i < bytesSaida.length; i++) {
                bytesSaida[i] = 0;
            }
            try {
                long tInicial = System.currentTimeMillis();
                do {
                    BigInteger bigInt = BigInteger.valueOf(10000);
                    bytesSaida = bigInt.toByteArray();
                    envia = new DatagramPacket(bytesSaida, bytesSaida.length, endServidor, portoServidor);
                    socket.send(envia);
                    tDecorrido = System.currentTimeMillis() - tInicial;
                    bytesEnviados += 10000;
                } while (tDecorrido < 10000);
            } catch (Exception e) {
                System.err.println("ERRO: " + e.toString());
            }

            vazaoU = util.bytesConvert(bytesEnviados) / (tDecorrido / 1000.0F);
            System.out.println("Vazão (UPLOAD) Cliente: " + vazaoU + " mb/s");

            for (int i = 0; i < bytesEntrada.length; i++) {
                bytesEntrada[i] = 0;
            }
            DatagramPacket recebe;
            String verificaSePodeReceber = entradaControle.readUTF();
            saidaControle.writeUTF("OKE");
            saidaControle.flush();
            if (verificaSePodeReceber.equals("OKR")) {
                System.out.println("Começou a receber");
                try {
                     recebe = new DatagramPacket(bytesEntrada, bytesEntrada.length);
                    long tInicial2 = System.currentTimeMillis();
                    do {
                        socket.receive(recebe);
                        tDecorrido2 = System.currentTimeMillis() - tInicial2;
                        bytesLidos += ByteBuffer.wrap(recebe.getData()).getInt();
                    } while (tDecorrido2 < 10000);

                    vazaoD = util.bytesConvert(bytesLidos) / (tDecorrido2 / 1000.0F);
                    System.out.println("Vazão (DOWNLOAD) Cliente: " + vazaoD + " mb/s");
                    Arquivo.escreveArq("C:\\Users\\T-GAMER\\Pictures\\trab_redes\\trabalho_redes\\src\\arquivo\\larguraCliente.txt", Float.toString(vazaoU), Float.toString(vazaoD));
                } catch (Exception e) {
                    System.err.println("ERRO: " + e.toString());
                }
            }

            String verificaSePodeFinalizar = entradaControle.readUTF();
            if (verificaSePodeFinalizar.equals("CF")) {
                saidaControle.writeUTF("CF2");
                calculoCliente.razaoTempoTransferencia();
                saidaControle.flush();
                saidaControle.close();
                entradaControle.close();
                controle.close();
            }

        } catch (Exception e) {
            System.err.println("ERRO: " + e.toString());
        }
    }
}
