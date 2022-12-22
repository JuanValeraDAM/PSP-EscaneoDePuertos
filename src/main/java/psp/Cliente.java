package psp;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Cliente {


    public static void main(String[] args) throws IOException {

        DatagramSocket datagramSocket = new DatagramSocket();
        Scanner sc = new Scanner(System.in);

        System.out.println("Introduce el primer valor del rango de puertos: ");
        String primerValor = sc.next();
        System.out.println("Introduce el segundo valor: ");
        String segundoValor = sc.next();

        byte[] buffer = String.join("-", primerValor, segundoValor).getBytes();

        SocketAddress destino = new InetSocketAddress("localhost", Servidor.PUERTOUDP);
        DatagramPacket pkt = new DatagramPacket(
                buffer, buffer.length, destino
        );


        try {
            datagramSocket.send(pkt);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] buffer2=new byte[1024];
        DatagramPacket pkt2= new DatagramPacket(buffer2, buffer2.length,destino);
        try {
            datagramSocket.receive(pkt2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        byte[] puertosRechazados = pkt2.getData();
        String mensajePuertos = new String(puertosRechazados, 0, pkt2.getLength(), StandardCharsets.UTF_8);

        System.out.println(mensajePuertos);

    }
}

