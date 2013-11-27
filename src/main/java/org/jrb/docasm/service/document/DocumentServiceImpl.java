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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jrb.docasm.domain.Document;
import org.jrb.docasm.repository.DocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheLoader.InvalidCacheLoadException;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;

/**
 * JPA implementation of a {@link DocumentService}.
 * 
 * @author <a href="mailto:brulejr@gmail.com">Jon Brule</a>
 */
@Service("documentService")
@Transactional
public class DocumentServiceImpl implements DocumentService {

	private final static Logger LOG = LoggerFactory.getLogger(DocumentService.class);
	
	@Autowired
	private DocumentRepository documentRepository;
	
	private int maxCacheSize = 1000;

	private LoadingCache<NamedKey, Document> documentCache = CacheBuilder.newBuilder()
			.maximumSize(maxCacheSize)
			.build(new CacheLoader<NamedKey, Document>() {
				@Override
				public Document load(final NamedKey key) throws DocumentServiceException {
					try {
						return loadDocument(key);
					} catch (Throwable t) {
						LOG.error(t.getMessage(), t);
						throw new DocumentServiceException(t.getMessage(), t);
					}
				}
			});
	
	@Override
	public Document createDocument(final Document submitted)
			throws DuplicateDocumentException, InvalidDocumentException, DocumentServiceException {
		try {
			final Document document = new Document.Builder()
					.setName(submitted.getName())
					.setTemplateUri(submitted.getTemplateUri())
					.setDescription(submitted.getDescription())
					.markAsCreated().build();
			return documentRepository.save(document);
		} catch (final DataIntegrityViolationException e) {
			throw new DuplicateDocumentException("Duplicate document! submitted = " + submitted, e);
		} catch (final IllegalArgumentException e) {
			throw new InvalidDocumentException("Invalid document! submitted = " + submitted, e);
		} catch (final Throwable t) {
			throw new DocumentServiceException("Unable to create document! submitted = " + submitted, t);
		}
	}
	
	@Override
	public Document createDocument(final String name, final URI templateUri, final String description)
			throws DuplicateDocumentException, InvalidDocumentException, DocumentServiceException {
		try {
			final Document document = new Document.Builder()
					.setName(name)
					.setTemplateUri(templateUri)
					.setDescription(description)
					.markAsCreated().build();
			return documentRepository.save(document);
		} catch (final DataIntegrityViolationException e) {
			throw new DuplicateDocumentException("Duplicate document! name = " + name, e);
		} catch (final IllegalArgumentException e) {
			throw new InvalidDocumentException("Invalid document! name = " + name, e);
		} catch (final Throwable t) {
			throw new DocumentServiceException("Unable to create document! name = " + name, t);
		}
	}
	@Override
	public void deleteDocument(final Long id) throws UnknownDocumentException, DocumentServiceException {
		try {
			final Document document = documentRepository.findOne(id);
			if (document != null) {
				documentRepository.delete(document);
				documentCache.invalidate(document);
			} else {
				throw new UnknownDocumentException("Document is unknown! id = " + id);
			}
		} catch (final UnknownDocumentException e) {
			throw e;
		} catch (final Throwable t) {
			throw new DocumentServiceException("Unable to delete document! id = " + id, t);
		}
	}

	@Override
	public void deleteDocument(final String name) throws UnknownDocumentException, DocumentServiceException {
		try {
			final Document document = documentRepository.findByName(name);
			if (document != null) {
				documentRepository.delete(document);
				documentCache.invalidate(document);
			} else {
				throw new UnknownDocumentException("Document is unknown! name = " + name);
			}
		} catch (final UnknownDocumentException e) {
			throw e;
		} catch (final Throwable t) {
			throw new DocumentServiceException("Unable to delete document! name = " + name, t);
		}
	}

	@Override
	public Document findDocument(final Long id) throws UnknownDocumentException, DocumentServiceException {
		try {
			return documentCache.get(new NamedKey(id));
		} catch(final InvalidCacheLoadException e) {
			throw new UnknownDocumentException("Document is unknown! id = " + id, e);
		} catch (final Throwable t) {
			throw new DocumentServiceException("Unable to find document! id = " + id, t);
		}
	}

	@Override
	public Document findDocument(final Long id, final boolean forceRetrieve) 
			throws UnknownDocumentException, DocumentServiceException {
		try {
			return documentCache.getIfPresent(new NamedKey(id));
		} catch(final InvalidCacheLoadException e) {
			throw new UnknownDocumentException("Document is unknown! id = " + id, e);
		} catch (final Throwable t) {
			throw new DocumentServiceException("Unable to find document! id = " + id, t);
		}
	}

	@Override
	public Document findDocument(final String name) throws UnknownDocumentException, DocumentServiceException {
		try {
			return documentCache.get(new NamedKey(name));
		} catch(final InvalidCacheLoadException e) {
			throw new UnknownDocumentException("Document is unknown! name = " + name, e);
		} catch (final Throwable t) {
			throw new DocumentServiceException("Unable to find document! name = " + name, t);
		}
	}

	@Override
	public Document findDocument(final String name, final boolean forceRetrieve) 
			throws UnknownDocumentException, DocumentServiceException {
		try {
			return documentCache.getIfPresent(new NamedKey(name));
		} catch(final InvalidCacheLoadException e) {
			throw new UnknownDocumentException("Document is unknown! name = " + name, e);
		} catch (final Throwable t) {
			throw new DocumentServiceException("Unable to find document! name = " + name, t);
		}
	}

	private Document loadDocument(final NamedKey key) throws IOException {
		final Document document = (key.hasId()) 
				? documentRepository.findOne(key.getId())
						: documentRepository.findByName(key.getName());
		if (document != null) {
			final URI templateUri = document.getTemplateUri();
			try (final InputStream is = templateUri.toURL().openStream()) {
				document.setTemplate(IOUtils.toString(is));
			}
		}
		return document;
	}
	
	@Override
	public List<Document> retrieveDocuments(final Document criteria) throws DocumentServiceException {
		try {
			return Lists.newArrayList(documentRepository.findAll());
		} catch (final Throwable t) {
			throw new DocumentServiceException("Unable to retrieve documents! criteria = " + criteria, t);
		}
	}
	
	@Override
	public Page<Document> retrieveDocuments(final Document criteria, Pageable pageable) throws DocumentServiceException {
		try {
			return documentRepository.findAll(pageable);
		} catch (final Throwable t) {
			throw new DocumentServiceException("Unable to retrieve documents! criteria = " + criteria, t);
		}
	}

	public void setMaxCacheSize(final int maxCacheSize) {
		this.maxCacheSize = maxCacheSize;
	}
	
	@Override
	public Document updateDocument(final Document document) throws InvalidDocumentException, DocumentServiceException {
		try {
			final Document updated = new Document.Builder()
				.from(documentRepository.findOne(document.getId()))
				.setName(document.getName())
				.setTemplateUri(document.getTemplateUri())
				.setDescription(document.getDescription())
				.markAsUpdated()
				.build();
			return documentRepository.save(updated);
		} catch (final IllegalArgumentException e) {
			throw new InvalidDocumentException("Invalid document! document = " + document, e);
		} catch (final Throwable t) {
			throw new DocumentServiceException("Unable to create document! document = " + document, t);
		}
	}

}
