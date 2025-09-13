package com.example.omok.Packet;

public enum PacketType {
    // 클라이언트로 부터 받아서 브로드캐스팅할 패킷
    READY(0),
    COORDINATE(1),
    INITIALIZE(2),
    EXIT(3),

    // 서버가 일방적으로 클라이언트한테 전파하는 패킷
    DISCRIMINATE(4),
    GAMESTART(5);

    private final int packetType;

    PacketType(int packetType) {
        this.packetType = packetType;
    }

    public int getPacketType() {
        return packetType;
    }
}
