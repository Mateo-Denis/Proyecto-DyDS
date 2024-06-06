package presenters.storagePresenter;


import models.storerModel.IStorerModel;
import views.storageView.IStorageView;

import static utils.MessageTypes.*;

public class StoragePresenter implements IStoragePresenter {
	private IStorageView storageView;
	private final IStorerModel storerModel;

	public StoragePresenter(IStorerModel storerModel, IStorageView storageView) {
		this.storerModel = storerModel;
		this.storageView = storageView;
	}

	public void start() {
		storageView.setStoragePresenter(this);
		initListeners();
		updateSavedSearches();
	}

	private void initListeners(){
		storerModel.addAccessFailureListener(() -> storageView.showMessage(ACCESS_FAILURE));
		storerModel.addPageDeleteSuccessListener(() -> {
				storageView.showMessage(PAGE_DELETE_SUCCESS);
			updateSavedSearches();
			storageView.resetComboBoxSelection();
			storageView.setSavedSearch("");
		});
		storerModel.addPageDeleteFailureListener(() -> storageView.showMessage(PAGE_DELETE_FAILURE));
		storerModel.addPageSaveSuccessListener(this::updateSavedSearches);
	}

	public void updateSavedSearches() {
		storageView.setSavedSearches(storerModel.getSavedTitles().stream().sorted().toArray(String[]::new));
	}

	@Override
	public void onSavedSearchSelected(String selectedItem) {
		storageView.setSavedSearch(storerModel.getSavedExtract(selectedItem));
	}

	@Override
	public void onDeleteSavedSearch(String selectedItem) {
		storerModel.deleteEntry(selectedItem);
		storageView.setSavedSearches(storerModel.getSavedTitles().stream().sorted().toArray(String[]::new));
	}

	@Override
	public void onSaveChanges(String selectedItem, String text) {
		storerModel.saveInfo(selectedItem, text);
	}
}