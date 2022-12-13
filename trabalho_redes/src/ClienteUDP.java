import java.io.*;
import java.math.BigInteger;
import java.net.*;
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
            InetAddress endServidor = InetAddress.getByName("DESKTOP-HO7UDGJ");
            int portoServidor = Integer.parseInt("9087");
            int portoServidorTCP = Integer.parseInt("9086");
            Socket controle = new Socket(endServidor, portoServidorTCP);
            CalculoCliente calculoCliente = new CalculoCliente();
            int tamanhoBuffer = 1440;
            byte[] bytesEntrada = new byte[tamanhoBuffer];
            byte[] bytesSaida = new byte[tamanhoBuffer];
            long tDecorrido = 0;
            long tDecorrido2 = 0;
            long bytesEnviados = 0;
            long bytesLidos = 0;
            float vazaoD = 0,vazaoU = 0;
            DataOutputStream saidaControle = new DataOutputStream(new BufferedOutputStream(controle.getOutputStream()));
            DataInputStream entradaControle = new DataInputStream(new BufferedInputStream(controle.getInputStream()));

            DatagramPacket envia;

            for (int i = 0; i < bytesSaida.length; i++) {
                bytesSaida[i] = 0;
            }

            for (int i = 0; i < bytesEntrada.length; i++) {
                bytesEntrada[i] = 0;
            }

            socket.setSoTimeout(11 * 4000);

            try {
                envia = new DatagramPacket(bytesSaida, bytesSaida.length, endServidor, portoServidor);
                long tInicial = System.currentTimeMillis();
                do {
                    socket.send(envia);
                    tDecorrido = System.currentTimeMillis() - tInicial;
                    bytesEnviados += envia.getLength();
                } while (tDecorrido < 10000);
                vazaoU = util.bytesConvert(bytesEnviados) / (tDecorrido / 1000.0F);
                System.out.println("Vazão (UPLOAD) Cliente: " + vazaoU + " mb/s");
            } catch (SocketTimeoutException socketTimeoutException) {
                System.out.println("TimeOut Cliente.");
            } catch (Exception e) {
                System.err.println("ERRO: " + e.toString());
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
                        bytesLidos += recebe.getLength();
                    } while (tDecorrido2 < 10000);

                    vazaoD = util.bytesConvert(bytesLidos) / (tDecorrido2 / 1000.0F);
                    System.out.println("Vazão (DOWNLOAD) Cliente: " + vazaoD + " mb/s");
                    Arquivo.escreveArq("src/larguraCliente.txt", Float.toString(vazaoU), Float.toString(vazaoD));
                    Arquivo.escreveArq("src/larguraClienteTempo.txt", Long.toString(tDecorrido2), Long.toString(tDecorrido));
                } catch (SocketTimeoutException socketTimeoutException) {
                    System.out.println("TimeOut Cliente.");
                } catch (Exception e) {
                    System.err.println("ERRO: " + e.toString());
                }
            }

            String verificaSePodeFinalizar = entradaControle.readUTF();
            if (verificaSePodeFinalizar.equals("CF")) {
                saidaControle.writeUTF("CF2");
                calculoCliente.vazaoMaxima();
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
