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
package org.jrb.docasm.web.controller;

import java.util.List;

import javax.annotation.PostConstruct;

import org.jrb.commons.web.MessageResponse;
import org.jrb.commons.web.ResponseUtils;
import org.jrb.commons.web.controller.CrudControllerUtils;
import org.jrb.commons.web.controller.CrudControllerUtils.CreateEntityCallback;
import org.jrb.commons.web.controller.CrudControllerUtils.DeleteEntityCallback;
import org.jrb.commons.web.controller.CrudControllerUtils.FindEntityCallback;
import org.jrb.commons.web.controller.CrudControllerUtils.RetrieveEntitiesCallback;
import org.jrb.commons.web.controller.CrudControllerUtils.UpdateEntityCallback;
import org.jrb.docasm.domain.Document;
import org.jrb.docasm.service.document.DocumentService;
import org.jrb.docasm.service.document.DocumentServiceException;
import org.jrb.docasm.service.document.DuplicateDocumentException;
import org.jrb.docasm.service.document.InvalidDocumentException;
import org.jrb.docasm.service.document.UnknownDocumentException;
import org.jrb.docasm.web.response.DocumentListResponse;
import org.jrb.docasm.web.response.DocumentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * RESTful API for managing {@link Document} entities and their actions.
 * 
 * @author <a href="mailto:brulejr@gmail.com">Jon Brule</a>
 */
@Controller
@RequestMapping("/api/document")
public class DocumentController {

	@Autowired
	private DocumentService documentService;

	@Autowired
	private ResponseUtils responseUtils;

	private CrudControllerUtils<Document, DocumentResponse, DocumentListResponse> controllerUtils;

	@PostConstruct
	public void init() {
		this.controllerUtils =
				new CrudControllerUtils<Document, DocumentResponse, DocumentListResponse>(responseUtils);
	}

	/**
	 * RESTful CRUD endpoint to create a document.
	 * 
	 * @param document
	 *            the document to be created.
	 * @return a Spring MVC response containing the newly-created document
	 * @throws DuplicateDocumentException
	 *             if attempt made to create a document that already exists
	 * @throws InvalidDocumentException
	 *             if the document to be created does not pass the established
	 *             validation rules
	 * @throws DocumentServiceException
	 *             if an unexpected error occurred while creating a document
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<DocumentResponse> createDocument(@RequestBody final Document document)
			throws DuplicateDocumentException, InvalidDocumentException, DocumentServiceException {

		return controllerUtils.createEntity(
				document,
				Document.class,
				DocumentResponse.class,
				getClass(),
				new CreateEntityCallback<Document>() {
					@Override
					public Document createEntity(Document submitted) {
						return documentService.createDocument(submitted);
					}
				});
	}

	/**
	 * RESTful CRUD endpoint to create a document.
	 * 
	 * @param documentId
	 *            the identifier of the document to be deleted
	 * @return a Spring MVC response containing the deletion status
	 * @throws UnknownDocumentException
	 *             if attempt made to delete an unregistered document
	 * @throws DocumentServiceException
	 *             if an unexpected error occurred while deleting a document
	 */
	@RequestMapping(value = "{documentId}", method = RequestMethod.DELETE)
	public ResponseEntity<MessageResponse> deleteDocument(@PathVariable final Long documentId)
			throws UnknownDocumentException, DocumentServiceException {

		return controllerUtils.deleteEntity(
				documentId,
				Document.class,
				DocumentResponse.class,
				DocumentController.class,
				new DeleteEntityCallback<Document>() {
					@Override
					public void deleteEntity(Long entityId) {
						documentService.deleteDocument(documentId);
					}
				});
	}

	/**
	 * RESTful CRUD endpoint to find an existing document.
	 * 
	 * @param documentId
	 *            the identifier of the desired document
	 * @return a Spring MVC response containing the found entity
	 * @throws UnknownDocumentException
	 *             if attempt made to locate an unregistered document
	 * @throws DocumentServiceException
	 *             if an unexpected error occurred while finding a document
	 */
	@RequestMapping(value = "{documentId}", method = RequestMethod.GET)
	public ResponseEntity<DocumentResponse> findEntity(@PathVariable final Long documentId)
			throws UnknownDocumentException, DocumentServiceException {

		return controllerUtils.findEntity(
				documentId,
				Document.class,
				DocumentResponse.class,
				DocumentController.class,
				new FindEntityCallback<Document>() {
					@Override
					public Document findEntity(Long entityId) {
						return documentService.findDocument(documentId);
					}
				});
	}

	/**
	 * RESTful CRUD endpoint to retrieve existing documents.
	 * 
	 * @return a Spring MVC response containing the entity list
	 * @throws DocumentServiceException
	 *             if an unexpected error occurred while retrieving the
	 *             documents
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<DocumentListResponse> retrieveDocuments() throws DocumentServiceException {

		return controllerUtils.retrieveEntities(
				DocumentListResponse.class,
				new RetrieveEntitiesCallback<Document>() {
					@Override
					public List<Document> retrieveEntities() {
						return documentService.retrieveDocuments(null);
					}
				});

	}

	/**
	 * RESTful CRUD endpoint to update an existing document.
	 * 
	 * @param documentId
	 *            the identifier of the document to be updated
	 * @param document
	 *            the document updates
	 * @return a Spring MVC response containing the updated document
	 * @throws InvalidDocumentException
	 *             if the document updates do not pass the established
	 *             validation rules
	 * @throws UnknownDocumentException
	 *             if attempt made to updated an unregistered document
	 * @throws DocumentServiceException
	 *             if an unexpected error occurred while updating a document
	 */
	@RequestMapping(value = "{documentId}", method = RequestMethod.PATCH)
	public ResponseEntity<DocumentResponse> updateDocument(
			@PathVariable final Long documentId,
			@RequestBody final Document document)
			throws InvalidDocumentException, UnknownDocumentException, DocumentServiceException {
		return controllerUtils.updateEntity(
				documentId,
				document,
				Document.class,
				DocumentResponse.class,
				DocumentController.class,
				new UpdateEntityCallback<Document>() {
					@Override
					public Document updateEntity(Long entityId, Document submitted) {
						final Document existing = documentService.findDocument(documentId);
						final Document.Builder builder = new Document.Builder().from(existing);
						if (controllerUtils.different(submitted.getName(), existing.getName())) {
							builder.setName(submitted.getName());
						}
						if (controllerUtils.different(submitted.getTemplateUri(), existing.getTemplateUri())) {
							builder.setTemplateUri(submitted.getTemplateUri());
						}
						if (controllerUtils.different(submitted.getDescription(), existing.getDescription())) {
							builder.setDescription(submitted.getDescription());
						}
						return documentService.updateDocument(builder.build());
					}
				});
	}

}
