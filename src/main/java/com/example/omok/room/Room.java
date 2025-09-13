package com.example.omok.room;

import com.example.omok.Packet.Packet;
import com.example.omok.Packet.PacketType;
import com.example.omok.player.Player;
import com.example.omok.serialize.Serialization;

import java.io.IOException;
import java.util.*;

public class Room {
    private final String roomId;
    private final List<Player> players;
    private Integer currentTurnColor;
    private int[][] board;

    private final Serialization serialization;
    private RoomStatus status;


    public Room() {
        this.roomId = UUID.randomUUID().toString();
        this.players = Collections.synchronizedList(new ArrayList<>());
        this.setStatus(RoomStatus.WAIT);
        this.board = new int[19][19];
        this.serialization = new Serialization();
    }

    private void initializeBoard() {
        this.currentTurnColor = -1;
        for (int[] row : this.board) {
            Arrays.fill(row, -1);
        }
    }
    
    public boolean isAllPlayersReady() {
        return this.players.stream().filter(
                Player::getIsReady
        ).count() >= 2;
    }

    public boolean isRoomReadyToGame() {
        return this.status == RoomStatus.WAIT && this.isAllPlayersReady();
    }
    
    public void sendOpponentPlayerIds() throws IOException {
        if (this.status == RoomStatus.READY) {
            for (Player player : this.getPlayers()) {
                Player opponentPlayer = this.getPlayers().stream()
                        .filter(p -> !p.getUserId().equals(player.getUserId()))
                        .findFirst().orElse(null);
                if(opponentPlayer != null) {
                    byte[] opponentPlayerBytes = serialization.serializePacket(
                            new Packet(
                                  PacketType.READY,
                                  0,
                                  0,
                                  0,
                                  0,
                                  0,
                                  opponentPlayer.getUserId()
                            )
                    );
                    player.getSocket().getOutputStream().write(opponentPlayerBytes);
                    player.getSocket().getOutputStream().flush();
                }
            }
        }
    }

    public void broadcast(byte[] message) {
        for (Player player : this.getPlayers()) {
            try {
                player.getSocket().getOutputStream().write(message);
                player.getSocket().getOutputStream().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addPlayer(Player player) throws IOException {
        int playerColor = players.size();
        player.setPlayerColor(playerColor);
        player.getSocket().getOutputStream().flush();
        players.add(player);
        this.sendOpponentPlayerIds();
    }

    public void removePlayer(Player player) throws IOException {
        player.getSocket().close();
        players.remove(player);
    }

    public void startGame() {
        if (this.status != RoomStatus.READY) {
            return;
        }
        this.initializeBoard();
        this.broadcast(
                serialization.serializePacket(
                        new Packet(
                                PacketType.GAMESTART, // 얘만 봄
                                0,
                                0,
                                0,
                                0,
                                0,
                                ""
                        )
                )
        );
        this.setStatus(RoomStatus.IN_PROGRESS);
    }

    public void endGame() {
        if (this.status != RoomStatus.IN_PROGRESS) {
            return;
        }
        this.setStatus(RoomStatus.FINISHED);
    }
    
    public void initializeGame() throws IOException {
        if (this.status != RoomStatus.FINISHED) {
            return;
        }
        this.initializeBoard();
        for(Player player : this.players) {
            player.setIsReady(false);
        }
        this.setStatus(RoomStatus.WAIT);
    }

    public boolean isPlayerTurn(int playerColor) {
        return currentTurnColor == playerColor;
    }

    public void changeTurn(int playColor) {
        this.currentTurnColor = playColor;
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
                && board[currentY][currentX] == color
        ) {
            count++;
            currentX += dx;
            currentY += dy;
        }
        return count;
    }

    public void placeStone(Packet coordinatePacket) {
        if (this.status == RoomStatus.IN_PROGRESS) {
            return;
        }
        if (this.isPlayerTurn(coordinatePacket.getPlayerColor())) {
            System.out.println("현재 플레이어의 차례가 아닙니다.");
            return;
        }
        board[coordinatePacket.getY()][coordinatePacket.getX()] = coordinatePacket.getPlayerColor();
        this.changeTurn(coordinatePacket.getPlayerColor());
        this.broadcast(
                serialization.serializePacket(
                        coordinatePacket
                )
        );
        if (this.checkWinPlayer(coordinatePacket)) {
            this.broadcast(
                    serialization.serializePacket(
                        new Packet(
                               PacketType.DISCRIMINATE, // 얘만 봄
                                0,
                                coordinatePacket.getPlayerColor(), // 얘만 봄
                                0,
                                0,
                                0,
                                ""
                        )
                    )
            );
        }
    }

    public String getRoomId() {
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
