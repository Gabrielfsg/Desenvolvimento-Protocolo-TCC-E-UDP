import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.math.BigInteger;
import java.net.*;
import java.nio.ByteBuffer;

public class ServidorUDP {
    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket(9087);
            System.out.printf("Server Start \n");
            String endServidor = InetAddress.getLocalHost().getHostName();
            int portoServidor = socket.getLocalPort();
            System.out.println("Endereço do servidor: \t" + endServidor);
            System.out.println("Ouvindo no porto: \t" + portoServidor);
            ServerSocket serverSocket = new ServerSocket(9086);
            Socket controle;
            controle = serverSocket.accept();
            Util util = new Util();
            CalculoServidor calculoServidor = new CalculoServidor();
            int tamanhoBuffer = 8000;
            byte[] bytesEntrada = new byte[10000];
            byte[] bytesSaida = new byte[tamanhoBuffer];
            InetAddress endCliente = null;
            int portoCliente = 0;
            long tDecorrido = 0;
            long tDecorrido2 = 0;
            long bytesEnviados = 0;
            float vazaoD = 0,vazaoU = 0;
            long bytesLidos = 0;
            DataOutputStream saidaControle = new DataOutputStream(new BufferedOutputStream(controle.getOutputStream()));
            DataInputStream entradaControle = new DataInputStream(new BufferedInputStream(controle.getInputStream()));

            for (int i = 0; i < bytesEntrada.length; i++) {
                bytesEntrada[i] = 0;
            }

            for (int i = 0; i < bytesSaida.length; i++) {
                bytesSaida[i] = 0;
            }

            socket.setSoTimeout(11 * 1000);

            try {
                DatagramPacket recebe = new DatagramPacket(bytesEntrada, bytesEntrada.length);
                long tInicial = System.currentTimeMillis();
                do {
                    socket.receive(recebe);
                    tDecorrido = System.currentTimeMillis() - tInicial;
                    bytesLidos += ByteBuffer.wrap(recebe.getData()).getInt();
                    endCliente = recebe.getAddress();
                    portoCliente = recebe.getPort();
                } while (tDecorrido < 10000);
                vazaoD = util.bytesConvert(bytesLidos) / (tDecorrido / 1000.0F);
                System.out.println("Vazão (DOWNLOAD) Servidor: " + vazaoD + " mb/s");
            } catch (SocketTimeoutException socketTimeoutException) {
            } catch (Exception e) {
                System.err.println("ERRO: " + e.toString());
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
                        BigInteger bigInt = BigInteger.valueOf(8000);
                        bytesSaida = bigInt.toByteArray();
                        envia = new DatagramPacket(bytesSaida, bytesSaida.length, endCliente, portoCliente);
                        socket.send(envia);
                        tDecorrido2 = System.currentTimeMillis() - tInicial2;
                        bytesEnviados += 8000;
                    } while (tDecorrido2 < 10000);

                    vazaoU = util.bytesConvert(bytesEnviados) / (tDecorrido2 / 1000.0F);
                    System.out.println("Vazão (UPLOAD) Servidor: " + vazaoU + " mb/s");
                    Arquivo.escreveArq("larguraServidor.txt", Float.toString(vazaoU), Float.toString(vazaoD));
                    Arquivo.escreveArq("larguraServidorTempo.txt", Long.toString(tDecorrido2), Long.toString(tDecorrido));
                } catch (SocketTimeoutException socketTimeoutException) {
                } catch (Exception e) {
                    System.err.println("ERRO: " + e.toString());
                }
                saidaControle.writeUTF("CF");
                saidaControle.flush();
                String verificaSePodeFinalizar = entradaControle.readUTF();
                if (verificaSePodeFinalizar.equals("CF2")) {
                    calculoServidor.vazaoMaxima();
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
