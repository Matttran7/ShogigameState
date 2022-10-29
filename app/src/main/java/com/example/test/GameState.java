package com.example.test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

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

public class GameState{
    /* [other class]/
     * Board (reference to copy constructor)
     * Pieces
     * */

    /**
     * Grave (2)
     * Button (new game)
     * TextView (____'s Turn / ____ Wins!)
     * Turn (keep track) X
     *
     * Current selected piece
     * Selected space
     * Locations
     *
     * Players
     *
     * *** if stalemate ***
     * Previous turn state
     * */
    private boolean turn; // [true = Player 1, false = other]
    private Board board;
    private final int p1 = 0;
    private final int p2 = 1;
    private Graveyard grave_1;
    private Graveyard grave_2;
    private ArrayList<Piece> pieces1;
    private ArrayList<Piece> pieces2;
    private ArrayList<ArrayList<Integer>> cords;
    private String banner;
    private int turnCount = 0;

    /**
     * Current state of the game constructor
     */
    public GameState() { //Cntr
        turn = !first();
        board = new Board();
        assignPieces();
        grave_1 = new Graveyard();
        grave_2 = new Graveyard();
        pieces1 = new ArrayList<Piece>();
        pieces2 = new ArrayList<Piece>();
        cords = new ArrayList<>();

        changeTurn();
        //Declare turn the opposite of first so we can call the changeTurn method
        //to assign banner a value and correct the turn value bc I'm lazy
    }

    /**
     * Current state of the game deep copy constructor
     */
    public GameState(int id, GameState orig) {
        this.turn = orig.turn;
        this.board = orig.board;
        assignPieces();
        this.grave_1 = orig.grave_1;
        this.grave_2 = orig.grave_2;
        this.pieces1.addAll(orig.pieces1);
        this.pieces2.addAll(orig.pieces2);
    }

// UNNECESSARY FOR THIS ASSIGNMENT
//    /**
//     * Draw board
//     * */
//    public void onDraw(Canvas canvas){
//        // draw lines
//        // draw tiles
//        int tileSize = board.getTileSize();
//        for(Tile tile : board.tiles){
//            canvas.drawRect(tile.getXcord(),tile.getYcord(),tile.getXcord()+tileSize,tile.getYcord()+tileSize, );
//        }
//    }

    /**
     * Determine next turn based on current turn
     */
    public void changeTurn() {
        turn = !turn;

        if (turn) {
            banner = "Player one's Turn";
        } else{
            banner = "Player two's turn";
        }
    }

    /**
     * Display all game state information in the TextView
     */
    @Override
    public String toString() {
        Random rand = new Random();
        String string = "Player 1 Pieces: ";
        int iterations = 0;
        for (Piece p : pieces1) {
            if (!(p.getRow() == -1 || p.getCol() == -1)) { //Don't mention promotion pieces ugh
                string = string + p.pieceType + " at (" + p.getCol() + ", " + p.getRow() + ")";
                iterations++;
                if (iterations != pieces1.size()) {
                    string = string + "; ";
                } else {
                    string = string + ". ";
                }
            }
        }
        // newline
        string = string + "\n";
        string = string + "Player 2 Pieces: ";
        iterations = 0;
        for (Piece p : pieces2) {
            if (!(p.getRow() == -1 || p.getCol() == -1)) { //Don't mention promotion pieces ugh
                string = string + p.pieceType + " at (" + p.getCol() + ", " + p.getRow() + ")";
                iterations++;
                if (iterations != pieces2.size()) {
                    string = string + "; ";
                } else {
                    string = string + ". ";
                }
            }
        }
        // newline
        string = string + "\n";
        if (!isChecked()) {
            string = string + "No one is in check. ";
        }
        // newline
        string = string + "\n";
        string = string + "Number of turns made: " + turnCount + ". ";
        // newline
        string = string + "\n";
        string = string + "Possible moves for King:";
        moveKing(turn);
        // make a random move bc not human player yet
        int randomMove = rand.nextInt(cords.size());
        int movex = cords.get(randomMove).get(0);
        int movey = cords.get(randomMove).get(1);
        for(ArrayList<Integer> arraylist : cords){
            string = string + "[";
            string = string + arraylist.remove(0) + ", ";
            string = string + arraylist.remove(0) + "], ";
        }
        // newline
        string = string + "\n";
        string = string + "King moves [ " + movex + " , " + movey + " ]";
        return string + banner + ".";
    }


    //Make methods for defined actions
    //make pieces
    private void assignPieces() {
        for (Piece.GAME_PIECES piece : Piece.GAME_PIECES.values()) {
            for (int i = 0; i < piece.getAmount(); i++) {
                pieces1.add(new Piece(piece, Piece.DIRECTION.FORWARD)); //player 1 (id = 0)
                pieces2.add(new Piece(piece,Piece.DIRECTION.BACKWARD)); //player 2 (id = 1)
            } // for i
        } // for pieces

        placePieces(pieces1, 0);
        placePieces(pieces2, 1);
    }


    /**
     * Assigns rows and columns to each piece for initial setup for each player
     * Promotion pieces are unassigned due to them not existing on the board at that time
     *
     * */
    private void placePieces(ArrayList<Piece> heehee, int id) { //board is 9x9 tiles
        //front row is 9 pawns
        //middle row is 1 space, bishop, 5 spaces, rook, 1 space (left to right from players pov)
        //back row is lance, knight, silver, gold, king, gold, silver, knight, lance
        int pawnNum = 0, lanceNum = 0, knightNum = 0, goldNum = 0, silvNum = 0;

        if (id == 0) { //forward facing pieces (player 1)
            for (Piece p : heehee) {
                switch (p.pieceType.getID()) { //What kind of piece is it
                    case R.drawable.promoted_bishop: case R.drawable.promoted_lance:
                    case R.drawable.promoted_knight: case R.drawable.promoted_pawn:
                    case R.drawable.promoted_rook: case R.drawable.promoted_silv_gen:
                        //if it's a promoted piece skip it
                        break;
                    case R.drawable.pawn:
                        p.setRow(6); //up down
                        p.setCol(pawnNum); //side to side
                        pawnNum++;
                        break;
                    case R.drawable.bishop:
                        p.setRow(7);
                        p.setCol(1);
                        break;
                    case R.drawable.rook:
                        p.setRow(7);
                        p.setCol(7);
                        break;
                    case R.drawable.lance:
                        p.setRow(8);
                        if (lanceNum == 0) {
                            p.setCol(0);
                        } else {
                            p.setCol(8);
                        }
                        lanceNum++;
                        break;
                    case R.drawable.knight:
                        p.setRow(8);
                        if (knightNum == 0) {
                            p.setCol(1);
                        } else {
                            p.setCol(7);
                        }
                        knightNum++;
                        break;
                    case R.drawable.silv_gen:
                        p.setRow(8);
                        if (silvNum == 0) {
                            p.setCol(2);
                        } else {
                            p.setCol(6);
                        }
                        silvNum++;
                        break;
                    case R.drawable.gold_gen:
                        p.setRow(8);
                        if (goldNum == 0) {
                            p.setCol(3);
                        } else {
                            p.setCol(5);
                        }
                        goldNum++;
                        break;
                    case R.drawable.king:
                        p.setRow(8);
                        p.setCol(4);
                        break;
                }
            }
        } //end p1 setup

        if (id == 1) { //backward facing pieces (player 2)
            for (Piece p : heehee) {
                switch (p.pieceType.getID()) { //What kind of piece is it
                    case R.drawable.promoted_bishop: case R.drawable.promoted_lance:
                    case R.drawable.promoted_knight: case R.drawable.promoted_pawn:
                    case R.drawable.promoted_rook: case R.drawable.promoted_silv_gen:
                            //if it's a promoted piece skip it
                            break;
                    case R.drawable.pawn:
                        p.setRow(2); //up down
                        p.setCol(pawnNum); //side to side
                        pawnNum++;
                        break;
                    case R.drawable.bishop: //switch cols for opponent bishop and rook bc perspective
                        p.setRow(1);
                        p.setCol(7);
                        break;
                    case R.drawable.rook:
                        p.setRow(1);
                        p.setCol(1);
                        break;
                    case R.drawable.lance:
                        p.setRow(0);
                        if (lanceNum == 0) {
                            p.setCol(0);
                        } else {
                            p.setCol(8);
                        }
                        lanceNum++;
                        break;
                    case R.drawable.knight:
                        p.setRow(0);
                        if (knightNum == 0) {
                            p.setCol(1);
                        } else {
                            p.setCol(7);
                        }
                        knightNum++;
                        break;
                    case R.drawable.silv_gen:
                        p.setRow(0);
                        if (silvNum == 0) {
                            p.setCol(2);
                        } else {
                            p.setCol(6);
                        }
                        silvNum++;
                        break;
                    case R.drawable.gold_gen:
                        p.setRow(0);
                        if (goldNum == 0) {
                            p.setCol(3);
                        } else {
                            p.setCol(5);
                        }
                        goldNum++;
                        break;
                    case R.drawable.king:
                        p.setRow(0);
                        p.setCol(4);
                        break;
                }
            }
        } //end p2 setup
    }


    // see who goes first
    public boolean first() {
        Random rand = new Random();
        int i = rand.nextInt(11);
        if (i < 6) {
            return true;
        } else {
            return false;
        }
    }
    // check and checkmate methods
    public boolean isChecked() {return false;}
    public boolean isCheckmated() {return false;}

    /**
     * Pre-set selected pieces and order
     *
     * @return selected piece from pieces array otherwise 0 (none)
     */
    public int selectPiece() {
        switch(turnCount) {
            case 1:
                turnCount++;
                return pieces1.indexOf(Piece.GAME_PIECES.PAWN);
            case 2:
                turnCount++;
                return pieces2.indexOf(Piece.GAME_PIECES.PAWN);
            case 3:
                return pieces1.indexOf(Piece.GAME_PIECES.ROOK);
        }
        return 0;
    }

    /**
     * Piece movement (hard coded now, flexible code later)
     */
    public void movePiece() {

        // identify the piece
        switch(turnCount) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;

        }
        changeTurn();
        turnCount++;
    }

    /**
     * Piece capture (hard coded now, flexible code later)
     * pieces: (none) (skipping for GameState, from meeting)
     */
    public int pieceCapture() {
        // ID of piece captured
        // If 0, then no pieces were captured
        // Sends ID to graveyard
        return 0;
        // identify the piece
    }

    public void moveLance() {
        //Complete a bounds check on the board based on the lance's specific movements
        // must clear "cords" first, it holds all the possible moves for the current selected piece
        cords.clear();
        ArrayList<Integer> toStore = new ArrayList<>();

        if (turnCount == 1) {
            int yCoord = pieces1.getCol();
            int xCoord = pieces1.getRow();

            for(int i = yCoord - 1; i > 0 ; --i){
                toStore.add(xCoord);
                toStore.add(i);
                cords.add(toStore);
                toStore.clear();
            }
        }
    }

    public void moveKing(boolean theTurn){
        cords.clear();
        ArrayList<Integer> toStore = new ArrayList<>();
        int ycord = -1;
        int xcord = -1;
        int lever = 0;
        if(theTurn) {
             ycord = pieces1.get(pieces1.indexOf(Piece.GAME_PIECES.KING)).getCol();
             xcord = pieces1.get(pieces1.indexOf(Piece.GAME_PIECES.KING)).getRow();
             lever = 1;
        }
        if(!theTurn) {
             ycord = pieces2.get(pieces2.indexOf(Piece.GAME_PIECES.KING)).getCol();
             xcord = pieces2.get(pieces2.indexOf(Piece.GAME_PIECES.KING)).getRow();
             lever = 1;
        }
        if(lever == 1) {
            if (xcord > 0) {
                toStore.add(xcord - 1);
                toStore.add(ycord);
                cords.add(toStore);
                toStore.clear();
            }
            if (xcord > 0 && ycord < 9) {
                toStore.add(xcord - 1);
                toStore.add(ycord + 1);
                cords.add(toStore);
                toStore.clear();
            }
            if (ycord < 9) {
                toStore.add(xcord);
                toStore.add(ycord + 1);
                cords.add(toStore);
                toStore.clear();
            }
            if (xcord < 9 && ycord < 9) {
                toStore.add(xcord + 1);
                toStore.add(ycord + 1);
                cords.add(toStore);
                toStore.clear();
            }
            if (xcord < 9) {
                toStore.add(xcord + 1);
                toStore.add(ycord);
                cords.add(toStore);
                toStore.clear();
            }
            if (xcord < 9 && ycord > 0) {
                toStore.add(xcord + 1);
                toStore.add(ycord - 1);
                cords.add(toStore);
                toStore.clear();
            }
            if (ycord > 0) {
                toStore.add(xcord);
                toStore.add(ycord - 1);
                cords.add(toStore);
                toStore.clear();
            }
            if (xcord > 0 && ycord > 0) {
                toStore.add(xcord - 1);
                toStore.add(ycord - 1);
                cords.add(toStore);
                toStore.clear();
            }
        } // lever

        }// if turncount

}
