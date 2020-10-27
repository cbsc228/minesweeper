import javax.swing.*;

public class Minesweeper{

    //driver for game
    public static void main(String[] args) {
        GameBoard game = new GameBoard();
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.setSize(500, 500);
        game.setVisible(true);
    }
}
