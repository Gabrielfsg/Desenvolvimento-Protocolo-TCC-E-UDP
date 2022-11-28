import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

public class ClienteUDP {
    public static void main(String[] args) {
        try {

            DatagramSocket socket = new DatagramSocket();
            System.out.println("=== Cliente iniciado!");


            String endCliente = InetAddress.getLocalHost().getHostName();
            int portoCliente  = socket.getLocalPort();

            System.out.println("Endereço do cliente: \t"+ endCliente  );
            System.out.println("Porto do cliente: \t"+ portoCliente );

            InetAddress endServidor = InetAddress.getByName("lar-linc-pc18");
            int portoServidor = Integer.parseInt("8484");

            byte[] bytesEntrada = new byte[10000];
            byte[] bytesSaida = new byte[10000];
            long tDecorrido = 0;
            int bytesEnviados = 0;
            int bytesLidos = 0;
            float vazao = 0;

            DatagramPacket envia;

            for(int i =0; i < bytesSaida.length; i++) {
                bytesSaida[i]=0;
            }

            long tInicial = System.currentTimeMillis();
            do {
                BigInteger bigInt = BigInteger.valueOf(10000);
                bytesSaida = bigInt.toByteArray();
                envia = new DatagramPacket(bytesSaida, bytesSaida.length, endServidor, portoServidor);
                socket.send( envia );
                bytesEnviados += 10000;
                tDecorrido = System.currentTimeMillis() - tInicial;
            } while (tDecorrido < 10000);

            vazao = (bytesEnviados * 8) / (tDecorrido / 1000.0F);
            System.out.println("Vazão (UPLOAD) Cliente: " + vazao + " bit/s");

            for(int i =0; i < bytesEntrada.length; i++) {
                bytesEntrada[i]=0;
            }

            DatagramPacket recebe = new DatagramPacket(bytesEntrada, bytesEntrada.length);
            vazao = 0;
            tDecorrido = 0;
            tInicial = System.currentTimeMillis();
            do {
                socket.receive(recebe);
                tDecorrido = System.currentTimeMillis() - tInicial;
                bytesLidos += ByteBuffer.wrap(recebe.getData()).getInt();
            } while (tDecorrido < 10000);

            vazao = (bytesLidos * 8) / (tDecorrido / 1000.0F);
            System.out.println("Vazão (DOWNLOAD) Cliente: " + vazao + " bit/s");

        } catch( Exception e) {
            System.err.println( "ERRO: " + e.toString() );
        }
    }
}
