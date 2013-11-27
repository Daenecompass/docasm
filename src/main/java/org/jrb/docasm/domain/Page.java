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

/**
 * Domain entity for a page for the Document Assembler application.
 * 
 * @author <a href="mailto:brulejr@gmail.com">Jon Brule</a>
 */
@Entity
public class Page {

	/**
	 * Builder for a {@link Page} domain entity.
	 * 
	 * @author <a href="mailto:brulejr@gmail.com">Jon Brule</a>
	 */
	public static class Builder {

		private final Page page;

		public Builder() {
			page = new Page();
		}

		public Page build() {
			return page;
		}

		public Builder from(final Page doc) {
			page.createdOn = doc.createdOn;
			page.description = doc.description;
			page.id = doc.id;
			page.name = doc.name;
			page.updatedOn = doc.updatedOn;
			return this;
		}

		public Builder markAsCreated() {
			final Date timestamp = new Date();
			page.createdOn = timestamp;
			page.updatedOn = timestamp;
			return this;
		}

		public Builder markAsUpdated() {
			final Date timestamp = new Date();
			page.updatedOn = timestamp;
			return this;
		}

		public Builder setDescription(final String description) {
			page.description = description;
			return this;
		}

		public Builder setName(final String name) {
			page.name = name;
			return this;
		}

		public Builder setTemplate(final String template) {
			page.template = template;
			return this;
		}

		public Builder setTemplateUri(final URI templateUri) {
			page.templateUri = templateUri;
			return this;
		}

	}

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = true)
	private String description;

	@Column(nullable = true)
	private URI templateUri;

	@Column(nullable = false)
	private Date createdOn;

	@Column(nullable = false)
	private Date updatedOn;

	@Transient
	private String template;

	private Page() {
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

	public URI getTemplateUri() {
		return templateUri;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

}