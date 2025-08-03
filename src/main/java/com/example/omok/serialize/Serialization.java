package com.example.omok.serialize;

import com.example.omok.omok.Omok;

import java.nio.charset.StandardCharsets;

public class Serialization {
    private final Integer BYTES_PER_INT = 4;
    private final Integer INTEGER_BYTE_SIZE = 4;

    public byte[] serializeObjectToByte(Omok omok) {
        byte[] userIdBytes = omok.getUserId().getBytes(StandardCharsets.UTF_8);
        int userIdLength = userIdBytes.length;

        // userId의 길이 정보 BYTES_PER_INT
        int totalByteSize = BYTES_PER_INT + userIdLength + (4 * INTEGER_BYTE_SIZE);
        byte[] byteArray = new byte[totalByteSize];

        int offset = 0;
        offset = int2Byte(byteArray, offset, userIdLength);

        System.arraycopy(userIdBytes, 0, byteArray, offset, userIdLength);
        offset += userIdLength;

        offset = int2Byte(byteArray, offset, omok.getRoomId());

        offset = int2Byte(byteArray, offset, omok.getX());

        offset = int2Byte(byteArray, offset, omok.getY());

        int2Byte(byteArray, offset, omok.getPlayerColor());

        return byteArray;
    }

    // 빅엔디안
    private int int2Byte(byte[] byteArray, int offset, int value) {
        // 숫자를 바이트로 변환
        byteArray[offset++] = (byte) ((value >> 24) & 0xFF);
        byteArray[offset++] = (byte) ((value >> 16) & 0xFF);
        byteArray[offset++] = (byte) ((value >> 8) & 0xFF);
        byteArray[offset++] = (byte) (value & 0xFF);

        // offset을 증가
        return offset;
    }
}
