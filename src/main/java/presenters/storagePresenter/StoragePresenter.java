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
		getSavedSeriesTitles();
	}

	private void initListeners(){
		storerModel.addAccessFailureListener(() -> storageView.showMessage(ACCESS_FAILURE));

		storerModel.addPageDeleteSuccessListener(() -> {
				storageView.showMessage(PAGE_DELETE_SUCCESS);
			getSavedSeriesTitles();
			storageView.resetComboBoxSelection();
			storageView.setSavedSearch("");
		});

		storerModel.addPageDeleteFailureListener(() -> storageView.showMessage(PAGE_DELETE_FAILURE));

		storerModel.addPageSaveSuccessListener(this::getSavedSeriesTitles);

		storerModel.addTitlesAccessSuccessListener(() -> {
			storageView.setSavedSearches(storerModel.getLastSavedTitles().stream().sorted().toArray(String[]::new));
		});

		storerModel.addExtractAccessSuccessListener(() -> {
			storageView.setSavedSearch(storerModel.getLastSavedExtract());
		});
	}

	public void getSavedSeriesTitles() {
		storerModel.getSavedTitles();
	}

	@Override
	public void onSavedSearchSelected(String selectedItem) {
		storerModel.getSavedExtract(selectedItem);
	}

	@Override
	public void onDeleteSavedSearch(String selectedItem) {
		storerModel.deleteEntry(selectedItem);
	}

	@Override
	public void onSaveChanges(String selectedItem, String text) {
		storerModel.saveInfo(selectedItem, text);
	}
}