import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URI;
import java.util.Objects;

public class GameBoard extends JFrame {

    //holds the tiles that make up the game
    private Tile[][] gameTiles;

    //holds the number of tiles revealed
    private int revealedTiles;

    //holds the size of the board
    private int boardSize = 8;

    //holds the number of mines in the game
    private int mineNum = 15;

    private final int mineID = -1;

    //holds indicator for if game is active or not
    private boolean gameActiveIndicator;

    //timer and the timer delay for display
    private Timer gameTimer;
    private int currentTime = 0;
    private final int timerDelay = 1000;

    //constructor
    public GameBoard(){

        //set title of frame
        this.setTitle("Minesweeper");

        //panels to hold game elements
        JPanel displayPanel = new JPanel();
        JPanel gamePanel = new JPanel();

        //initialize the GridBagLayout
        GridBagLayout layout = new GridBagLayout();
        displayPanel.setLayout(layout);
        gamePanel.setLayout(layout);
        this.setLayout(layout);
        GridBagConstraints frameConstraints = new GridBagConstraints();
        GridBagConstraints settingConstraints = new GridBagConstraints();
        GridBagConstraints boardConstraints = new GridBagConstraints();
        GridBagConstraints displayConstraints = new GridBagConstraints();

        //start of the menu bar
        JMenuBar menuBar = new JMenuBar();
        //create the game and help menu
        JMenu gameMenu = new JMenu("Game");
        gameMenu.setMnemonic('G');
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');

        //create the start new game menu item
        JMenuItem newGameItem = new JMenuItem("New Game");

        //the settings menu pop up with buttons and drop downs to manage game settings
        JFrame settingsFrame = new JFrame("Settings");
        settingsFrame.setSize(300,300);
        settingsFrame.setLayout(layout);
        //settings buttons
        JButton beginnerButton = new JButton("Beginner");
        JButton intermediateButton = new JButton("Intermediate");
        JButton expertButton = new JButton("Expert");
        //input interfaces and labels
        JLabel sizeLabel = new JLabel("Size:");
        JTextArea sizeInput = new JTextArea(String.valueOf(boardSize));
        JLabel mineNumLabel = new JLabel("Mines:");
        JTextArea mineNumInput = new JTextArea(String.valueOf(mineNum));
        //apply settings button
        JButton applySettings = new JButton("Apply");
        JTextArea messageBox = new JTextArea();


        //add the buttons to the settings frame
        JButton[] settingButtons = {beginnerButton, intermediateButton, expertButton};
        for(int i = 0; i < settingButtons.length; i++){
            settingConstraints.gridx = i;
            settingConstraints.gridy = 0;
            settingsFrame.add(settingButtons[i], settingConstraints);
        }

        //create timer, mine count, and separator for the user display
        JLabel timerDisplay = new JLabel("0");
        timerDisplay.setPreferredSize(new Dimension(30, 20));
        JLabel timerLabel = new JLabel("Timer");

        JLabel mineCountDisplay = new JLabel(String.valueOf(mineNum));
        mineCountDisplay.setPreferredSize(new Dimension(30, 20));
        JLabel mineCountLabel = new JLabel("Mines");

        JLabel separator = new JLabel();

        //add interface items to the setting menu with their placement
        settingConstraints.gridx = 0;
        settingConstraints.gridy = 1;
        settingsFrame.add(sizeLabel, settingConstraints);

        settingConstraints.gridx = 1;
        settingConstraints.gridy = 1;
        settingConstraints.gridwidth = 1;
        settingsFrame.add(sizeInput, settingConstraints);

        settingConstraints.gridx = 0;
        settingConstraints.gridy = 2;
        settingsFrame.add(mineNumLabel, settingConstraints);

        settingConstraints.gridx = 1;
        settingConstraints.gridy = 2;
        settingConstraints.gridwidth = 1;
        settingsFrame.add(mineNumInput, settingConstraints);

        settingConstraints.gridx = 0;
        settingConstraints.gridy = 3;
        settingsFrame.add(applySettings, settingConstraints);

        settingConstraints.gridx = 0;
        settingConstraints.gridy = 4;
        settingConstraints.gridwidth = 3;
        settingConstraints.fill = GridBagConstraints.HORIZONTAL;
        settingsFrame.add(messageBox, settingConstraints);

        //add display items to the display panel
        displayConstraints.gridx = 0;
        displayConstraints.gridy = 0;
        displayPanel.add(timerLabel, displayConstraints);

        displayConstraints.gridx = 0;
        displayConstraints.gridy = 1;
        displayPanel.add(timerDisplay, displayConstraints);

        displayConstraints.gridx = 1;
        displayConstraints.gridy = 0;
        displayConstraints.fill = GridBagConstraints.VERTICAL;
        separator.setPreferredSize(new Dimension(75, 40));
        displayPanel.add(separator, displayConstraints);

        displayConstraints.gridx = 2;
        displayConstraints.gridy = 0;
        displayPanel.add(mineCountLabel, displayConstraints);

        displayConstraints.gridx = 2;
        displayConstraints.gridy = 1;
        displayPanel.add(mineCountDisplay, displayConstraints);

        //add action listeners to the difficulty buttons and apply setting button
        newGameItem.addActionListener(e -> {
            resetBoard(gamePanel);
            boardSize = Integer.parseInt(sizeInput.getText());
            mineNum = Integer.parseInt(mineNumInput.getText());
            drawBoard(boardConstraints, gamePanel);
            setVisible(false);
            setVisible(true);
            gameTimer.stop();
            currentTime = 0;
            timerDisplay.setText("0");
        });
        beginnerButton.addActionListener(e -> {
            //set the parameters in text area to size = 4 and mine num = 4
            sizeInput.setText("4");
            mineNumInput.setText("4");
        });
        intermediateButton.addActionListener(e -> {
            //set the parameters in text area to size = 8 and mine num = 15
            sizeInput.setText("8");
            mineNumInput.setText("15");
        });
        expertButton.addActionListener(e -> {
            //set the parameters in text area to size = 12 and mine num = 40
            sizeInput.setText("12");
            mineNumInput.setText("40");
        });
        applySettings.addActionListener(e -> {
            int mineInput = Integer.parseInt(mineNumInput.getText());
            int lowerLimit = 2;
            int upperLimit = (int) (boardSize * boardSize) / 2;
            if (mineInput >= lowerLimit || mineInput <= upperLimit){
                //the user has input a valid pair of board size and mine number
                resetBoard(gamePanel);
                boardSize = Integer.parseInt(sizeInput.getText());
                mineNum = Integer.parseInt(mineNumInput.getText());
                drawBoard(boardConstraints, gamePanel);
                messageBox.setText("");
                mineCountDisplay.setText(mineNumInput.getText());
                gameTimer.stop();
                currentTime = 0;
                timerDisplay.setText("0");
                gameActiveIndicator = false;
                setVisible(false);
                setVisible(true);
            }
            else{
                //the user has input an invalid pair of board size and mine number
                messageBox.setText("Please enter a number of mines at least" + "\n" + "2 and no more than half the size squared");
            }

        });

        //makes it so board size input only allows numbers and backspace
        sizeInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                sizeInput.setEditable((e.getKeyChar() >= '0' && e.getKeyChar() <= '9') || e.getKeyCode() == KeyEvent.VK_BACK_SPACE);
            }
        });

        //makes it so that mine number input only allows numbers and backspace
        mineNumInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                mineNumInput.setEditable((e.getKeyChar() >= '0' && e.getKeyChar() <= '9') || e.getKeyCode() == KeyEvent.VK_BACK_SPACE);
            }
        });

        //create settings menu item
        JMenuItem settingsItem = new JMenuItem("Settings");
        settingsItem.addActionListener(e -> settingsFrame.setVisible(true));

        //create the exit menu item
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit( 0 ));

        //create the how to play menu item
        JMenuItem howToPlayItem= new JMenuItem("How to Play");
        howToPlayItem.addActionListener(e -> {
            try {
                Desktop desktop = Desktop.getDesktop();
                URI url = new URI("https://en.wikipedia.org/wiki/Microsoft_Minesweeper");
                desktop.browse(url);
            } catch (Exception exception){
                exception.printStackTrace();
            }
        });

        ActionListener timerAction = e -> {
            int newTime = currentTime++;
            timerDisplay.setText(String.valueOf(newTime));
        };

        gameTimer = new Timer(timerDelay, timerAction);

        //add menu items to menus
        gameMenu.add(newGameItem);
        gameMenu.add(settingsItem);
        gameMenu.add(exitItem);
        helpMenu.add(howToPlayItem);

        //add menus to menu bar
        menuBar.add(gameMenu);
        menuBar.add(helpMenu);

        //add menu bar to game frame
        setJMenuBar(menuBar);

        //add panels to the frame
        frameConstraints.gridx = 0;
        frameConstraints.gridy = 0;
        this.add(displayPanel, frameConstraints);

        frameConstraints.gridx = 0;
        frameConstraints.gridy = 1;
        this.add(gamePanel, frameConstraints);

        //draw the game board
        drawBoard(boardConstraints, gamePanel);
    }

    //starts a new game of minesweeper
    private void startNewGame(Tile selectedTile){
        //initialize tile counters
        revealedTiles = 0;
        //set game to active
        gameActiveIndicator = true;
        //position of the initially selected tile
        int initX = selectedTile.getxPos();
        int initY = selectedTile.getyPos();
        //place the mines on the board
        placeMineTiles(initX, initY);
        //place the number tiles on the board
        placeNumTiles();
        //start the game timer
        gameTimer.start();
    }

    //recursive function to reveal tiles
    private void revealTiles(Tile selectedTile){
        int mine = -1;
        int blank = 0;

        //do nothing if this tile has already been revealed
        if (selectedTile.getRevealedIndicator()){
            return;
        }

        if (selectedTile.getTileType() == mine){//tile is a mine
            //user has lost the game
            //reveal all tiles, set all tiles to non-pressable, display losing message, give start new game option, set game to inactive
            gameActiveIndicator = false;
            gameTimer.stop();
            currentTime = 0;
            for (int x = 0; x < boardSize; x++){
                for (int y = 0; y < boardSize; y++){
                    gameTiles[x][y].showTile();
                    gameTiles[x][y].setRevealIndicator(true);
                    gameTiles[x][y].setEnabled(false);
                }
            }
            showMessage(false);
        }
        else if (selectedTile.getTileType() > blank){//tile is a number
            //reveal the number tile
            revealedTiles++;
            selectedTile.setRevealIndicator(true);
            selectedTile.showTile();
            selectedTile.setEnabled(false);
        }
        else if (selectedTile.getTileType() == blank){//tile is blank
            //reveal this tile
            //make a recursive call to reveal the surrounding black tiles and their neighboring number tiles
            revealedTiles++;
            selectedTile.setRevealIndicator(true);
            selectedTile.showTile();
            selectedTile.setEnabled(false);
            Tile[] neighborTiles = getNeighbors(selectedTile).clone();
            for (Tile neighborTile : neighborTiles) {
                if (neighborTile != null) {
                    revealTiles(neighborTile);
                }
            }

        }
    }


    //gets the neighbors of the given tile and returns them in an array
    private Tile[] getNeighbors(Tile selectedTile){
        Tile[] neighborTiles = new Tile[8];
        int originX = selectedTile.getxPos();
        int originY = selectedTile.getyPos();
        int tileCount = 0;
        for (int x = originX - 1; x <= originX + 1; x++){
            for (int y = originY - 1; y <= originY + 1; y++){
                //make sure not to get the originally selected tile
                if (!(x == originX && y == originY)){
                    //make sure to not go out of bounds
                    if (!(x < 0 || x >= boardSize || y < 0 || y >= boardSize)){
                        neighborTiles[tileCount] = gameTiles[x][y];
                        tileCount++;
                    }
                }
            }
        }
        return neighborTiles;
    }


    private void drawBoard(GridBagConstraints boardConstraints, JPanel gamePanel){
        //array to clone to global tile holder
        Tile[][] tiles = new Tile[boardSize][boardSize];
        //action handler for clicking a tile
        tileAction tileAction = new tileAction();
        //adds tiles to the board
        for (int x = 0; x < boardSize; x++){
            for (int y = 0; y < boardSize; y++){
                boardConstraints.gridx = x;
                boardConstraints.gridy = y;
                Tile newTile = new Tile(x, y);
                newTile.setRevealIndicator(false);
                tiles[x][y] = newTile;
                newTile.addActionListener(tileAction);
                gamePanel.add(newTile, boardConstraints);
            }
        }
        //clones tile holder to global tile array
        gameTiles = tiles.clone();
    }

    //removes all in play tiles from the board
    private void resetBoard(JPanel gamePanel){
        //sets game to inactive
        gameActiveIndicator = false;
        for (int x = 0; x < boardSize; x++){
            for (int y = 0; y < boardSize; y++){
                gamePanel.remove(gameTiles[x][y]);
            }
        }
    }

    private String getImgPath(int neighboringMineCount){
        String imgPath = null;
        switch (neighboringMineCount) {
            case 0 -> imgPath = "res/blank.png";
            case 1 -> imgPath = "res/one.png";
            case 2 -> imgPath = "res/two.png";
            case 3 -> imgPath = "res/three.png";
            case 4 -> imgPath = "res/four.png";
            case 5 -> imgPath = "res/five.png";
            case 6 -> imgPath = "res/six.png";
            case 7 -> imgPath = "res/seven.png";
            case 8 -> imgPath = "res/eight.png";
            default -> System.out.println("We got to the part of the switch statement that we should never get too. Oops.");
        }
        return imgPath;
    }

    private void placeMineTiles(int initX, int initY){
        ClassLoader loader = getClass().getClassLoader();
        Icon mineImg = new ImageIcon(Objects.requireNonNull(loader.getResource("res/mine.png")));
        int randX;
        int randY;
        int min = 0;
        int minePlacementCount = 0;
        do {
            randX = (int)(Math.random() * (boardSize - min) + min);
            randY = (int)(Math.random() * (boardSize - min) + min);
            if ((randX != initX) && (randY != initY)){
                if (gameTiles[randX][randY].getTileType() == 0){
                    gameTiles[randX][randY].setTileType(mineID, mineImg);
                    minePlacementCount++;
                }

            }
        }while(minePlacementCount < mineNum);
    }

    private void placeNumTiles(){
        ClassLoader loader = getClass().getClassLoader();
        for (int x = 0; x < boardSize; x++){
            for (int y = 0; y < boardSize; y++){
                if (gameTiles[x][y].getTileType() != -1){
                    //use the getNeighbors function to get the list of neighbors,
                    //then count the ones are are mines. use that to assign tile type
                    Tile[] neighbors;
                    int neighboringMineCount = 0;
                    //check the neighbors for mines and increment mine count when one is found
                    neighbors = getNeighbors(gameTiles[x][y]).clone();
                    for (Tile neighbor : neighbors) {
                        if (neighbor != null) {
                            if (neighbor.getTileType() == mineID) {
                                neighboringMineCount++;
                            }
                        }
                    }

                    //assign the appropriate image and tile type based on the number of mines around this tile
                    String imgPath = getImgPath(neighboringMineCount);
                    Icon tileImg = new ImageIcon(Objects.requireNonNull(loader.getResource(imgPath)));
                    gameTiles[x][y].setTileType(neighboringMineCount, tileImg);
                }

            }
        }
    }

    //handles when a tile is clicked
    private class tileAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent event){
            Tile currentTile = (Tile) event.getSource();
            if (!gameActiveIndicator){
                startNewGame(currentTile);
            }
            revealTiles(currentTile);
            //checks if the user has won the game
            if (revealedTiles == (boardSize * boardSize) - mineNum){
                gameTimer.stop();
                currentTime = 0;
                showMessage(true);
            }
        }
    }

    private void showMessage(boolean winCheck){
        String message;
        if(winCheck){
            //display the user has won the game
            message = "You won!";
        }
        else{
            //display the user has lost the game
            message = "You lost";
        }
        JFrame winScreen = new JFrame();
        JTextArea winText = new JTextArea(message);
        winText.setEditable(false);
        winScreen.add(winText);
        winScreen.setSize(150, 75);
        winScreen.setVisible(true);
    }

}

