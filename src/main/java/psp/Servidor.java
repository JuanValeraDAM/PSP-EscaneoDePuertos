package psp;
/*
- Aplicación cliente/servidor.
- El propósito es que el servidor haga un escaneo de puertos al cliente, y le envíe al cliente el resultado.
- El cliente solicita el escaneo enviando un número de puerto inicial y un número de puerto final al servidor, en un paquete UDP.
- El servidor hace el escaneo (intenta conectarse a todos esos puertos del cliente, tomando nota de a cuáles puede y cuáles no puede).
- Al acabar el escaneo, el servidor envía el resultado (lista de puertos abiertos, y cantidad de puertos cerrados), también en un paquete UDP.
- Hecho eso, el cliente visualiza el resultado por consola y termina.
- El servidor debe poder atender a varios clientes a la vez.

Cosa 1: para hacer pruebas, será interesante usar más de un PC.
Cosa 2: connect(SocketAddress endpoint, int timeout)
 */
/*
    Explicaciones:
        * En el arranca del servidor es donde debe recibir el paquete UDP.
        * Recuerda que el array de bytes del paquete lo tienes que pasar a String.
        * Al comprobar si se conecta hay que recoger una ioException, no la del timeout
        *
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {


    public static final int PUERTOUDP = 7000;
    private final ExecutorService pool;
    private final DatagramSocket datagramSocket;

    private final DatagramPacket datagramPacket;

    public Servidor() throws IOException {

        pool = Executors.newFixedThreadPool(10);

        datagramPacket = null;
        datagramSocket=new DatagramSocket(PUERTOUDP);
    }

    public static void main(String[] args) {
        try {
            Servidor servidor = new Servidor();
            servidor.arranca();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void arranca() {
        while (true) {
            try {
                byte[] buffer = new byte[1024];
                DatagramPacket pkt = new DatagramPacket(buffer, buffer.length);
                try {
                    datagramSocket.receive(pkt);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Se ha conectado un cliente");
                pool.submit(new HiloServidor(pkt));
            } catch (IOException e) {
                System.err.printf("*** Problema conectado con un cliente: " + e);
            }
        }
    }
}
