package com.example.omok.deserialize;

import com.example.omok.packet.Packet;
import com.example.omok.packet.PacketType;

public class Deserialization {
    public Packet deserializePacket(byte[] data) {
        Packet packet = new Packet(
                PacketType.fromPacketType(bytes2Int(data, 0)),
                bytes2Int(data, 4),
                bytes2Int(data, 8),
                bytes2Int(data, 12),
                bytes2Int(data, 16),
                bytes2Int(data, 20),
                ""
        );

        return packet;
    }

    // 빅엔디안
    private Integer bytes2Int(byte[] byteArray, int offset) {
        // 바이트 배열을 숫자로 변환
        return ((byteArray[offset]   & 0xFF) << 24) |
                ((byteArray[offset+1] & 0xFF) << 16) |
                ((byteArray[offset+2] & 0xFF) << 8)  |
                ( byteArray[offset+3] & 0xFF);
    }
}
