package com.example.test;

/**
 *
 * @author Kathryn Weidman
 * @author Emma Kelly
 * @author Brent Torres
 * @author Matthew Tran
 *
 * @version 10/11/2022
 *
 * */

public class Tile {
    private int row;
    private int col;
    private float xcord; // top left
    private float ycord; // top left
    public Tile() {
        row = 0;
        col = 0;
        xcord = 0;
        ycord = 0;
    }

    public Tile(int _row, int _col, float _xcord, float _ycord) {
        row = _row;
        col = _col;
        xcord = _xcord;
        ycord = _ycord;
    }
    public float getXcord(){return xcord;}
    public float getYcord(){return ycord;}
}
