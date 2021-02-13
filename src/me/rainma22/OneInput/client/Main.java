package me.rainma22.OneInput.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Main {
    private static boolean failsafe=true;
    public static boolean isFailsafe() {
        return failsafe;
    }

    public static void setFailsafe(boolean failsafe) {
        Main.failsafe = failsafe;
    }


    public static void main(String[] args) throws Exception {

        DatagramSocket socket=new DatagramSocket(2020);
        byte[] b=new byte[]{0};
        DatagramPacket packet=new DatagramPacket(b,b.length, InetAddress.getByName(args[0]),2021);
        socket.send(packet);
        b=new byte[1024];
        DatagramPacket finalPacket = packet;
        Thread t= new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (isFailsafe()){
                try {
                    socket.send(finalPacket);
                    Thread.sleep(1000);
                    System.out.println("sent");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        while (true) {
            packet = new DatagramPacket(b, b.length);
            socket.receive(packet);
            failsafe=false;
            String s=new String(packet.getData());
            System.out.println(s);
            Thread.sleep(10);
        }
    }
}
