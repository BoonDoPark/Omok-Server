package com.example.omok.room;

public enum RoomStatus {
    WAIT(0),
    READY(1),
    IN_PROGRESS(2),
    FINISHED(3),

    CREATE_ROOM(21),
    JOIN_ROOM(22);

    private final Integer roomStatusCode;

    RoomStatus(Integer roomStatusCode) {
        this.roomStatusCode = roomStatusCode;
    }

    public Integer getRoomStatusCode() {
        return roomStatusCode;
    }

    public static RoomStatus fromCode(int code) {
        for (RoomStatus status : RoomStatus.values()) {
            if (status.getRoomStatusCode() == code) return status;
        }
        return null;
    }
}
