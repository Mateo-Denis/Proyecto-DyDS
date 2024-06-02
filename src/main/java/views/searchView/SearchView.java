package views.searchView;

import models.searchModel.ISearchModel;
import models.storageModel.IStorageModel;
import presenters.searchPresenter.ISearchPresenter;
import presenters.searchPresenter.SearchPresenter;

import javax.swing.*;
import java.awt.*;
public class SearchView extends JComponent implements ISearchView {
	private JTextField searchField;
	private JButton searchButton;
	private JTextPane searchResultPane;
	private JButton saveLocallyButton;
	private JPanel searchPanel;

	private final ISearchPresenter presenter;

	public SearchView(ISearchModel searchModel, IStorageModel storageModel) {
		presenter = new SearchPresenter(this, searchModel, storageModel);

		searchResultPane.setContentType("text/html");

		searchButton.addActionListener(e -> presenter.onSearchButtonClicked(searchField.getText()));

		saveLocallyButton.addActionListener(e -> presenter.onSaveLocallyButtonClicked(searchResultPane.getText(), searchField.getText()));
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

