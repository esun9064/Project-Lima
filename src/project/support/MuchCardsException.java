package project.support;

/**
 * Exception for when too many cards are on the board.
 * @author Eric
 */
public class MuchCardsException extends RuntimeException{
	public MuchCardsException()
	{
		super();
	}
	
	public MuchCardsException(String message)
	{
		super(message);
	}
}