package com.example.omok.deserialize;

import com.example.omok.omok.Omok;

import java.nio.charset.StandardCharsets;

public class Deserialization {

    public Omok deserializeByte2Object(byte[] byteArray) {
        int offset = 0;

        int userIdLength = byte2Int(byteArray, offset);
        offset += 4;

        byte[] userIdBytes = new byte[userIdLength];

        System.arraycopy(byteArray, offset, userIdBytes, 0, userIdLength);

        String userId = new String(userIdBytes, StandardCharsets.UTF_8);
        offset += userIdLength;

        int roomId = byte2Int(byteArray, offset);
        offset += 4;

        int x = byte2Int(byteArray, offset);
        offset += 4;

        int y = byte2Int(byteArray, offset);
        offset += 4;

        int playerColor = byte2Int(byteArray, offset);

        Omok omok = new Omok();
        omok.setUserId(userId);
        omok.setRoomId(roomId);
        omok.setX(x);
        omok.setY(y);
        omok.setPlayerColor(playerColor);

        return omok;
    }

    // 빅엔디안
    private Integer byte2Int(byte[] byteArray, int offset) {
        // 바이트 배열을 숫자로 변환
        return ((byteArray[offset]   & 0xFF) << 24) |
                ((byteArray[offset+1] & 0xFF) << 16) |
                ((byteArray[offset+2] & 0xFF) << 8)  |
                ( byteArray[offset+3] & 0xFF);
    }
}
