/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package project.support;

/**
 *
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