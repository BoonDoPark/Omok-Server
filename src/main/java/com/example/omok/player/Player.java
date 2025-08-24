package com.example.omok.player;

import java.net.Socket;

public class Player {
    private final String userId;
    private final Integer playerColor;
    private final Socket socket;

    public Player(String userId, Integer playerColor, Socket socket) {
        this.userId = userId;
        this.playerColor = playerColor;
        this.socket = socket;
    }

    public String getUserId() {
        return userId;
    }

    public Integer getPlayerColor() {
        return playerColor;
    }

    public Socket getSocket() {
        return socket;
    }
}
