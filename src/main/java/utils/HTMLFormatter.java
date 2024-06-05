package utils;

public class HTMLFormatter {
	public static String textToHtml(String textToFormat) {

		StringBuilder builder = new StringBuilder();

		builder.append("<font face=\"arial\">");

		String fixedText = textToFormat
				.replace("'", "`"); //Replace to avoid SQL errors, we will have to find a workaround..

		builder.append(fixedText);

		builder.append("</font>");

		return builder.toString();
	}

	public static String searchResultToHtml(SearchResult searchResult) {
		StringBuilder builder = new StringBuilder();

		builder.append("<html><font face=\"arial\">");

		String title = searchResult.getTitle();
		String snippet = searchResult.getSnippet();

		String fixedTitle = title.replace("'", "`");
		String fixedSnippet = snippet.replace("'", "`");

		builder.append(fixedTitle + ": " + fixedSnippet);

		builder.append("</font></html>");

		return builder.toString();
	}
}
