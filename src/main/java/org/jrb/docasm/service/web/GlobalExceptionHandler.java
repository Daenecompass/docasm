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
package org.jrb.docasm.service.web;

import org.jrb.docasm.service.document.DuplicateDocumentException;
import org.jrb.docasm.service.document.InvalidDocumentException;
import org.jrb.docasm.service.document.UnknownDocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Handles application-wide errors for the Document Assembly application.
 * 
 * @author <a href="mailto:brulejr@gmail.com">Jon Brule</a>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	private final static Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@Autowired
	private ResponseUtils utils;

	/**
	 * Converts one of several client-based bad request exceptions into an HTTP
	 * 400 response with an error body. The mapped exceptions are as follows:
	 * <ul>
	 * <li>{@link InvalidDocumentException}</li>
	 * </ul>
	 * 
	 * @param e
	 *            the client exception
	 * @return the error body
	 */
	@ExceptionHandler({ InvalidDocumentException.class })
	public ResponseEntity<MessageResponse> handleClientBadRequest(final Exception e) {
		if (LOG.isDebugEnabled()) {
			LOG.debug(e.getMessage(), e);
		}
		return utils.createMessageResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	/**
	 * Converts one of several client-based conflict exceptions into an HTTP 409
	 * response with an error body. The mapped exceptions are as follows:
	 * <ul>
	 * <li>{@link DuplicateDocumentException}</li>
	 * </ul>
	 * 
	 * @param e
	 *            the client exception
	 * @return the error body
	 */
	@ExceptionHandler({ DuplicateDocumentException.class })
	public ResponseEntity<MessageResponse> handleConflictError(final Exception e) {
		if (LOG.isDebugEnabled()) {
			LOG.debug(e.getMessage(), e);
		}
		return utils.createMessageResponse(e.getMessage(), HttpStatus.CONFLICT);
	}

	/**
	 * Converts one of several client-based not found exceptions into an HTTP
	 * 404 response with an error body. The mapped exceptions are as follows:
	 * <ul>
	 * <li>{@link UnknownDocumentException}</li>
	 * </ul>
	 * 
	 * @param e
	 *            the client exception
	 * @return the error body
	 */
	@ExceptionHandler({ UnknownDocumentException.class })
	public ResponseEntity<MessageResponse> handleNotFoundError(final Exception e) {
		if (LOG.isDebugEnabled()) {
			LOG.debug(e.getMessage(), e);
		}
		return utils.createMessageResponse(e.getMessage(), HttpStatus.NOT_FOUND);
	}

}
