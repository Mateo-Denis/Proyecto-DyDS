package utils.wiki;

import java.sql.Timestamp;

public class RatedWikiPage extends WikiPage{

	private int rating;
	private Timestamp timestamp;

	public RatedWikiPage(String title, String id, int rating, Timestamp timestamp) {
		super(title, "", id);
		this.rating = rating;
		this.timestamp = timestamp;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}
	@Override
	public String toString() {
		return title + " - " + rating + " ( " + timestamp.toString() + " )";
	}

}
