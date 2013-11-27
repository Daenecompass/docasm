package org.jrb.docasm.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.jrb.docasm.domain.Document;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocumentTest {

	private final static Logger LOG = LoggerFactory.getLogger(DocumentTest.class);

	@Test
	public void test(){
		LOG.info("BEGIN: test()");
		try {

			final Document doc1 = new Document.Builder()
				.setName("ABC")
				.markAsCreated()
				.build();
			assertNotNull(doc1);
			assertEquals("ABC", doc1.getName());
			assertNull(doc1.getDescription());
			assertNotNull(doc1.getCreatedOn());
			assertNotNull(doc1.getUpdatedOn());

			final Document doc2 = new Document.Builder()
				.setName("DEF")
				.setDescription("Manifest #2")
				.markAsCreated()
				.build();
			assertNotNull(doc2);
			assertEquals("DEF", doc2.getName());
			assertEquals("Manifest #2", doc2.getDescription());
			assertNotNull(doc2.getCreatedOn());
			assertNotNull(doc2.getUpdatedOn());

			final Document doc3 = new Document.Builder()
				.from(doc2)
				.build();
			assertNotNull(doc3);
			assertEquals("DEF", doc3.getName());
			assertEquals("Manifest #2", doc3.getDescription());
			assertNotNull(doc3.getCreatedOn());
			assertNotNull(doc3.getUpdatedOn());

		} catch (Throwable t) {
			LOG.error(t.getMessage(), t);
			fail(t.getMessage());
		}
		LOG.info("END: test()");
	}

}
