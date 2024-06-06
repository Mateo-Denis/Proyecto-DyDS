package models.storerModel;

import models.listeners.failure.AccessFailureListener;
import models.listeners.failure.PageDeleteFailureListener;
import models.listeners.failure.PageSaveFailureListener;
import models.listeners.failure.RatingSaveFailureListener;
import models.listeners.success.PageDeleteSuccessListener;
import models.listeners.success.PageSaveSuccessListener;
import models.listeners.success.RatedSearchHasFinishedListener;
import models.listeners.success.RatingSaveSuccessListener;
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

	List<String> getSavedTitles();
	String getSavedExtract(String title);
	void saveInfo(String title, String text);
	void deleteEntry(String title);

	void getRatedSeries();

	void saveRating(RatedWikiPage ratedPageToSave, boolean isUpdate);
	int getRating(String pageID);

	DefaultListModel<RatedWikiPage> getRatedSeriesModel();
}
