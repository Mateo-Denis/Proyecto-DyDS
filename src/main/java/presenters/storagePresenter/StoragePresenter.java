package presenters.storagePresenter;


import models.searchModel.ISearchModel;
import models.storageModel.IStorageModel;
import views.searchView.ISearchView;
import views.searchView.SearchView;
import views.storageView.IStorageView;
import views.storageView.StorageView;

public class StoragePresenter implements IStoragePresenter {
	private IStorageView storageView;
	private final IStorageModel storageModel;

	public StoragePresenter(IStorageModel storageModel, IStorageView storageView) {
		this.storageModel = storageModel;
		this.storageView = storageView;
	}

	public void start() {
		storageView.setStoragePresenter(this);
		storageView.setSavedSearches(storageModel.getSavedTitles().stream().sorted().toArray(String[]::new));
	}

	@Override
	public void onSavedSearchSelected(String selectedItem) {
		storageView.setSavedSearch(storageModel.getSavedExtract(selectedItem));
	}

	@Override
	public void onDeleteSavedSearch(String selectedItem) {
		storageModel.deleteEntry(selectedItem);
		storageView.setSavedSearches(storageModel.getSavedTitles().stream().sorted().toArray(String[]::new));
	}

	@Override
	public void onSaveChanges(String selectedItem, String text) {
		storageModel.saveInfo(selectedItem, text);
	}
}