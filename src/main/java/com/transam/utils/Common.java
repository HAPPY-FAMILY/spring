package com.transam.utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;

public class Common {
	
	public static String findSessionId(String sessionId) {
		Object obj = SecurityUtils.getSubject().getSession().getAttribute(sessionId);
		if (obj != null) {
			return obj.toString();
		}
		return null;
	}

	public static Object findUserSession() {
		return (Object) SecurityUtils.getSubject().getSession().getAttribute("userSession");
	}

	public static String trimComma(String para) {
		if (StringUtils.isNotBlank(para)) {
			if (para.endsWith(",")) {
				return para.substring(0, para.length() - 1);
			} else {
				return para;
			}
		} else {
			return "";
		}
	}


}
