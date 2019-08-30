/*

Code written by: Adam Tremarche
Date Submitted: 11/16/2018
Class: CSC 413
Instructor: Anthony Souza

*/

package GameObjects;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static javax.imageio.ImageIO.read;

//The HealthUp class handles the creation of the "H" tiles. The "H" tile is a power up that when touched by the player
//will disappear granting the player two additional HP.
public class HealthUp extends Tiles {

    private int xPos;                   //x coordinate of the tile
    private int yPos;                   //y coordinate of the tile
    private final int RANK = 3;         //rank of the tile (used to ID the tile "type" outside this class
    private BufferedImage img;          //image of the tile
    private Rectangle boxCollider;      //rectangle used to detect collisions

    //Constructor:
    //sets the default positional values of the tile, creates the boxCollider for the tile, and loads the image of the
    //tile from file
    public HealthUp() {
        this.xPos = 0;
        this.yPos = 0;
        this.boxCollider = new Rectangle();

        try {
            this.img = read(new File("health.png"));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    //Method used to set the position of the tile
    public void setPosition(int x, int y) {
        this.xPos = x;
        this.yPos = y;
    }

    //Method used to set the position and dimensions of the boxCollider
    public void setBoxCollider(int x, int y, int w, int h) {
        this.boxCollider.x = this.xPos + x;
        this.boxCollider.y = this.yPos + y;
        this.boxCollider.width = w;
        this.boxCollider.height = h;
    }

    //Method used to move the boxCollider off of the accessible portion of the game world
    private void hideBoxCollider() {
        this.boxCollider.x = 0;
        this.boxCollider.y = 0;
    }

    //returns the boxCollider rectangle
    public Rectangle getBoxCollider() {
        return boxCollider;
    }

    //returns the x coordinate of the tile
    public int getXpos() {
        return this.xPos;
    }

    //returns the y coordinate of the tile
    public int getYpos() {
        return this.yPos;
    }

    //returns the rank of the tile
    public int getRank() {
        return RANK;
    }

    //Method replaces the image of the tile with a blank image as well as calls hideBoxCollider()
    public void sleepTile() {
        try {
            this.img = read(new File("blank.png"));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        hideBoxCollider();
    }

    //draws the tile to a graphics object
    public void drawImage(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, this.xPos, this.yPos, null);
    }
}
