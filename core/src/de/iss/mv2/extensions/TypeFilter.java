package de.iss.mv2.extensions;

import java.lang.reflect.Modifier;

/**
 * A filter that skips all classes that can not be casted to a given type.
 * @author Marcel Singer
 *
 */
public class TypeFilter implements ClassFilter {

	/**
	 * Holds the class to compare to.
	 */
	private final Class<?> filterClass;
	/**
	 * Indicates if the class to load must not be abstract.
	 */
	private boolean requireNonAbstract = false;
	/**
	 * Indicates if the class to load has to be a class.
	 */
	private boolean requireClassType = false;
	/**
	 * Indicates if the class to load has to be an interface.
	 */
	private boolean requireInterface = false;
	

	
	/**
	 * Creates a new instance of {@link TypeFilter}.
	 * @param allowedClass The class to compare to.
	 */
	public TypeFilter(Class<?> allowedClass) {
		filterClass = allowedClass;
	}
	
	/**
	 * Creates a new instance of {@link TypeFilter}.
	 * @param requireNonAbstract Indicates if the class to load must not be abstract.
	 * @param requireClassType Indicates if the class to load has to be a class.
	 * @param requireInterface Indicates if the class to load has to be an interface.
	 * @param allowedClass The class to compare to.
	 */
	public TypeFilter(boolean requireNonAbstract, boolean requireClassType, boolean requireInterface, Class<?> allowedClass) {
		this(allowedClass);
		this.requireClassType = requireClassType;
		this.requireNonAbstract = requireNonAbstract;
		this.requireInterface = requireInterface;
	}

	@Override
	public boolean filterClass(String className, Class<?> current) {
		if (!filterClass.isAssignableFrom(current))
			return false;
		if(requireNonAbstract && Modifier.isAbstract(current.getModifiers())) return false;
		if(requireClassType && (current.isInterface() || current.isPrimitive() || current.isEnum() || current.isAnnotation() || current.isArray())) return false;
		if(requireInterface && !current.isInterface()) return false;
		return true;
	}
	
	/**
	 * Sets if the class to load must not be abstract.
	 * @param require {@code true}, if the class to load must not be abstract.
	 */
	public void setRequireNonAbstract(boolean require){
		this.requireNonAbstract = require;
	}
	
	/**
	 * Sets if the class to load has to be a class.
	 * @param require {@code true}, if the class to load has to be a class.
	 */
	public void setRequireClassType(boolean require){
		this.requireClassType = require;
	}
	
	/**
	 * Sets if the class to load has to be an interface.
	 * @param require {@code true}, if the class to load has to be an interface.
	 */
	public void setRequireInterface(boolean require){
		this.requireInterface = require;
	}

}