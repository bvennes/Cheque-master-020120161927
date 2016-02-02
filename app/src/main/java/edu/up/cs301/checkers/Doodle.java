package edu.up.cs301.checkers;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;

/**
 *
 * The Doodle class creates a SurfaceView,
 * then draws some sample pieces onto it in various
 * positions.
 *
 * @author Sean Tollisen, Branden Vennes, Dominic Ferrari, and Brandon Sit.
 */

/**
 External Citation
    Date:       1/26/2016
    Problem:    Was not entirely sure how to draw in android java
    Resource:   In-class sample code
    (https://learning.up.edu/moodle/pluginfile.php/212505/mod_resource/content/0/Doodle.java);
    Solution:   Imitated sample code
 */

public class Doodle extends SurfaceView {

    final int SIDELENGTH = 125;
    final int TOPBUFFER = 3;
    final int LEFTBUFFER = 100;
    final int NUMPIECES = 12;

    Paint red;
    Paint black;
    Paint outlineWhite;
    Paint outlineGrey;
    Paint highlightPaint;
    Paint colorBlindRed;

    int[] xGridPositions = new int[9];
    int[] yGridPositions = new int[9];

    CheckerPiece[] playerPieces = new CheckerPiece[12];
    CheckerPiece[] opponentPieces = new CheckerPiece[12];

    boolean setup = false;
    boolean colorBlind = false;

    CheckerPiece currHighlighted;

    //The various constructors for various numbers of arguments
    public Doodle(Context context) {
        super(context);
        //Set the SurfaceView to be drawn (not default)
        setWillNotDraw(false);
    }

    public Doodle(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public Doodle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }

    public Doodle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setWillNotDraw(false);
    }

    /**
     * setupCheckerBoard initializes and adds both the opponent's and player's pieces
     * DOES NOT draw the game board (see onDraw)
     */
    /**
     External Citation
     Date:       1/31/2016
     Problem:    Forgot the method to redraw the surface view
     Resource:   Stack Overflow Post
     http://stackoverflow.com/questions/18607335/how-to-update-a-surfaceview
     Solution:   invalidate();
     */
    public void setupCheckerboard() {

        // sets opponent's bottom four pieces (row 3)
        for (int i = 0; i < 4; i++) {
            CheckerPiece c = new CheckerPiece();

            // Additional sidelength sets pieces off-centered with respect to eachother
            c.movePiece(LEFTBUFFER + (SIDELENGTH * (2*i)) + SIDELENGTH, TOPBUFFER);
            opponentPieces[i] = c;
        }
        // sets opponent's middle four pieces (row 2)
        for (int i = 0; i < 4; i++) {
            CheckerPiece c = new CheckerPiece();
            c.movePiece(LEFTBUFFER + (SIDELENGTH * (2*i)), SIDELENGTH + TOPBUFFER);
            opponentPieces[i + 4] = c;
        }
        // sets opponent's top four pieces (row 1)
        for (int i = 0; i < 4; i++) {
            CheckerPiece c = new CheckerPiece();
            // Additional sidelength to off-center
            c.movePiece(LEFTBUFFER + (SIDELENGTH * (2*i)) + SIDELENGTH, SIDELENGTH * 2 + TOPBUFFER);
            opponentPieces[i + 8] = c;
        }
        // sets player's top four pieces (row 6)
        for (int i = 0; i < 4; i++) {
            CheckerPiece c = new CheckerPiece();
            //Set the piece's chess position
            c.setChess(((2*(i))+1), 'C');
            c.movePiece(LEFTBUFFER + (SIDELENGTH * (2 * i)), SIDELENGTH * 5 + TOPBUFFER);
            playerPieces[i] = c;
        }
        // sets player's middle four pieces (row 7)
        for (int i = 0; i < 4; i++) {
            CheckerPiece c = new CheckerPiece();
            //Set the piece's chess position
            c.setChess(((2*(i))+2), 'B');
            // Additional sidelength to off-center
            c.movePiece(LEFTBUFFER + ((SIDELENGTH * (2 * i)) + SIDELENGTH), SIDELENGTH * 6 + TOPBUFFER);
            playerPieces[i + 4] = c;
        }
        //sets player's bottom four pieces (row 8)
        for (int i = 0; i < 4; i++) {
            CheckerPiece c = new CheckerPiece();
            //Set the piece's chess position
            c.setChess(((2*i)+1), 'A');
            c.movePiece(LEFTBUFFER + (SIDELENGTH * (2 * i)), SIDELENGTH * 7 + TOPBUFFER);
            playerPieces[i + 8] = c;
        }

        //outlineGrey is the piece outline paint
        outlineGrey = new Paint();
        outlineGrey.setColor(0xffafafaf);
        outlineGrey.setStyle(Paint.Style.STROKE);
        outlineGrey.setStrokeWidth(8f);

        // paint used to highlight selected piece
        highlightPaint = new Paint();
        highlightPaint.setColor(0xffffff00);

        // allows the new setup to be added to the board
        setup = true;

        invalidate();
    }

    /**
     * @return the currently highlighted checker piece
     */
    public CheckerPiece getCurrHighlighted() {
        return currHighlighted;
    }

    /**
     * If input boolean is true then colorblind mode is activated
     * colorblind mode is deactivated otherwise
     *
     * @param on a boolean value corrsponding to whether color blind mode should
     *           be activated or deactivated
     */
    public void setColorBlindMode(boolean on) {
        if (on) {
            colorBlind = true;
            invalidate();
        }
        else if (!on) {
            colorBlind = false;
            invalidate();
        }
    }

    /**
     * highlightChecker determines if a checker is intended to be highlighted
     * and does so if necessary. If necessary, currently highlighted piece returns to normal.
     *
     * @param xLoc the x-location of the user's tap
     * @param yLoc the y-location of the user's tap
     */
    public void highlightChecker(float xLoc, float yLoc) {
        // counter used to determine the newly highlighted piece
        int counter = 0;
        // only player's pieces can be chosen by the player
        for (CheckerPiece aPiece : playerPieces) {
            //performs check using checkerPiece's built-in location check method checkIfWithin
            if (aPiece.checkIfWithin((int) xLoc, (int) yLoc)) {
                //Check if the selected piece is highlighted
                if (!aPiece.getHighlighted()){
                    //Remove other current highlights
                    for (CheckerPiece worldPiece : playerPieces) {
                        worldPiece.removeHighlight();
                    }
                    //Highlight the piece
                   aPiece.highlight();
               }
                //Remove the highlight if it is highlighted
                else if (aPiece.getHighlighted()) {
                    aPiece.removeHighlight();
                }
                //Record the selected piece
                currHighlighted = aPiece;
                break;
            }
            counter++;
        }
        invalidate();
    }

    /**
     * onDraw draws the checkerboard and any necessary checkers and highlights layered
     * on top if given permission
     *
     * @param canvas
     */
    public void onDraw(Canvas canvas) {

        // initialize colors
        red = new Paint();
        black = new Paint();
        outlineWhite = new Paint();
        colorBlindRed = new Paint();
        colorBlindRed.setColor(0xffcc0066);
        outlineWhite.setColor(0xffffffff);
        outlineWhite.setStyle(Paint.Style.STROKE);
        outlineWhite.setStrokeWidth(10f);
        red.setColor(0xffa82d2d);
        black.setColor(0xff000000);

        /*
        * re-initialize the checkerboard grid
        * xGridPositions represents the top left corner x positions of the board
        * yGridPositions represents the top left corner y Positions of the board
        */
        for (int x = 0; x < 9; x++) {
            // each square is 125dp long, 100 shifts all squares to the right (used for centering)
            xGridPositions[x] = (x * SIDELENGTH) + 100;
            for (int y = 0; y < 9; y++) {
                yGridPositions[y] = (y * SIDELENGTH) + TOPBUFFER;
            }
        }

        // draw board on surface view
        // toggle used to determine color for each square
        Boolean toggle = true;
        for (int x = 0; x < 8; x++) {
            toggle = !toggle;
            for (int y = 0; y < 8; y++) {
                if (toggle) {
                    if (!colorBlind) {
                        canvas.drawRect((float) xGridPositions[x], (float) yGridPositions[y], (float) xGridPositions[x + 1], (float) yGridPositions[y + 1], red);
                    }
                    else {
                        canvas.drawRect((float) xGridPositions[x], (float) yGridPositions[y], (float) xGridPositions[x + 1], (float) yGridPositions[y + 1], colorBlindRed);
                    }
                } else {
                    canvas.drawRect((float) xGridPositions[x], (float) yGridPositions[y], (float) xGridPositions[x + 1], (float) yGridPositions[y + 1], black);
                }
                // draws outlines of squares
                canvas.drawRect((float) xGridPositions[x], (float) yGridPositions[y], (float) xGridPositions[x + 1], (float) yGridPositions[y + 1], outlineWhite);
                toggle = !toggle;
            }
        }

        if (setup) {
            //draw, outline, and highlight (if necessary) the player's and opponent's pieces pieces
            for (int i = 0; i < NUMPIECES; i++) {
                // player's pieces are either red, colorBlindRed, or highlighted
                if (!playerPieces[i].getHighlighted()) {
                    if (!colorBlind) {
                        canvas.drawOval((float) playerPieces[i].getX(), (float) playerPieces[i].getY(),
                                (float) playerPieces[i].getX() + SIDELENGTH, (float) playerPieces[i].getY() + SIDELENGTH, red);
                    }
                    else {
                        canvas.drawOval((float) playerPieces[i].getX(), (float) playerPieces[i].getY(),
                                (float) playerPieces[i].getX() + SIDELENGTH, (float) playerPieces[i].getY() + SIDELENGTH, colorBlindRed);
                    }
                }
                else {
                    canvas.drawOval((float) playerPieces[i].getX(), (float) playerPieces[i].getY(),
                            (float) playerPieces[i].getX() + SIDELENGTH, (float) playerPieces[i].getY() + SIDELENGTH, highlightPaint);
                }
                canvas.drawOval((float) playerPieces[i].getX(), (float) playerPieces[i].getY(),
                        (float) playerPieces[i].getX() + SIDELENGTH, (float) playerPieces[i].getY() + SIDELENGTH, outlineGrey);

                //opponent's pieces can't be highlighted so always black
                canvas.drawOval((float) opponentPieces[i].getX(), (float) opponentPieces[i].getY(),
                            (float) opponentPieces[i].getX() + SIDELENGTH, (float) opponentPieces[i].getY() + SIDELENGTH, black);
                canvas.drawOval((float) opponentPieces[i].getX(), (float) opponentPieces[i].getY(),
                        (float) opponentPieces[i].getX() + SIDELENGTH, (float) opponentPieces[i].getY() + SIDELENGTH, outlineGrey);
            }
        }
    }
}