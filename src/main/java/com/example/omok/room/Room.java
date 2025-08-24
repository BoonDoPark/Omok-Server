package com.example.omok.room;

import com.example.omok.Packet.Packet;
import com.example.omok.player.Player;

import java.net.Socket;
import java.util.*;

public class Room {
    private final Integer roomId;
    private RoomStatus status;
    private final List<Player> players;
    private static Integer nextRoomId = 1;
    private int[][] OMOK_BOARD;
    private final Set<String> readyPlayers = new HashSet<>();

    public Room() {
        this.roomId = nextRoomId++;
        this.players = new ArrayList<>();
        this.status = RoomStatus.WAIT;
        this.OMOK_BOARD = new int[19][19];
        for (int[] row : this.OMOK_BOARD) {
            Arrays.fill(row, -1);
        }
    }

    public int addPlayer(Socket socket, String userId) {
        int playerColor = players.size();
        players.add(new Player(userId, playerColor, socket));

        if (players.size() == 2) {
            this.status = RoomStatus.READY;
            System.out.println("방 " + roomId + " 게임 시작 준비");
        }
        return playerColor;
    }

    public boolean setPlayerReady(String userId) {
        readyPlayers.add(userId);
        return readyPlayers.size() == 2;
    }

    public void removePlayer(Socket socket) {
        players.removeIf(player -> player.getSocket().equals(socket));
        if (players.isEmpty()) {
            endGame(RoomStatus.END.getRoomStatusCode());
            System.out.println("방 " + roomId + " 게임 종료");
        }
    }

    public void startGame(Integer statusCode) {
        if(statusCode.equals(RoomStatus.START.getRoomStatusCode())) {
            this.status = RoomStatus.START;
            this.OMOK_BOARD = new int[19][19];
            for (int[] row : this.OMOK_BOARD) {
                Arrays.fill(row, -1);
            }
        }

    }

    public void endGame(Integer statusCode) {
        if(statusCode.equals(RoomStatus.END.getRoomStatusCode())) {
            this.status = RoomStatus.END;
            for (int[] row : this.OMOK_BOARD) {
                Arrays.fill(row, -1);
            }
            System.out.println("방 " + roomId + " 게임 종료 (END)");
        }
    }

    public void resetNewGame(Integer statusCode) {
        if(statusCode.equals(RoomStatus.READY.getRoomStatusCode())) {
            this.status = RoomStatus.READY;
//            this.readyPlayers.clear();
        }
    }


    public Boolean checkWinPlayer(Packet omok) {
        int x = omok.getX();
        int y = omok.getY();
        int color = omok.getPlayerColor();
        int[][] directions = {
                {1, 0},  // 가로
                {0, 1},  // 세로
                {1, 1},  // 우하향 대각선
                {1, -1}  // 우상향 대각선
        };

        for(int[] dir : directions) {
            int count = 1
                    + compareOmok(x, y, dir[0], dir[1], color)
                    + compareOmok(x, y, -dir[0], -dir[1], color);
            if(count == 5) return true;
        }
        return false;
    }

    private Integer compareOmok(int startX, int startY, int dx, int dy, int color) {
        int count = 0;
        int currentX = startX + dx;
        int currentY = startY + dy;

        while(
                currentX >= 0
                && currentX < 19
                && currentY >= 0
                && currentY < 19
                && OMOK_BOARD[currentY][currentX] == color
        ) {
            count++;
            currentX += dx;
            currentY += dy;
        }
        return count;
    }

    public void placeStone(Packet omok) {
        OMOK_BOARD[omok.getY()][omok.getX()] = omok.getPlayerColor();
    }

    public Integer getRoomId() {
        return roomId;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus roomStatus) {
        status = roomStatus;
    }
}
