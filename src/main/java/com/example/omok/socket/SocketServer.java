package com.example.omok.socket;


import com.example.omok.handler.Handler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Component
public class SocketServer {
    private List<Socket> players = new ArrayList<>();

    public void start() {
        try {
            ServerSocket server = new ServerSocket(8082);
            System.out.println("Socket 연결 중...");

            while (true) {
                Socket client = server.accept();
                String userId = UUID.randomUUID().toString();
                System.out.println("socket 연결");
                players.add(client);
                int playerColor = players.indexOf(client);

                Handler handler = new Handler(client, userId, players, playerColor);

                new Thread(handler).start();
            }
        } catch (IOException e) {
            System.out.println("error = " + e.getMessage());
        }
    }
}
