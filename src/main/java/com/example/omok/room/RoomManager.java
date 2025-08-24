package com.example.omok.room;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoomManager {
    private final Map<Integer, Room> rooms;
    public RoomManager(Map<Integer, Room> rooms) {
        this.rooms = rooms;
    }

    public void createRoom() {
        Room newRoom = new Room();
        rooms.put(newRoom.getRoomId(), newRoom);
    }

    public Room joinRoom(Integer roomId) {
        Room room = rooms.get(roomId);
        if(room.getStatus() == RoomStatus.WAIT) {
            System.out.println(roomId + "번 방에 입장하셨습니다.");
            return room;
        }
        System.out.println("플레이어가 방에 입장할 수 없습니다.");
        return null;
    }

    public List<Integer> getJoinableRooms() {
        return rooms.values().stream()
                .filter(room -> room.getStatus() == RoomStatus.WAIT)
                .map(Room::getRoomId)
                .toList();
    }

    public void removeRoom(Integer roomId) {
        rooms.remove(roomId);
        System.out.println("해당 방 삭제");
    }


}
