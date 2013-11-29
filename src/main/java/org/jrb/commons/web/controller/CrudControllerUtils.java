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

	/**
	 * Callback used by {@link CrudControllerUtils} to perform the actual logic
	 * to create an entity.
	 * 
	 * @author <a href="mailto:brulejr@gmail.com">Jon Brule</a>
	 * 
	 * @param <E>
	 *            the type of entity affected by this callback
	 */
	public interface CreateEntityCallback<E> {

		/**
		 * Creates an entity.
		 * 
		 * @param entity
		 *            the entity to be created
		 * @return the newly-created entity
		 */
		E createEntity(E entity);

	}

	/**
	 * Callback used by {@link CrudControllerUtils} to perform the actual logic
	 * to delete an entity.
	 * 
	 * @author <a href="mailto:brulejr@gmail.com">Jon Brule</a>
	 * 
	 * @param <E>
	 *            the type of entity affected by this callback
	 */
	public interface DeleteEntityCallback<E> {

		/**
		 * Deletes an entity.
		 * 
		 * @param entityId
		 *            the identifier of the entity to be deleted
		 */
		void deleteEntity(Long entityId);
	}

	/**
	 * Callback used by {@link CrudControllerUtils} to perform the actual logic
	 * to locate an entity.
	 * 
	 * @author <a href="mailto:brulejr@gmail.com">Jon Brule</a>
	 * 
	 * @param <E>
	 *            the type of entity affected by this callback
	 */
	public interface FindEntityCallback<E> {

		/**
		 * Finds an entity given its identifier.
		 * 
		 * @param entityId
		 *            the entity identifier
		 * @return to corresponding entity
		 */
		E findEntity(Long entityId);

	}

	/**
	 * Callback used by {@link CrudControllerUtils} to perform the actual logic
	 * to retrieve multiple entities.
	 * 
	 * @author <a href="mailto:brulejr@gmail.com">Jon Brule</a>
	 * 
	 * @param <E>
	 *            the type of entity affected by this callback
	 */
	public interface RetrieveEntitiesCallback<E> {

		/**
		 * Retrieves all entities of the associated type.
		 * 
		 * @return to corresponding entity list
		 */
		List<E> retrieveEntities();

	}

	/**
	 * Callback used by {@link CrudControllerUtils} to perform the actual logic
	 * to update an entity.
	 * 
	 * @author <a href="mailto:brulejr@gmail.com">Jon Brule</a>
	 * 
	 * @param <E>
	 *            the type of entity affected by this callback
	 */
	public interface UpdateEntityCallback<E> {

		/**
		 * Updates an entity.
		 * 
		 * @param entityId
		 *            the identifier of the entity to be updated
		 * @param entity
		 *            the updated entity
		 * @return the newly-updated entity
		 */
		E updateEntity(Long entityId, E entity);

	}

	private final ResponseUtils responseUtils;

	public CrudControllerUtils(final ResponseUtils responseUtils) {
		this.responseUtils = responseUtils;
	}

	/**
	 * Spring MVC controller utility method that creates a domain entity. Within
	 * a successful response, the following HATEOAS link(s) generated:
	 * <ul>
	 * <li>a <em>self</em> link pointing to at which this entity may be found</li>
	 * <li>a <em>collections</em> link giving access to all entities</li>
	 * </ul>
	 * 
	 * @param entity
	 *            the entity to be created
	 * @param entityClass
	 *            the class of the entity to be created
	 * @param entityResponseClass
	 *            the class to use in generating the response
	 * @param controllerClass
	 *            the controller class used for generating HATEOAS links
	 * @param callback
	 *            a callback containing the actual logic to create the entity
	 * @return a Spring MVC response containing the newly-created entity
	 */
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

	/**
	 * Spring MVC controller utility method that deletes a domain entity. Within
	 * a successful response, the following HATEOAS link(s) generated:
	 * <ul>
	 * <li>a <em>collections</em> link giving access to all entities</li>
	 * </ul>
	 * 
	 * @param entityId
	 *            the identifier of the domain entity to delete
	 * @param entityClass
	 *            the class of the entity to be deleted
	 * @param entityResponseClass
	 *            the class to use in generating the response
	 * @param controllerClass
	 *            the controller class used for generating HATEOAS links
	 * @param callback
	 *            a callback containing the actual logic to delete the entity
	 * @return a Spring MVC response containing the deletion status
	 */
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

	/**
	 * Determines if two strings, each of may be null, are different.
	 * 
	 * @param a
	 *            the first string
	 * @param b
	 *            the second string
	 * @return <code>true</code> if the strings are different; otherwise,
	 *         <code>false</code> if they are the same
	 */
	public <T> boolean different(final T a, final T b) {
		return !(a == null ? b == null : a.equals(b));
	}

	/**
	 * Calculates an entity link relation from a given class name. This relation
	 * is built from the camel case of the plural of the class name.
	 * 
	 * @param classname
	 *            the entity class name
	 * @return the link relation
	 */
	protected String entityRel(final Class<?> classname) {
		return StringUtils.uncapitalize(English.plural(classname.getSimpleName()));
	}

	/**
	 * Spring MVC controller utility method that find a domain entity. Within a
	 * successful response, the following HATEOAS link(s) generated:
	 * <ul>
	 * <li>a <em>collections</em> link giving access to all entities</li>
	 * </ul>
	 * 
	 * @param entityId
	 *            the identifier of the entity sought
	 * @param entityClass
	 *            the class of the entity to be found
	 * @param entityResponseClass
	 *            the class to use in generating the response
	 * @param controllerClass
	 *            the controller class used for generating HATEOAS links
	 * @param callback
	 *            a callback containing the actual logic to find the entity
	 * @return a Spring MVC response containing the found entity
	 */
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

	/**
	 * Spring MVC controller utility method that retrieves entities of a
	 * particular type.
	 * 
	 * @param entityListClass
	 *            the class to use in generating the response
	 * @param callback
	 *            a callback containing the actual logic to retrieve the
	 *            entities
	 * @return a Spring MVC response containing the entity list
	 */
	public ResponseEntity<L> retrieveEntities(
			final Class<L> entityListResponseClass,
			final RetrieveEntitiesCallback<E> callback) {
		final L response = responseUtils.createResponse(entityListResponseClass);
		final List<E> entityList = callback.retrieveEntities();
		response.setContent(entityList);
		return responseUtils.finalize(response, HttpStatus.OK);
	}

	/**
	 * Spring MVC controller utility method that updates a existing domain
	 * entity. Within a successful response, the following HATEOAS link(s)
	 * generated:
	 * <ul>
	 * <li>a <em>self</em> link pointing to at which this entity may be found</li>
	 * <li>a <em>collections</em> link giving access to all entities</li>
	 * </ul>
	 * 
	 * @param entityId
	 *            the identifier of the entity to be updated
	 * @param entity
	 *            the updated entity data
	 * @param entity
	 *            the entity to be updated
	 * @param entityClass
	 *            the class of the entity to be updated
	 * @param entityResponseClass
	 *            the class to use in generating the response
	 * @param controllerClass
	 *            the controller class used for generating HATEOAS links
	 * @param callback
	 *            a callback containing the actual logic to update the entity
	 * @return a Spring MVC response containing the updated entity
	 */
	public ResponseEntity<R> updateEntity(
			final Long entityId,
			final E entity,
			final Class<E> entityClass,
			final Class<R> entityResponseClass,
			final Class<?> controllerClass,
			final UpdateEntityCallback<E> callback) {

		final R response = responseUtils.createResponse(entityResponseClass);
		final E updatedEntity = callback.updateEntity(entityId, entity);
		response.setEntity(updatedEntity);

		response.add(linkTo(controllerClass).slash(updatedEntity).withSelfRel());
		response.add(linkTo(controllerClass).withRel(entityRel(entityClass)));

		return responseUtils.finalize(response, HttpStatus.OK);
	}

}
