package models.searcherModel;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import utils.exceptions.WikiAPIRequestException;
import utils.SearchResult;
import utils.wiki.WikipediaPageAPI;
import utils.wiki.WikipediaSearchAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearcherModel implements ISearcherModel {

	private final WikipediaSearchAPI searchAPI;
	private final WikipediaPageAPI pageAPI;

	public SearcherModel() {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl("https://en.wikipedia.org/w/")
				.addConverterFactory(ScalarsConverterFactory.create())
				.build();

		searchAPI = retrofit.create(WikipediaSearchAPI.class);
		pageAPI = retrofit.create(WikipediaPageAPI.class);
	}
	@Override
	public List<SearchResult> searchForTerm(String term) throws WikiAPIRequestException {
		Response<String> callForSearchResponse = null;
		try {
			callForSearchResponse = searchAPI.searchForTerm(term + " (Tv series) articletopic:\"television\"").execute();
			Gson gson = new Gson();
			JsonObject jobj = gson.fromJson(callForSearchResponse.body(), JsonObject.class);
			JsonObject query = jobj.get("query").getAsJsonObject();
			JsonArray jsonResults = query.get("search").getAsJsonArray();

			List<SearchResult> results = new ArrayList<>();
			for (JsonElement je : jsonResults) {
				JsonObject searchResult = je.getAsJsonObject();
				String searchResultTitle = searchResult.get("title").getAsString();
				String searchResultPageId = searchResult.get("pageid").getAsString();
				String searchResultSnippet = searchResult.get("snippet").getAsString();
				results.add(new SearchResult(searchResultTitle, searchResultPageId, searchResultSnippet));
			}
			return results;
		} catch (IOException e) {
			throw new WikiAPIRequestException(e.getMessage());
		}
	}

	@Override
	public String getExtractByPageID(String pageID) throws WikiAPIRequestException {
		try {
			Response<String> callForPageResponse = pageAPI.getExtractByPageID(pageID).execute();
			Gson gson = new Gson();
			JsonObject jobj2 = gson.fromJson(callForPageResponse.body(), JsonObject.class);
			JsonObject query2 = jobj2.get("query").getAsJsonObject();
			JsonObject pages = query2.get("pages").getAsJsonObject();
			JsonObject page = pages.entrySet().iterator().next().getValue().getAsJsonObject();
			JsonElement searchResultExtract2 = page.get("extract");
			return searchResultExtract2 != null ? searchResultExtract2.getAsString().replace("\\n", "\n") : "No Results";
		} catch (IOException e) {
			throw new WikiAPIRequestException(e.getMessage());
		}
	}
}
