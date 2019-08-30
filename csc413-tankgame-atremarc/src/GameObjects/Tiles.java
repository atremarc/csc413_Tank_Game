/*

Code written by: Adam Tremarche
Date Submitted: 11/16/2018
Class: CSC 413
Instructor: Anthony Souza

*/

package GameObjects;

import java.awt.*;

//The Tiles class is an abstract class extended by all the tile objects in the game (SolidWall, BreakWall, HealthUp,
//SpeedUp, and FireUp) it allows the TankGame class to create and effect these tiles without knowing which exact tile is
//being accessed, as well as allows the Map to be comprised of any combination of tiles.
public abstract class Tiles {

    public abstract void setPosition (int x, int y);

    public abstract void drawImage(Graphics g);

    public abstract Rectangle getBoxCollider ();

    public abstract void setBoxCollider(int x, int y, int w, int h);

    public abstract int getXpos ();

    public abstract int getYpos ();

    public abstract int getRank ();

    public abstract void sleepTile();

    //This Method returns the tile of choice to the calling class by comparing a integer with the tiles given rank.
    public static Tiles getTile (int rank) {
        Tiles tile = null;
        if (rank == 1) {
            try {
                Class c = Class.forName("GameObjects.SolidWall");
                tile = (Tiles) c.getDeclaredConstructor().newInstance();
            } catch (Exception ex) {
                System.out.println("Tiles dun goofed! Rank: " + rank);
                System.out.println(ex.getMessage());
            }
        } else if (rank == 2) {
            try {
                Class c = Class.forName("GameObjects.BreakWall");
                tile = (Tiles) c.getDeclaredConstructor().newInstance();
            } catch (Exception ex) {
                System.out.println("Tiles dun goofed! Rank: " + rank);
                System.out.println(ex.getMessage());
            }
        } else if (rank == 3) {
            try {
                Class c = Class.forName("GameObjects.HealthUp");
                tile = (Tiles) c.getDeclaredConstructor().newInstance();
            } catch (Exception ex) {
                System.out.println("Tiles dun goofed! Rank: " + rank);
                System.out.println(ex.getMessage());
            }
        } else if (rank == 4) {
            try {
                Class c = Class.forName("GameObjects.SpeedUp");
                tile = (Tiles) c.getDeclaredConstructor().newInstance();
            } catch (Exception ex) {
                System.out.println("Tiles dun goofed! Rank: " + rank);
                System.out.println(ex.getMessage());
            }
        } else if (rank == 5) {
            try {
                Class c = Class.forName("GameObjects.FireUp");
                tile = (Tiles) c.getDeclaredConstructor().newInstance();
            } catch (Exception ex) {
                System.out.println("Tiles dun goofed! Rank: " + rank);
                System.out.println(ex.getMessage());
            }
        }
        return tile;
    }
}
