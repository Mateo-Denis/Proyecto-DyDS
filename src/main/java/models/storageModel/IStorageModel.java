package models.storageModel;

import utils.SearchResult;

import java.util.List;

public interface IStorageModel {
	List<String> getSavedTitles();
	String getSavedExtract(String title);
	void saveInfo(String title, String text);
	void deleteEntry(String title);
}
