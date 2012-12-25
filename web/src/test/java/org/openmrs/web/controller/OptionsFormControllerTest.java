package org.openmrs.web.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.net.BindException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.GlobalProperty;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.LoginCredential;
import org.openmrs.api.db.UserDAO;
import org.openmrs.util.OpenmrsConstants;
import org.openmrs.web.test.BaseWebContextSensitiveTest;
import org.openmrs.web.test.WebTestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class OptionsFormControllerTest extends BaseWebContextSensitiveTest {
	
	private User user;
	
	private UserDAO userDao;
	
	private OptionsFormController controller;
	
	@Autowired
	private WebTestHelper testHelper;
	
	@Before
	public void setUp() {
		Context.authenticate("admin", "test");
		user = Context.getAuthenticatedUser();
		controller = (OptionsFormController) applicationContext.getBean("optionsForm");
		userDao = (UserDAO) applicationContext.getBean("userDAO");
	}
	
	@Test
	public void shouldChangeSecretQuestionAndAnswer() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest("POST", "");
		request.setParameter("secretQuestionPassword", "test");
		request.setParameter("secretQuestionNew", "test_question");
		
		String answer = "test_answer";
		request.setParameter("secretAnswerNew", answer);
		request.setParameter("secretAnswerConfirm", answer);
		
		HttpServletResponse response = new MockHttpServletResponse();
		controller.handleRequest(request, response);
		
		LoginCredential loginCredential = userDao.getLoginCredential(user);
		assertEquals(answer, loginCredential.getSecretAnswer());
	}
	
	@Test
	public void shouldRejectEmptySecretAnswer() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest("POST", "");
		request.setParameter("secretQuestionPassword", "test");
		request.setParameter("secretQuestionNew", "test_question");
		
		String emptyAnswer = "";
		request.setParameter("secretAnswerNew", emptyAnswer);
		request.setParameter("secretAnswerConfirm", emptyAnswer);
		
		HttpServletResponse response = new MockHttpServletResponse();
		controller.handleRequest(request, response);
		
		LoginCredential loginCredential = userDao.getLoginCredential(user);
		assertNull(loginCredential.getSecretAnswer());
	}
	
	@Test
	public void shouldRejectEmptySecretQuestion() throws Exception {
		LoginCredential loginCredential = userDao.getLoginCredential(user);
		String originalQuestion = loginCredential.getSecretQuestion();
		
		MockHttpServletRequest request = new MockHttpServletRequest("POST", "");
		request.setParameter("secretQuestionPassword", "test");
		request.setParameter("secretQuestionNew", "");
		
		String emptyAnswer = "test_answer";
		request.setParameter("secretAnswerNew", emptyAnswer);
		request.setParameter("secretAnswerConfirm", emptyAnswer);
		
		HttpServletResponse response = new MockHttpServletResponse();
		controller.handleRequest(request, response);
		
		loginCredential = userDao.getLoginCredential(user);
		assertEquals(originalQuestion, loginCredential.getSecretQuestion());
	}
	
	/**
	 * @see OptionsFormController#onSubmit(HttpServletRequest,HttpServletResponse,Object,BindException)
	 * @verifies accept email address as username if enabled
	 */
	@Test
	public void onSubmit_shouldAcceptEmailAddressAsUsernameIfEnabled() throws Exception {
		//given
		Context.getAdministrationService().saveGlobalProperty(
		    new GlobalProperty(OpenmrsConstants.GLOBAL_PROPERTY_USER_REQUIRE_EMAIL_AS_USERNAME, "true"));
		MockHttpServletRequest post = testHelper.newPOST("/options.form");
		post.addParameter("username", "ab@gmail.com");
		
		//when
		testHelper.handle(post);
		
		//then
		Assert.assertThat("ab@gmail.com", is(Context.getAuthenticatedUser().getUsername()));
	}
	
	/**
	 * @see OptionsFormController#onSubmit(HttpServletRequest,HttpServletResponse,Object,BindException)
	 * @verifies reject invalid email address as username if enabled
	 */
	@Test
	public void onSubmit_shouldRejectInvalidEmailAddressAsUsernameIfEnabled() throws Exception {
		//given
		Context.getAdministrationService().saveGlobalProperty(
		    new GlobalProperty(OpenmrsConstants.GLOBAL_PROPERTY_USER_REQUIRE_EMAIL_AS_USERNAME, "true"));
		MockHttpServletRequest post = testHelper.newPOST("/options.form");
		post.addParameter("username", "ab@");
		
		//when
		testHelper.handle(post);
		
		//then
		Assert.assertThat("ab@", is(not(Context.getAuthenticatedUser().getUsername())));
	}
	
	/**
	 * @see OptionsFormController#onSubmit(HttpServletRequest,HttpServletResponse,Object,BindException)
	 * @verifies accept 2 characters as username
	 */
	@Test
	public void onSubmit_shouldAccept2CharactersAsUsername() throws Exception {
		//given
		MockHttpServletRequest post = testHelper.newPOST("/options.form");
		post.addParameter("username", "ab");
		
		//when
		testHelper.handle(post);
		
		//then
		Assert.assertThat("ab", is(Context.getAuthenticatedUser().getUsername()));
	}
	
	/**
	 * @see OptionsFormController#onSubmit(HttpServletRequest,HttpServletResponse,Object,BindException)
	 * @verifies reject 1 character as username
	 */
	@Test
	public void onSubmit_shouldReject1CharacterAsUsername() throws Exception {
		//given
		MockHttpServletRequest post = testHelper.newPOST("/options.form");
		post.addParameter("username", "a");
		
		//when
		testHelper.handle(post);
		
		//then
		Assert.assertThat("a", is(not(Context.getAuthenticatedUser().getUsername())));
	}
}
