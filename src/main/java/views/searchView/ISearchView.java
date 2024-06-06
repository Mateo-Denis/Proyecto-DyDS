package views.searchView;

import presenters.ratingPresenter.RatingPresenter;
import presenters.searchPresenter.ISearchPresenter;
import utils.wiki.RatedWikiPage;
import utils.wiki.WikiPage;
import views.storageView.IsRatedImagePanel;

import javax.swing.*;
import java.net.HttpCookie;

public interface ISearchView {
	void showSearchResultsPopup(JPopupMenu searchOptionsMenu);
	void showSearchResult(String text);
	void showMessage(String title, String message, int messageType);

	void setWorkingStatus();
	void setWaitingStatus();
	void setSearchPresenter(ISearchPresenter searchPresenter);
	void setSearchTextField(String title);
	String getSearchTextField();

	void setCurrentPage(String title, String extract, String pageID);

	WikiPage getCurrentPage();

	String getSearchResultHTML();

	JSlider getRatingSlider();

	IsRatedImagePanel getIsRatedIndicator();
}
