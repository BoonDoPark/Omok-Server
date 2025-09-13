package com.example.omok.deserialize;

import com.example.omok.Packet.Packet;
import com.example.omok.player.Player;

import java.nio.charset.StandardCharsets;

public class Deserialization {
    public Packet deserializePacket(byte[] data) {
        Packet packet = new Packet();
        packet.setX(bytes2Int(data, 4));
        packet.setY(bytes2Int(data, 8));
        packet.setPlayerColor(bytes2Int(data, 12));

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
