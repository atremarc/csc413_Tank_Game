/*

Code written by: Adam Tremarche
Date Submitted: 11/16/2018
Class: CSC 413
Instructor: Anthony Souza

*/

package GameObjects;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import static javax.imageio.ImageIO.read;
import java.io.IOException;

//The Bullet class handles the Bullet object which is fired from the Tanks.
public class Bullet {

    private int xPos;                       //x coordinate of the bullet
    private int yPos;                       //y coordinate of the bullet
    private int xVol;                       //x velocity of the bullet
    private int yVol;                       //y velocity of the bullet
    private int angle;                      //angle of the bullet

    private final int SPEEDMULTIPIER = 4;   //speed of the bullet

    private BufferedImage img;              //image of the bullet
    private Rectangle boxCollider;          //rectangle used to detect collisions

    //Constructor:
    //sets the coordinates and angle of the bullet, creates a boxCollider, as well as loads the image for bullets
    public Bullet (int x, int y, int ang) {
        this.xPos = x;
        this.yPos = y;
        this.angle = ang;
        boxCollider = new Rectangle();
        try {
            this.img = read( new File ("bullet.png"));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    //Method moves bullet forward at the angle it is currently traveling. Also, this method moves the boxCollider with
    //the object
    public void update () {
        xVol = (int) Math.round(SPEEDMULTIPIER * Math.cos(Math.toRadians(angle)));
        yVol = (int) Math.round(SPEEDMULTIPIER * Math.sin(Math.toRadians(angle)));
        xPos += xVol;
        yPos += yVol;
        setBoxCollider(9,9,10,10);
    }

    //Method sets the position and dimensions of the boxCollider
    private void setBoxCollider(int x, int y, int w, int h){
        this.boxCollider.x = this.xPos + x;
        this.boxCollider.y = this.yPos + y;
        this.boxCollider.width = w;
        this.boxCollider.height = h;
    }

    //Method returns the boxCollieder
    public Rectangle getBoxCollider () {
        return boxCollider;
    }

    //Method draws the bullet to a graphics object
    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(xPos + 16, yPos + 16);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
    }
}
