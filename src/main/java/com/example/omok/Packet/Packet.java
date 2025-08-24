package com.example.omok.Packet;



public class Packet {
    private Integer roomStatusCode;
    private Integer playerColor;
    private Integer x;
    private Integer y;

    public Packet() {
        this.roomStatusCode = 0;
        this.x = 0;
        this.y = 0;
        this.playerColor = 0;
    };

    public Packet(Integer roomStatusCode, int x, int y, int palyerColor) {
        this.roomStatusCode = roomStatusCode;
        this.x = x;
        this.y = y;
        this.playerColor = palyerColor;
    }

    public Integer getRoomStatusCode() {
        return roomStatusCode;
    }

    public void setRoomStatusCode(Integer roomStatusCode) {
        this.roomStatusCode = roomStatusCode;
    }

    public Integer getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(Integer playerColor) {
        this.playerColor = playerColor;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }
}
