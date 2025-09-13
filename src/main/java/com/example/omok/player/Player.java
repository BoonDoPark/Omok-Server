package com.example.omok.player;

import com.example.omok.Packet.Packet;
import com.example.omok.Packet.PacketType;
import com.example.omok.serialize.Serialization;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

public class Player {
    private final String userId;
    private final Socket socket;
    private Integer playerColor;
    private boolean isReady;
    private final Serialization serialization;

    public Player(
            Integer playerColor,
            Socket socket,
            boolean isReady
    ) {
        this.userId = UUID.randomUUID().toString();
        this.playerColor = playerColor;
        this.socket = socket;
        this.isReady = isReady;
        this.serialization = new Serialization();
    }

    public String getUserId() {
        return userId;
    }

    public Integer getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(Integer playerColor) throws IOException {
        this.playerColor = playerColor;
        this.notify(
                new Packet(
                        PacketType.READY,
                        0,
                        0,
                        0,
                        0,
                        playerColor, // 얘만 봄
                        ""
                )
        );
    }

    public Socket getSocket() {
        return socket;
    }

    public boolean getIsReady() {
        return isReady;
    }

    public void setIsReady(boolean ready) throws IOException {
        isReady = ready;
        this.notify(
                new Packet(
                        PacketType.READY,
                        1, // 얘만 봄
                        0,
                        0,
                        0,
                        0,
                        ""
                )
        );
    }

    public void notify(Packet packet) throws IOException {
        this.socket.getOutputStream().write(
            serialization.serializePacket(packet)
        );
    }
}
