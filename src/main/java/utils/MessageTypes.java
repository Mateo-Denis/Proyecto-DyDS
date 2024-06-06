package utils;

import javax.swing.*;

public enum MessageTypes {

	PAGE_SAVE_SUCCESS("Page saved successfully"
			, "The searched page was successfully saved into the database"
			, JOptionPane.INFORMATION_MESSAGE),
	PAGE_SAVE_FAILURE("Error while saving page"
			, "The searched page was successfully saved into the database"
			, JOptionPane.ERROR_MESSAGE),
	RATING_SAVE_SUCCESS("Rating saved successfully"
			, "The rating was successfully saved into the database"
			, JOptionPane.INFORMATION_MESSAGE),
	RATING_SAVE_FAILURE("Error while saving rating"
			, "An error occurred while saving the rating into the database"
			, JOptionPane.ERROR_MESSAGE),
	EMPTY_RATING_SAVE_ATTEMPT("Error while saving rating"
			, "Cannot save a rating for an empty page. Please search for a page first."
			, JOptionPane.ERROR_MESSAGE),
	EMPTY_PAGE_SAVE_ATTEMPT("Error while saving page"
			, "Cannot save an empty page. Please search for a page first."
			, JOptionPane.ERROR_MESSAGE),
	ACCESS_FAILURE("Error while accessing database"
			, "An error occurred while accessing the database"
			, JOptionPane.ERROR_MESSAGE),
	PAGE_DELETE_SUCCESS("Page deleted successfully"
			, "The page was successfully deleted from the database"
			, JOptionPane.INFORMATION_MESSAGE),
	PAGE_DELETE_FAILURE("Error while deleting page"
			, "An error occurred while deleting the page from the database"
			, JOptionPane.ERROR_MESSAGE),
	PAGE_SEARCH_FAILURE("Error while searching page"
			, "An error occurred while searching the page from the Wikipedia API"
			, JOptionPane.ERROR_MESSAGE);

	private final String title;
	private final String message;
	private final int messageType;
	MessageTypes(String title, String message, int messageType) {
		this.title = title;
		this.message = message;
		this.messageType = messageType;
	}

	public String getTitle() {
		return title;
	}

	public String getMessage() {
		return message;
	}

	public int getMessageType() {
		return messageType;
	}

}
