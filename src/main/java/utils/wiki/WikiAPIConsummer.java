package utils.wiki;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import utils.SearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WikiAPIConsummer {
	private final WikipediaSearchAPI searchAPI;
	private final WikipediaPageAPI pageAPI;

	public WikiAPIConsummer() {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl("https://en.wikipedia.org/w/")
				.addConverterFactory(ScalarsConverterFactory.create())
				.build();

		searchAPI = retrofit.create(WikipediaSearchAPI.class);
		pageAPI = retrofit.create(WikipediaPageAPI.class);
	}

	public String getExtractByPageID(String pageID) throws IOException {
		try{
			String toReturn;
			Response<String> callForPageResponse = pageAPI.getExtractByPageID(pageID).execute();
			Gson gson = new Gson();
			JsonObject jobj2 = gson.fromJson(callForPageResponse.body(), JsonObject.class);
			JsonObject query2 = jobj2.get("query").getAsJsonObject();
			JsonObject pages = query2.get("pages").getAsJsonObject();
			JsonObject page = pages.entrySet().iterator().next().getValue().getAsJsonObject();
			JsonElement searchResultExtract2 = page.get("extract");
			if(searchResultExtract2 != null){
				toReturn = searchResultExtract2.getAsString().replace("\\n", "\n");
			}else {
				toReturn = "No Results";
			}
			return toReturn;
		} catch (IOException e) {
			return "No Results";
		}
	}

	public List<SearchResult> searchForTerm(String term) throws IOException{
		Response<String> callForSearchResponse;
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
}

}
