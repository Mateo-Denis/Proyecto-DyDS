package presenters.ratingPresenter;

import models.searcherModel.ISearcherModel;
import models.storerModel.IStorerModel;
import views.ratingView.IRatingView;

public class RatingPresenter implements IRatingPresenter {
	private IRatingView ratingView;
	private ISearcherModel searcherModel;
	private IStorerModel storerModel;

	public RatingPresenter(IRatingView ratingView, ISearcherModel searcherModel, IStorerModel storerModel) {
		this.ratingView = ratingView;
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
		storerModel.addRatedSearchHasFinishedListener(this::refreshRatedSeries);
	}

	private void refreshRatedSeries() {
		ratingView.setRatedSeriesList(storerModel.getRatedSeriesModel());
	}
	public void updateRatedSeries() {
		storerModel.getRatedSeries();
		ratingView.setRatedSeriesList(storerModel.getRatedSeriesModel());
	}

	public void ratedSeriesClicked() {
		searcherModel.searchRatedSeries(ratingView.getRatedSeriesSelected());
	}
}