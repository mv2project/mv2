package de.iss.mv2.extensions;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * A loader to load extension classes.
 * @author Marcel Singer
 * @param <T> The type of classes to load.
 *
 */
public class ExtensionLoader<T> extends InstantiableLoader {

	/**
	 * Creates a new instance of {@link ExtensionLoader}.
	 * @param extensionType The class of the extension(s) to load.
	 * @param file The file of the jar to load.
	 * @throws IOException if an I/O error occurs.
	 */
	public ExtensionLoader(Class<T> extensionType, File file) throws IOException {
		super(extensionType, file);
	}

	/**
	 * Loads and returns the extension with the given class name.
	 * @param name The class name of the extension to return.
	 * @return The loaded extension.
	 * @throws ClassNotFoundException If the class with the given name was not found.
	 * @throws NoSuchMethodException If the needed constructor was not found-
	 * @throws SecurityException If the class can not be instantiated for security reasons.
	 * @throws InstantiationException If there was an exception during the instantiation.
	 * @throws IllegalAccessException If there was an access exception.
	 * @throws InvocationTargetException If there was an invocation exception.
	 */
	@SuppressWarnings("unchecked")
	public T getInstance(String name) throws ClassNotFoundException,
			NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException,
			InvocationTargetException {
		Class<T> cl = (Class<T>) load(name);
		Constructor<T> constr = cl.getConstructor();
		if(!constr.isAccessible()) constr.setAccessible(true);
		return constr.newInstance();
	}

	/**
	 * Loads and returns the extension with the given class. If there is an exception during the loading {@code null} is returned.
	 * @param name The class name of the extension to load.
	 * @return The loaded extension.
	 */
	public T getInstanceOrNull(String name) {
		try {
			T ext = getInstance(name);
			return ext;
		} catch (Throwable th) {
			return null;
		}
	}
	
	/**
	 * Returns all extensions available inside the given jar.
	 * @return All extensions available inside the given jar.
	 */
	public List<T> getAllInstances(){
		loadAllIgnoreExceptions(false);
		T ext;
		List<T> result = new ArrayList<T>();
		for(String name : getLoadedNames()){
			ext = getInstanceOrNull(name);
			if(ext != null) result.add(ext);
		}
		return result;
	}

}
