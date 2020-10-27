import javax.swing.*;
import java.awt.*;

public class Tile extends JButton {

    //stores the images for the tile
    private Icon tileImage;

    //stores the type for the tile
    // 0 = blank tile
    // 1-8 = number tile
    // -1 = mine tile
    private int tileType;

    //holds the position of this tile
    private int xPos;
    private int yPos;

    //holds indicator for if tile is revealed or not
    private boolean revealIndicator;

    //constructor
    public Tile(int x, int y){
        super();
        xPos = x;
        yPos = y;
        this.setPreferredSize(new Dimension(20, 20));
    }

    //sets the tile type
    public void setTileType(int type, Icon image){
        tileType = type;
        tileImage = image;
    }
    //returns the tile type
    public int getTileType(){
        return tileType;
    }

    //shows the tile type to the player
    public void showTile(){
        super.setIcon(tileImage);
    }
    //returns the tile x position
    public int getxPos(){
        return xPos;
    }
    //returns the tile y position
    public int getyPos(){
        return yPos;
    }
    //sets the revealed indicator
    void setRevealIndicator(boolean indicator){
        revealIndicator = indicator;
    }
    //gets the revealed indicator
    public boolean getRevealedIndicator(){
        return revealIndicator;
    }
}
