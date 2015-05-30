package de.iss.mv2.extensions;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A loader to load and filter classes from a JAR file.
 * @author Marcel Singer
 *
 */
public class FilteredLoader extends JarLoader {
	
	/**
	 * Holds the filters to apply.
	 */
	private final List<ClassFilter> filters = new ArrayList<ClassFilter>();
	/**
	 * The filter mode.
	 */
	private FilterMode mode = FilterMode.AND;

	/**
	 * Creates a new instance of {@link FilteredLoader}.
	 * @param filters The filters to apply.
	 * @param jarFile The file of the jar to load.
	 * @throws IOException if an I/O error occurs.
	 */
	public FilteredLoader(Iterable<ClassFilter> filters, File jarFile)
			throws IOException {
		super(jarFile);
		for (ClassFilter f : filters) {
			this.filters.add(f);
		}
	}

	/**
	 * Creates a new instance of {@link FilteredLoader}.
	 * @param filter The filter to apply.
	 * @param jarFile The file of the jar to load.
	 * @throws IOException if an I/O error occurs.
	 */
	public FilteredLoader(ClassFilter filter, File jarFile) throws IOException {
		super(jarFile);
		this.filters.add(filter);
	}
	
	/**
	 * Creates a new instance of {@link FilteredLoader}.
	 * @param jarFile The file of the jar to load.
	 * @param filters The filters to apply.
	 * @throws IOException if an I/O error occurs.
	 */
	public FilteredLoader(File jarFile, ClassFilter... filters) throws IOException{
		this(Arrays.asList(filters), jarFile);
	}

	/**
	 * Returns the filter mode.
	 * @return The filter mode.
	 */
	public FilterMode getMode() {
		return mode;
	}

	/**
	 * Sets the filter mode.
	 * @param mode The filter mode to set.
	 */
	public void setMode(FilterMode mode) {
		this.mode = mode;
	}

	@Override
	protected boolean check(String name, Class<?> toCheck) {
		if (mode == FilterMode.AND) {
			for (ClassFilter f : filters) {
				if(!f.filterClass(name, toCheck)) return false;
			}
			return true;
		} else {
			for(ClassFilter f : filters){
				if(f.filterClass(name, toCheck)) return true;
			}
			return false;
		}

	}

	

}
