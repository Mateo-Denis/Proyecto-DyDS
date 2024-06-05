package views.storageView;

import presenters.storagePresenter.StoragePresenter;

public interface IStorageView {
	void setSavedSearches(String[] savedSearches);
	void setSavedSearch(String text);
	void showMessage(String message);

	void setStoragePresenter(StoragePresenter storagePresenter);
}
