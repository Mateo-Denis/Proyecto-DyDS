package models.searchModel;

import exceptions.WikiAPIRequestException;
import utils.SearchResult;

import java.util.List;

public interface ISearchModel {
	List<SearchResult> searchForTerm(String term) throws WikiAPIRequestException;
	String getExtractByPageID(String pageID) throws WikiAPIRequestException;
}
