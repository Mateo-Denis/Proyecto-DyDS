package models.storageModel;

import java.util.List;

import utils.DataBase;

public class StorageModel implements IStorageModel {
	@Override
	public List<String> getSavedTitles() {
		return DataBase.getTitles();
	}

	@Override
	public String getSavedExtract(String title) {
		return DataBase.getExtract(title);
	}

	@Override
	public void saveInfo(String title, String text) {
		DataBase.saveInfo(title.replace("'", "`"), text);
	}

	@Override
	public void deleteEntry(String title) {
		DataBase.deleteEntry(title);
	}
}