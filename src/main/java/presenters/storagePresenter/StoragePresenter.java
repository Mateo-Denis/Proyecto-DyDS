package presenters.storagePresenter;


import models.storageModel.IStorageModel;
import views.storageView.IStorageView;

public class StoragePresenter implements IStoragePresenter {
	private final IStorageView view;
	private final IStorageModel model;

	public StoragePresenter(IStorageView view, IStorageModel model) {
		this.view = view;
		this.model = model;
		view.setSavedSearches(model.getSavedTitles().stream().sorted().toArray(String[]::new));
	}

	@Override
	public void onSavedSearchSelected(String selectedItem) {
		view.setSavedSearch(model.getSavedExtract(selectedItem));
	}

	@Override
	public void onDeleteSavedSearch(String selectedItem) {
		model.deleteEntry(selectedItem);
		view.setSavedSearches(model.getSavedTitles().stream().sorted().toArray(String[]::new));
	}

	@Override
	public void onSaveChanges(String selectedItem, String text) {
		model.saveInfo(selectedItem, text);
	}
}