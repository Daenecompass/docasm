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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Composite key of a name and an identifier.
 * 
 * @author <a href="mailto:brulejr@gmail.com">Jon Brule</a>
 */
public class NamedKey {

	private final String name;
	private final Long id;

	public NamedKey(final String name) {
		this(name, null);
	}

	public NamedKey(final Long id) {
		this(null, id);
	}

	public NamedKey(final String name, final Long id) {
		this.name = name;
		this.id = id;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		final NamedKey rhs = (NamedKey) obj;
		return new EqualsBuilder()
				.append(name, rhs.name)
				.append(id, rhs.id)
				.build();
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public boolean hasId() {
		return id != null;
	}
	
	public boolean hasName() {
		return name != null;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31)
				.append(name)
				.append(id)
				.build();
	}

	@Override
	public String toString() {
		return "NamedKey [name=" + name + ", id=" + id + "]";
	}

}
