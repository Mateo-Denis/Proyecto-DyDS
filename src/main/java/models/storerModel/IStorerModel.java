package models.storerModel;

import models.listeners.database.failure.AccessFailureListener;
import models.listeners.database.failure.PageDeleteFailureListener;
import models.listeners.database.failure.PageSaveFailureListener;
import models.listeners.database.failure.RatingSaveFailureListener;
import models.listeners.database.success.*;
import models.listeners.wikisearch.RatedSearchHasFinishedListener;
import utils.wiki.RatedWikiPage;

import javax.swing.*;
import java.util.List;

public interface IStorerModel {
	void addPageSaveSuccessListener(PageSaveSuccessListener listener);

	void addPageSaveFailureListener(PageSaveFailureListener listener);

	void addPageDeleteSuccessListener(PageDeleteSuccessListener listener);

	void addPageDeleteFailureListener(PageDeleteFailureListener listener);

	void addAccessFailureListener(AccessFailureListener listener);

	void addRatingSaveSuccessListener(RatingSaveSuccessListener listener);

	void addRatingSaveFailureListener(RatingSaveFailureListener listener);

	void addRatedSearchHasFinishedListener(RatedSearchHasFinishedListener listener);

	void addTitlesAccessSuccessListener(TitlesAccessSuccessListener listener);

	void addExtractAccessSuccessListener(ExtractAccessSuccessListener listener);

	void getSavedTitles();

	List<String> getLastSavedTitles();

	void getSavedExtract(String title);

	String getLastSavedExtract();

	void saveInfo(String title, String text);
	void deleteEntry(String title);

	void getRatedSeries();

	void saveRating(RatedWikiPage ratedPageToSave, boolean isUpdate);
	int getRating(String pageID);

	DefaultListModel<RatedWikiPage> getRatedSeriesModel();

}
