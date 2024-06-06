package presenters.storagePresenter;


import models.storerModel.IStorerModel;
import views.storageView.IStorageView;

import javax.swing.*;

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
		storerModel.addAccessFailureListener(() -> storageView.showMessage("Error while accessing database"
				, "An error occurred while accessing the database"
				, JOptionPane.ERROR_MESSAGE));
		storerModel.addPageDeleteSuccessListener(() -> {
				storageView.showMessage("Page deleted successfully"
				, "The page was successfully deleted from the database"
				, JOptionPane.INFORMATION_MESSAGE);
			updateSavedSearches();
		});
		storerModel.addPageDeleteFailureListener(() -> storageView.showMessage("Error while deleting page"
				, "An error occurred while deleting the page from the database"
				, JOptionPane.ERROR_MESSAGE));
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