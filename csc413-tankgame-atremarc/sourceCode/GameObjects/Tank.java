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
import java.io.IOException;

import static javax.imageio.ImageIO.read;

import Core.TankGame;

//The Tank class handles everything having to do with the player controlled Tank objects in the game.
public class Tank {

    private int xPos;                               //x coordinate of the tank
    private int yPos;                               //y coordinate of the tank
    private int xVol;                               //x velocity of the tank
    private int yVol;                               //y velocity of the tank
    private int angle;                              //angle of the tank
    private int rank;                               //rank of the tank
    private int hp;                                 //health points of the tank
    private int lives;                              //remaining lives of the tank
    private int xCamera;                            //x coordinate player camera is focused onto
    private int yCamera;                            //y coordinate player camera is focused onto
    private int speedMultiplier = 2;                //used to control the movement speed of the tank
    private int rateOfFire = 100;                   //used to control the rate of fire of the tank
    private int lastFired = 0;                      //used to control the rate of fire of the tank

    private final int xOffset = -320;               //used to fix the camera if too close to the edge of the world
    private final int yOffset = -480;               //used to fix the camera if too close to the edge of the world
    private final int ROTATIONSPEED = 4;            //used to control the rotation speed of the tank

    private BufferedImage img;                      //stores image of the tank
    private Rectangle boxCollider;                  //rectangle used to detect collisions

    private boolean UpPressed;                      //used to signal that up has been pressed
    private boolean DownPressed;                    //used to signal that down has been pressed
    private boolean RightPressed;                   //used to signal that right has been pressed
    private boolean LeftPressed;                    //used to signal that left has been pressed
    private boolean ShootPressed;                   //used to signal that shoot has been pressed
    private boolean control;                        //used to remove control of the tank after player loses all lives

    private TankGame gameInstance;                  //current game instance of the tank game. used to call the game
                                                    //clock, which influences the rate of fire for the tank


    //Constructor:
    //Assigns game instance, coordinates, velocity, angle, rank, and image to the tank. Initializes the tanks HP to 5,
    //lives to 3, and control to true. Creates the boxCollider. And sets up the camera.
    public Tank(int x, int y, int vx, int vy, int angle, int rk, BufferedImage img, TankGame game) {
        this.gameInstance = game;
        this.xPos = x;
        this.yPos = y;
        this.xVol = vx;
        this.yVol = vy;
        this.img = img;
        this.angle = angle;
        this.rank = rk;
        this.hp = 5;
        this.lives = 3;
        this.control = true;
        this.boxCollider = new Rectangle();
        this.xCamera = x + xOffset;
        this.yCamera = y + yOffset;
        this.cameraBorder();
    }

    //Method returns x coordinate
    public int getxPos() {
        return xPos;
    }

    //Method returns y coordinate
    public int getyPos() {
        return yPos;
    }

    //Method returns angle
    public int getAngle() {
        return angle;
    }

    //Method returns rank
    public int getRank() {
        return rank;
    }

    //Method returns x coordinate of the camera
    public int getxCamera() {
        return xCamera;
    }

    //Method returns y coordinate of the camera
    public int getyCamera() {
        return yCamera;
    }

    //Method increments the HP of the tank by input
    public void incrementHp (int value) {
        hp += value;
    }

    //Method increases the fire rate of the tank, by halving the amount of frames required between shots
    public void fireUp () {
        rateOfFire /= 2;
    }

    //Method increments the speed of the tank by 1
    public void speedUp () {
        speedMultiplier++;
    }

    //Method sets the x coordinate of the tank to the input
    public void setxPos (int x) {
        xPos = x;
    }

    //Method sets the x coordinate of the tank to the input
    public void setyPos (int y) {
        yPos = y;
    }

    //Method sets UpPressed to true
    void toggleUpPressed() {
        this.UpPressed = true;
    }

    //Method sets DownPressed to true
    void toggleDownPressed() {
        this.DownPressed = true;
    }

    //Method sets RightPressed to true
    void toggleRightPressed() {
        this.RightPressed = true;
    }

    //Method sets LeftPressed to true
    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    //Method sets UpPressed to false
    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    //Method sets DownPressed to false
    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    //Method sets RightPressed to false
    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    //Method sets LeftPressed to false
    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    //Method sets ShootPressed to true
    void toggleShootPressed() {
        this.ShootPressed = true;
    }

    //Method sets ShootPressed to false
    void unToggleShootPressed() {
        this.ShootPressed = false;
    }

    //Adapted from Prof. Souza's Tank demo
    //The update() method sets the boxCollider to the position of the tank, checks if the player is allowed to control
    //the tank, and then activates the action method associated with the current keys being pressed. Next, the method
    //checks the hp and lives of the player and if all lives are expired the method replaces the tank with a blank image
    //and sets control to false
    public void update() {
        this.setBoxCollider(11, 11, 49, 49);
        if(control) {
            if (this.UpPressed) {
                this.moveForwards();
            }
            if (this.DownPressed) {
                this.moveBackwards();
            }
            if (this.LeftPressed) {
                this.rotateLeft();
            }
            if (this.RightPressed) {
                this.rotateRight();
            }
            if (this.ShootPressed) {
                this.shoot();
            }
        }
        if (this.hp <= 0) {
            this.lives--;
            this.hp = 5;
        }
        if (this.lives <= 0) {
            try {
                img = read(new File("blank.png"));
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            this.control = false;
        }
    }

    //Adapted from Prof. Souza's Tank demo
    //Method rotates the tank to the left
    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    //Adapted from Prof. Souza's Tank demo
    //Method rotates the tank to the right
    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }

    //Adapted from Prof. Souza's Tank demo
    //Method moves the tank backwards in the direction it is facing, as well as moves the camera with the tank
    private void moveBackwards() {
        xVol = (int) Math.round(speedMultiplier * Math.cos(Math.toRadians(angle)));
        yVol = (int) Math.round(speedMultiplier * Math.sin(Math.toRadians(angle)));
        xPos -= xVol;
        yPos -= yVol;
        this.xCamera = this.xPos + xOffset;
        this.yCamera = this.yPos + yOffset;
        checkBorder();
        cameraBorder();
    }

    //Adapted from Prof. Souza's Tank demo
    //Method moves the tank forwards in the direction it is facing, as well as moves the camera with the tank
    private void moveForwards() {
        xVol = (int) Math.round(speedMultiplier * Math.cos(Math.toRadians(angle)));
        yVol = (int) Math.round(speedMultiplier * Math.sin(Math.toRadians(angle)));
        xPos += xVol;
        yPos += yVol;
        this.xCamera = this.xPos + xOffset;
        this.yCamera = this.yPos + yOffset;
        checkBorder();
        cameraBorder();
    }

    //Method adds a bullet to the game if the tank has not fired a shot in the allotted number of frames equal to
    //rateOfFire
    private void shoot() {
        if(this.gameInstance.getGameClock() > (lastFired + rateOfFire)) {
            lastFired = this.gameInstance.getGameClock();
            this.gameInstance.addBullet(this);
        }
    }

    //Adapted from Prof. Souza's Tank demo
    //Method stops the tank from driving off the game world
    private void checkBorder() {
        if (xPos < 30) {
            xPos = 30;
        }
        if (xPos >= TankGame.WORLD_WIDTH - 88) {
            xPos = TankGame.WORLD_WIDTH - 88;
        }
        if (yPos < 40) {
            yPos = 40;
        }
        if (yPos >= TankGame.WORLD_HEIGHT - 80) {
            yPos = TankGame.WORLD_HEIGHT - 80;
        }
    }

    //Method prevents the camera from orienting drawing coordinates off of the game world
    private void cameraBorder() {
        if (xCamera < 0) {
            xCamera = 0;
        }
        if (xCamera > TankGame.WORLD_WIDTH - 640) {
            xCamera = TankGame.WORLD_WIDTH - 640;
        }
        if (yCamera < 0) {
            yCamera = 0;
        }
        if (yCamera > TankGame.WORLD_HEIGHT - 960) {
            yCamera = TankGame.WORLD_HEIGHT - 960;
        }
    }

    //Method sets the position and dimensions of the boxCollider
    private void setBoxCollider(int x, int y, int w, int h){
        this.boxCollider.x = this.xPos + x;
        this.boxCollider.y = this.yPos + y;
        this.boxCollider.width = w;
        this.boxCollider.height = h;
    }

    //Method returns the boxCollider
    public Rectangle getBoxCollider () {
        return boxCollider;
    }

    //Method used to display hp and lives of the Tank. Used in the paintComponent() overwrite located in TankGame
    @Override
    public String toString() {
        return "HP: " + this.hp + "  Lives: " + this.lives;
    }


    //Adapted from Prof. Souza's Tank demo
    //Method draws the tank to a graphics object
    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(xPos, yPos);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
    }


}
