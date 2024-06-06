package models.searcherModel;

import models.listeners.search.RatedSeriesDoubleClickedListener;
import utils.exceptions.WikiAPIRequestException;
import utils.SearchResult;
import utils.wiki.RatedWikiPage;

import java.util.List;

public interface ISearcherModel {
	void addRatedSeriesDoubleClickedListener(RatedSeriesDoubleClickedListener listener);
	void searchForTerm(String term) throws WikiAPIRequestException;

	List<SearchResult> getSearchResultsForLastTerm();

	void getExtractByPageID(String pageID) throws WikiAPIRequestException;

	String getExtractOfLastPageSearched();

	void searchRatedSeries(RatedWikiPage ratedSeriesSelected);
}
