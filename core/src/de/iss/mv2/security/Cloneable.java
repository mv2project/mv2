package de.iss.mv2.security;

/**
 * A class thats content can be cloned.
 * @author Marcel Singer
 *
 * @param <T> Specifies the type of the cloned object. 
 */
public interface Cloneable<T> {
	
	/**
	 * Creates a functional copy of this object.
	 * @return The created copy.
	 */
	public T doClone();

}
