package com.hsecure.hancompass.demo.rp.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exception 클래스
 * 
 * @author haspori
 * @since 2016.10.10
 */
public class RpException extends Exception {
	private static final long serialVersionUID = 1L;

	protected Logger mLogger = LoggerFactory.getLogger(getClass());

	private int mErrorCode = 0;
	private String mErrorMessage = null;
	private String mStackTrace = null;

	public RpException() {

	}

	/**
	 * Exception 생성자
	 * 
	 * @author haspori
	 * @param int       pErrorCode
	 * @param Exception pException
	 */
	public RpException(int pErrorCode, Exception pException) {
		super(pException);

		StringWriter aStackTraceStringWriter = null;

		/*********************************************************************/

		this.mErrorCode = pErrorCode;

		if ((this.mErrorCode - 1000) > 0) {
			this.mErrorMessage = RpErrorCodeInternal.getErrorMessage(this.mErrorCode);
		} else {
			this.mErrorMessage = RpErrorCodeUAF.getErrorMessage(this.mErrorCode);
		}

		aStackTraceStringWriter = new StringWriter();
		pException.printStackTrace(new PrintWriter(aStackTraceStringWriter));
		this.mStackTrace = aStackTraceStringWriter.toString();

		/*********************************************************************/
	}

	/**
	 * Exception 생성자
	 * 
	 * @author haspori
	 * @param int pErrorCode
	 */
	public RpException(int pErrorCode) {
		StringWriter aStackTraceStringWriter = null;

		/*********************************************************************/

		this.mErrorCode = pErrorCode;

		if ((this.mErrorCode - 1000) > 0) {
			this.mErrorMessage = RpErrorCodeInternal.getErrorMessage(this.mErrorCode);
		} else {
			this.mErrorMessage = RpErrorCodeUAF.getErrorMessage(this.mErrorCode);
		}

		aStackTraceStringWriter = new StringWriter();
		this.printStackTrace(new PrintWriter(aStackTraceStringWriter));
		this.mStackTrace = aStackTraceStringWriter.toString();

		/*********************************************************************/
	}

	/**
	 * Exception 생성자
	 * 
	 * @author haspori
	 * @param int       pErrorCode
	 * @param int       pInternalErrorCode
	 * @param Exception pException
	 */
	public RpException(int pErrorCode, int pInternalErrorCode, Exception pException) {
		super(pException);

		StringWriter aStackTraceStringWriter = null;
		String aErrorMessage = null;

		/*********************************************************************/

		this.mErrorCode = pErrorCode;

		/*-----------------------------------------------------------------------
		 * 1. RP 서버 내부 에러일 경우 ErrorMessage 정의
		 *  [InternalErrorCode] InternalErrorMessage
		 *-----------------------------------------------------------------------*/
		if ((this.mErrorCode - 1000) > 0) {
			aErrorMessage = RpErrorCodeInternal.getErrorMessage(this.mErrorCode);
		} else {
			aErrorMessage = RpErrorCodeUAF.getErrorMessage(this.mErrorCode);
		}

		this.mErrorMessage = "[" + pInternalErrorCode + "] " + aErrorMessage;

		aStackTraceStringWriter = new StringWriter();
		pException.printStackTrace(new PrintWriter(aStackTraceStringWriter));
		this.mStackTrace = aStackTraceStringWriter.toString();

		/*********************************************************************/
	}

	/**
	 * Exception 생성자
	 * 
	 * @author haspori
	 * @param int pErrorCode
	 * @param int pInternalErrorCode
	 */
	public RpException(int pErrorCode, int pInternalErrorCode) {
		StringWriter aStackTraceStringWriter = null;
		String aErrorMessage = null;

		/*********************************************************************/

		this.mErrorCode = pErrorCode;

		/*-----------------------------------------------------------------------
		 * 1. RP 서버 내부 에러일 경우 ErrorMessage 정의
		 *  [InternalErrorCode] InternalErrorMessage
		 *-----------------------------------------------------------------------*/
		if ((this.mErrorCode - 1000) > 0) {
			aErrorMessage = RpErrorCodeInternal.getErrorMessage(this.mErrorCode);
		} else {
			aErrorMessage = RpErrorCodeUAF.getErrorMessage(this.mErrorCode);
		}
		this.mErrorMessage = "[" + pInternalErrorCode + "] " + aErrorMessage;

		aStackTraceStringWriter = new StringWriter();
		this.printStackTrace(new PrintWriter(aStackTraceStringWriter));
		this.mStackTrace = aStackTraceStringWriter.toString();

		/*********************************************************************/
	}

	/**
	 * 로그 출력 및 Exception stacktrace 출력
	 * 
	 * @author haspori
	 */
	public void printErrorLog() {
		/*********************************************************************/

		mLogger.error("[ ErrorCode : " + this.mErrorCode + " - " + "ErrorMessage : " + this.mErrorMessage + " ]");
		mLogger.error("\n" + this.mStackTrace);

		/*********************************************************************/
	}

	/**
	 * get Error Code
	 * 
	 * @author haspori
	 * @return int
	 */
	public int getErrorCode() {
		return this.mErrorCode;
	}

	/**
	 * get Error Message
	 * 
	 * @author haspori
	 * @return String
	 */
	public String getErrorMessage() {
		return this.mErrorMessage;
	}

	public String getStackTraceString() {
		return this.mStackTrace;
	}
}
