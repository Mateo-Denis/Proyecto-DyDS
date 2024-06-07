package models.storerModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import models.listeners.database.failure.AccessFailureListener;
import models.listeners.database.failure.PageDeleteFailureListener;
import models.listeners.database.failure.PageSaveFailureListener;
import models.listeners.database.failure.RatingSaveFailureListener;
import models.listeners.database.success.*;
import models.listeners.wikisearch.RatedSearchHasFinishedListener;
import utils.DataBase;
import utils.wiki.RatedWikiPage;

import javax.swing.*;

public class StorerModel implements IStorerModel {


	private final List<AccessFailureListener> accessFailureListeners;
	private final List<PageSaveSuccessListener> saveSuccessListeners;
	private final List<PageSaveFailureListener> saveFailureListeners;
	private final List<PageDeleteSuccessListener> deleteSuccessListeners;
	private final List<PageDeleteFailureListener> deleteFailureListeners;
	private final List<RatingSaveSuccessListener> ratingSaveSuccessListeners;
	private final List<RatingSaveFailureListener> ratingSaveFailureListeners;
	private final List<RatedSearchHasFinishedListener> ratedSearchHasFinishedListeners;
	private final List<TitlesAccessSuccessListener> titlesAccessSuccessListeners;
	private final List<ExtractAccessSuccessListener> extractAccessSuccessListeners;
	private DefaultListModel<RatedWikiPage> ratedSeries;
	private String lastSevedExtract;
	private ArrayList<String> lastSavedTitles;
	public StorerModel() {
		this.accessFailureListeners = new LinkedList<>();
		this.saveSuccessListeners = new LinkedList<>();
		this.saveFailureListeners = new LinkedList<>();
		this.deleteSuccessListeners = new LinkedList<>();
		this.deleteFailureListeners = new LinkedList<>();
		this.ratingSaveSuccessListeners = new LinkedList<>();
		this.ratingSaveFailureListeners = new LinkedList<>();
		this.ratedSearchHasFinishedListeners = new LinkedList<>();
		this.titlesAccessSuccessListeners = new LinkedList<>();
		this.extractAccessSuccessListeners = new LinkedList<>();
	}

	@Override
	public void addAccessFailureListener(AccessFailureListener listener) {
		accessFailureListeners.add(listener);
	}
	@Override
	public void addPageSaveSuccessListener(PageSaveSuccessListener listener) {
		saveSuccessListeners.add(listener);
	}

	@Override
	public void addPageSaveFailureListener(PageSaveFailureListener listener) {
		saveFailureListeners.add(listener);
	}

	@Override
	public void addPageDeleteSuccessListener(PageDeleteSuccessListener listener) {
		deleteSuccessListeners.add(listener);
	}

	@Override
	public void addPageDeleteFailureListener(PageDeleteFailureListener listener) {
		deleteFailureListeners.add(listener);
	}

	@Override
	public void addRatingSaveSuccessListener(RatingSaveSuccessListener listener) {
		ratingSaveSuccessListeners.add(listener);
	}
	@Override
	public void addRatingSaveFailureListener(RatingSaveFailureListener listener) {
		ratingSaveFailureListeners.add(listener);
	}

	public void addRatedSearchHasFinishedListener(RatedSearchHasFinishedListener listener) {
		ratedSearchHasFinishedListeners.add(listener);
	}

	public void addTitlesAccessSuccessListener(TitlesAccessSuccessListener listener) {
		titlesAccessSuccessListeners.add(listener);
	}

	public void addExtractAccessSuccessListener(ExtractAccessSuccessListener listener) {
		extractAccessSuccessListeners.add(listener);
	}

	private void notifyAccessFailed() {
		for (AccessFailureListener listener: accessFailureListeners) {
			listener.onFailure();
		}
	}

	private void notifySaveSuccess() {
		for (PageSaveSuccessListener listener: saveSuccessListeners) {
			listener.onSuccess();
		}
	}

	private void notifySaveFailure() {
		for (PageSaveFailureListener listener: saveFailureListeners) {
			listener.onFailure();
		}
	}

	private void notifyDeleteSuccess() {
		for (PageDeleteSuccessListener listener: deleteSuccessListeners) {
			listener.onSuccess();
		}
	}

	private void notifyDeleteFailure() {
		for (PageDeleteFailureListener listener: deleteFailureListeners) {
			listener.onFailure();
		}
	}

	private void notifyRatingSaveSuccess() {
		for (RatingSaveSuccessListener listener: ratingSaveSuccessListeners) {
			listener.onSuccess();
		}
	}

	private void notifyRatingSaveFailure() {
		for (RatingSaveFailureListener listener: ratingSaveFailureListeners) {
			listener.onFailure();
		}
	}
	private void notifyRatedSeriesWereRetrieved() {
		for (RatedSearchHasFinishedListener listener: ratedSearchHasFinishedListeners) {
			listener.onSuccess();
		}
	}
	private void notifyTitlesAccessSuccess() {
		for (TitlesAccessSuccessListener listener: titlesAccessSuccessListeners) {
			listener.onSuccess();
		}
	}

	private void notifyExtractAccessSuccess() {
		for (ExtractAccessSuccessListener listener: extractAccessSuccessListeners) {
			listener.onSuccess();
		}
	}



	@Override
	public void getSavedTitles() {
		try {
			lastSavedTitles = DataBase.getTitles();
			notifyTitlesAccessSuccess();
		} catch (SQLException e) {
			lastSavedTitles = null;
			notifyAccessFailed();
		}
	}
	@Override
	public ArrayList<String> getLastSavedTitles() {
		return lastSavedTitles;
	}

	@Override
	public void getSavedExtract(String title) {
		try {
			lastSevedExtract = DataBase.getExtract(title);
			notifyExtractAccessSuccess();
		} catch (SQLException e) {
			lastSevedExtract = null;
			notifyAccessFailed();
		}
	}
	@Override
	public String getLastSavedExtract() {
		return lastSevedExtract;
	}

	@Override
	public void saveInfo(String title, String text) {
		try {
			DataBase.saveInfo(title.replace("'", "`"), text);
			notifySaveSuccess();
		} catch (SQLException e) {
			notifySaveFailure();
		}
	}

	@Override
	public void deleteEntry(String title) {
		try {
			DataBase.deleteEntry(title);
			notifyDeleteSuccess();
		}catch (SQLException e){
			notifyDeleteFailure();
		}
	}

	public int getRating(String id) {
		try {
			return DataBase.getRating(id);
		} catch (SQLException e) {
			notifyAccessFailed();
			return -1;
		}
	}

	public void getRatedSeries() {
		try {
			List<RatedWikiPage> listFromDataBase = DataBase.getRatedPages();
			listFromDataBase.sort(Comparator.reverseOrder());
			ratedSeries = new DefaultListModel<>();
			for (RatedWikiPage ratedWikiPage : listFromDataBase) {
				ratedSeries.addElement(ratedWikiPage);
			}
			notifyRatedSeriesWereRetrieved();
		} catch (SQLException e) {
			notifyAccessFailed();
		}


	}

	public DefaultListModel<RatedWikiPage> getRatedSeriesModel() {
		return ratedSeries;
	}

	public void saveRating(RatedWikiPage ratedWikiPage, boolean isUpdate) {
		try {

			if (isUpdate) {
				DataBase.updateRatedPage(Integer.parseInt(ratedWikiPage.getId())
						, ratedWikiPage.getTitle()
						, ratedWikiPage.getRating()
						, ratedWikiPage.getTimestamp().toString());
			} else {
				DataBase.insertRatedPage(Integer.parseInt(ratedWikiPage.getId())
						, ratedWikiPage.getTitle()
						, ratedWikiPage.getRating()
						, ratedWikiPage.getTimestamp().toString());
			}

			notifyRatingSaveSuccess();
		}catch (SQLException e){
			notifyRatingSaveFailure();
		}
	}

}