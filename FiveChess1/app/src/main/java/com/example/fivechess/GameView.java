package com.example.fivechess;


import android.content.Context;   
import android.content.res.Resources;   
import android.graphics.Bitmap;   
import android.graphics.Canvas;   
import android.graphics.Color;   
import android.graphics.Paint;   
import android.graphics.Paint.Style;   
import android.graphics.drawable.Drawable;   
import android.util.AttributeSet;   
import android.util.Log;   
import android.view.KeyEvent;   
import android.view.MotionEvent;   
import android.view.View;   
import android.widget.TextView;   
  
/*
 * A Gomoku Game
 *  
 *  Peng Sun
 *   
 */  
//create a custom view
public class GameView extends View{   
  
       
    protected static int GRID_SIZE = 10;   
    protected static int GRID_WIDTH = 30;
    protected static int CHESS_DIAMETER = 26;
    protected static int mStartX;
    protected static int mStartY;
  
    private Bitmap[] mChessBW;
    private static int[][] mGridArray;
       
    boolean key = false;   
  
    int wbflag = 1;  //color flag
    int mWinFlag = 0;  //indicate whether some one wins the game
    private final int BLACK=1;   
    private final int WHITE=2;   
       
    int mGameState = GAMESTATE_RUN; //4 game states,, pre run,pause end
    static final int GAMESTATE_PRE = 0;   
    static final int GAMESTATE_RUN = 1;   
    static final int GAMESTATE_PAUSE = 2;   
    static final int GAMESTATE_END = 3;   

    public TextView mStatusTextView;
       
    CharSequence mText;   
    CharSequence STRING_WIN = "White win! /n Press Key to start new game.";
    CharSequence STRING_LOSE = "Black win! /n Press Key to start new game.";
    CharSequence STRING_EQUAL = "Tie! /n Press Key to start new Game.";
  //default constructors for game view.
    public GameView(Context context, AttributeSet attrs, int defStyle) {   
        super(context, attrs, defStyle);
      }   
  
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);   
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);   
           
        init();   // initialize the view
    }

    /**
     * initialize the view. set gamestate run, black play first. winflag 0
     * creat grids. with grid_side-1 clickable rows
     */
    public void init() {   
        mGameState = 1;
        wbflag = BLACK;
        mWinFlag = 0;
        mGridArray = new int[GRID_SIZE-1][GRID_SIZE-1];   
           
        mChessBW = new Bitmap[2];   
  
        Bitmap bitmap = Bitmap.createBitmap(CHESS_DIAMETER, CHESS_DIAMETER, Bitmap.Config.ARGB_8888);   
        Canvas canvas = new Canvas(bitmap);   
        Resources r = this.getContext().getResources();   
  
        Drawable tile = r.getDrawable(R.drawable.chess1);   
        tile.setBounds(0, 0, CHESS_DIAMETER, CHESS_DIAMETER);   
        tile.draw(canvas);   
        mChessBW[0] = bitmap;   
  
        tile = r.getDrawable(R.drawable.chess2);   
        tile.setBounds(0, 0, CHESS_DIAMETER, CHESS_DIAMETER);   
        tile.draw(canvas);   
        mChessBW[1] = bitmap;   
  }   
  
       
    public void setTextView(TextView tv){   //text view method
        mStatusTextView =tv;   
        mStatusTextView.setVisibility(View.INVISIBLE);   
    }   
  
    @Override  
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {   
        mStartX = w / 2 - GRID_SIZE * GRID_WIDTH / 2;   
        mStartY = h / 2 - GRID_SIZE * GRID_WIDTH / 2;   
    }   
       
     @Override  
    public boolean onTouchEvent(MotionEvent event){   
        switch (mGameState) {   
        case GAMESTATE_PRE:   
            break;   
        case GAMESTATE_RUN: {   
                int x;   
                int y;   
                float x0 = GRID_WIDTH - (event.getX() - mStartX) % GRID_WIDTH;   
                float y0 = GRID_WIDTH - (event.getY() - mStartY) % GRID_WIDTH;   
                if (x0 < GRID_WIDTH / 2) {   
                    x = (int) ((event.getX() - mStartX) / GRID_WIDTH);   
                } else {   
                    x = (int) ((event.getX() - mStartX) / GRID_WIDTH) - 1;   
                }   
                if (y0 < GRID_WIDTH / 2) {   
                    y = (int) ((event.getY() - mStartY) / GRID_WIDTH);   
                } else {   
                    y = (int) ((event.getY() - mStartY) / GRID_WIDTH) - 1;   
                }   
                if ((x >= 0 && x < GRID_SIZE - 1)    //calculate the xy, if within range, put chess
                        && (y >= 0 && y < GRID_SIZE - 1)) {   
                    if (mGridArray[x][y] == 0) {   
                        if (wbflag == BLACK) {   
                            putChess(x, y, BLACK);     //if black playing, put black
                            //this.mGridArray[x][y] = 1;    
                            if(checkWin(BLACK)){
                                mText = STRING_LOSE;   
                                mGameState = GAMESTATE_END;   
                                showTextView(mText);   
                            }else if(checkFull()){
                                mText = STRING_EQUAL;   
                                mGameState = GAMESTATE_END;   
                                showTextView(mText);   
                            }   
                               
                            wbflag = WHITE;   
                        } else if (wbflag == WHITE) {   
                            putChess(x, y, WHITE);   
                            //this.mGridArray[x][y] = 2;   
                            if(checkWin(WHITE)){   
                                mText = STRING_WIN;   
                                mGameState = GAMESTATE_END;   
                                showTextView(mText);   
                            }else if(checkFull()){
                                mText = STRING_EQUAL;   
                                mGameState = GAMESTATE_END;   
                                showTextView(mText);   
                            }   
                            wbflag = BLACK;   
                        }   
                    }   
                }   
            }   
               
            break;   
        case GAMESTATE_PAUSE:   
            break;   
        case GAMESTATE_END:   
            break;   
        }   
           
        this.invalidate();   
        return true;   
           
    }    
       
    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent msg) {   //if key pressed after game, restart.
        Log.e("KeyEvent.KEYCODE_DPAD_CENTER", " " + keyCode);   
           
        if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER){   
            switch(mGameState){   
            case GAMESTATE_PRE:   
                break;   
            case GAMESTATE_RUN:   
                break;   
            case GAMESTATE_PAUSE:   
                break;   
            case GAMESTATE_END:   
            {
                   
                Log.e("Fire Key Pressed:::", "FIRE");   
                mGameState = GAMESTATE_RUN;   
                this.setVisibility(View.VISIBLE);   
                this.mStatusTextView.setVisibility(View.INVISIBLE);   
                this.init();   
                this.invalidate();   
                   
                   
            }   
                break;              
            }   
        }   
           
        return super.onKeyDown(keyCode, msg);   
    }   
  
    @Override  
    public void onDraw(Canvas canvas) {   
  
        canvas.drawColor(Color.GREEN);
  

        {   
            Paint paintRect = new Paint();   
            paintRect.setColor(Color.GRAY);   
            paintRect.setStrokeWidth(2);   
            paintRect.setStyle(Style.STROKE);   
  
            for (int i = 0; i < GRID_SIZE; i++) {   
                for (int j = 0; j < GRID_SIZE; j++) {   
                    int mLeft = i * GRID_WIDTH + mStartX;   
                    int mTop = j * GRID_WIDTH + mStartY;   
                    int mRright = mLeft + GRID_WIDTH;   
                    int mBottom = mTop + GRID_WIDTH;   
                    canvas.drawRect(mLeft, mTop, mRright, mBottom, paintRect);   
                }   
            }   
               

            paintRect.setStrokeWidth(4);   
            canvas.drawRect(mStartX, mStartY, mStartX + GRID_WIDTH*GRID_SIZE, mStartY + GRID_WIDTH*GRID_SIZE, paintRect);   
        }   
  

          
        for (int i = 0; i < GRID_SIZE-1; i++) {   
            for (int j = 0; j < GRID_SIZE-1; j++) {   
                if(mGridArray[i][j] == BLACK){   //check the array, and paint accoding to the color.

                     {   
                        Paint paintCircle = new Paint();   
                        paintCircle.setColor(Color.BLACK);   
                        canvas.drawCircle(mStartX + (i+1) * GRID_WIDTH, mStartY + (j+1)* GRID_WIDTH, CHESS_DIAMETER/2, paintCircle);   
                    }    
                       
                }else if(mGridArray[i][j] == WHITE){   

                    {   
                        Paint paintCircle = new Paint();   
                        paintCircle.setColor(Color.WHITE);   
                        canvas.drawCircle(mStartX + (i+1) * GRID_WIDTH, mStartY + (j+1)* GRID_WIDTH, CHESS_DIAMETER/2, paintCircle);   
                    }   
                }   
            }   
        }   
    }   
       
    public void putChess(int x, int y, int blackwhite){   //put the chess into the array
        mGridArray[x][y] = blackwhite;   
    }

    /**
     * algorithm to check if someone wins the game.
     *
     *
     */
    public boolean checkWin(int wbflag){   
        for(int i = 0; i < GRID_SIZE - 1 ; i++ )
            for(int j = 0; j < GRID_SIZE - 1; j++){

                if(((i+4) < (GRID_SIZE - 1))&&  //check limits.
                   (mGridArray[i][j] == wbflag) && (mGridArray[i+1][j] == wbflag)&& (mGridArray[i + 2][j] == wbflag) && (mGridArray[i + 3][j] == wbflag) && (mGridArray[i + 4][j] == wbflag)){   
                    Log.e("check win or loss:", wbflag + "win");   
                       
                    mWinFlag = wbflag;   
                }   

                if(((j+4) < (GRID_SIZE - 1))&&   
                           (mGridArray[i][j] == wbflag) && (mGridArray[i][j+1] == wbflag)&& (mGridArray[i ][j+ 2] == wbflag) && (mGridArray[i ][j+ 3] == wbflag) && (mGridArray[i ][j+ 4] == wbflag)){   
                            Log.e("check win or loss:", wbflag + "win");   
                               
                            mWinFlag = wbflag;   
                        }   
                   

                if(((j+4) < (GRID_SIZE - 1))&& ((i+4) < (GRID_SIZE - 1)) &&   
                           (mGridArray[i][j] == wbflag) && (mGridArray[i+1][j+1] == wbflag)&& (mGridArray[i + 2 ][j+ 2] == wbflag) && (mGridArray[i + 3][j+ 3] == wbflag) && (mGridArray[i + 4 ][j+ 4] == wbflag)){   
                            Log.e("check win or loss:", wbflag + "win");   
                               
                            mWinFlag = wbflag;   
                        }   
                   

                if(((i-4) >= 0)&& ((j+4) < (GRID_SIZE - 1)) &&   
                           (mGridArray[i][j] == wbflag) && (mGridArray[i-1][j+1] == wbflag)&& (mGridArray[i - 2 ][j+ 2] == wbflag) && (mGridArray[i - 3][j+ 3] == wbflag) && (mGridArray[i - 4 ][j+ 4] == wbflag)){   
                            Log.e("check win or loss:", wbflag + "win");   
                               
                            mWinFlag = wbflag;   
                        }   
        }   
           
        if( mWinFlag == wbflag){   
            return true;       
        }else  
            return false;   
           
           
    }   
       
    public boolean checkFull(){   
        int mNotEmpty = 0;   
        for(int i = 0; i < GRID_SIZE -1; i ++)   
            for(int j = 0; j < GRID_SIZE - 1; j ++){   
                if(mGridArray[i][j] != 0) mNotEmpty +=1;   
            }   
           
        if(mNotEmpty == (GRID_SIZE-1)*(GRID_SIZE-1)) return true;   
        else return false;   
    }   
       
    public void showTextView(CharSequence mT){   
        this.mStatusTextView.setText(mT);   
        mStatusTextView.setVisibility(View.VISIBLE);   
           
    }   
  
}  

