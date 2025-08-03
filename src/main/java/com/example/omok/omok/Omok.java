package com.example.omok.omok;



public class Omok {
    private Integer roomId;
    private String userId;
    private Integer x;
    private Integer y;
    // 0 흑돌, 1 흰돌
    private Integer playerColor;

    public Omok() {
        this.userId = "";
        this.roomId = 0;
        this.x = 0;
        this.y = 0;
        this.playerColor = 0;
    };

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(Integer playerColor) {
        this.playerColor = playerColor;
    }
}
