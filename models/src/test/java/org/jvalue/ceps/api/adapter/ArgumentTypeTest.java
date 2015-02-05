package org.jvalue.ceps.api.adapter;


import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Assert;
import org.junit.Test;

public final class ArgumentTypeTest {

	@Test
	public void testJson() throws Exception {
		ObjectMapper mapper = new ObjectMapper();

		Assert.assertEquals(ArgumentType.BOOLEAN, mapper.treeToValue(mapper.valueToTree(ArgumentType.BOOLEAN), ArgumentType.class));
		Assert.assertEquals(ArgumentType.NUMBER, mapper.treeToValue(mapper.valueToTree(ArgumentType.NUMBER), ArgumentType.class));
		Assert.assertEquals(ArgumentType.STRING, mapper.treeToValue(mapper.valueToTree(ArgumentType.STRING), ArgumentType.class));
	}

}
