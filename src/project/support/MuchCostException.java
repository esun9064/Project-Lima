package project.support;

/**
 * Exception for when player does not have enough wild cat points.
 * @author Eric
 */
public class MuchCostException extends RuntimeException{
	
	public MuchCostException()
	{
		super();
	}
	
	public MuchCostException(String message)
	{
		super(message);
	}
}