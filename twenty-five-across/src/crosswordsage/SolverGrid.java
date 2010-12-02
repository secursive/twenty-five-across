package crosswordsage;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.*;

import twentyfiveacross.ejbs.GameManagerRemote;
import twentyfiveacross.ejbs.SquareUnit;

import java.awt.*;

public class SolverGrid extends Grid
{
	private GameManagerRemote gameManager;
	private int currentGame; 
	
    public SolverGrid()
    {
    }

    public SolverGrid(Crossword cw, GameManagerRemote gm, int cg)
    {
        super(cw);
        this.gameManager = gm;
    	this.currentGame = cg;
    }

    public void applySolveState(int gameId, int size) {//Collection<SquareUnit> ss, int size) {
    	if(size != this.squares.size()) {
    		System.err.println("Tried to apply a solve state with a different number of squares from the puzzle!");
    		return;
    	}
    	for (int i=0; i<squares.size(); i++) {
        	Square s = (Square)squares.get(i);
        	int xpos = s.getXPos();
        	int ypos = s.getYPos();
        	s.setLetter(getLetter(gameId, xpos, ypos));
    	}
    }
    
    private String getLetter(int gameId, int xpos, int ypos) {
    	return gameManager.getLetter(gameId, xpos, ypos);
	}

	public void validate()
    {
        //redraw the squares with appropriate background colours
        for(int i=0; i<squares.size(); i++)
        {
            Square s = (Square)squares.get(i);
            if(s.getWord() != null)
            {
                s.setBackground(Color.WHITE);

                if(s.getWordAcross() == selectedWord || s.getWordDown() == selectedWord)
                {
                    s.setBackground(Color.PINK);
                }
                if(s == selectedSquare)
                {
                    s.setBackground(Color.RED);
                }
            }
        }
        super.validate();
    }

    public void init()
    {
        //add a mouse listener to every square
        for(int i=0; i<squares.size(); i++)
        {
            Square s = (Square)squares.get(i);
            s.setLetter(" ");
            s.addMouseListener(new MListener());
        }
        this.addKeyListener(new KListener());
        requestFocus();
    }

    public void setSelectedWord(Word w)
    {
        selectedWord = w;
        selectedSquare = findSquare(w.getY(), w.getX());
        validate();
    }

    public void revealWord()
    {
        for(int i=0; i<squares.size(); i++)
        {
            Square s = (Square)squares.get(i);
            Word wd = s.getWordDown();
            Word wa = s.getWordAcross();

            if(wd == selectedWord)
            {
                if (wd.getWordDirection() == Word.ACROSS)
                {
                    String answerLetter = String.valueOf(wd.getWord().charAt(s.getLetterIndexAcross()));
                    s.setLetter(answerLetter);
                }
                else if (wd.getWordDirection() == Word.DOWN)
                {
                    String answerLetter = String.valueOf(wd.getWord().charAt(s.getLetterIndexDown()));
                    s.setLetter(answerLetter);
                }
            }
            else if(wa == selectedWord)
            {
                if (wa.getWordDirection() == Word.ACROSS)
                {
                    String answerLetter = String.valueOf(wa.getWord().charAt(s.getLetterIndexAcross()));
                    s.setLetter(answerLetter);
                }
                else if (wa.getWordDirection() == Word.DOWN)
                {
                    String answerLetter = String.valueOf(wa.getWord().charAt(s.getLetterIndexDown()));
                    s.setLetter(answerLetter);
                }
            }
        }
    }

    public void checkSolution()
    {
        for(int i=0; i<squares.size(); i++)
        {
            Square s = (Square)squares.get(i);
            s.setIsCorrect(true);

            if(s.getLetter() != " ")
            {
                Word w = s.getWord();
                if (w != null)
                {
                    if (w.getWordDirection() == Word.ACROSS)
                    {
                        String answerLetter = String.valueOf(w.getWord().charAt(s.getLetterIndexAcross()));
                        if (s.getLetter().equals(answerLetter.toUpperCase()))
                        {
                            s.setIsCorrect(true);
                        }
                        else
                        {
                            s.setIsCorrect(false);
                        }
                    }
                    else if (w.getWordDirection() == Word.DOWN)
                    {
                        String answerLetter = String.valueOf(w.getWord().charAt(s.getLetterIndexDown()));
                        if (s.getLetter().equals(answerLetter.toUpperCase()))
                        {
                            s.setIsCorrect(true);
                        }
                        else
                        {
                            s.setIsCorrect(false);
                        }
                    }
                }
            }
            s.validate();
            s.repaint();
        }
        validate();
    }


    public Square getNextSquare()
    {
        if(selectedWord.getWordDirection() == Word.ACROSS)
        {
            Square s = findSquare(selectedSquare.getXPos(), selectedSquare.getYPos()+1);
            if(s!=null && s.getWord()!=null) return s;
        }
        else if(selectedWord.getWordDirection() == Word.DOWN)
        {
            Square s = findSquare(selectedSquare.getXPos()+1, selectedSquare.getYPos());
            if(s!=null && s.getWord()!=null) return s;
        }
        return selectedSquare;
    }

    class MListener implements MouseListener
    {
        public void mouseClicked(MouseEvent e)
        {
            selectedSquare = (Square)e.getSource();
            selectedWord = selectedSquare.getNextWord();
            requestFocus();
            validate();
        }

        public void mouseEntered(MouseEvent e)
        {
        }

        public void mouseExited(MouseEvent e)
        {
        }

        public void mousePressed(MouseEvent e)
        {
        }

        public void mouseReleased(MouseEvent e)
        {
        }

    }

    class KListener implements KeyListener
    {
        public void keyPressed(KeyEvent e)
        {
        }

        public void keyReleased(KeyEvent e)
        {
        }

        public void keyTyped(KeyEvent e)
        {
            String s = null;
            if (e.getKeyChar() == '\b')
            {
                s = " ";
            }
            else
            {
                s = String.valueOf(e.getKeyChar());
            }
            if(gameManager.setLetter(currentGame, selectedSquare.getXPos(), selectedSquare.getYPos(), s)) {
            	selectedSquare.setLetter(s);
            	selectedSquare = getNextSquare();
            } else {
            	// noop
            }
            validate();
        }
    }
}
