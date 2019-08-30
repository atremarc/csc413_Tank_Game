/*

Code written by: Adam Tremarche
Date Submitted: 11/16/2018
Class: CSC 413
Instructor: Anthony Souza

*/

package GameObjects;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//The Controller class handles all keyboard presses used to control the player Tanks in the game.
//The majority of this code was adapted from Prof. Souza's Tank demo.
public class Controller implements KeyListener {

    private Tank t1;                //player controlled Tank object
    private final int up;           //used to store key value for move up
    private final int down;         //used to store key value for move down
    private final int right;        //used to store key value for move right
    private final int left;         //used to store key value for move left
    private final int shoot;        //used to store key value for shoot

    //Constructor:
    //Assigns a player to the controller as well as establishes keys that player uses to control their tank
    public Controller(Tank t1, int up, int down, int left, int right, int shoot) {
        this.t1 = t1;
        this.up = up;
        this.down = down;
        this.right = right;
        this.left = left;
        this.shoot = shoot;
    }


    //Included by Prof. Souza so I left it in
    @Override
    public void keyTyped(KeyEvent ke) {
    }

    //Method sends signal to the Tank object to trigger action for each key press
    @Override
    public void keyPressed(KeyEvent ke) {
        int keyPressed = ke.getKeyCode();
        if (keyPressed == up) {
            this.t1.toggleUpPressed();
        }
        if (keyPressed == down) {
            this.t1.toggleDownPressed();
        }
        if (keyPressed == left) {
            this.t1.toggleLeftPressed();
        }
        if (keyPressed == right) {
            this.t1.toggleRightPressed();
        }
        if (keyPressed == shoot) {
            this.t1.toggleShootPressed();
        }
    }

    //Method sends signal to the Tank object to trigger end of action for each key press
    @Override
    public void keyReleased(KeyEvent ke) {
        int keyReleased = ke.getKeyCode();
        if (keyReleased  == up) {
            this.t1.unToggleUpPressed();
        }
        if (keyReleased == down) {
            this.t1.unToggleDownPressed();
        }
        if (keyReleased  == left) {
            this.t1.unToggleLeftPressed();
        }
        if (keyReleased  == right) {
            this.t1.unToggleRightPressed();
        }
        if (keyReleased == shoot) {
            this.t1.unToggleShootPressed();
        }
    }
}
