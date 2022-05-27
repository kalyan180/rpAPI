package com.benchmarkuniverse.rp.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.benchmarkuniverse.rp.exception.UnAuthorizedException;
public class HelloRestControllerTest extends BaseControllerTest {

	@Test
	public void validWithDifferentValidParam() throws Exception {
		String overtimeURL = api_version+"hello";

		ResultActions resultActions = performBasicGet(overtimeURL);
		resultActions.andExpect(MockMvcResultMatchers.status().is(200)).andExpect(status().isOk());

	}

	@Test(expected = UnAuthorizedException.class)
	public void validWithValidParamAndInvalidAuthorizationHeader() throws Exception {
		String overtimeURL = api_version+"hello";

		performBasicGetWithInvalidAuthHeader(overtimeURL);
	}


}
