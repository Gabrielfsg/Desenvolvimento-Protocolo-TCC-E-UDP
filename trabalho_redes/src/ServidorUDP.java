import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.math.BigInteger;
import java.net.*;
import java.nio.ByteBuffer;

public class ServidorUDP {
    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket(8587);
            System.out.printf("Server Start \n");
            String endServidor = InetAddress.getLocalHost().getHostName();
            int portoServidor = socket.getLocalPort();
            System.out.println("Endereço do servidor: \t" + endServidor);
            System.out.println("Ouvindo no porto: \t" + portoServidor);
            CalculoServidor calculoServidor = new CalculoServidor();
            int tamanhoBuffer = 10000;
            byte[] bytesEntrada = new byte[tamanhoBuffer];
            byte[] bytesSaida = new byte[tamanhoBuffer];
            InetAddress endCliente = null;
            int portoCliente = 0;
            long tDecorrido = 0;
            long tDecorrido2 = 0;
            long bytesEnviados = 0;
            float vazaoD = 0,vazaoU = 0;
            long bytesLidos = 0;
            ServerSocket serverSocket = new ServerSocket(8586);
            Socket controle;
            Util util = new Util();
            controle = serverSocket.accept();
            DataOutputStream saidaControle = new DataOutputStream(controle.getOutputStream());
            DataInputStream entradaControle = new DataInputStream(controle.getInputStream());

            for (int i = 0; i < bytesEntrada.length; i++) {
                bytesEntrada[i] = 0;
            }

            try {
                DatagramPacket recebe = new DatagramPacket(bytesEntrada, bytesEntrada.length);
                long tInicial = System.currentTimeMillis();
                do {
                    socket.receive(recebe);
                    endCliente = recebe.getAddress();
                    portoCliente = recebe.getPort();
                    tDecorrido = System.currentTimeMillis() - tInicial;
                    bytesLidos += ByteBuffer.wrap(recebe.getData()).getInt();
                } while (tDecorrido < 10000);

                vazaoD = util.bytesConvert(bytesLidos) / (tDecorrido / 1000.0F);
                System.out.println("Vazão (DOWNLOAD) Servidor: " + vazaoD + " mb/s");
            } catch (Exception e) {
                System.err.println("ERRO: " + e.toString());
            }
            for (int i = 0; i < bytesSaida.length; i++) {
                bytesSaida[i] = 0;
            }

            saidaControle.writeUTF("OKR");
            saidaControle.flush();
            String verificaSePodeEnviar = entradaControle.readUTF();
            if (verificaSePodeEnviar.equals("OKE")) {
                System.out.println("Começou a enviar");
                try {
                    DatagramPacket envia;
                    long tInicial2 = System.currentTimeMillis();
                    do {
                        BigInteger bigInt = BigInteger.valueOf(10000);
                        bytesSaida = bigInt.toByteArray();
                        envia = new DatagramPacket(bytesSaida, bytesSaida.length, endCliente, portoCliente);
                        socket.send(envia);
                        tDecorrido2 = System.currentTimeMillis() - tInicial2;
                        bytesEnviados += 10000;
                    } while (tDecorrido2 < 10000);

                    vazaoU = util.bytesConvert(bytesEnviados) / (tDecorrido2 / 1000.0F);
                    System.out.println("Vazão (UPLOAD) Servidor: " + vazaoU + " mb/s");
                    Arquivo.escreveArq("C:\\Users\\T-GAMER\\Pictures\\trab_redes\\trabalho_redes\\src\\arquivo\\larguraServidor.txt", Float.toString(vazaoU), Float.toString(vazaoD));
                } catch (Exception e) {
                    System.err.println("ERRO: " + e.toString());
                }
                saidaControle.writeUTF("CF");
                saidaControle.flush();
                String verificaSePodeFinalizar = entradaControle.readUTF();
                if (verificaSePodeFinalizar.equals("CF2")) {
                    calculoServidor.razaoTempoTransferencia();
                    saidaControle.close();
                    entradaControle.close();
                    controle.close();
                }
            }

        } catch (Exception e) {
            System.err.println("ERRO: " + e.toString());
        }
    }

}
