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

import java.util.Collections;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Utilities for managing RESTful responses for the Document Assembly
 * application.
 * 
 * @author <a href="mailto:brulejr@gmail.com">Jon Brule</a>
 */
public class ResponseUtils {

	@Resource
	private Environment env;

	@Resource
	private String product;

	@Resource
	private String version;

	public <R extends Response> R createResponse(Class<R> responseClass) {
		try {
			final R response = responseClass.newInstance();
			response.setProduct(product);
			response.setVersion(version);
			response.setStartTime(new Date());
			return response;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public ResponseEntity<MessageResponse> createMessageResponse(final String message) {
		final MessageResponse response = createResponse(MessageResponse.class);
		response.setMessage(message);
		return finalize(response, HttpStatus.OK);
	}

	public ResponseEntity<MessageResponse> createMessageResponse(final String message, final HttpStatus status) {
		final MessageResponse response = createResponse(MessageResponse.class);
		response.setMessage(message);
		return finalize(response, status);
	}

	public <R extends Response> ResponseEntity<R> finalize(final R response) {
		return finalize(response, HttpStatus.OK, new HttpHeaders());
	}

	public <R extends Response> ResponseEntity<R> finalize(final R response, final HttpStatus status) {
		return finalize(response, status, new HttpHeaders());
	}

	public <R extends Response> ResponseEntity<R> finalize(final R response, final HttpStatus status, final HttpHeaders headers) {
		final long startTime = response.getStartTime().getTime();
		response.setElapsedTime(new Date().getTime() - startTime);
		for (final Map.Entry<String, ?> header : response.getHeaders().entrySet()) {
			final String value = header.getValue().toString();
			headers.put(header.getKey(), Collections.singletonList(value));
		}
		response.setStatus(status);
		return new ResponseEntity<R>(response, headers, response.getStatus());
	}

}
