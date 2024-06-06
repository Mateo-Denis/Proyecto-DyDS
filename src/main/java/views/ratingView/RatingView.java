package views.ratingView;

import presenters.ratingPresenter.IRatingPresenter;
import utils.wiki.RatedWikiPage;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RatingView extends JComponent implements IRatingView{
	private JPanel ratingTab;
	private JList<RatedWikiPage> ratedSeriesList;
	private RatedWikiPage ratedSeriesSelected;
	private IRatingPresenter ratingPresenter;

	public RatingView() {}

	public void start() {
		initListeners();
	}
	public void setRatingPresenter(IRatingPresenter ratingPresenter) {
		this.ratingPresenter = ratingPresenter;
	}
	private void initListeners() {
		ratedSeriesList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent click) {
				if (click.getClickCount() == 2) {
					ratingPresenter.ratedSeriesClicked();
				}
			}
		});
	}

	public RatedWikiPage getRatedSeriesSelected() {
		return ratedSeriesList.getSelectedValue();
	}

	public void setRatedSeriesList(DefaultListModel<RatedWikiPage> listModel){
		ratedSeriesList.setModel(listModel);
		initListeners();
	}


}
