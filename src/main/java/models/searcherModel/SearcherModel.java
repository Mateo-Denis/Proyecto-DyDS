package models.searcherModel;

import models.listeners.wikisearch.RatedSeriesDoubleClickedListener;
import models.listeners.wikisearch.WikiSearchFailureListener;
import utils.SearchResult;
import utils.wiki.RatedWikiPage;
import utils.wiki.WikiAPIConsummer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearcherModel implements ISearcherModel {

	private final List<RatedSeriesDoubleClickedListener> ratedSeriesDoubleClickedListeners = new ArrayList<>();
	private final List<WikiSearchFailureListener> wikiSearchFaiulreListeners = new ArrayList<>();
	private List<SearchResult> searchResultsForLastTerm;
	private String extractForLastPageSearched;
	private final WikiAPIConsummer wikiAPIConsummer;

	public SearcherModel(WikiAPIConsummer wikiAPIConsummer) {
		this.wikiAPIConsummer = wikiAPIConsummer;
	}

	@Override
	public void addRatedSeriesDoubleClickedListener(RatedSeriesDoubleClickedListener listener) {
		ratedSeriesDoubleClickedListeners.add(listener);
	}
	@Override
	public void addWikiSearchFailureListener(WikiSearchFailureListener listener) {
		wikiSearchFaiulreListeners.add(listener);
	}


	private void notifyRatedSeriesDoubleClickedListeners(RatedWikiPage ratedWikiPage){
		for (RatedSeriesDoubleClickedListener listener: ratedSeriesDoubleClickedListeners) {
			listener.onDoubleClick(ratedWikiPage);
		}
	}

	private void notifyWikiSearchFailure() {
		for (WikiSearchFailureListener listener: wikiSearchFaiulreListeners) {
			listener.onFailure();
		}
	}


	@Override
	public void searchForTerm(String term) {
		try {
		searchResultsForLastTerm = wikiAPIConsummer.searchForTerm(term);
		} catch (IOException e) {
			notifyWikiSearchFailure();
		}
	}
	@Override
	public List<SearchResult> getSearchResultsForLastTerm() {
		return searchResultsForLastTerm;
	}

	@Override
	public void getExtractByPageID(String pageID) {
		try {
			extractForLastPageSearched = wikiAPIConsummer.getExtractByPageID(pageID);
		} catch (IOException e) {
			notifyWikiSearchFailure();
		}
	}
	@Override
	public String getExtractOfLastPageSearched() {
		return extractForLastPageSearched;
	}


	public void searchRatedSeries(RatedWikiPage ratedWikiPage){
		notifyRatedSeriesDoubleClickedListeners(ratedWikiPage);
	}
}
