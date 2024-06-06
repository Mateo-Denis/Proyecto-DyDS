package presenters.ratingPresenter;

import utils.exceptions.WikiAPIRequestException;
import mainWindow.ContainerWindow;
import models.searcherModel.ISearcherModel;
import models.storerModel.IStorerModel;
import utils.HTMLFormatter;
import utils.wiki.RatedWikiPage;
import views.ratingView.IRatingView;
import views.searchView.ISearchView;

import javax.swing.*;

public class RatingPresenter implements IRatingPresenter {
	private IRatingView ratingView;
	private ISearchView searchView;
	private ISearcherModel searcherModel;
	private IStorerModel storerModel;
	private ContainerWindow containerWindow;


	public RatingPresenter(IRatingView ratingView, ISearchView searchView, ISearcherModel searcherModel, IStorerModel storerModel) {
		this.ratingView = ratingView;
		this.searchView = searchView;
		this.searcherModel = searcherModel;
		this.storerModel = storerModel;
	}

	public void start() {
		ratingView.setRatingPresenter(this);
		initListeners();
		updateRatedSeries();
	}
	private void initListeners(){
		storerModel.addRatingSaveSuccessListener(this::updateRatedSeries);
		storerModel.addRatedSearchHasFinishedListener(() -> {
			ratingView.setRatedSeriesList(storerModel.getRatedSeriesModel());
		});
	}

	public void updateRatedSeries() {
		storerModel.getRatedSeries();
	}

	public void ratedSeriesClicked() {
		try {
			searchView.setWorkingStatus();
			containerWindow.getContainerTabbedPane().setSelectedIndex(0);
			RatedWikiPage ratedSeries = ratingView.getRatedSeriesSelected();
			String pageID = ratedSeries.getId();
			String selectedResultTitle = ratedSeries.getTitle();
			String extract = searcherModel.getExtractByPageID(pageID);
			String textToShow;

			textToShow = "<h1>" + ratedSeries.getTitle() + "</h1>" + HTMLFormatter.textToHtml(extract);
			searchView.setCurrentPage(selectedResultTitle, extract, pageID);
			searchView.showSearchResult(textToShow);

			searchView.setWaitingStatus();

		} catch (WikiAPIRequestException e) {
			throw new RuntimeException(e);
		}
	}
}
