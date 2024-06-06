package models.searcherModel;

import utils.exceptions.WikiAPIRequestException;
import utils.SearchResult;

import java.util.List;

public interface ISearcherModel {
	List<SearchResult> searchForTerm(String term) throws WikiAPIRequestException;
	String getExtractByPageID(String pageID) throws WikiAPIRequestException;
}
