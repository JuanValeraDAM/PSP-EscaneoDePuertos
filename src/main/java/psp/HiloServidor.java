package psp;

import java.io.IOException;
import java.net.*;

public class HiloServidor implements Runnable {


    private int puertosRechazados = 0;
    private DatagramPacket pkt;
    private StringBuilder stringBuilder;

    public HiloServidor(DatagramPacket pkt) throws IOException {
        stringBuilder=new StringBuilder();
        this.pkt=pkt;
    }

    @Override
    public void run() {



        byte[] puertos = pkt.getData();
        String puertos1 = new String(puertos, 0, pkt.getLength());
        String[] puertosArray = puertos1.split("-");
        int primerPuerto = Integer.parseInt(puertosArray[0]);
        int segundoPuerto = Integer.parseInt(puertosArray[1]);

        for (int i = primerPuerto; i < segundoPuerto; i++) {


            try (Socket socket = new Socket()) {
                SocketAddress socketAddress = new InetSocketAddress(pkt.getAddress(), i);
                socket.connect(socketAddress, 1000);
                stringBuilder.append(i).append(" ");
            } catch (IOException e) {
                puertosRechazados++;

            }
        }

        String paquetePuertos= "Se ha establecido conexión con los puertos: "+stringBuilder
                +" y han rechazado la conexión un total de: "+puertosRechazados+" puertos.";
        byte[] buffer = paquetePuertos.getBytes();


        SocketAddress destino = new InetSocketAddress(pkt.getAddress(), pkt.getPort());

        DatagramPacket pkt2 = new DatagramPacket(
                buffer, buffer.length, destino
        );
        try (DatagramSocket datagramSocket=new DatagramSocket()){
            datagramSocket.send(pkt2);
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }
}