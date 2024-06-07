package models.searcherModel;

import models.listeners.wikisearch.RatedSeriesDoubleClickedListener;
import models.listeners.wikisearch.WikiSearchFailureListener;
import utils.exceptions.WikiAPIRequestException;
import utils.SearchResult;
import utils.wiki.RatedWikiPage;

import java.util.List;

public interface ISearcherModel {
	void addRatedSeriesDoubleClickedListener(RatedSeriesDoubleClickedListener listener);

	void addWikiSearchFailureListener(WikiSearchFailureListener listener);

	void searchForTerm(String term) throws WikiAPIRequestException;

	List<SearchResult> getSearchResultsForLastTerm();

	void getExtractByPageID(String pageID) throws WikiAPIRequestException;

	String getExtractOfLastPageSearched();

	void searchRatedSeries(RatedWikiPage ratedSeriesSelected);

}
