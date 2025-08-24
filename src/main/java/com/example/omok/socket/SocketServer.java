package com.example.omok.socket;


import com.example.omok.handler.Handler;
import com.example.omok.room.Room;
import com.example.omok.room.RoomStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Component
public class SocketServer {
    private final List<Room> rooms = new ArrayList<>();

    public void start() {
        try {
            ServerSocket server = new ServerSocket(8082);
            System.out.println("Socket 연결 중...");

            while (true) {
                Socket client = server.accept();
                String userId = UUID.randomUUID().toString();
                System.out.println("socket 연결");

                Room room = findOrCreateRoom();
                int playerColor = room.addPlayer(client, userId);

                Handler handler = new Handler(client, userId, playerColor, room);

                new Thread(handler).start();
            }
        } catch (IOException e) {
            System.out.println("error = " + e.getMessage());
        }
    }

    private Room findOrCreateRoom() {
        for (Room room : rooms) {
            if (room.getStatus() == RoomStatus.WAIT) {
                System.out.println("기존 방 " + room.getRoomId() + "에 클라이언트 추가");
                return room;
            }
        }

        Room newRoom = new Room();
        rooms.add(newRoom);
        System.out.println("새로운 방 " + newRoom.getRoomId() + " 생성");
        return newRoom;
    }
}
