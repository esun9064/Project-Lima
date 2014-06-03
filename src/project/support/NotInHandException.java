/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package project.support;

/**
 *
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
