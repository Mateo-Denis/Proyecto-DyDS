package unitTests.models;
import models.storerModel.StorerModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import utils.DataBase;
import utils.wiki.RatedWikiPage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StorerModelTest {

	private StorerModel storerModel;
	private AtomicBoolean isNotified;
	private ArrayList<String> mockTitles;

	private RatedWikiPage mockPage;
	@BeforeEach
	public void setUp() {
		storerModel = new StorerModel();
		isNotified = new AtomicBoolean(false);
		mockTitles = new ArrayList<>(Arrays.asList("Title1", "Title2", "Title3"));
		mockPage = new RatedWikiPage("Title1", "346", 5, new java.sql.Timestamp(System.currentTimeMillis()));
	}

	@Test
	public void testGetSavedTitlesSuccess() throws SQLException {
		try (MockedStatic<DataBase> mockedDatabase = Mockito.mockStatic(DataBase.class)) {
			mockedDatabase.when(DataBase::getTitles).thenReturn(mockTitles);

			storerModel.getSavedTitles();

			assertEquals(mockTitles, storerModel.getLastSavedTitles());
		}
	}

	@Test
	public void testGetSavedTitlesFailure() throws SQLException {
		storerModel.addAccessFailureListener(() -> isNotified.set(true));
		try (MockedStatic<DataBase> mockedDatabase = Mockito.mockStatic(DataBase.class)) {
			mockedDatabase.when(DataBase::getTitles).thenThrow(new SQLException());

			storerModel.getSavedTitles();

			assertNull(storerModel.getLastSavedTitles());
			assertTrue(isNotified.get());
		}
	}

	@Test
	public void testGetSavedExtractSuccess() throws SQLException {
		String mockExtract = "Extract content";
		try (MockedStatic<DataBase> mockedDatabase = Mockito.mockStatic(DataBase.class)) {
			mockedDatabase.when(() -> DataBase.getExtract("Title1")).thenReturn(mockExtract);

			storerModel.getSavedExtract("Title1");

			assertEquals(mockExtract, storerModel.getLastSavedExtract());
		}
	}

	@Test
	public void testGetSavedExtractFFailure() throws SQLException {
		storerModel.addAccessFailureListener(() -> isNotified.set(true));
		try (MockedStatic<DataBase> mockedDatabase = Mockito.mockStatic(DataBase.class)) {
			mockedDatabase.when(() -> DataBase.getExtract("Title1")).thenThrow(new SQLException());

			storerModel.getSavedExtract("Title1");

			assertNull(storerModel.getLastSavedExtract());
			assertTrue(isNotified.get());
		}
	}

	@Test
	public void testSaveInfoSuccess() throws SQLException {
		storerModel.addPageSaveSuccessListener(() -> isNotified.set(true));
		try (MockedStatic<DataBase> nonRelevant = mockStatic(DataBase.class)) {
			storerModel.saveInfo("title", "extract");
			assertTrue(isNotified.get());
		}
	}


	@Test
	public void testSaveInfoFailure() throws SQLException {
		storerModel.addPageSaveFailureListener(() -> isNotified.set(true));
		try (MockedStatic<DataBase> mockedDatabase = mockStatic(DataBase.class)) {
			mockedDatabase.when(() -> DataBase.saveInfo(anyString(),anyString())).thenThrow(new SQLException());
			storerModel.saveInfo("title", "extract");
			assertTrue(isNotified.get());
		}
	}


	@Test
	public void testDeletePageSuccess() throws SQLException {
		storerModel.addPageDeleteSuccessListener(() -> isNotified.set(true));
		try (MockedStatic<DataBase> nonRelevant = mockStatic(DataBase.class)) {
			storerModel.saveInfo("title1", "extract1");
			storerModel.saveInfo("title2", "extract2");
			storerModel.deleteEntry("title1");
			storerModel.getSavedExtract("title1");
			assertTrue(isNotified.get());
			assertNull(storerModel.getLastSavedExtract());
		}
	}

	@Test
	public void testDeletePageFailure() throws SQLException {
		storerModel.addPageDeleteFailureListener(() -> isNotified.set(true));
		try (MockedStatic<DataBase> mockedDatabase = Mockito.mockStatic(DataBase.class)) {
			mockedDatabase.when(() -> DataBase.deleteEntry(anyString())).thenThrow(new SQLException());
			storerModel.deleteEntry("non-existing title");
			assertTrue(isNotified.get());
		}
	}

	@Test
	public void testGetRatingSuccess() throws SQLException {
		int mockRating = 5;

		try (MockedStatic<DataBase> mockedDatabase = Mockito.mockStatic(DataBase.class)) {
			mockedDatabase.when(() -> DataBase.getRating("1")).thenReturn(mockRating);

			int rating = storerModel.getRating("1");

			assertEquals(mockRating, rating);
		}
	}

	@Test
	public void testGetRatingFailure() throws SQLException {
		try (MockedStatic<DataBase> mockedDatabase = Mockito.mockStatic(DataBase.class)) {
			mockedDatabase.when(() -> DataBase.getRating("1")).thenThrow(new SQLException());

			int rating = storerModel.getRating("1");

			assertEquals(-1, rating);
		}
	}

	@Test
	public void testSaveRatingSuccess() throws SQLException {

		storerModel.addRatingSaveSuccessListener(() -> isNotified.set(true));
		try (MockedStatic<DataBase> nonRelevant = Mockito.mockStatic(DataBase.class)) {

			storerModel.saveRating(mockPage, false);
			assertTrue(isNotified.get());
			isNotified.set(false);
			storerModel.saveRating(mockPage, true);
			assertTrue(isNotified.get());
		}
	}

	@Test
	public void testSaveRatingFailure() throws SQLException {
		storerModel.addRatingSaveFailureListener(() -> isNotified.set(true));
		try (MockedStatic<DataBase> mockedDatabase = Mockito.mockStatic(DataBase.class)) {
			mockedDatabase.when(() -> DataBase.insertRatedPage(anyInt(), anyString(), anyInt(), anyString())).thenThrow(new SQLException());
			mockedDatabase.when(() -> DataBase.updateRatedPage(anyInt(), anyString(), anyInt(), anyString())).thenThrow(new SQLException());

			storerModel.saveRating(mockPage, false);
			assertTrue(isNotified.get());
			isNotified.set(false);
			storerModel.saveRating(mockPage, true);
			assertTrue(isNotified.get());
		}
	}
}
