package com.example.omok.Packet;

public class RoomStatusPacket {
    private Integer statusCode;

    public RoomStatusPacket() {}

    public Integer getStatuscode() {
        return statusCode;
    }

    public void setStatuscode(Integer statusCode) {
        this.statusCode = statusCode;
    }
}
