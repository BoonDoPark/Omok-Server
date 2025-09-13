package com.example.omok.serialize;

import com.example.omok.Packet.Packet;
import com.example.omok.player.Player;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Serialization {
    private final Integer INTEGER_BYTE_SIZE = 4;
    private final Integer STRING_BYTE_SIZE = 32;

    public byte[] serializePacket(Packet packet) {
        byte[] byteArray = new byte[
                6 * INTEGER_BYTE_SIZE + packet.getPlayerId().length()
                + STRING_BYTE_SIZE
        ];

        List<byte[]> byteElements = new ArrayList<>();
        byteElements.add(int2Bytes(packet.getPacketType().getPacketType()));
        byteElements.add(int2Bytes(packet.getIsPlayerReady()));
        byteElements.add(int2Bytes(packet.getWhoWon()));
        byteElements.add(int2Bytes(packet.getX()));
        byteElements.add(int2Bytes(packet.getY()));
        byteElements.add(int2Bytes(packet.getPlayerColor()));

        for (int i = 0; i < byteElements.size(); i++) {
            System.arraycopy(
                    byteElements.get(i),
                    0,
                    byteArray,
                    i*INTEGER_BYTE_SIZE,
                    INTEGER_BYTE_SIZE
            );
        }
        System.arraycopy(
                packet.getPlayerId().getBytes(),
                0,
                byteArray,
                byteElements.size()*INTEGER_BYTE_SIZE+STRING_BYTE_SIZE,
                STRING_BYTE_SIZE
        );


        return byteArray;
    }

    public byte[] serializeStatus(Integer statusCode) {
        return int2Bytes(statusCode);
    }

    private byte[] int2Bytes(int value) {
        return new byte[]{
                (byte) (value >> 24),
                (byte) (value >> 16),
                (byte) (value >> 8),
                (byte) value
        };
    }
}
