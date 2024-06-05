package views.searchView;

import presenters.searchPresenter.ISearchPresenter;
import utils.wiki.WikiPage;

import javax.swing.*;

public interface ISearchView {
	void showSearchResultsPopup(JPopupMenu searchOptionsMenu);
	void showSearchResult(String text);
	void showMessage(String message);
	void setWorkingStatus();
	void setWaitingStatus();
	void setSearchPresenter(ISearchPresenter searchPresenter);
	void setSearchTextField(String title);
	String getSearchTextField();

	void setCurrentPage(String title, String extract, String pageID);

	WikiPage getCurrentPage();

	String getSearchResultHTML();
}
