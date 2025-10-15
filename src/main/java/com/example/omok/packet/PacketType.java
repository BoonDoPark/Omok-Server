package com.example.omok.packet;

import java.util.Arrays;

public enum PacketType {
    // 클라이언트로 부터 받아서 브로드캐스팅할 패킷
    PLAYER_READY(0),
    PLAYER_COORDINATE(1),
    PLAYER_INITIALIZE(2),
    PLAYER_EXIT(3),

    // 서버가 일방적으로 클라이언트한테 전파하는 패킷
    SERVER_SEND_DISCRIMINATE(4),
    SERVER_SEND_GAMESTART(5),

    // 서버가 에러를 클라이언트에게 전달할 패킷
    COORDINATE_FAIL(6);

    ;



    private final int packetType;

    PacketType(int packetType) {
        this.packetType = packetType;
    }

    public int getPacketType() {
        return packetType;
    }

    public static PacketType fromPacketType(int packetType) {
        return Arrays.stream(PacketType.values())
                .filter(pt -> pt.getPacketType() == packetType)
                .findFirst()
                .orElse(null);
    }
}
