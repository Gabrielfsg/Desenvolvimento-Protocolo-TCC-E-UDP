import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class ServidorUDP {
    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket(8484);
            System.out.printf("Server Start");
            String endServidor = InetAddress.getLocalHost().getHostName();
            int portoServidor = socket.getLocalPort();
            System.out.println("Endereço do servidor: \t" + endServidor );
            System.out.println("Ouvindo no porto: \t" + portoServidor );

            int tamanhoBuffer = 10000;
            byte[] bytesEntrada = new byte[tamanhoBuffer];
            byte[] bytesSaida = new byte[tamanhoBuffer];
            InetAddress endCliente = null;
            int portoCliente = 0;
            long tDecorrido = 0;
            int bytesEnviados = 0;
            float vazao = 0;
            int bytesLidos = 0;

            for(int i =0; i < bytesEntrada.length; i++) {
                bytesEntrada[i]=0;
            }

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

            for(int i =0; i < bytesSaida.length; i++) {
                bytesSaida[i]=0;
            }

            DatagramPacket envia;
            vazao = 0;
            tDecorrido = 0;
            tInicial = System.currentTimeMillis();
            do {
                BigInteger bigInt = BigInteger.valueOf(10000);
                bytesSaida = bigInt.toByteArray();
                envia = new DatagramPacket(bytesSaida, bytesSaida.length, endCliente, portoCliente);
                socket.send( envia );
                bytesEnviados += 10000;
                tDecorrido = System.currentTimeMillis() - tInicial;
            } while (tDecorrido < 10000);

            vazao = (bytesEnviados * 8) / (tDecorrido / 1000.0F);
            System.out.println("Vazão (UPLOAD) Servidor: " + vazao + " bit/s");

        } catch( Exception e) {
            System.err.println( "ERRO: " + e.toString() );
        }
    }

}
