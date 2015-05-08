package de.iss.mv2.server;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Represent the available server bindings.
 * 
 * @author Marcel Singer
 *
 */
public class ServerBindings {

	/**
	 * Holds the available bindings.
	 */
	private final Map<String, ServerBinding> bindings = new HashMap<String, ServerBinding>();

	/**
	 * Creates a new instance of {@link ServerBindings}.
	 */
	public ServerBindings() {

	}

	/**
	 * Adds a binding to this store.
	 * 
	 * @param binding
	 *            The binding to add.
	 * @throws IllegalArgumentException
	 *             Is thrown if the given binding is null.
	 */
	public void addBinding(ServerBinding binding)
			throws IllegalArgumentException {
		if (binding == null)
			throw new IllegalArgumentException("The binding may not be null.");
		bindings.put(binding.getAddress(), binding);
	}

	/**
	 * Returns the binding for the given address.
	 * 
	 * @param address
	 *            The address of the binding to return.
	 * @return The binding for the given address.
	 * @throws NoSuchElementException
	 *             If no binding was found.
	 */
	public ServerBinding getBinding(String address)
			throws NoSuchElementException {
		if (!bindings.containsKey(address))
			throw new NoSuchElementException(
					"There is no binding for the given address.");
		return bindings.get(address);
	}

	/**
	 * Returns the addresses of all available bindings.
	 * 
	 * @return The addresses of all available bindings.
	 */
	public Set<String> getAvailableAddresses() {
		return bindings.keySet();
	}
	
	/**
	 * Returns the addresses of all available bindings.
	 * @return The addresses of all available bindings.
	 */
	public String[] getAvailableAddressesArray(){
		String[] str = new String[bindings.keySet().size()];
		str = bindings.keySet().toArray(str);
		return str;
	}
	
	/**
	 * Checks if there is a binding for the given address.
	 * @param address The address to check.
	 * @return {@code true} if there is a binding for the given address.
	 */
	public boolean hasBidnding(String address){
		return bindings.containsKey(address);
	}

}
