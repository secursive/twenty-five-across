package twentyfiveacross.ejbs;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="squares")
@NamedQueries({
    @NamedQuery(name = "setSquare", query = "UPDATE SquareUnit s SET s.letter = :letter, s.username = :username WHERE s.game.gameId = :gameId AND s.posx = :x AND s.posy = :y"),
    @NamedQuery(name = "getSquare", query = "SELECT s FROM SquareUnit s WHERE s.game.gameId = :gameId")
})
public class SquareUnit implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int squareId;
	private String letter;
	private int posx;
	private int posy;
	private String username;
	private Game game;
	
	public SquareUnit() {
		squareId = (int) (Math.random() * (double)Integer.MAX_VALUE);
		letter = "";
		username = "";
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getSquareId() {
		return squareId;
	}
	public void setSquareId(int squareId) {
		this.squareId = squareId;
	}
	public String getLetter() {
		return letter;
	}
	public void setLetter(String letter) {
		this.letter = letter;
	}
	public int getPosx() {
		return posx;
	}
	public void setPosx(int posx) {
		this.posx = posx;
	}
	public int getPosy() {
		return posy;
	}
	public void setPosy(int posy) {
		this.posy = posy;
	}
	@ManyToOne
    @JoinColumn(name = "gameid")
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
