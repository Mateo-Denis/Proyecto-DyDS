package views.storageView;

import presenters.storagePresenter.StoragePresenter;
import utils.MessageTypes;

public interface IStorageView {
	void setSavedSearches(String[] savedSearches);
	void setSavedSearch(String text);
	void showMessage(MessageTypes messageType);

	void setStoragePresenter(StoragePresenter storagePresenter);

	void resetComboBoxSelection();
}
