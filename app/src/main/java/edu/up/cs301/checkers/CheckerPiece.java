package edu.up.cs301.checkers;

/**
 * Checker piece used to store the variables of each checker piece on the board
 *
 * @author Sean Tollisen, Branden Vennes, Dominic Ferrari, and Brandon Sit.
 */
public class CheckerPiece {

    private int xPos;
    private int yPos;
    private boolean dead;
    private boolean highlighted;
    private int centerX;
    private int centerY;
    private final int RADIUS = 50;
    private int xPosChess;
    private char yPosChess;

    public CheckerPiece() {
        xPos = 0;
        yPos = 0;
        dead = false;
        highlighted = false;
        centerX = 0;
        centerY = 0;
    }

    public CheckerPiece(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
        centerX = xPos + 50;
        centerY = yPos + 50;
        dead = false;
    }

    public boolean checkIfWithin(int givenXPos, int givenYPos) {
        if ( (givenXPos <= centerX + RADIUS && givenXPos >= centerX - RADIUS) && (givenYPos <= centerY + RADIUS && givenYPos >= centerY - RADIUS) ) {
            return true;
        }
        return false;
    }

    public int getX() {
        return xPos;
    }
    public int getY() {
        return yPos;
    }

    public void movePiece(int newX, int newY) {
        xPos = newX;
        yPos = newY;
        centerX = xPos + 50;
        centerY = yPos + 50;
    }

    //Return a string with the piece's position
    //in "(numberX)(letterY)" format
    public String getChessPos(){
        //Temporary string to hold the product
        return ("" + xPosChess + yPosChess);
    }

    //Set a letter representation of Y
    public void setChess(int Xpos, char Ypos){
        xPosChess = Xpos;
        yPosChess = Ypos;
    }

    public void highlight() {
        highlighted = true;
    }

    public void removeHighlight() {
        highlighted = false;
    }

    public boolean getHighlighted() {
        return highlighted;
    }

    public void killPiece() {
        dead = true;
    }
}
