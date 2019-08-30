/*

Code written by: Adam Tremarche
Date Submitted: 11/16/2018
Class: CSC 413
Instructor: Anthony Souza

*/

package Core;

import java.io.File;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import static javax.imageio.ImageIO.read;
import java.util.ArrayList;

import GameObjects.*;

//TankGame is the Main class of the game. It contains the main() method, handles everything having to do with the game
//world, as well as the drawing the game to the jFrame window.
public class TankGame extends JPanel {

    public static final int SCREEN_WIDTH = 1280;                //Width of the display window
    public static final int SCREEN_HEIGHT = 960;                //Height of the display window
    public static final int WORLD_WIDTH = 2560;                 //Width of the game world
    public static final int WORLD_HEIGHT = 1920;                //Height of the game world

    private BufferedImage world;                                //holds an image of the whole game world
    private BufferedImage p1Camera;                             //image of just player one's view
    private BufferedImage p2Camera;                             //image of just player two's view
    private BufferedImage miniMap;                              //minimap of the game world
    private Graphics2D buffer;                                  //used to draw to the game world image
    private JFrame jf;                                          //jFrame for displaying the game
    private int gameClock;                                      //gameClock is used time elements in the game

    private Floor floor;                                        //object for displaying the floor of the game world
    private Tank player1;                                       //player one's tank object
    private Tank player2;                                       //player two's tank object
    private Map gameMap;                                        //object for generating the tile map

    private int[][] gameGrid;                                   //holds the game world tile map
    private ArrayList<Tiles> gameTiles = new ArrayList<>();     //holds all the tile objects in the game
    private ArrayList<Bullet> p1bullets = new ArrayList<>();    //holds all of player one's bullet objects
    private ArrayList<Bullet> p2bullets = new ArrayList<>();    //holds all of player two's bullet objects

    //Method called by main(). initGame() runs once before the game-loop and takes care of most of the object creation
    //and initialization for the game. This includes:
    //
    //          -creating and setting up the jFrame
    //          -loading the tank and floor graphics
    //          -creating the tank and floor objects
    //          -initializing the gameClock
    //          -creating the tile map and all tile objects via the Map class and buildTile() method
    //          -creating Controller objects for each player
    private void initGame() {

        //creating and setting up the jFrame
        this.jf = new JFrame("Tank Game");
        this.jf.setSize(SCREEN_WIDTH,SCREEN_HEIGHT + 30);
        this.jf.setResizable(false);
        jf.setLocationRelativeTo(null);
        this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jf.setLayout(new BorderLayout());

        //loading the the tank and floor graphics
        this.world = new BufferedImage(TankGame.WORLD_WIDTH, TankGame.WORLD_HEIGHT, BufferedImage.TYPE_INT_RGB);
        BufferedImage t1img = null, t2img = null, floorimg = null;
        try {
            System.out.println(System.getProperty("user.dir"));
            t1img = read(new File("tank1.png"));
            t2img = read(new File("tank2.png"));
            floorimg = read(new File("background.png"));
            System.out.println("Sprites Loaded!");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        //creating the tank and floor objects
        player1 = new Tank(200, 200, 0, 0, 0, 1, t1img, this);
        player2 = new Tank(2360,1720,0,0,180, 2, t2img, this);
        floor = new Floor(floorimg);

        //initializing the gameClock
        this.gameClock = 0;

        //creating the tile map and all tile objects via the Map class
        this.gameMap = gameMap.fromFile("map.txt");
        this.gameGrid = this.gameMap.getGrid();
        buildTile(this.gameGrid);

        //creating Controller objects for each player
        Controller p1Control = new Controller(player1, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);
        Controller p2Control = new Controller(player2, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_1);
        this.jf.addKeyListener(p1Control);
        this.jf.addKeyListener(p2Control);

        this.jf.add(this);
        this.jf.setVisible(true);
    }

    //Method called by initGame(). Used to create Tile objects to fill a 2D Array
    private void buildTile (int[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                Tiles newTile = Tiles.getTile(grid[i][j]);
                if (newTile != null) {
                    newTile.setPosition((j*32), (i*32));
                    newTile.setBoxCollider(0, 0, 32, 32);
                    gameTiles.add(newTile);
                }
            }
        }
    }

    //Method called by repaint() in main(). Iterates through all game Tiles and calls drawImage() from the Tiles class
    private void drawTile () {
        Tiles t;
        for (int i = 0; i < gameTiles.size(); i++) {
            t = gameTiles.get(i);
            t.drawImage(buffer);
        }
    }

    //Method called by other objects. Used to get the game clock for timing mechanics
    public int getGameClock () {
        return gameClock;
    }

    //Method called by Tank objects to add new Bullet Objects to the respective ArrayList<Bullet> for that player
    public void addBullet (Tank tank) {
        if(tank.getRank() == 1) {
            p1bullets.add( new Bullet(player1.getxPos(), player1.getyPos(), player1.getAngle()));
        } else {
            p2bullets.add( new Bullet(player2.getxPos(), player2.getyPos(), player2.getAngle()));
        }

    }

    //Method called by main(). Iterates through each bullet and calls the update() from the Bullet class
    private void bulletUpdate() {
        Bullet b;
        for (int i = 0; i < p1bullets.size(); i++) {
            b = p1bullets.get(i);
            b.update();
        }
        for (int i = 0; i < p2bullets.size(); i++) {
            b = p2bullets.get(i);
            b.update();
        }
    }

    //Method called my repaint() in main(). Iterates through each bullet and calls drawImage() from the bullet class
    private void bulletDraw() {
        Bullet b;
        for (int i = 0; i <  p1bullets.size(); i++) {
            b = p1bullets.get(i);
            b.drawImage(buffer);
        }
        for (int i = 0; i <  p2bullets.size(); i++) {
            b = p2bullets.get(i);
            b.drawImage(buffer);
        }
    }

    //Method called by main(). Method iterates through each collidable Object (Tiles, Tanks, and Bullets) and updates
    //the game based on each type of collision. The order of this method follows thus:
    //
    //          -check each player against every Tile object in the game
    private void collisionDetection () {

        //players w/ tiles:
        //For loop iterates through each tile and then for each player checks to see if collision has occurred. Then, if
        //the tile is a SolidWall or BreakWall the player's position is reverted to its previous position by one pixel.
        //If the tile is of a powerUp type(HealthUp, SpeedUp, FireUp), that tile is hidden and the player is given a
        //power up status.
        for (int i = 0; i < gameTiles.size(); i++) {
            if (player1.getBoxCollider().intersects(gameTiles.get(i).getBoxCollider())) {
                if (gameTiles.get(i).getRank() < 3) {
                    if(player1.getxPos() < gameTiles.get(i).getXpos()) {
                        player1.setxPos(player1.getxPos() - 1);
                    } else if (player1.getxPos() > gameTiles.get(i).getXpos()) {
                        player1.setxPos(player1.getxPos() + 1);
                    }
                    if (player1.getyPos() < gameTiles.get(i).getYpos()) {
                        player1.setyPos(player1.getyPos() - 1);
                    } else if (player1.getyPos() > gameTiles.get(i).getYpos()) {
                        player1.setyPos(player1.getyPos() + 1);
                    }
                } else if (gameTiles.get(i).getRank() == 3) {
                    gameTiles.get(i).sleepTile();
                    player1.incrementHp(2);
                } else if (gameTiles.get(i).getRank() == 4) {
                    gameTiles.get(i).sleepTile();
                    player1.speedUp();
                } else if (gameTiles.get(i).getRank() == 5) {
                    gameTiles.get(i).sleepTile();
                    player1.fireUp();
                }

            }
            if (player2.getBoxCollider().intersects(gameTiles.get(i).getBoxCollider())) {
                if (gameTiles.get(i).getRank() < 3) {
                    if(player2.getxPos() < gameTiles.get(i).getXpos()) {
                        player2.setxPos(player2.getxPos() - 1);
                    } else if (player2.getxPos() > gameTiles.get(i).getXpos()) {
                        player2.setxPos(player2.getxPos() + 1);
                    }
                    if (player2.getyPos() < gameTiles.get(i).getYpos()) {
                        player2.setyPos(player2.getyPos() - 1);
                    } else if (player2.getyPos() > gameTiles.get(i).getYpos()) {
                        player2.setyPos(player2.getyPos() + 1);
                    }
                } else if (gameTiles.get(i).getRank() == 3) {
                    gameTiles.get(i).sleepTile();
                    player2.incrementHp(2);
                } else if (gameTiles.get(i).getRank() == 4) {
                    gameTiles.get(i).sleepTile();
                    player2.speedUp();
                } else if (gameTiles.get(i).getRank() == 5) {
                    gameTiles.get(i).sleepTile();
                    player2.fireUp();
                }
            }

            //bullets w/ tiles and players:
            //During the same for loop of checking all the Tiles as the players, and after the player's collision is
            //checked the bullets are then checked for collision with players and tiles. In all cases, if a bullet
            //collides with an object it is removed, and if that object is a BreakWall object that tile is removed, but
            //if that object is a player Tank that player loses 1 HP.
            for(int j = 0; j < p1bullets.size(); j++) {
                if (p1bullets.get(j).getBoxCollider().intersects(gameTiles.get(i).getBoxCollider())) {
                    p1bullets.remove(j);
                    if (gameTiles.get(i).getRank() == 2) {
                        gameTiles.remove(i);
                    }
                }
                else if (p1bullets.get(j).getBoxCollider().intersects(player2.getBoxCollider())) {
                    p1bullets.remove(j);
                    player2.incrementHp(-1);
                }
            }
            for(int j = 0; j < p2bullets.size(); j++) {
                if (p2bullets.get(j).getBoxCollider().intersects(gameTiles.get(i).getBoxCollider())) {
                    p2bullets.remove(j);
                    if (gameTiles.get(i).getRank() == 2) {
                        gameTiles.remove(i);
                    }
                }
                else if (p2bullets.get(j).getBoxCollider().intersects(player1.getBoxCollider())) {
                    p2bullets.remove(j);
                    player1.incrementHp(-1);
                }
            }
        }
    }

    //*** Main method of the game. ***
    //In this method the gameInstance is initialized and then the game-loop is executed in perpetuity, until the game
    //window is closed.
    public static void main (String [] args) {
        Thread x;
        TankGame gameInstance = new TankGame();
        gameInstance.initGame();

        //This is the game-loop. Here:
        //
        //          -the gameClock is updated
        //          -both player Tanks are updated
        //          -collision is detected
        //          -the bullets are updated
        //          -the game world is repainted
        //          -the number of frames per second is controlled
        try {
            while (true) {
                gameInstance.gameClock++;
                gameInstance.player1.update();
                gameInstance.player2.update();
                gameInstance.collisionDetection();
                gameInstance.bulletUpdate();
                gameInstance.repaint();
                Thread.sleep(1000 / 144);
            }
        } catch (InterruptedException ignored) {

        }
    }


    //paintComponent() is overridden to handle all the graphical requirements of redrawing the game world.
    //This method sets the buffer Graphics2D object to the world and then calls the class specific drawImage() methods
    //of the Floor and Tank objects as well as the aggregating draw methods from the TankGame class drawTile() and
    //bulletDraw(). Then, the three "camera" BufferedImage objects are created from the world BufferedImage, and these
    //three images are drawn to the jFrame. Finally, the method draws text to the screen, showing the player's remaining
    //lives and HP.
    @Override
    public void paintComponent(Graphics g) {

        //Create graphics object for drawing to the jFrame and set buffer equal to world
        Graphics2D g2 = (Graphics2D) g;
        buffer = world.createGraphics();
        super.paintComponent(g2);

        //Draw the floor, the tiles, the bullets, and the tanks. The order here effects the layering of the objects onto
        //the screen.
        this.floor.drawImage(buffer);
        this.drawTile();
        this.bulletDraw();
        this.player1.drawImage(buffer);
        this.player2.drawImage(buffer);

        //Each "camera" gets an image from world
        this.p1Camera = world.getSubimage(this.player1.getxCamera(), this.player1.getyCamera(), 640, 960 );
        this.p2Camera = world.getSubimage(this.player2.getxCamera(), this.player2.getyCamera(), 640, 960 );
        this.miniMap = world;

        //Each "camera" is drawn to the jFrame
        g2.drawImage (p1Camera,640,0, null);
        g2.drawImage (p2Camera, 0, 0, null);
        g2.drawImage (miniMap, 512, 768, 256, 192, null);

        //Write text to the jFrame to display each player's lives and HP
        g2.setFont(new Font("Helvetica",Font.BOLD,24));
        g2.drawString(player2.toString(), 16, 896);
        g2.drawString(player1.toString(), 1084,896);
    }
}
