package utils.wiki;

import java.sql.Timestamp;

public class RatedWikiPage extends WikiPage implements Comparable<RatedWikiPage>{

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
		return rating + " - " + title + " @ ( " + timestamp.toString() + " )";
	}

	@Override
	public int compareTo(RatedWikiPage o) {
		if (rating > o.rating) {
			return 1;
		} else if (rating < o.rating) {
			return -1;
		} else {
			return 0;
		}
	}
}
