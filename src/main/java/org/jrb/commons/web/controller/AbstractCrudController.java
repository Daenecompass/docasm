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

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.atteo.evo.inflector.English;
import org.jrb.commons.web.EntityListResponse;
import org.jrb.commons.web.EntityResponse;
import org.jrb.commons.web.MessageResponse;
import org.jrb.commons.web.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Base entity CRUD controller.
 * 
 * @author <a href="mailto:brulejr@gmail.com">Jon Brule</a>
 */
public abstract class AbstractCrudController<E> {

	@Autowired
	private ResponseUtils utils;

	protected abstract Class<E> entityClass();

	protected abstract Class<? extends EntityResponse<E>> entityResponseClass();

	protected abstract Class<? extends EntityListResponse<E>> entityListResponseClass();

	protected abstract E createEntityCallback(E submitted);

	protected abstract void deleteEntityCallback(Long entityId);

	protected abstract E findEntityCallback(Long entityId);

	protected abstract List<E> retrieveEntitiesCallback();

	protected abstract E updateEntityCallback(Long entityId, E submitted);

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<EntityResponse<E>> createEntity(@RequestBody final E submitted) {

		final EntityResponse<E> response = utils.createResponse(entityResponseClass());
		final E created = createEntityCallback(submitted);
		response.setEntity(created);

		response.add(linkTo(getClass()).slash(created).withSelfRel());
		response.add(linkTo(getClass()).withRel(StringUtils.uncapitalize(English.plural(entityClass().getSimpleName()))));

		final HttpHeaders headers = new HttpHeaders();
		headers.setLocation(linkTo(getClass()).slash(created).toUri());

		return utils.finalize(response, HttpStatus.CREATED, headers);
	}

	@RequestMapping(value = "{entityId}", method = RequestMethod.DELETE)
	public ResponseEntity<MessageResponse> deleteEntity(@PathVariable final Long entityId) {
		final MessageResponse response = utils.createResponse(MessageResponse.class);
		deleteEntityCallback(entityId);
		response.setMessage(entityClass().getSimpleName() + "(id=" + entityId + ") has been deleted");
		return utils.finalize(response, HttpStatus.OK);
	}

	protected <T> boolean different(final T a, final T b) {
		return !(a == null ? b == null : a.equals(b));
	}

	@RequestMapping(value = "{entityId}", method = RequestMethod.GET)
	public ResponseEntity<EntityResponse<E>> findEntity(@PathVariable final Long entityId) {
		final EntityResponse<E> response = utils.createResponse(entityResponseClass());
		final E entity = findEntityCallback(entityId);
		response.setEntity(entity);
		return utils.finalize(response, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<EntityListResponse<E>> retrieveDocuments() {
		final EntityListResponse<E> response = utils.createResponse(entityListResponseClass());
		final List<E> entityList = retrieveEntitiesCallback();
		response.setContent(entityList);
		return utils.finalize(response, HttpStatus.OK);
	}

	@RequestMapping(value = "{entityId}", method = RequestMethod.PATCH)
	public ResponseEntity<EntityResponse<E>> updateDocument(
			@PathVariable final Long entityId,
			@RequestBody final E submitted) {
		final EntityResponse<E> response = utils.createResponse(entityResponseClass());
		final E updated = updateEntityCallback(entityId, submitted);
		response.setEntity(updated);
		return utils.finalize(response, HttpStatus.OK);
	}

}
