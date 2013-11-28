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
package org.jrb.commons.web.controller;

import java.text.MessageFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.atteo.evo.inflector.English;
import org.jrb.commons.web.MessageResponse;
import org.jrb.commons.web.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Base controller for the root URI.
 *
 * @author <a href="mailto:brulejr@gmail.com">Jon Brule</a>
 */
public abstract class AbstractRootController {

	@Autowired
	private ResponseUtils utils;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<MessageResponse> home() {
		final MessageResponse response = utils.createResponse(MessageResponse.class);
		
		response.setMessage(responseMessage(response));
		
		addLinks(response);
		
		return utils.finalize(response);
	}

	protected abstract void addLinks(final MessageResponse response);

	protected String responseMessage(final MessageResponse response) {
		final String timestamp = DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.format(new Date());
		final String message = MessageFormat.format("{0}(v{1}) - {2}", response.getProduct(), response.getVersion(), timestamp); 
		return message;
	}

	protected String entityRel(Class<?> classname) {
		return StringUtils.uncapitalize(English.plural(classname.getSimpleName()));
	}

}