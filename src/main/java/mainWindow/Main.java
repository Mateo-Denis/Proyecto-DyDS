package mainWindow;

import models.searchModel.SearchModel;
import models.storageModel.StorageModel;
import presenters.searchPresenter.SearchPresenter;
import presenters.storagePresenter.StoragePresenter;
import views.searchView.SearchView;
import views.storageView.StorageView;

public class Main {

	public static void main(String[] args) {
		ContainerWindow containerWindow = new ContainerWindow();


		SearchModel searchModel = new SearchModel();
		StorageModel storageModel = new StorageModel();

		SearchView searchView = containerWindow.getSearchViewTab();
		StorageView storageView = containerWindow.getStorageViewTab();

		SearchPresenter searchPresenter = new SearchPresenter(searchModel, storageModel, searchView);
		searchPresenter.start();
		StoragePresenter storagePresenter = new StoragePresenter(storageModel, storageView);
		storagePresenter.start();

		searchView.start();
		storageView.start();



	}

}
