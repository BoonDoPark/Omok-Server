package com.example.omok.handler;

import com.example.omok.deserialize.Deserialization;
import com.example.omok.omok.Omok;
import com.example.omok.serialize.Serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Handler implements Runnable {
    private final Socket client;
    private final String userId;
    private final Deserialization deserialization;
    private final Serialization serialization;
    private final List<Socket> players;
    private final int playColor;

    public Handler(Socket clientSocket, String userId, List<Socket> players, int playColor) {
        this.client = clientSocket;
        this.userId = userId;
        this.players = players;
        this.playColor = playColor;
        this.deserialization = new Deserialization();
        this.serialization = new Serialization();
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = client.getInputStream();
            OutputStream outputStream = client.getOutputStream();

            Omok omok = new Omok();
            omok.setUserId(this.userId);
            omok.setPlayerColor(playColor);
            byte[] bytes = serialization.serializeObjectToByte(omok);
            outputStream.write(bytes);
            outputStream.flush();
            System.out.println("클라이언트에게 " + this.userId + "' 전송 완료.");

            byte[] buffer = new byte[1024];

            while (true) {
                int bytesRead = inputStream.read(buffer);

                if (bytesRead == -1) {
                    System.out.println("클라이언트 연결 종료.");
                    break;
                }

                if (bytesRead > 0) {
                    byte[] receivedBytes = new byte[bytesRead];
                    System.arraycopy(buffer, 0, receivedBytes, 0, bytesRead);

                    try {
                        Omok receivedOmok = deserialization.deserializeByte2Object(receivedBytes);
                        System.out.println(
                                "수신된 Omok :  UserId: '" + receivedOmok.getUserId()
                                        + "', RoomId: " + receivedOmok.getRoomId()
                                        + ", X: " + receivedOmok.getX()
                                        + ", Y: " + receivedOmok.getY()
                                        + ", color: " + receivedOmok.getPlayerColor()
                        );


                        byte[] responseBytes = serialization.serializeObjectToByte(receivedOmok);
                        List<Socket> playersCopy = new ArrayList<>(players);
                        for (Socket player : playersCopy) {
                            try {
                                OutputStream playerOutputStream = player.getOutputStream();
                                playerOutputStream.write(responseBytes);
                                System.out.println("메세지 전송 완료.");
                                playerOutputStream.flush();
                            } catch (IOException e) {
                                System.err.println("메시지 전송 실패");
                            }
                        }

                    } catch (Error e) {
                        System.err.println(e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("연결 종료");
        }
    }
}
