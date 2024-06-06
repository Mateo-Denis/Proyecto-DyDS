package views.ratingView;

import presenters.ratingPresenter.IRatingPresenter;
import utils.wiki.RatedWikiPage;

import javax.swing.*;

public interface IRatingView {
	void setRatingPresenter(IRatingPresenter ratingPresenter);

	void setRatedSeriesList(DefaultListModel<RatedWikiPage> listModel);

	RatedWikiPage getRatedSeriesSelected();
}
