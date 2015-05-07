package de.iss.mv2.gui;

/**
 * Handles an user request to either add or remove an item to or from an {@link EditableListView}.
 * @author Marcel Singer
 * @param <T> The type of the elements in the {@link EditableListView}.
 *
 */
public interface EditableListListener<T> {
	
	/**
	 * Handles the users request to add an item.
	 * @param sender The component that received the request.
	 */
	public void addItem(EditableListView<T> sender);
	
	/**
	 * Handles the users request to remove the specified item.
	 * @param sender The component that received the request.
	 * @param selectedItem The item to be removed.
	 * @param selectedIndex The index of the item to be removed.
	 */
	public void removeItem(EditableListView<T> sender, T selectedItem, int selectedIndex);
	

}
