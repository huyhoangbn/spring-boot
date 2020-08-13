/**
 * Jul 23, 2018
 */
package com.javaspring.sercurity.filter;

import com.javaspring.entity.Agentconnection;
import com.javaspring.reposity.AgenconnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author OI
 *
 */
public class AuthenticationFilter extends OncePerRequestFilter {

	private @Autowired
	AgenconnectionRepository agenconnectionRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String token = getTokenFromRequest(req);
			if (StringUtils.hasText(token)) {
				Agentconnection agent = agenconnectionRepository.findOneByToken(token);
				UsernamePasswordAuthenticationToken authentication = //
						new UsernamePasswordAuthenticationToken(agent, null, null);
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		filterChain.doFilter(req, res);
	}

	private String getTokenFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken)) {
			return bearerToken;
		}
		return null;
	}

}
