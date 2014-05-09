/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package project.support;

/**
 *
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
