package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;
import java.util.concurrent.ThreadLocalRandom;
import static java.time.temporal.ChronoUnit.SECONDS;;

public class MineSweapPart extends JFrame
{
  private static final long serialVersionUID = 1L;
  private static final int WINDOW_HEIGHT = 760;
  private static final int WINDOW_WIDTH = 760;
  private static final int MINE_GRID_ROWS = 16;
  private static final int MINE_GRID_COLS = 16;

  private static final int NO_MINES_IN_PERIMETER_GRID_VALUE = 0;
  private static final int ALL_MINES_IN_PERIMETER_GRID_VALUE = 8;
  private static final int IS_A_MINE_IN_GRID_VALUE = 9;
  
  private static int TOTAL_MINES;
  private static int guessedMinesLeft;
  private static int actualMinesLeft;

  private static final String UNEXPOSED_FLAGGED_MINE_SYMBOL = "@";
  private static final String EXPOSED_MINE_SYMBOL = "M";
  
  // visual indication of an exposed MyJButton
  private static final Color CELL_EXPOSED_BACKGROUND_COLOR = Color.lightGray;
  // colors used when displaying the getStateStr() String
  private static final Color CELL_EXPOSED_FOREGROUND_COLOR_MAP[] = {Color.lightGray, Color.blue, Color.green, Color.cyan, Color.yellow, 
                                           Color.orange, Color.pink, Color.magenta, Color.red, Color.red};
  private static final LocalTime STARTTIME = LocalTime.now();

  private boolean running = true;
  // holds the "number of mines in perimeter" value for each MyJButton 
  private int[][] mineGrid = new int[MINE_GRID_ROWS][MINE_GRID_COLS];
  
  String[] difficulties = {"Easy", "Normal", "Hard"};
  
  public MineSweapPart()
  {
    this.setTOTAL_MINES(16);
    guessedMinesLeft = this.getTOTAL_MINES();
    actualMinesLeft = this.getTOTAL_MINES();
	this.setTitle("MineSweap                                                         " + 
                  MineSweapPart.guessedMinesLeft +" Mines left");
    this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    this.setResizable(false);
    this.setLayout(new GridLayout(MINE_GRID_ROWS, MINE_GRID_COLS, 0, 0));
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);

    this.createContents();
    // place MINES number of mines in mineGrid and adjust all of the "mines in perimeter" values
    this.setMines();
    this.setVisible(true);
  }

  public MineSweapPart(int i) {
	    this.setTOTAL_MINES(i);
	    guessedMinesLeft = this.getTOTAL_MINES();
	    actualMinesLeft = this.getTOTAL_MINES();
		this.setTitle("MineSweap                                                         " + 
	                  MineSweapPart.guessedMinesLeft +" Mines left");
	    this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
	    this.setResizable(false);
	    this.setLayout(new GridLayout(MINE_GRID_ROWS, MINE_GRID_COLS, 0, 0));
	    this.setDefaultCloseOperation(EXIT_ON_CLOSE);

	    this.createContents();
	    // place MINES number of mines in mineGrid and adjust all of the "mines in perimeter" values
	    this.setMines();
	    this.setVisible(true);
}

public void createContents()
  {  
    for (int gr = 0; gr < MINE_GRID_ROWS; ++gr)
    {  
      for (int gc = 0; gc < MINE_GRID_COLS; ++gc)
      {  
        // set sGrid[gr][gc] entry to 0 - no mines in it's perimeter
        this.mineGrid[gr][gc] = 0; 
        // create a MyJButton that will be at location (br, bc) in the GridLayout
        MyJButton but = new MyJButton("", gr, gc); 
        // register the event handler with this MyJbutton
        but.addActionListener(new MyListener());
        // add the MyJButton to the GridLayout collection
        this.add(but);
      }  
    }
  }
  
  // place TOTAL_MINES number of mines in mineGrid and adjust all of the "mines in perimeter" values
  // 40 pts
  private void setMines() {
	  for (int i = 0; i < getTOTAL_MINES(); i++) {
    	//Genereates peuderandomized number for a row and column to place mine
		  int rRow = ThreadLocalRandom.current().nextInt(0, MINE_GRID_ROWS);
		  int rCol = ThreadLocalRandom.current().nextInt(0, MINE_GRID_COLS);
    	
    	//Check if a mine already exists in this coordinate and it surronding coordinate
		  if((mineGrid[rRow][rCol] != IS_A_MINE_IN_GRID_VALUE)) {
			  mineGrid[rRow][rCol] = IS_A_MINE_IN_GRID_VALUE; //marks mine on grid
			  
			  if(rCol + 1 < mineGrid[rRow].length && mineGrid[rRow][rCol+1] != IS_A_MINE_IN_GRID_VALUE) {
	                 mineGrid[rRow][rCol + 1]++;
			  }
			  if(rCol- 1 >= 0 && mineGrid[rRow][rCol-1] != IS_A_MINE_IN_GRID_VALUE) {
	                 mineGrid[rRow][rCol - 1]++;
			  }
		  }
    	  
		  //Checks if the next row mines are in bounds and increments appropriately. 
		  if(rRow + 1 < mineGrid.length) { 
			  if(mineGrid[rRow + 1][rCol] != IS_A_MINE_IN_GRID_VALUE) {
    			mineGrid[rRow + 1][rCol]++;
             }	  
			  if(rCol + 1 < mineGrid[rRow + 1].length && mineGrid[rRow+1][rCol+1] != IS_A_MINE_IN_GRID_VALUE) {
                 mineGrid[rRow + 1][rCol + 1]++;
             }
			  if(rCol- 1 >= 0 && mineGrid[rRow+1][rCol-1] != IS_A_MINE_IN_GRID_VALUE) {
                 mineGrid[rRow + 1][rCol - 1]++;
			  }
		  }

		//Checks if the previous row mines are in bounds and increments appropriately.
         if(rRow - 1 >= 0 ) {
        	 if(mineGrid[rRow - 1][rCol] != IS_A_MINE_IN_GRID_VALUE) {
     			mineGrid[rRow - 1][rCol]++;
              }	  
 			  if(rCol + 1 < mineGrid[rRow - 1].length && mineGrid[rRow-1][rCol+1] != IS_A_MINE_IN_GRID_VALUE) {
                  mineGrid[rRow - 1][rCol + 1]++;
              }
 			  if(rCol- 1 >= 0 && mineGrid[rRow-1][rCol-1] != IS_A_MINE_IN_GRID_VALUE) {
                  mineGrid[rRow - 1][rCol - 1]++;
 			  }
         }          
	  }
  }

  private String getGridValueStr(int row, int col)
  {
    // no mines in this MyJbutton's perimeter
    if ( this.mineGrid[row][col] == NO_MINES_IN_PERIMETER_GRID_VALUE )
      return "";
    // 1 to 8 mines in this MyJButton's perimeter
    else if ( this.mineGrid[row][col] > NO_MINES_IN_PERIMETER_GRID_VALUE && 
              this.mineGrid[row][col] <= ALL_MINES_IN_PERIMETER_GRID_VALUE )
      return "" + this.mineGrid[row][col];
    // this MyJButton in a mine
    else // this.mineGrid[row][col] = IS_A_MINE_IN_GRID_VALUE
      return MineSweapPart.EXPOSED_MINE_SYMBOL;
  }



  // nested private class
  private class MyListener implements ActionListener
  {
    public void actionPerformed(ActionEvent event)
    {
      if ( running )
      {
        // used to determine if ctrl or alt key was pressed at the time of mouse action
        int mod = event.getModifiers();
        MyJButton mjb = (MyJButton)event.getSource();
        // is the MyJbutton that the mouse action occurred in flagged
        boolean flagged = mjb.getText().equals(MineSweapPart.UNEXPOSED_FLAGGED_MINE_SYMBOL);
        // is the MyJbutton that the mouse action occurred in already exposed
        boolean exposed = mjb.getBackground().equals(CELL_EXPOSED_BACKGROUND_COLOR);
        // flag a cell : ctrl + left click
        if ( !flagged && !exposed && (mod & ActionEvent.CTRL_MASK) != 0 )
        {
          mjb.setText(MineSweapPart.UNEXPOSED_FLAGGED_MINE_SYMBOL);
          --MineSweapPart.guessedMinesLeft;
          // if the MyJbutton that the mouse action occurred in is a mine
          // 10 pts
          if ( mineGrid[mjb.ROW][mjb.COL] == IS_A_MINE_IN_GRID_VALUE )
          {
        	// what else do you need to adjust?
              MineSweapPart.actualMinesLeft--;
            // could the game be over?
              if(MineSweapPart.actualMinesLeft == 0) {
            	JOptionPane.showMessageDialog(null, "You sweeped all the MINES!!!\n Time taken: " + MineSweapPart.STARTTIME.until(LocalTime.now(), SECONDS) + " sec"); 
            	System.exit(0);
              }
          }
          
          setTitle("MineSweap                                                         " + 
                   MineSweapPart.guessedMinesLeft +" Mines left");
        }
        // unflag a cell : alt + left click
        else if ( flagged && !exposed && (mod & ActionEvent.ALT_MASK) != 0 )
        {
          mjb.setText("");
          ++MineSweapPart.guessedMinesLeft;
          // if the MyJbutton that the mouse action occurred in is a mine
          // 10 pts
          if ( mineGrid[mjb.ROW][mjb.COL] == IS_A_MINE_IN_GRID_VALUE )
          {
            // what else do you need to adjust?
        	  ++MineSweapPart.actualMinesLeft;
            // could the game be over?
          }
          setTitle("MineSweap                                                         " + 
                    MineSweapPart.guessedMinesLeft +" Mines left");
        }
        // expose a cell : left click
        else if ( !flagged && !exposed )
        {
          exposeCell(mjb);
        }  
      }
    }
    
    public void exposeCell(MyJButton mjb)
    {
      if ( !running )
        return;
      
      // expose this MyJButton 
      mjb.setBackground(CELL_EXPOSED_BACKGROUND_COLOR);
      mjb.setForeground(CELL_EXPOSED_FOREGROUND_COLOR_MAP[mineGrid[mjb.ROW][mjb.COL]]);
      mjb.setText(getGridValueStr(mjb.ROW, mjb.COL));
      
      // if the MyJButton that was just exposed is a mine
      // 20 pts
      if ( mineGrid[mjb.ROW][mjb.COL] == IS_A_MINE_IN_GRID_VALUE )
      {  
      	JOptionPane.showMessageDialog(null, "You stepped on a mine!\n You lose."); 
      	System.exit(0);
      }
      
      // if the MyJButton that was just exposed has no mines in its perimeter
      // 20 pts
      if ( mineGrid[mjb.ROW][mjb.COL] == NO_MINES_IN_PERIMETER_GRID_VALUE )
      {

        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                if (!(x == 0 && y == 0)
                		&& (mjb.ROW + x >= 0) 
                		&& (mjb.ROW + x < MineSweapPart.MINE_GRID_ROWS) 
                		&& (mjb.COL + y >= 0) && (mjb.COL + y < MineSweapPart.MINE_GRID_COLS)) {

                    int index = ((mjb.ROW + x) * MineSweapPart.MINE_GRID_ROWS) + (mjb.COL + y);
                    MyJButton newButton = (MyJButton) (mjb.getParent().getComponent(index));
                    
                    if (!newButton.getText().equals(MineSweapPart.UNEXPOSED_FLAGGED_MINE_SYMBOL) &&
                            !newButton.getBackground().equals(CELL_EXPOSED_BACKGROUND_COLOR)) {
                        exposeCell(newButton); //recusion used.
                    }
                }
           }   
        }
      }
    }
  }
    
      
  // nested private class


  public static void main(String[] args)
  {
	  DifficultySelector i = new DifficultySelector();
	  new MineSweapPart(i.pane());
  }

public int getTOTAL_MINES() {
	return TOTAL_MINES;
}

public void setTOTAL_MINES(int tOTAL_MINES) {
	TOTAL_MINES = tOTAL_MINES;
}

}
