package unitTests.models;

import models.listeners.wikisearch.RatedSeriesDoubleClickedListener;
import models.listeners.wikisearch.WikiSearchFailureListener;
import models.searcherModel.SearcherModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import utils.SearchResult;
import utils.wiki.RatedWikiPage;
import utils.wiki.WikiAPIConsummer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SearcherModelTest {

	@Mock
	private WikiAPIConsummer wikiAPIConsummer;
	private AtomicBoolean isNotified;
	private SearcherModel searcherModel;
	private List<SearchResult> mockResults;
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		searcherModel = new SearcherModel(wikiAPIConsummer);
		isNotified = new AtomicBoolean(false);
		mockResults = Arrays.asList(new SearchResult("Title1", "12", "asd")
				, new SearchResult("Title2", "13", "asdasdasd"));
	}

	@Test
	public void testSearchForTermSuccess() throws IOException {

		when(wikiAPIConsummer.searchForTerm("term")).thenReturn(mockResults);

		searcherModel.searchForTerm("term");

		assertEquals(mockResults, searcherModel.getSearchResultsForLastTerm());
	}

	@Test
	public void testSearchForTermFailure() throws IOException {
		when(wikiAPIConsummer.searchForTerm("term")).thenThrow(new IOException());
		searcherModel.addWikiSearchFailureListener( () -> isNotified.set(true));

		searcherModel.searchForTerm("term");

		assertTrue(isNotified.get());
	}

	@Test
	public void testGetExtractByPageIDSuccess() throws IOException {
		String mockExtract = "Extract content";
		when(wikiAPIConsummer.getExtractByPageID("pageID")).thenReturn(mockExtract);

		searcherModel.getExtractByPageID("pageID");

		assertEquals(mockExtract, searcherModel.getExtractOfLastPageSearched());
	}

	@Test
	public void testGetExtractByPageIDFailure() throws IOException {
		when(wikiAPIConsummer.getExtractByPageID("pageID")).thenThrow(new IOException());
		searcherModel.addWikiSearchFailureListener( () -> isNotified.set(true));

		searcherModel.getExtractByPageID("pageID");

		assertTrue(isNotified.get());
	}

	@Test
	public void testSearchRatedSeries() {
		RatedWikiPage mockPage = new RatedWikiPage("1", "Title1", 5, new java.sql.Timestamp(System.currentTimeMillis()));
		searcherModel.addRatedSeriesDoubleClickedListener((ratedWikiPage) -> isNotified.set(true));

		searcherModel.searchRatedSeries(mockPage);

		assertTrue(isNotified.get());
	}

	@Test
	public void testNotifyWikiSearchFailure() throws IOException{
		searcherModel.addWikiSearchFailureListener(() -> isNotified.set(true));
		when(wikiAPIConsummer.searchForTerm("error generating term")).thenThrow(new IOException());

		searcherModel.searchForTerm("error generating term");

		assertTrue(isNotified.get());
	}
}

