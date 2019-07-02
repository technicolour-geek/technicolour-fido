package com.hsecure.hancompass.demo.rp.exception;

/**
 * UAF Server에서 발생하는 에러코드 (HTTP 상태코드)
 * 
 * @author haspori
 * @since 2016.10.10
 */
public class RpErrorCodeUAF {
	/**
	 * Error Code : 200 <br>
	 * Erorr Message : SUCCESS <br>
	 * SUCCESS
	 */
	public static final int UAF_ERROR_CODE_200_SUCCESS = 200;
	private static final String UAF_ERROR_MESSAGE_200_SUCCESS = "SUCCESS";

	/**
	 * Error Code : 400 <br>
	 * Erorr Message : INVALID REQUEST <br>
	 * INVALID REQUEST
	 */
	public static final int UAF_ERROR_CODE_400_INVALID_REQUEST = 400;
	private static final String UAF_ERROR_MESSAGE_400_INVALID_REQUEST = "INVALID REQUEST";

	/**
	 * Error Code : 401 <br>
	 * Erorr Message : USER ACCESS DENIED <br>
	 * USER ACCESS DENIED
	 */
	public static final int UAF_ERROR_CODE_401_USER_ACCESS_DENIED = 401;
	private static final String UAF_ERROR_MESSAGE_401_USER_ACCESS_DENIED = "USER ACCESS DENIED";

	/**
	 * Error Code : 403 <br>
	 * Erorr Message : FORBIDDEN <br>
	 * FORBIDDEN
	 */
	public static final int UAF_ERROR_CODE_403_FORBIDDEN = 403;
	private static final String UAF_ERROR_MESSAGE_403_FORBIDDEN = "FORBIDDEN";

	/**
	 * Error Code : 404 <br>
	 * Erorr Message : RESOURCE NOT FOUND <br>
	 * RESOURCE NOT FOUND
	 */
	public static final int UAF_ERROR_CODE_404_RESOURCE_NOT_FOUND = 404;
	private static final String UAF_ERROR_MESSAGE_404_RESOURCE_NOT_FOUND = "RESOURCE NOT FOUND";

	/**
	 * Error Code : 500 <br>
	 * Erorr Message : SERVER ERROR <br>
	 * SERVER ERROR
	 */
	public static final int UAF_ERROR_CODE_500_SERVER_ERROR = 500;
	private static final String UAF_ERROR_MESSAGE_500_SERVER_ERROR = "SERVER ERROR";

	/**
	 * Error Code : 503 <br>
	 * Erorr Message : TIMEOUT <br>
	 * TIMEOUT
	 */
	public static final int UAF_ERROR_CODE_503_SERVER_ERROR = 503;
	private static final String UAF_ERROR_MESSAGE_503_SERVER_ERROR = "TIMEOUT";

	/**
	 * getErrorMessage
	 * 
	 * @author haspori
	 * @param int pErrorCode
	 * @return String Error Message
	 */
	public static String getErrorMessage(int pErrorCode) {
		String aErrorMessage = null;

		/*********************************************************************/

		switch (pErrorCode) {
		case UAF_ERROR_CODE_200_SUCCESS:
			aErrorMessage = UAF_ERROR_MESSAGE_200_SUCCESS;
			break;
		case UAF_ERROR_CODE_400_INVALID_REQUEST:
			aErrorMessage = UAF_ERROR_MESSAGE_400_INVALID_REQUEST;
			break;
		case UAF_ERROR_CODE_401_USER_ACCESS_DENIED:
			aErrorMessage = UAF_ERROR_MESSAGE_401_USER_ACCESS_DENIED;
			break;
		case UAF_ERROR_CODE_403_FORBIDDEN:
			aErrorMessage = UAF_ERROR_MESSAGE_403_FORBIDDEN;
			break;
		case UAF_ERROR_CODE_404_RESOURCE_NOT_FOUND:
			aErrorMessage = UAF_ERROR_MESSAGE_404_RESOURCE_NOT_FOUND;
			break;
		case UAF_ERROR_CODE_500_SERVER_ERROR:
			aErrorMessage = UAF_ERROR_MESSAGE_500_SERVER_ERROR;
			break;
		case UAF_ERROR_CODE_503_SERVER_ERROR:
			aErrorMessage = UAF_ERROR_MESSAGE_503_SERVER_ERROR;
			break;
		default:
			aErrorMessage = "Unknown";
		}

		/*********************************************************************/

		return aErrorMessage;
	}
}
