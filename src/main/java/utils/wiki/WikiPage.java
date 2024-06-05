package utils.wiki;

public class WikiPage {

	private String title;
	private String extract;
	private String id;
	private int rating;

	public WikiPage(String title, String extract, String id, int rating) {
		this.title = title;
		this.extract = extract;
		this.id = id;
		this.rating = rating;
	}

	public String getTitle() {
		return title;
	}

	public String getExtract() {
		return extract;
	}

	public String getId() {
		return id;
	}

	public int getRating() {
		return rating;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public void setExtract(String extract) {
		this.extract = extract;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	@Override
	public String toString() {
		return title;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof WikiPage) {
			WikiPage page = (WikiPage) obj;
			return page.getTitle().equals(title);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return title.hashCode();
	}
}
