package com.example.omok.handler;

import com.example.omok.deserialize.Deserialization;
import com.example.omok.Packet.Packet;
import com.example.omok.player.Player;
import com.example.omok.room.Room;
import com.example.omok.room.RoomStatus;
import com.example.omok.serialize.Serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class Handler implements Runnable {
    private final Socket client;
    private final String userId;
    private final int playerColor;
    private final Room room;
    private final Deserialization deserialization;
    private final Serialization serialization;

    public Handler(Socket clientSocket, String userId, int playerColor, Room room) {
        this.client = clientSocket;
        this.userId = userId;
        this.playerColor = playerColor;
        this.room = room;
        this.deserialization = new Deserialization();
        this.serialization = new Serialization();
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = client.getInputStream();
            OutputStream outputStream = client.getOutputStream();

            Player selfPlayer = new Player(this.userId, this.playerColor, this.client);
            outputStream.write(serialization.serializePlayer(selfPlayer));
            outputStream.flush();
            System.out.println("클라이언트에게 " + this.userId + " 전송 완료.");

            if (room.getStatus() == RoomStatus.READY) {
                System.out.println("게임 시작 준비. 양측 플레이어에게 userId 전송");
                for (Player player : room.getPlayers()) {
                    Player opponentPlayer = room.getPlayers().stream()
                            .filter(p -> !p.getUserId().equals(player.getUserId()))
                            .findFirst().orElse(null);
                    if(opponentPlayer != null) {
                        byte[] opponentPlayerBytes = serialization.serializePlayer(opponentPlayer);
                        player.getSocket().getOutputStream().write(opponentPlayerBytes);
                        player.getSocket().getOutputStream().flush();
                    }
                }
                Packet readyPacket = new Packet(RoomStatus.READY.getRoomStatusCode(), 0, 0, -1);
                broadcast(serialization.serializePacket(readyPacket));
            }


            while (true) {
                byte[] packetBytes = new byte[16];
                int bytesRead = inputStream.read(packetBytes);
                if (bytesRead == -1) break;

                Packet receivedPacket = deserialization.deserializePacket(packetBytes);
                RoomStatus requestType = RoomStatus.fromCode(receivedPacket.getRoomStatusCode());

                switch (requestType) {
                    case PALYER_READY:
                        if(room.getStatus() == RoomStatus.READY) {
                            if(room.setPlayerReady(this.userId)) {
                                room.startGame(RoomStatus.START.getRoomStatusCode());
                                Packet startPacket = new Packet(RoomStatus.START.getRoomStatusCode(), 0, 0, -1);
                                broadcast(serialization.serializePacket(startPacket));
                            }
                        } else if(room.getStatus() == RoomStatus.END) {
                            if(room.getStatus() == RoomStatus.END) {
                                room.resetNewGame(RoomStatus.READY.getRoomStatusCode());
                            }
                        }
                        break;
                    case PLACE_STONE:
                        if(room.getStatus() == RoomStatus.START) {
                            room.placeStone(receivedPacket);
                            broadcast(serialization.serializePacket(receivedPacket));

                            if(room.checkWinPlayer(receivedPacket)) {
                                // 승패 정보를 담은 END 패킷 전송
                                Packet endPacket = new Packet(RoomStatus.END.getRoomStatusCode(),
                                        receivedPacket.getX(), receivedPacket.getY(), receivedPacket.getPlayerColor());
                                broadcast(serialization.serializePacket(endPacket));

                                room.endGame(RoomStatus.END.getRoomStatusCode());
                            }
                        }
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                room.removePlayer(this.client);
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("연결 종료");
        }
    }

    private void broadcast(byte[] message) {
        for (Player player : room.getPlayers()) {
            try {
                player.getSocket().getOutputStream().write(message);
                player.getSocket().getOutputStream().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
