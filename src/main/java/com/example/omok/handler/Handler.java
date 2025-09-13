package com.example.omok.handler;

import com.example.omok.Packet.PacketType;
import com.example.omok.deserialize.Deserialization;
import com.example.omok.Packet.Packet;
import com.example.omok.player.Player;
import com.example.omok.room.Room;

import java.io.IOException;

public class Handler implements Runnable {
    private final Player player;
    private final Room room;
    private final Deserialization deserialization;

    public Handler(Player player, Room room) {
        this.player = player;
        this.room = room;
        this.deserialization = new Deserialization();
    }

    @Override
    public void run() {
        try {
            while (true) {
                byte[] packetBytes = new byte[1024];
                int bytesRead = this.player.getSocket().getInputStream().read(packetBytes);
                if (bytesRead == -1) break;

                Packet receivedPacket = deserialization.deserializePacket(packetBytes);

                // 사용자의 게임 준비 완료 패킷
                if (receivedPacket.getPacketType().equals(PacketType.READY)) {
                    this.player.setIsReady(true);
                    if (room.isRoomReadyToGame()) {
                        room.startGame();
                    }
                }

                // 사용자의 돌 좌표 정보 패킷
                if (receivedPacket.getPacketType().equals(PacketType.COORDINATE)) {
                    room.placeStone(receivedPacket);
                }

                if (receivedPacket.getPacketType().equals(PacketType.INITIALIZE)) {
                    room.initializeGame();
                }

                if (receivedPacket.getPacketType().equals(PacketType.EXIT)) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                this.room.removePlayer(this.player);
                room.endGame();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("연결 종료");
        }
    }
}
