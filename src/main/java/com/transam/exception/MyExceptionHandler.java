package com.transam.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * 统一异常处理，有效地针对异步和非异步请求
 * 不同异常会到不同页面
 */
public class MyExceptionHandler implements HandlerExceptionResolver {

	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		ex.printStackTrace();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("ex", ex);
		//是否异步请求
		 if (!(request.getHeader("accept").indexOf("application/json") > -1 || (request  
                 .getHeader("X-Requested-With")!= null && request  
                 .getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1))) {
					response.setStatus(500);
					return new ModelAndView("/error/500", model);
		 }else{
			 try {  
				 response.setStatus(500);
                 PrintWriter writer = response.getWriter();  
                 writer.write(ex.getMessage());  
                 writer.flush();  
             } catch (IOException e) {  
            	 model.put("ex", e);
            	 return new ModelAndView("error/500", model);
             }  
             return null;  
		 }
	}
}
