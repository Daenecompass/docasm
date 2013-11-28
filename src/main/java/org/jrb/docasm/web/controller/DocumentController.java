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

import org.jrb.commons.web.controller.AbstractCrudController;
import org.jrb.docasm.domain.Document;
import org.jrb.docasm.service.document.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Provides handling for the application root URI.
 * 
 * @author <a href="mailto:brulejr@gmail.com">Jon Brule</a>
 */
@Controller
@RequestMapping("/api/document")
public class DocumentController extends AbstractCrudController<Document> {

	@Autowired
	private DocumentService documentService;

	@Override
	protected Class<Document> entityClass() {
		return Document.class;
	}

	@Override
	protected Class<DocumentResponse> entityResponseClass() {
		return DocumentResponse.class;
	}

	@Override
	protected Class<DocumentListResponse> entityListResponseClass() {
		return DocumentListResponse.class;
	}

	@Override
	protected Document createEntityCallback(Document submitted) {
		return documentService.createDocument(submitted);
	}

	@Override
	protected void deleteEntityCallback(Long documentId) {
		documentService.deleteDocument(documentId);
	}

	@Override
	protected Document findEntityCallback(Long documentId) {
		return documentService.findDocument(documentId);
	}

	@Override
	protected List<Document> retrieveEntitiesCallback() {
		return documentService.retrieveDocuments(null);
	}

	@Override
	protected Document updateEntityCallback(Long documentId, Document submitted) {
		final Document existing = documentService.findDocument(documentId);
		final Document.Builder builder = new Document.Builder().from(existing);
		if (different(submitted.getName(), existing.getName())) {
			builder.setName(submitted.getName());
		}
		if (different(submitted.getTemplateUri(), existing.getTemplateUri())) {
			builder.setTemplateUri(submitted.getTemplateUri());
		}
		if (different(submitted.getDescription(), existing.getDescription())) {
			builder.setDescription(submitted.getDescription());
		}
		return documentService.updateDocument(builder.build());		
	}

}
