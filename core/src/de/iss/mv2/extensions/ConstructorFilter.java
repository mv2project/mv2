package de.iss.mv2.extensions;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

/**
 * A filter to skip classes without a matching constructor.
 * @author Marcel Singer
 *
 */
public class ConstructorFilter implements ClassFilter {

	/**
	 * Indicates if the constructor has to be public.
	 */
	private boolean requirePublicConstructor = false;
	
	/**
	 * Holds the argument signature of the constructor to match.
	 */
	private final Class<?>[] constructorInfo;

	/**
	 * Creates a new instance of {@link ConstructorFilter}.
	 * @param argTypes The argument signature of the constructor to match.
	 */
	public ConstructorFilter(Class<?>... argTypes) {
		this.constructorInfo = argTypes;
	}
	
	/**
	 * Creates a new instance of {@link ConstructorFilter}.
	 * @param requirePublicConstructor {@code true}, if the constructor has to be public.
	 * @param argTypes The argument signature of the constructor to match.
	 */
	public ConstructorFilter(boolean requirePublicConstructor, Class<?>... argTypes) {
		this(argTypes);
		this.requirePublicConstructor = requirePublicConstructor;
	}

	@Override
	public boolean filterClass(String className, Class<?> current) {
		try {
			Constructor<?> cons = current.getConstructor(constructorInfo);
			if(cons == null) return false;
			if(requirePublicConstructor && !Modifier.isPublic(cons.getModifiers())) return false;
			return true;
		} catch (Throwable th) {
			return false;
		}
	}
	
	/**
	 * Sets if the constructor has to be public.
	 * @param require {@code true}, if the constructor has to be public.
	 */
	public void setRequirePublicConstructor(boolean require){
		this.requirePublicConstructor = require;
	}
	
	

}