package org.jvalue.ceps.ods;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class OdsDataSourceMetaData {

	private final String name, title, author, authorEmail, notes, url, termsOfUse;

	@JsonCreator
	public OdsDataSourceMetaData(
			@JsonProperty("name") String name,
			@JsonProperty("title") String title,
			@JsonProperty("author") String author,
			@JsonProperty("authorEmail") String authorEmail,
			@JsonProperty("notes") String notes,
			@JsonProperty("url") String url,
			@JsonProperty("termsOfUse") String termsOfUse) {

		this.name = name;
		this.title = title;
		this.author = author;
		this.authorEmail = authorEmail;
		this.notes = notes;
		this.url = url;
		this.termsOfUse = termsOfUse;
	}


	public String getName() {
		return name;
	}


	public String getTitle() {
		return title;
	}


	public String getAuthor() {
		return author;
	}


	public String getAuthorEmail() {
		return authorEmail;
	}


	public String getNotes() {
		return notes;
	}


	public String getUrl() {
		return url;
	}


	public String getTermsOfUse() {
		return termsOfUse;
	}


}
