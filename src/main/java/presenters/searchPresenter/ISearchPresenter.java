package presenters.searchPresenter;

import utils.SearchResult;

public interface ISearchPresenter {
	void onSearchButtonClicked();
	void onSearchResultClicked(SearchResult sr);
	void onSaveLocallyButtonClicked();

	void onSaveRatingButtonClicked();
}
