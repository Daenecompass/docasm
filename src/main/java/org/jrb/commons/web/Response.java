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
import java.util.List;
import java.util.Map;

import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;

/**
 * Base RESTFUL resource that supports application identification and status
 * within the response headers.
 *
 * @author <a href="mailto:brulejr@gmail.com">Jon Brule</a>
 */
public interface Response {

	void add(Link link);
	
	void add(Iterable<Link> links);
	
	void addHeader(String key, Object value);

	<T> T getHeader(String key, Class<T> headerClass);

	Map<String, Object> getHeaders();

	Long getElapsedTime();
	
	Link getLink(String rel);
	
	List<Link> getLinks();

	String getProduct();

	Date getStartTime();

	HttpStatus getStatus();

	String getVersion();
	
	boolean hasLinks();

	void setElapsedTime(Long elapsedTime);

	void setProduct(String product);

	void setStartTime(Date startTime);

	void setStatus(HttpStatus status);

	void setVersion(String version);

}