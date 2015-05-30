package de.iss.mv2.extensions;

import java.io.File;
import java.io.IOException;

/**
 * A loader that filters all not instantiable classes and those that can not be casted to a given type.
 * @author Marcel Singer
 *
 */
public class InstantiableLoader extends FilteredLoader {

	/**
	 * Creates a new instance of {@link InstantiableLoader}.
	 * @param allowedClass The class to be compared to.
	 * @param jarFile The file of the jar to load.
	 * @throws IOException if an I/O error occurs.
	 */
	public InstantiableLoader(Class<?> allowedClass, File jarFile)
			throws IOException {
		super(jarFile, new ConstructorFilter(true), new TypeFilter(true, true, false, allowedClass));
		
	}

	

}