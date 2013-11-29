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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Base RESTFUL resource that supports application identification and status
 * within the response headers.
 * 
 * @author <a href="mailto:brulejr@gmail.com">Jon Brule</a>
 */
public abstract class AbstractResponse extends ResourceSupport implements Response {

	public final static String HEADER_ELAPSED_TIME = "x-elapsed-time";
	public final static String HEADER_PRODUCT = "x-product";
	public final static String HEADER_START_TIME = "x-start-time";
	public final static String HEADER_STATUS = "x-status";
	public final static String HEADER_VERSION = "x-version";

	@JsonIgnore
	private final Map<String, Object> headers = new HashMap<>();

	@Override
	public void addHeader(final String key, final Object value) {
		headers.put(key, value);
	}

	@Override
	public <T> T getHeader(final String key, final Class<T> headerClass) {
		return headerClass.cast(headers.get(key));
	}

	@Override
	public Map<String, Object> getHeaders() {
		return headers;
	}

	@Override
	@JsonIgnore
	public Long getElapsedTime() {
		return getHeader(HEADER_ELAPSED_TIME, Long.class);
	}

	@Override
	@JsonIgnore
	public String getProduct() {
		return getHeader(HEADER_PRODUCT, String.class);
	}

	@Override
	@JsonIgnore
	public Date getStartTime() {
		return getHeader(HEADER_START_TIME, Date.class);
	}

	@Override
	@JsonIgnore
	public HttpStatus getStatus() {
		return getHeader(HEADER_STATUS, HttpStatus.class);
	}

	@Override
	@JsonIgnore
	public String getVersion() {
		return getHeader(HEADER_VERSION, String.class);
	}

	@Override
	public void setElapsedTime(final Long elapsedTime) {
		headers.put(HEADER_ELAPSED_TIME, elapsedTime);
	}

	@Override
	public void setProduct(final String product) {
		headers.put(HEADER_PRODUCT, product);
	}

	@Override
	public void setStartTime(final Date startTime) {
		headers.put(HEADER_START_TIME, startTime);
	}

	@Override
	public void setStatus(final HttpStatus status) {
		headers.put(HEADER_STATUS, status);
	}

	@Override
	public void setVersion(final String version) {
		headers.put(HEADER_VERSION, version);
	}

}
