import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

public class ServidorUDP {
    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket(8585);
            System.out.printf("Server Start");
            String endServidor = InetAddress.getLocalHost().getHostName();
            int portoServidor = socket.getLocalPort();
            System.out.println("Endereço do servidor: \t" + endServidor);
            System.out.println("Ouvindo no porto: \t" + portoServidor);

            int tamanhoBuffer = 10000;
            byte[] bytesEntrada = new byte[tamanhoBuffer];
            byte[] bytesSaida = new byte[tamanhoBuffer];
            InetAddress endCliente = null;
            int portoCliente = 0;
            long tDecorrido = 0;
            long tDecorrido2 = 0;
            long bytesEnviados = 0;
            float vazao = 0;
            long bytesLidos = 0;
            Socket controle = new Socket("lar-linc-pc19.local", portoServidor);
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

                vazao = (bytesLidos * 8) / (tDecorrido / 1000.0F);
                System.out.println("Vazão (DOWNLOAD) Servidor: " + vazao + " bit/s");
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

                    vazao = (bytesEnviados * 8) / (tDecorrido2 / 1000.0F);
                    System.out.println("Vazão (UPLOAD) Servidor: " + vazao + " bit/s");
                } catch (Exception e) {
                    System.err.println("ERRO: " + e.toString());
                }
            }

        } catch (Exception e) {
            System.err.println("ERRO: " + e.toString());
        }
    }

}
