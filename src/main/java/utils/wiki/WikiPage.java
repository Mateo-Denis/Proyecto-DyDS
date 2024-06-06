package utils.wiki;

public class WikiPage {

	protected String title;
	private String extract;
	private String id;

	public WikiPage(String title, String extract, String id) {
		this.title = title;
		this.extract = extract;
		this.id = id;
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
	public void setTitle(String title) {
		this.title = title;
	}

	public void setExtract(String extract) {
		this.extract = extract;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return title;
	}

}
