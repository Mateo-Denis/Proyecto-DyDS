package presenters.searchPresenter;

import models.searcherModel.ISearcherModel;
import models.storerModel.IStorerModel;
import utils.exceptions.WikiAPIRequestException;
import utils.HTMLFormatter;
import utils.SearchResult;
import utils.wiki.RatedWikiPage;
import utils.wiki.WikiPage;
import views.searchView.ISearchView;

import javax.swing.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class SearchPresenter implements ISearchPresenter{

	private final ISearchView searchView;
	private final ISearcherModel searcherModel;
	private final IStorerModel storerModel;

	public SearchPresenter(ISearcherModel searcherModel, IStorerModel storerModel, ISearchView searchView) {
		this.searcherModel = searcherModel;
		this.storerModel = storerModel;
		this.searchView = searchView;
	}

	public void start() {
		searchView.setSearchPresenter(this);
		initListeners();
	}

	private void initListeners(){
		storerModel.addPageSaveSuccessListener(() -> searchView.showMessage("Page saved successfully"
				, "The searched page was successfully saved into the database"
				, JOptionPane.INFORMATION_MESSAGE));
		storerModel.addPageSaveFailureListener(() -> searchView.showMessage("Error while saving page"
				, "An error occurred while saving the page into the database"
				, JOptionPane.ERROR_MESSAGE));
		storerModel.addRatingSaveSuccessListener(() -> searchView.showMessage("Rating saved successfully"
				, "The rating was successfully saved into the database"
				, JOptionPane.INFORMATION_MESSAGE));
		storerModel.addRatingSaveFailureListener(() -> searchView.showMessage("Error while saving rating"
				, "An error occurred while saving the rating into the database"
				, JOptionPane.ERROR_MESSAGE));
	}

	@Override
	public void onSearchButtonClicked() {
		new Thread(() -> {
			searchView.setWorkingStatus();
			String searchText = searchView.getSearchTextField();
			try {
				List<SearchResult> results = searcherModel.searchForTerm(searchText);
				JPopupMenu searchOptionsMenu = new JPopupMenu("Search Results");
				for (SearchResult sr : results) {
					JMenuItem menuItem = new JMenuItem(HTMLFormatter.searchResultToHtml(sr));
					menuItem.addActionListener(e -> onSearchResultClicked(sr));
					searchOptionsMenu.add(menuItem);
				}
				searchView.showSearchResultsPopup(searchOptionsMenu);
			} catch (WikiAPIRequestException e) {
				searchView.showMessage("Error while searching."
						, "An error occurred while searching for the term in the Wikipedia API"
						, JOptionPane.ERROR_MESSAGE);
			}
			searchView.setWaitingStatus();
		}).start();
	}

	@Override
	public void onSearchResultClicked(SearchResult sr) {
		new Thread(() -> {
			try {
				searchView.setWorkingStatus();
				String extract;
				String selectedResultTitle;
				String pageID;
				String textToShow;
				int rating;

				selectedResultTitle = sr.getTitle();
				extract = searcherModel.getExtractByPageID(sr.getPageID());
				pageID = sr.getPageID();
				textToShow = "<h1>" + sr.getTitle() + "</h1>" + HTMLFormatter.textToHtml(extract);
				searchView.setCurrentPage(selectedResultTitle, extract, pageID);
				searchView.showSearchResult(textToShow);
				rating = storerModel.getRating(pageID);
				if (rating != -1) {
					searchView.getIsRatedIndicator().setIsRated(true);
					searchView.getRatingSlider().setValue(rating);
				} else {
					searchView.getIsRatedIndicator().setIsRated(false);
					searchView.getRatingSlider().setValue(5);
				}
				searchView.setWaitingStatus();
			} catch (WikiAPIRequestException e) {
				searchView.showMessage("Error while fetching page."
						, "An error occurred while fetching the page from the Wikipedia API"
						, JOptionPane.ERROR_MESSAGE);
			}
		}).start();
	}



	@Override
	public void onSaveLocallyButtonClicked() {
		searchView.setWorkingStatus();
		WikiPage pageToSave = searchView.getCurrentPage();
		String textToSave = searchView.getSearchResultHTML();
		if (!textToSave.isEmpty()) {
			storerModel.saveInfo(pageToSave.getTitle(), textToSave);
		}
		searchView.setWaitingStatus();
	}

	@Override
	public void onSaveRatingButtonClicked() {
		WikiPage pageToSave;
		int rating;
		RatedWikiPage ratedPageToSave;
		searchView.setWorkingStatus();

		pageToSave = searchView.getCurrentPage();
		rating = searchView.getRatingSlider().getValue();

		ratedPageToSave = new RatedWikiPage(pageToSave.getTitle(),
				pageToSave.getId(),
				rating,
				new Timestamp(System.currentTimeMillis()));

		boolean isUpdate = searchView.getIsRatedIndicator().isRated();
		storerModel.saveRating(ratedPageToSave, isUpdate);

		searchView.setWaitingStatus();
	}


}
