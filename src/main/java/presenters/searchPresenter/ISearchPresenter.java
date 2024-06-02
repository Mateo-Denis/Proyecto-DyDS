package presenters.searchPresenter;

import utils.SearchResult;

public interface ISearchPresenter {
	void onSearchButtonClicked(String searchText);
	void onSearchResultClicked(SearchResult sr);
	void onSaveLocallyButtonClicked(String text, String title);
}
