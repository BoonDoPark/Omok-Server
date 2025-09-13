package com.example.omok.Packet;

public class CoordinatePacket {
    private Integer x;
    private Integer y;
    private Integer playerColor;

    public CoordinatePacket() {}

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

    public Integer getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(Integer playerColor) {
        this.playerColor = playerColor;
    }
}
