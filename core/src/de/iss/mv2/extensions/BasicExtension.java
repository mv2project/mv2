package de.iss.mv2.extensions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An abstract extension that supports basic functionality to manage available extension objects.
 * @author Marcel Singer
 *
 */
public abstract class BasicExtension implements Extension {

	/**
	 * The internal extension object store.
	 */
	private final Map<Class<?>, List<Object>> extensionClassStore = new HashMap<Class<?>, List<Object>>();
	
	
	@Override
	public <T> Iterable<T> getExtensionObjects(Class<? extends T> type,
			boolean matchExact) {
		List<T> result = new ArrayList<T>();
		if(matchExact){
			if(extensionClassStore.containsKey(type)){
				copy(result, extensionClassStore.get(type));
			}
			return result;
		}
		for(Class<?> cl : extensionClassStore.keySet()){
			if(type.isAssignableFrom(cl)){
				copy(result, extensionClassStore.get(cl));
			}
		}
		return result;
	}
	
	/**
	 * Copies all entries of the given source.
	 * @param target The target to copy to.
	 * @param source The source to copy from.
	 */
	@SuppressWarnings("unchecked")
	private <T> void copy(List<T> target, Iterable<?> source){
		if(source == null) return;
		T val;
		for(Object object : source){
			try{
				val = (T) object;
			}catch(ClassCastException ex){
				continue;
			}
			if(!target.contains(val)) target.add(val);
		}
	}
	

}
