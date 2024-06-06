package models.storerModel;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import models.listeners.failure.AccessFailureListener;
import models.listeners.failure.PageDeleteFailureListener;
import models.listeners.failure.PageSaveFailureListener;
import models.listeners.failure.RatingSaveFailureListener;
import models.listeners.success.PageDeleteSuccessListener;
import models.listeners.success.PageSaveSuccessListener;
import models.listeners.success.RatingSaveSuccessListener;
import models.listeners.success.RatedSearchHasFinishedListener;
import utils.DataBase;
import utils.wiki.RatedWikiPage;

import javax.swing.*;

public class StorerModel implements IStorerModel {


	private List<AccessFailureListener> accessFailureListeners;
	private List<PageSaveSuccessListener> saveSuccessListeners;
	private List<PageSaveFailureListener> saveFailureListeners;
	private List<PageDeleteSuccessListener> deleteSuccessListeners;
	private List<PageDeleteFailureListener> deleteFailureListeners;
	private List<RatingSaveSuccessListener> ratingSaveSuccessListeners;
	private List<RatingSaveFailureListener> ratingSaveFailureListeners;
	private List<RatedSearchHasFinishedListener> ratedSearchHasFinishedListeners;
	private DefaultListModel<RatedWikiPage> ratedSeries;
	public StorerModel() {
		this.accessFailureListeners = new LinkedList<AccessFailureListener>();
		this.saveSuccessListeners = new LinkedList<PageSaveSuccessListener>();
		this.saveFailureListeners = new LinkedList<PageSaveFailureListener>();
		this.deleteSuccessListeners = new LinkedList<PageDeleteSuccessListener>();
		this.deleteFailureListeners = new LinkedList<PageDeleteFailureListener>();
		this.ratingSaveSuccessListeners = new LinkedList<RatingSaveSuccessListener>();
		this.ratingSaveFailureListeners = new LinkedList<RatingSaveFailureListener>();
		this.ratedSearchHasFinishedListeners = new LinkedList<RatedSearchHasFinishedListener>();
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



	@Override
	public List<String> getSavedTitles() {
		try {
			return DataBase.getTitles();
		} catch (SQLException e) {
			notifyAccessFailed();
			return null;
		}
	}

	@Override
	public String getSavedExtract(String title) {
		try {
			return DataBase.getExtract(title);
		} catch (SQLException e) {
			notifyAccessFailed();
			return null;
		}
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