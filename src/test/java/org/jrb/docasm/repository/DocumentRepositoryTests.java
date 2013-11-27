package org.jrb.docasm.repository;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.jrb.docasm.Application;
import org.jrb.docasm.domain.Document;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DocumentRepositoryTests {

	private final static Logger LOG = LoggerFactory.getLogger(DocumentRepositoryTests.class);

	@Autowired
	private DocumentRepository repository;

	private void createDocument(final String name, final boolean enabled, final String description) throws IOException {
		final Resource templateUri = new ClassPathResource("templates/test.json");
		final Document document = new Document.Builder()
				.setName(name)
				.setTemplateUri(templateUri.getURI())
				.setDescription(description)
				.markAsCreated()
				.build();
		repository.save(document);
	}

	@Test
	public void test01_createDocuments() {
		LOG.info("BEGIN: test01_createDocuments()");
		try {

			createDocument("ABC", true, "Test1");
			createDocument("DEF", true, "Test2");
			createDocument("GHI", false, "Test3");
			createDocument("JKL", true, "Test4");
			createDocument("MNO", false, "Test5");

		} catch (Throwable t) {
			LOG.error(t.getMessage(), t);
			fail(t.getMessage());
		}
		LOG.info("END: test01_createDocuments()");
	}

	@Test
	public void test02_findsDocuments() {
		LOG.info("BEGIN: test02_findsDocuments()");
		try {

			final Document domain1 = repository.findByName("ABC");
			assertNotNull(domain1);
			assertNotNull(domain1.getId());
			assertEquals("ABC", domain1.getName());
			assertEquals("Test1", domain1.getDescription());
			assertNotNull(domain1.getCreatedOn());
			assertNotNull(domain1.getUpdatedOn());

		} catch (Throwable t) {
			LOG.error(t.getMessage(), t);
			fail(t.getMessage());
		}
		LOG.info("END: test02_findsDocuments()");
	}

	@Test(expected = DataIntegrityViolationException.class)
	public void test03_duplicateDocument() {
		LOG.info("BEGIN: test02_findsDocuments()");
		try {

			createDocument("ABC", true, "TestA");

		} catch (DataIntegrityViolationException e) {
			throw e;
		} catch (Throwable t) {
			LOG.error(t.getMessage(), t);
			fail(t.getMessage());
		}
		LOG.info("END: test03_duplicateDocument()");
	}

	@Test
	public void test04_retrieveDocuments() {
		LOG.info("BEGIN: test04_retrieveDocuments()");
		try {

			final Page<Document> allDocuments = repository.findAll(new PageRequest(0, 10));
			assertThat(allDocuments.getTotalElements(), is(5L));
			assertThat(allDocuments.getNumberOfElements(), is(5));

		} catch (Throwable t) {
			LOG.error(t.getMessage(), t);
			fail(t.getMessage());
		}
		LOG.info("END: test04_retrieveDocuments()");
	}
	
}
