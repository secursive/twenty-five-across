package twentyfiveacross.ejbs;

import javax.persistence.Entity;
import crosswordsage.*;

import javax.persistence.Id;
import javax.ejb.*;
import javax.jws.WebService;
import javax.jws.WebMethod;
import java.io.File;
import java.util.Random;

//@Entity
public class Game {
	public static final int PUZZLECORRECT = -1;
	public static final int USERRESIGNED = -2;
	
    @Id int gameId;
    short status; //1 = Finished, 2 = Active
    short accessType; //1 = Public, 2 = Private
    public String playingUser;
    public String resigningUser;
    
	private int solvedState = 0;
	  // With this variable, if >= 0, it's the number of times we've checked the puzzle against
	  // the solution.  If < 0, it means that the 
	
    private Crossword cw; // This is the crossword associated with the game
    private SolveState ss; // This contains the state of every square
    
    Game() {
    	gameId = (int) (Math.random() * (double)Integer.MAX_VALUE);
    	File f = new File("helloworld2");
    	System.err.println("Loading crossword...");
    	cw = CrosswordFileHandler.readCrosswordFromFile(f);
    	System.err.println("Loaded crossword! id: " + gameId);
    	ss = new SolveState(cw.getWidth(), cw.getHeight());
    }
    
    public Crossword getCrossword() {
    	return cw;
    }
    
    public String toString() {
    	return "A " + cw.getWidth() + " x " + cw.getHeight() + " crossword.";
    }

	public SolveState getSolveState() {
		return ss;
	}
	
	public void resign(String user) {
		solvedState = USERRESIGNED;
		resigningUser = user;
	}

	public Boolean setLetter(int x, int y, String letter) {
		if(false /*!letter.matches("/[A-Za-z0-9 ]/")*/) {
			System.err.println("User entered bad character: " + (int) letter.charAt(0));
			return false;
		}
		if(solvedState < 0) {
			System.err.println("setLetter called on a finished puzzle");
			return false;
		}
		ss.setState(x, y, letter);
		// TODO Auto-generated method stub
		return true;
	}
}
