package presenters.searchPresenter;

import mainWindow.ContainerWindow;
import models.searcherModel.ISearcherModel;
import models.storerModel.IStorerModel;
import models.listeners.search.RatedSeriesDoubleClickedListener;
import utils.exceptions.WikiAPIRequestException;
import utils.HTMLFormatter;
import utils.SearchResult;
import utils.wiki.RatedWikiPage;
import utils.wiki.WikiPage;
import views.searchView.ISearchView;
import static utils.MessageTypes.*;

import javax.swing.*;
import java.sql.Timestamp;
import java.util.List;

public class SearchPresenter implements ISearchPresenter{

	private final ISearchView searchView;
	private final ISearcherModel searcherModel;
	private final IStorerModel storerModel;
	private ContainerWindow containerWindow;
	public SearchPresenter(ISearcherModel searcherModel, IStorerModel storerModel, ISearchView searchView, ContainerWindow containerWindow) {
		this.searcherModel = searcherModel;
		this.storerModel = storerModel;
		this.searchView = searchView;
		this.containerWindow = containerWindow;
	}

	public void start() {
		searchView.setSearchPresenter(this);
		initListeners();
	}

	private void initListeners(){
		storerModel.addPageSaveSuccessListener(() -> searchView.showMessage(PAGE_SAVE_SUCCESS));

		storerModel.addPageSaveFailureListener(() -> searchView.showMessage(PAGE_SAVE_FAILURE));

		storerModel.addRatingSaveSuccessListener(() -> {
			searchView.showMessage(RATING_SAVE_SUCCESS);
			searchView.getIsRatedIndicator().setIsRated(true);
		});

		storerModel.addRatingSaveFailureListener(() -> searchView.showMessage(RATING_SAVE_FAILURE));

		searcherModel.addRatedSeriesDoubleClickedListener((ratedSeries) -> {
			try{
				searchView.setWorkingStatus();
				callSearchByID(ratedSeries.getTitle(), ratedSeries.getId());
				containerWindow.getContainerTabbedPane().setSelectedIndex(0);
			}catch (WikiAPIRequestException e){
				searchView.showMessage(PAGE_SEARCH_FAILURE);
			}
			searchView.setWaitingStatus();
		});
	}


	@Override
	public void onSearchButtonClicked() {
		new Thread(() -> {
			searchView.setWorkingStatus();
			String searchText = searchView.getSearchTextField();
			try {
				searcherModel.searchForTerm(searchText);
				List<SearchResult> results = searcherModel.getSearchResultsForLastTerm();
				JPopupMenu searchOptionsMenu = new JPopupMenu("Search Results");
				for (SearchResult sr : results) {
					JMenuItem menuItem = new JMenuItem(HTMLFormatter.searchResultToHtml(sr));
					menuItem.addActionListener(e -> onSearchResultClicked(sr));
					searchOptionsMenu.add(menuItem);
				}
				searchView.showSearchResultsPopup(searchOptionsMenu);
			} catch (WikiAPIRequestException e) {
				searchView.showMessage(PAGE_SEARCH_FAILURE);
			}
			searchView.setWaitingStatus();
		}).start();
	}

	@Override
	public void onSearchResultClicked(SearchResult sr) {
		new Thread(() -> {
			try {
				searchView.setWorkingStatus();
				callSearchByID(sr.getTitle(), sr.getPageID());
				searchView.setWaitingStatus();
			} catch (WikiAPIRequestException e) {
				searchView.showMessage(PAGE_SEARCH_FAILURE);
			}
		}).start();
	}

	private void callSearchByID(String title, String pageID) throws WikiAPIRequestException{

		String extract;
		String textToShow;

		searcherModel.getExtractByPageID(pageID);
		extract = searcherModel.getExtractOfLastPageSearched();

		textToShow = "<h1>" + title + "</h1>" + HTMLFormatter.textToHtml(extract);
		searchView.setCurrentPage(title, extract, pageID);
		searchView.showSearchResult(textToShow);
		int rating = storerModel.getRating(pageID);
		if (rating != -1) {
			searchView.getIsRatedIndicator().setIsRated(true);
			searchView.getRatingSlider().setValue(rating);
		} else {
			searchView.getIsRatedIndicator().setIsRated(false);
			searchView.getRatingSlider().setValue(5);
		}
	}



	@Override
	public void onSaveLocallyButtonClicked() {
		searchView.setWorkingStatus();
		WikiPage pageToSave = searchView.getCurrentPage();
		String textToSave = searchView.getSearchResultHTML();
		if (!pageToSave.getTitle().isEmpty()) {
			storerModel.saveInfo(pageToSave.getTitle(), textToSave);
		}else {
			searchView.showMessage(EMPTY_PAGE_SAVE_ATTEMPT);
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

		if(pageToSave.getTitle().isEmpty()){
			searchView.showMessage(EMPTY_RATING_SAVE_ATTEMPT);
		}else {
			rating = searchView.getRatingSlider().getValue();

			ratedPageToSave = new RatedWikiPage(pageToSave.getTitle(),
					pageToSave.getId(),
					rating,
					new Timestamp(System.currentTimeMillis()));

			boolean isUpdate = searchView.getIsRatedIndicator().isRated();
			storerModel.saveRating(ratedPageToSave, isUpdate);
		}
		searchView.setWaitingStatus();
	}


}
