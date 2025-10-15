package com.example.omok.packet;


public class Packet {
    // TODO: 해당 패킷 분리 (필드에 값이 너무 많음
    private PacketType packetType;
    private final int isPlayerReady;  // 클라이언트에서 게임 준비 눌렀을 때
    private final int whoWon; // 누가 이겼는지
    private int x;
    private int y;
    private int playerColor;
    private String playerId;

    public Packet() {
        this.packetType = PacketType.PLAYER_READY;
        this.isPlayerReady = 0;
        this.whoWon = 0;
        this.x = 0;
        this.y = 0;
        this.playerColor = 0;
        this.playerId = "";
    };

    public Packet(
            PacketType packetType,
            int isPlayerReady,
            int whoWon,
            int x,
            int y,
            int playerColor,
            String playerId
    ) {
        this.packetType = packetType;
        this.isPlayerReady = isPlayerReady;
        this.whoWon = whoWon;
        this.x = x;
        this.y = y;
        this.playerColor = playerColor;
        this.playerId = playerId;
    }

    public PacketType getPacketType() {
        return packetType;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(int playerColor) {
        this.playerColor = playerColor;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public int getIsPlayerReady() {
        return isPlayerReady;
    }

    public int getWhoWon() {
        return whoWon;
    }
}
