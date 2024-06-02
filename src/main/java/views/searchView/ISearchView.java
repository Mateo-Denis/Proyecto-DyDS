package views.searchView;

import javax.swing.*;

public interface ISearchView {
	void showSearchResultsPopup(JPopupMenu searchOptionsMenu);
	void showSearchResult(String text);
	void showMessage(String message);
	void setWorkingStatus();
	void setWaitingStatus();
}
