package com.example.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View;

public class MainSV extends SurfaceView implements View.OnClickListener {

    private GameState firstInstance, secondInstance, thirdInstance;
    private Board board;
    private Paint paint;
    public MainSV(Context context, AttributeSet attrs) {
        super(context, attrs);
        firstInstance = new GameState();
        secondInstance = new GameState(0, firstInstance);
        //TODO: Fix gamestate copy cntr to make it from a specific player perspective
        thirdInstance = new GameState();

        board = new Board();
        paint = new Paint();
        paint.setARGB(255,255,0,0);
    }
    /**
     * Draw board
     * */
    public void onDraw(Canvas canvas){
        // draw lines
        // draw tiles
        int tileSize = board.getTileSize();
        for(Tile tile : board.tiles){
            canvas.drawRect((int)tile.getXcord(),(int)tile.getYcord(),(int)tile.getXcord()+(int)tileSize,(int)tile.getYcord()+(int)tileSize, paint);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.runButton) {

            text.setText(""); //Clear the string

            gameString = firstInstance.toString(); //Default board

            //TODO: Call a method here to make a change in firstInstance

            gameString = gameString + "\n" + firstInstance.toString() + "\n"; //Append after a change

            //TODO: Call a method here to make a change in firstInstance

            gameString = gameString + "\n" + firstInstance.toString() + "\n"; //Append after a change

            //TODO: Call a method here to make a change in firstInstance

            gameString = gameString + "\n" + firstInstance.toString() + "\n"; //Append after a change

            gameString = gameString + "\n" + secondInstance.toString() + "\n"
                    + thirdInstance.toString() + "\n" + "\n"; //Append the other two instances (should be the same)

            text.setText(gameString); //Print the string ver
        } // run Button
    }
}
