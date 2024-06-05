package views.searchView;

import presenters.searchPresenter.ISearchPresenter;
import utils.wiki.WikiPage;

import javax.swing.*;
import java.awt.*;
public class SearchView extends JComponent implements ISearchView {

	private JTextField searchField;
	private JButton searchButton;
	private JTextPane searchResultPane;
	private JButton saveLocallyButton;
	private JPanel searchPanel;
	private JPanel container;
	private ISearchPresenter searchPresenter;
	private WikiPage currentPage;

	public SearchView() {
		searchResultPane.setContentType("text/html");
		searchResultPane.setEditable(false);
	}

	public void setSearchPresenter(ISearchPresenter searchPresenter) {
		this.searchPresenter = searchPresenter;
	}

	@Override
	public void setSearchTextField(String title) {
		searchField.setText(title);
	}
	@Override
	public String getSearchTextField() {
		return searchField.getText();
	}

	public String getSearchResultHTML() {
		return searchResultPane.getText();
	}

	@Override
	public void setCurrentPage(String title, String extract, String pageID) {
		currentPage = new WikiPage(title, extract, pageID, 0);
	}

	@Override
	public WikiPage getCurrentPage() {
		return currentPage;
	}

	public void start(){
		currentPage = new WikiPage("", "", "", 0);
		initListeners();
	}
	private void initListeners(){
		searchButton.addActionListener(e -> searchPresenter.onSearchButtonClicked());

		saveLocallyButton.addActionListener(e -> searchPresenter.onSaveLocallyButtonClicked());
	}

	@Override
	public void showSearchResultsPopup(JPopupMenu searchOptionsMenu) {
		searchOptionsMenu.show(searchField, searchField.getX(), searchField.getY());
	}

	@Override
	public void showSearchResult(String text) {
		searchResultPane.setText(text);
		searchResultPane.setCaretPosition(0);
	}

	@Override
	public void showMessage(String message) {
		JOptionPane.showMessageDialog(searchPanel, message);
	}

	@Override
	public void setWorkingStatus() {
		for (Component c : this.searchPanel.getComponents()) c.setEnabled(false);
		searchResultPane.setEnabled(false);
	}

	@Override
	public void setWaitingStatus() {
		for (Component c : this.searchPanel.getComponents()) c.setEnabled(true);
		searchResultPane.setEnabled(true);
	}

	public JPanel getSearchPanel() {
		return searchPanel;
	}

}

