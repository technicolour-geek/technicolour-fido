package com.hsecure.hancompass.demo.rp.controller;

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hsecure.hancompass.demo.rp.exception.RpErrorCodeInternal;
import com.hsecure.hancompass.demo.rp.exception.RpException;
import com.hsecure.hancompass.demo.rp.service.HttpMessage;

@RestController
public class RestRelay {
	private static final Logger logger = LoggerFactory.getLogger(RestRelay.class);

	@Value("${hancompass.server.url}")
	private String serverUrlValue;

	@Value("${hancompass.challenge.url}")
	private String challengeUrlValue;
	
	@Value("${hancompass.assertion.challenge.url}")
	private String assertionChallengeUrlValue;

	@Value("${hancompass.regist.url}")
	private String registUrlValue;

	@Value("${hancompass.auth.url}")
	private String authUrlValue;

	@Value("${hancompass.dereg.url}")
	private String deregUrlValue;

	@Value("${connect.timeout}")
	private String connectTimeoutValue;

	@Value("${read.timeout}")
	private String readTimeoutValue;
	
	@GetMapping("/hello")
	public String hello() {
		return "Hello, HancomPass";
	}

	@PostMapping("/challengeService/getCredentialsChallenge")
	public ResponseEntity<String> getCredentialsChallenge(@RequestBody String requestBody) {
		logger.debug("");
		logger.debug("******* RP getCredentialsChallenge START *******");
		logger.debug("<<<<<<< RP getCredentialsChallenge Request Body : " + requestBody);

		final String targetUrl = serverUrlValue + challengeUrlValue;
		URL hancomPassUrl = null;
		try {
			hancomPassUrl = new URL(targetUrl);
		} catch (MalformedURLException e) {
			String errorMsg = "Invalid HancomPass Server Url : [Server Url:\" + targetUrl + \"]";
			logger.debug(errorMsg);
			return new ResponseEntity<String>(errorMsg, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		ResponseEntity<String> response = sendMessage(hancomPassUrl, requestBody);

		logger.debug("");
		logger.debug(">>>>>> RP getCredentialsChallenge Response : [" + response.getBody() + "], [" + response.getStatusCodeValue()
				+ "] <<<<<<");
		logger.debug("******* RP getCredentialsChallenge END *******");

		return response;
	}
	
	@PostMapping("/challengeService/getAssertionChallenge")
	public ResponseEntity<String> getAssertionChallenge(@RequestBody String requestBody) {
		logger.debug("");
		logger.debug("******* RP getAssertionChallenge START *******");
		logger.debug("<<<<<<< RP getAssertionChallenge Request Body : " + requestBody);

		final String targetUrl = serverUrlValue + assertionChallengeUrlValue;
		URL hancomPassUrl = null;
		try {
			hancomPassUrl = new URL(targetUrl);
		} catch (MalformedURLException e) {
			String errorMsg = "Invalid HancomPass Server Url : [Server Url:\" + targetUrl + \"]";
			logger.debug(errorMsg);
			return new ResponseEntity<String>(errorMsg, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		ResponseEntity<String> response = sendMessage(hancomPassUrl, requestBody);

		logger.debug("");
		logger.debug(">>>>>> RP getAssertionChallenge Response : [" + response.getBody() + "], [" + response.getStatusCodeValue()
				+ "] <<<<<<");
		logger.debug("******* RP getAssertionChallenge END *******");

		return response;
	}
	
	@PostMapping("/fidoService/registCredential")
	public ResponseEntity<String> registCredential(@RequestBody String requestBody) {
		logger.debug("");
		logger.debug("******* RP registCredential START *******");
		logger.debug("<<<<<<< RP registCredential Request Body : " + requestBody);

		final String targetUrl = serverUrlValue + registUrlValue;
		URL hancomPassUrl = null;
		try {
			hancomPassUrl = new URL(targetUrl);
		} catch (MalformedURLException e) {
			String errorMsg = "Invalid HancomPass Server Url : [Server Url:\" + targetUrl + \"]";
			logger.debug(errorMsg);
			return new ResponseEntity<String>(errorMsg, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		ResponseEntity<String> response = sendMessage(hancomPassUrl, requestBody);

		logger.debug("");
		logger.debug(">>>>>> RP registCredential Response : [" + response.getBody() + "], [" + response.getStatusCodeValue()
				+ "] <<<<<<");
		logger.debug("******* RP registCredential END *******");

		return response;
	}
	
	@PostMapping("/fidoService/verifyAssertion")
	public ResponseEntity<String> verifyAssertion(@RequestBody String requestBody) {
		logger.debug("");
		logger.debug("******* RP verifyAssertion START *******");
		logger.debug("<<<<<<< RP verifyAssertion Request Body : " + requestBody);

		final String targetUrl = serverUrlValue + authUrlValue;
		URL hancomPassUrl = null;
		try {
			hancomPassUrl = new URL(targetUrl);
		} catch (MalformedURLException e) {
			String errorMsg = "Invalid HancomPass Server Url : [Server Url:\" + targetUrl + \"]";
			logger.debug(errorMsg);
			return new ResponseEntity<String>(errorMsg, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		ResponseEntity<String> response = sendMessage(hancomPassUrl, requestBody);

		logger.debug("");
		logger.debug(">>>>>> RP verifyAssertion Response : [" + response.getBody() + "], [" + response.getStatusCodeValue()
				+ "] <<<<<<");
		logger.debug("******* RP verifyAssertion END *******");

		return response;
	}
	
	@PostMapping("/DeregistrationService/deregistration")
	public ResponseEntity<String> deregist(@RequestBody String requestBody) {
		logger.debug("");
		logger.debug("******* RP deregist START *******");
		logger.debug("<<<<<<< RP deregist Request Body : " + requestBody);

		final String targetUrl = serverUrlValue + deregUrlValue;
		URL hancomPassUrl = null;
		try {
			hancomPassUrl = new URL(targetUrl);
		} catch (MalformedURLException e) {
			String errorMsg = "Invalid HancomPass Server Url : [Server Url:\" + targetUrl + \"]";
			logger.debug(errorMsg);
			return new ResponseEntity<String>(errorMsg, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		ResponseEntity<String> response = sendMessage(hancomPassUrl, requestBody);

		logger.debug("");
		logger.debug(">>>>>> RP deregist Response : [" + response.getBody() + "], [" + response.getStatusCodeValue()
				+ "] <<<<<<");
		logger.debug("******* RP deregist END *******");

		return response;
	}

	private ResponseEntity<String> sendMessage(URL targetUrl, String message) {
		logger.debug("");
		logger.debug("== sendMessage START ==");

		int connectTimeout = Integer.parseInt(connectTimeoutValue);
		int readTimeout = Integer.parseInt(connectTimeoutValue);

		HttpMessage httpMessage = new HttpMessage(targetUrl, connectTimeout, readTimeout);

		String response = null;
		try {
			response = httpMessage.responseString(httpMessage.sendStreamMessage(null, message));
		} catch (RpException e) {
			logger.debug(e.getErrorMessage(), e.getErrorCode());
			return new ResponseEntity<String>("Code:" + e.getErrorCode() + ", Msg:" + e.getErrorMessage(),
					HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			String errorMsg = "Unknown Error(" + e.getMessage() + ")";
			logger.debug(errorMsg);
			return new ResponseEntity<String>(errorMsg, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		logger.debug("");
		logger.debug("sendMessage END");

		return new ResponseEntity<String>(response, HttpStatus.OK);
	}
}
