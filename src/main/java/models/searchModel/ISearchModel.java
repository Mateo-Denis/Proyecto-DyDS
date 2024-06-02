package models.searchModel;

import utils.SearchResult;

import java.util.List;

public interface ISearchModel {
	List<SearchResult> searchForTerm(String term);
	String getExtractByPageID(String pageID);
}
