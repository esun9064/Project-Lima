package project.support;

/**
 * Exception for when action is performed on a card not in the player hand.
 * @author Eric
 */
public class NotInHandException extends RuntimeException {
	
	public NotInHandException()
	{
		super();
	}
	
	public NotInHandException(String message)
	{
		super(message);
	}
}
