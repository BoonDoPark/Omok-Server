package com.example.omok.serialize;

import com.example.omok.Packet.Packet;
import com.example.omok.player.Player;

import java.nio.charset.StandardCharsets;

public class Serialization {
    private final Integer INTEGER_BYTE_SIZE = 4;

    public byte[] serializePacket(Packet packet) {
        byte[] byteArray = new byte[4 * INTEGER_BYTE_SIZE];

        // byteArray = data, srcPos=BytestartPoint, dest=byteArray, destPos=ByteArrayStartPoint, offset=4, length=ByteLength
        // deep copy
        System.arraycopy(int2Bytes(packet.getRoomStatusCode()), 0, byteArray, 0, INTEGER_BYTE_SIZE);
        System.arraycopy(int2Bytes(packet.getX()), 0, byteArray, 4, INTEGER_BYTE_SIZE);
        System.arraycopy(int2Bytes(packet.getY()), 0, byteArray, 8, INTEGER_BYTE_SIZE);
        System.arraycopy(int2Bytes(packet.getPlayerColor()), 0, byteArray, 12, INTEGER_BYTE_SIZE);
        return byteArray;
    }

    public byte[] serializePlayer(Player player) {
        byte[] userIdBytes = player.getUserId().getBytes(StandardCharsets.UTF_8);
        byte[] userIdLengthBytes = int2Bytes(userIdBytes.length);
        byte[] playerColorBytes = int2Bytes(player.getPlayerColor());

        int totalSize = 4 + userIdBytes.length + 4;
        byte[] data = new byte[totalSize];

        int offset = 0;
        System.arraycopy(userIdLengthBytes, 0, data, offset, 4);
        offset += 4;
        System.arraycopy(userIdBytes, 0, data, offset, userIdBytes.length);
        offset += userIdBytes.length;
        System.arraycopy(playerColorBytes, 0, data, offset, 4);

        return data;
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
