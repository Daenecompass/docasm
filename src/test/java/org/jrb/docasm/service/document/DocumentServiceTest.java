package org.jrb.docasm.service.document;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.jrb.docasm.Application;
import org.jrb.docasm.domain.Document;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Unit test cases for {@link DocumentServiceImpl}.
 * 
 * @author <a href="mailto:brulejr@gmail.com">Jon Brule</a>
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@ActiveProfiles("LOCAL")
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DocumentServiceTest {

	private final static Logger LOG = LoggerFactory.getLogger(DocumentServiceTest.class);

	private final static Resource TEMPLATE1 = new ClassPathResource("templates/test.json");
	private final static Resource TEMPLATE2 = new ClassPathResource("templates/test.xml");

	@Autowired
	private DocumentService documentService;
	
	private Document createDocument(final String name, final URI templateUri, final String description) throws IOException {
		final Document document = documentService.createDocument(name, templateUri, description);
		assertNotNull(document);
		assertNotNull(document.getId());
		assertEquals(name, document.getName());
		assertEquals(templateUri, document.getTemplateUri());
		assertEquals(description, document.getDescription());
		assertNotNull(document.getCreatedOn());
		assertNotNull(document.getUpdatedOn());
		return document;
	}
	
	@Test
	public void test01_CreateDocuments() {
		LOG.info("BEGIN: test01_CreateDocuments()");
		try {

			// create first document
			createDocument("DOC_1", TEMPLATE1.getURI(), "This is document #1");

			// create second document
			createDocument("DOC_2", TEMPLATE2.getURI(), "This is document #2");

			// create second document
			createDocument("DOC_3", TEMPLATE1.getURI(), "This is document #3");

			// create second document
			createDocument("DOC_4", TEMPLATE2.getURI(), "This is document #4");

			// attempt to recreate first document
			try {
				documentService.createDocument("DOC_1", TEMPLATE1.getURI(), "This is a dupe");
				fail("Unsuccessfully created a duplicate document!");
			} catch (final DuplicateDocumentException e) {
			}

		} catch (Throwable t) {
			LOG.error(t.getMessage(), t);
			fail(t.getMessage());
		}
		LOG.info("END: test01_CreateDocuments()");
	}

	@Test
	public void test02_FindDocuments() {
		LOG.info("BEGIN: test02_FindDocuments()");
		try {

			// find first document
			final Document document = documentService.findDocument("DOC_1");
			assertNotNull(document);
			assertNotNull(document.getId());
			assertEquals("DOC_1", document.getName());
			assertEquals(TEMPLATE1.getURI(), document.getTemplateUri());
			assertNotNull(document.getTemplate());
			assertEquals("This is document #1", document.getDescription());
			assertNotNull(document.getCreatedOn());
			assertNotNull(document.getUpdatedOn());

			try {
				documentService.findDocument("DOC_X");
				fail("Unsuccessfully found an unknown document!");
			} catch (final UnknownDocumentException e) {
			}

		} catch (Throwable t) {
			LOG.error(t.getMessage(), t);
			fail(t.getMessage());
		}
		LOG.info("END: test02_FindDocuments()");
	}

	@Test
	public void test03_RetrieveDocuments() {
		LOG.info("BEGIN: test03_RetrieveDocuments()");
		try {

			// find first document
			final List<Document> documents = documentService.retrieveDocuments(null);
			assertNotNull(documents);
			assertEquals(4, documents.size());

		} catch (Throwable t) {
			LOG.error(t.getMessage(), t);
			fail(t.getMessage());
		}
		LOG.info("END: test03_RetrieveDocuments()");
	}

	@Test
	public void test04_DeleteDocuments() {
		LOG.info("BEGIN: test04_DeleteDocuments()");
		try {

			// delete existing document
			documentService.deleteDocument("DOC_1");
			assertEquals(3, documentService.retrieveDocuments(null).size());

			// attempt to delete non-existing document
			try {
				documentService.deleteDocument("DOC_X");
				fail("Unsuccessfully deleted an unknown document!");
			} catch (final UnknownDocumentException e) {
			}

		} catch (Throwable t) {
			LOG.error(t.getMessage(), t);
			fail(t.getMessage());
		}
		LOG.info("END: test04_DeleteDocuments()");
	}

}
