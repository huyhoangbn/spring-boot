/**
 * Jul 23, 2018
 */
package com.javaspring.sercurity;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author OI
 *
 */
@Component
public class ErrorHandler implements AuthenticationEntryPoint {

	@Override
	public void commence( //
			HttpServletRequest req, //
			HttpServletResponse res, //
			AuthenticationException authException) throws IOException, ServletException {
		res.sendError( //
				HttpServletResponse.SC_FORBIDDEN, //
				"Access Denied" //
		);
	}

}
