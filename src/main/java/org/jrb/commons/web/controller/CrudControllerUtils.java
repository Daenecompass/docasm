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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * RESTful handling utilities for CRUD controllers.
 * 
 * @author <a href="mailto:brulejr@gmail.com">Jon Brule</a>
 */
public class CrudControllerUtils<E, R extends EntityResponse<E>, L extends EntityListResponse<E>> {

	public interface CreateEntityCallback<E> {
		E createEntity(E submitted);
	}

	public interface DeleteEntityCallback<E> {
		void deleteEntity(Long entityId);
	}

	public interface FindEntityCallback<E> {
		E findEntity(Long entityId);
	}

	public interface RetrieveEntitiesCallback<E> {
		List<E> retrieveEntities();
	}

	public interface UpdateEntityCallback<E> {
		E updateEntity(Long entityId, E submitted);
	}

	private final ResponseUtils responseUtils;
	
	public CrudControllerUtils(final ResponseUtils responseUtils) {
		this.responseUtils = responseUtils;
	}

	public ResponseEntity<R> createEntity(
			final E entity,
			final Class<E> entityClass,
			final Class<R> entityResponseClass,
			final Class<?> controllerClass,
			final CreateEntityCallback<E> callback) {

		final R response = responseUtils.createResponse(entityResponseClass);
		final E createdEntity = callback.createEntity(entity);
		response.setEntity(createdEntity);

		response.add(linkTo(controllerClass).slash(createdEntity).withSelfRel());
		response.add(linkTo(controllerClass).withRel(entityRel(entityClass)));

		final HttpHeaders headers = new HttpHeaders();
		headers.setLocation(linkTo(getClass()).slash(createdEntity).toUri());

		return responseUtils.finalize(response, HttpStatus.CREATED, headers);
	}

	public ResponseEntity<MessageResponse> deleteEntity(
			final Long entityId,
			final Class<E> entityClass,
			final Class<R> entityResponseClass,			
			final Class<?> controllerClass,
			final DeleteEntityCallback<E> callback) {

		final MessageResponse response = responseUtils.createResponse(MessageResponse.class);
		callback.deleteEntity(entityId);
		response.setMessage(entityClass.getSimpleName() + "(" + entityId + ") has been deleted");

		response.add(linkTo(controllerClass).withRel(entityRel(entityClass)));

		return responseUtils.finalize(response, HttpStatus.OK);
	}

	public <T> boolean different(final T a, final T b) {
		return !(a == null ? b == null : a.equals(b));
	}

	protected String entityRel(final Class<?> classname) {
		return StringUtils.uncapitalize(English.plural(classname.getSimpleName()));
	}

	public ResponseEntity<R> findEntity(
			final Long entityId,
			final Class<E> entityClass,			
			final Class<R> entityResponseClass,
			final Class<?> controllerClass,
			final FindEntityCallback<E> callback) {

		final R response = responseUtils.createResponse(entityResponseClass);
		final E entity = callback.findEntity(entityId);
		response.setEntity(entity);

		response.add(linkTo(controllerClass).withRel(entityRel(entityClass)));

		return responseUtils.finalize(response, HttpStatus.OK);
	}

	public ResponseEntity<L> retrieveEntities(
			final Class<L> entityListClass,
			final RetrieveEntitiesCallback<E> callback) {
		final L response = responseUtils.createResponse(entityListClass);
		final List<E> entityList = callback.retrieveEntities();
		response.setContent(entityList);
		return responseUtils.finalize(response, HttpStatus.OK);
	}

	public ResponseEntity<R> updateEntity(
			final Long entityId,
			final E submitted,
			final Class<E> entityClass,
			final Class<R> entityResponseClass,
			final Class<?> controllerClass,
			final UpdateEntityCallback<E> callback) {

		final R response = responseUtils.createResponse(entityResponseClass);
		final E updatedEntity = callback.updateEntity(entityId, submitted);
		response.setEntity(updatedEntity);

		response.add(linkTo(controllerClass).slash(updatedEntity).withSelfRel());
		response.add(linkTo(controllerClass).withRel(entityRel(entityClass)));

		return responseUtils.finalize(response, HttpStatus.OK);
	}

}
