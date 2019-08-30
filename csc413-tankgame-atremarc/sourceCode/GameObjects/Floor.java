/*

Code written by: Adam Tremarche
Date Submitted: 11/16/2018
Class: CSC 413
Instructor: Anthony Souza

*/

package GameObjects;

import java.awt.*;
import java.awt.image.BufferedImage;

//The Floor class simply draws a background image to the game world
public class Floor {

    private BufferedImage img;          //image for displaying

    //Constructor:
    //Assigns an image to the object
    public Floor (BufferedImage img) {
        this.img = img;
    }

    //Draws background to a graphics object
    public void drawImage(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, 0,0, null);
    }
}
