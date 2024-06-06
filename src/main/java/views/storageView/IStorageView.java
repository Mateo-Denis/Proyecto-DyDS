package views.storageView;

import presenters.storagePresenter.StoragePresenter;

public interface IStorageView {
	void setSavedSearches(String[] savedSearches);
	void setSavedSearch(String text);
	void showMessage(String message, String title, int messageType);

	void setStoragePresenter(StoragePresenter storagePresenter);
}
