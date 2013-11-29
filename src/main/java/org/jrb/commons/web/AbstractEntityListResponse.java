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
package org.jrb.commons.web;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Standard RESTFUL entity list resource.
 * 
 * @author <a href="mailto:brulejr@gmail.com">Jon Brule</a>
 */
@JsonInclude(Include.NON_EMPTY)
public class AbstractEntityListResponse<E> extends AbstractResponse implements EntityListResponse<E> {

	private List<E> content;
	private Integer number;
	private Integer numberOfElements;
	private Integer size;
	private Long totalElements;
	private Integer totalPages;

	/* (non-Javadoc)
	 * @see org.jrb.commons.web.EntityListResponse#getContent()
	 */
	@Override
	public List<E> getContent() {
		return content;
	}

	/* (non-Javadoc)
	 * @see org.jrb.commons.web.EntityListResponse#getNumber()
	 */
	@Override
	public Integer getNumber() {
		return number;
	}

	/* (non-Javadoc)
	 * @see org.jrb.commons.web.EntityListResponse#getNumberOfElements()
	 */
	@Override
	public Integer getNumberOfElements() {
		return numberOfElements;
	}

	/* (non-Javadoc)
	 * @see org.jrb.commons.web.EntityListResponse#getSize()
	 */
	@Override
	public Integer getSize() {
		return size;
	}

	/* (non-Javadoc)
	 * @see org.jrb.commons.web.EntityListResponse#getTotalElements()
	 */
	@Override
	public Long getTotalElements() {
		return totalElements;
	}

	/* (non-Javadoc)
	 * @see org.jrb.commons.web.EntityListResponse#getTotalPages()
	 */
	@Override
	public Integer getTotalPages() {
		return totalPages;
	}

	/* (non-Javadoc)
	 * @see org.jrb.commons.web.EntityListResponse#setContent(java.util.List)
	 */
	@Override
	public void setContent(final List<E> content) {
		this.content = content;
	}

	/* (non-Javadoc)
	 * @see org.jrb.commons.web.EntityListResponse#setPage(org.springframework.data.domain.Page)
	 */
	@Override
	public void setPage(final Page<E> page) {
		Assert.notNull(page);
		this.content = page.getContent();
		this.number = page.getNumber();
		this.numberOfElements = page.getNumberOfElements();
		this.size = page.getSize();
		this.totalElements = page.getTotalElements();
		this.totalPages = page.getTotalPages();
	}

}
