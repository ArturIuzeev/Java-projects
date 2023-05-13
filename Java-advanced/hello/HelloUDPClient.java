package info.kgeorgiy.ja.iuzeev.hello;

import info.kgeorgiy.java.advanced.hello.HelloClient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class HelloUDPClient implements HelloClient {
    @Override
    public void run(String host, int port, String prefix, int threads, int requests) {
        ExecutorService pullThreads = Executors.newFixedThreadPool(threads);
        InetSocketAddress address = new InetSocketAddress(host, port);

        IntStream.range(0, threads).forEach(x -> pullThreads.submit(() -> req(x, prefix, requests, address)));
        pullThreads.shutdown();
    }

    private void req(int x, String prefix, int requests, InetSocketAddress address) {

        try (DatagramSocket socket = new DatagramSocket()) {

            byte[] buffer = new byte[socket.getReceiveBufferSize()];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address);

            for (int i = 0; i < requests; i++) {
                String request = prefix + x + "_" + i;
                byte[] reqBuff = request.getBytes(StandardCharsets.UTF_8);

                while (true) {
                    try {
                        System.out.println(request);
                        packet.setData(reqBuff);
                        socket.send(packet);

                        packet.setData(buffer);
                        socket.receive(packet);
                        final String response = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
                        System.out.println(response);

                        if (response.equals("Hello, " + request)) {
                            break;
                        }

                    } catch (IOException ignored) {
                    }
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }
}
