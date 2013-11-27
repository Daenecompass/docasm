/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Jon Brule
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.jrb.docasm.domain;

import java.net.URI;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.springframework.hateoas.Identifiable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Domain entity for a document for the Document Assembler application.
 * 
 * @author <a href="mailto:brulejr@gmail.com">Jon Brule</a>
 */
@Entity
@JsonInclude(Include.NON_EMPTY)
public class Document implements Identifiable<Long> {

	/**
	 * Builder for a {@link Document} domain entity.
	 * 
	 * @author <a href="mailto:brulejr@gmail.com">Jon Brule</a>
	 */
	public static class Builder {

		private final Document document;

		public Builder() {
			document = new Document();
		}

		public Document build() {
			return document;
		}

		public Builder from(final Document doc) {
			document.createdOn = doc.createdOn;
			document.description = doc.description;
			document.id = doc.id;
			document.name = doc.name;
			document.updatedOn = doc.updatedOn;
			document.version = doc.version;
			return this;
		}

		public Builder markAsCreated() {
			final Date timestamp = new Date();
			document.createdOn = timestamp;
			document.updatedOn = timestamp;
			return this;
		}

		public Builder markAsUpdated() {
			final Date timestamp = new Date();
			document.updatedOn = timestamp;
			return this;
		}

		public Builder setDescription(final String description) {
			document.description = description;
			return this;
		}

		public Builder setName(final String name) {
			document.name = name;
			return this;
		}

		public Builder setTemplate(final String template) {
			document.template = template;
			return this;
		}

		public Builder setTemplateUri(final URI templateUri) {
			document.templateUri = templateUri;
			return this;
		}

	}

	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true, nullable = false)
	private String name;

	@Column(nullable = true)
	private String description;

	@Column(nullable = true)
	private URI templateUri;

	@Column(nullable = false)
	private Date createdOn;

	@Column(nullable = false)
	private Date updatedOn;
	
	@Version
	private Integer version;

	@Transient
	private String template;

	private Document() {
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public String getDescription() {
		return description;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@JsonIgnore
	public String getTemplate() {
		return template;
	}

	public URI getTemplateUri() {
		return templateUri;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public Integer getVersion() {
		return version;
	}

	public void setTemplate(final String template) {
		this.template = template;
	}

}