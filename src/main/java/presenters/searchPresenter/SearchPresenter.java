package presenters.searchPresenter;

import models.searchModel.ISearchModel;
import models.storageModel.IStorageModel;
import utils.HTMLFormatter;
import utils.SearchResult;
import views.searchView.ISearchView;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class SearchPresenter implements ISearchPresenter{

	private final ISearchView view;
	private final ISearchModel searchModel;
	private final IStorageModel storageModel;
	private String selectedResultTitle;
	private String text;

	public SearchPresenter(ISearchView view, ISearchModel searchModel, IStorageModel storageModel) {
		this.view = view;
		this.searchModel = searchModel;
		this.storageModel = storageModel;
	}

	@Override
	public void onSearchButtonClicked(String searchText) {
		new Thread(() -> {
			view.setWorkingStatus();
			try {
				List<SearchResult> results = searchModel.searchForTerm(searchText);
				JPopupMenu searchOptionsMenu = new JPopupMenu("Search Results");
				for (SearchResult sr : results) {
					JMenuItem menuItem = new JMenuItem(sr.getTitle());
					menuItem.addActionListener(e -> onSearchResultClicked(sr));
					searchOptionsMenu.add(menuItem);
				}
				view.showSearchResultsPopup(searchOptionsMenu);
			} catch (IOException e) {
				e.printStackTrace();
				view.showMessage("Error while searching.");
			}
			view.setWaitingStatus();
		}).start();
	}

	@Override
	public void onSearchResultClicked(SearchResult sr) {
		new Thread(() -> {
			view.setWorkingStatus();
			try {
				String extract = searchModel.getExtractByPageID(sr.getPageID());
				selectedResultTitle = sr.getTitle();
				text = "<h1>" + sr.getTitle() + "</h1>" + HTMLFormatter.textToHtml(extract);
				view.showSearchResult(text);
			} catch (IOException e) {
				e.printStackTrace();
				view.showMessage("Error while fetching page.");
			}
			view.setWaitingStatus();
		}).start();
	}

	@Override
	public void onSaveLocallyButtonClicked(String text, String title) {
		if (!text.isEmpty()) {
			searchModel.saveInfo(title, text);
		}
	}
}
