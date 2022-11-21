import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class ClienteUDP {
    public static void main(String[] args) {
        try {

            DatagramSocket socket = new DatagramSocket();
            System.out.println("=== Cliente iniciado!");


            String endCliente = InetAddress.getLocalHost().getHostName();
            int portoCliente  = socket.getLocalPort();

            System.out.println("Endereço do cliente: \t"+ endCliente  );
            System.out.println("Porto do cliente: \t"+ portoCliente );

            InetAddress endServidor = InetAddress.getByName("lar-linc-pc01.local");
            int portoServidor = Integer.parseInt("8484");

            int bS = 0;
            int totalBanda = 0;
            byte[] bytesEntrada = new byte[1000];
            byte[] bytesSaida = new byte[1000];
            long inicio = System.currentTimeMillis();

            for(int i =0; i < bytesEntrada.length; i++) {
                bytesEntrada[i]=0;
            }

            DatagramPacket recebe = new DatagramPacket(bytesEntrada, bytesEntrada.length);


            do {
                socket.receive(recebe);
                portoCliente = recebe.getPort();
                int bytesLidos = ByteBuffer.wrap(recebe.getData()).getInt();
                totalBanda += bytesLidos;
                long tempo = System.currentTimeMillis() - inicio;
                if (tempo > 0 && System.currentTimeMillis() % 10 == 0) {
                    System.out.println("Leu " + totalBanda + " bytes, Velocidade: " + ((totalBanda / tempo) / 1024) + "MB/s");
                }
            } while (totalBanda < 1000);

            DatagramPacket envia;

            do {
                bytesSaida[bS] = 10;
                bS++;
                envia = new DatagramPacket(bytesSaida, bytesSaida.length, endServidor, portoServidor);
                socket.send( envia );
                System.out.println("\t >>> Resposta enviada");
                System.out.println("\t\t para " + endCliente + " no porto " + portoCliente );
            } while (bS < bytesSaida.length);

        } catch( Exception e) {
            System.err.println( "ERRO: " + e.toString() );
        }
    }
}
