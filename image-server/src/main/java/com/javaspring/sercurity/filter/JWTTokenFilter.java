package com.javaspring.sercurity.filter;


import com.javaspring.entity.Agentconnection;
import com.javaspring.reposity.AgenconnectionRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTTokenFilter extends OncePerRequestFilter {
	private final static Logger log = LogManager.getLogger(JWTTokenFilter.class);

	private @Autowired
	AgenconnectionRepository agenconnectionRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String token = request.getHeader("Authorization");
			if (token == null) {
				log.info("dataReq: null");
				setResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized", "Không có xác thực!");
				return;
			}

			Agentconnection agentconnection = agenconnectionRepository.findOneByToken(token);

			if(agentconnection == null) {
				// check block
				setResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized", "Không có xác thực!");
				return;
			}else {
				if(agentconnection.getEnable() != Agentconnection.ENABLE) {
					setResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized", "Tài khoản bị khóa");
					return;
				}
			}			

			filterChain.doFilter(request, response);
		} catch (Exception e) {
			setResponse(response, HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized", e.getMessage());
			e.printStackTrace();
		}
	}

	private void setResponse(HttpServletResponse response, int sc, String error, String message) {
		response.setHeader("Content-Type", "application/json;charset=UTF-8");
		response.setStatus(sc);
		try {
			response.getWriter().write(errorResponse(sc, "Unauthorized", message));
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String errorResponse(int sc, String error, String message) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
		Map<String, String> m = new HashMap<String, String>();
		m.put("timestamp", df.format(new Date()));
		m.put("status", String.valueOf(sc));
		m.put("error", error);
		m.put("message", message);
		return new JSONObject(m).toString();
	}
}
