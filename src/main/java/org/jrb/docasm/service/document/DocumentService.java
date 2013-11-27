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
package org.jrb.docasm.service.document;

import java.net.URI;
import java.util.List;

import org.jrb.docasm.domain.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Defines the contract for a service that manages documents and their assembly.
 * 
 * @author <a href="mailto:brulejr@gmail.com">Jon Brule</a>
 */
public interface DocumentService {

	Document createDocument(Document document) 
			throws DuplicateDocumentException, InvalidDocumentException, DocumentServiceException;

	Document createDocument(String name, URI templateUri, String description) 
			throws DuplicateDocumentException, InvalidDocumentException, DocumentServiceException;

	void deleteDocument(Long id)
			throws UnknownDocumentException, DocumentServiceException;

	void deleteDocument(String name)
			throws UnknownDocumentException, DocumentServiceException;

	Document findDocument(Long id)
			throws UnknownDocumentException, DocumentServiceException;

	Document findDocument(Long id, boolean forceRetrieve)
			throws UnknownDocumentException, DocumentServiceException;

	Document findDocument(String name)
			throws UnknownDocumentException, DocumentServiceException;

	Document findDocument(String name, boolean forceRetrieve)
			throws UnknownDocumentException, DocumentServiceException;

	List<Document> retrieveDocuments(Document criteria)
			throws DocumentServiceException;

	Page<Document> retrieveDocuments(Document criteria, Pageable pageable)
			throws DocumentServiceException;

	Document updateDocument(Document document)
			throws InvalidDocumentException, DocumentServiceException;

}
