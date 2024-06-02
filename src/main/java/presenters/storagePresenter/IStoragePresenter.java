package presenters.storagePresenter;

public interface IStoragePresenter {
	void onSavedSearchSelected(String selectedItem);
	void onDeleteSavedSearch(String selectedItem);
	void onSaveChanges(String selectedItem, String text);

}
