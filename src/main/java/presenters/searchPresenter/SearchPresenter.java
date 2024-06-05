package presenters.searchPresenter;

import models.searchModel.ISearchModel;
import models.storageModel.IStorageModel;
import exceptions.WikiAPIRequestException;
import utils.HTMLFormatter;
import utils.SearchResult;
import utils.wiki.WikiPage;
import views.searchView.ISearchView;

import javax.swing.*;
import java.util.List;

public class SearchPresenter implements ISearchPresenter{

	private final ISearchView searchView;
	private final ISearchModel searchModel;
	private final IStorageModel storageModel;

	public SearchPresenter(ISearchModel searchModel, IStorageModel storageModel, ISearchView searchView) {
		this.searchModel = searchModel;
		this.storageModel = storageModel;
		this.searchView = searchView;
	}

	public void start() {
		searchView.setSearchPresenter(this);
	}

	@Override
	public void onSearchButtonClicked() {
		new Thread(() -> {
			searchView.setWorkingStatus();
			String searchText = searchView.getSearchTextField();
			try {
				List<SearchResult> results = searchModel.searchForTerm(searchText);
				JPopupMenu searchOptionsMenu = new JPopupMenu("Search Results");
				for (SearchResult sr : results) {
					JMenuItem menuItem = new JMenuItem(HTMLFormatter.searchResultToHtml(sr));
					menuItem.addActionListener(e -> onSearchResultClicked(sr));
					searchOptionsMenu.add(menuItem);
				}
				searchView.showSearchResultsPopup(searchOptionsMenu);
			} catch (WikiAPIRequestException e) {
				searchView.showMessage("Error while searching.");
			}
			searchView.setWaitingStatus();
		}).start();
	}

	@Override
	public void onSearchResultClicked(SearchResult sr) {
		new Thread(() -> {
			searchView.setWorkingStatus();
			String extract;
			String selectedResultTitle;
			String pageID;
			String textToShow;
			try {
				selectedResultTitle = sr.getTitle();
				extract = searchModel.getExtractByPageID(sr.getPageID());
				pageID = sr.getPageID();
				textToShow = "<h1>" + sr.getTitle() + "</h1>" + HTMLFormatter.textToHtml(extract);
				searchView.setCurrentPage(selectedResultTitle, extract, pageID);
				searchView.showSearchResult(textToShow);
				searchView.setWaitingStatus();
			} catch (WikiAPIRequestException e) {
				searchView.showMessage("Error while fetching page.");
			}
		}).start();
	}



	@Override
	public void onSaveLocallyButtonClicked() {
		searchView.setWorkingStatus();
		WikiPage pageToSave = searchView.getCurrentPage();
		String textToSave = searchView.getSearchResultHTML();
		if (!textToSave.isEmpty()) {
			storageModel.saveInfo(pageToSave.getTitle(), textToSave);
		}
		searchView.setWaitingStatus();
	}

}
