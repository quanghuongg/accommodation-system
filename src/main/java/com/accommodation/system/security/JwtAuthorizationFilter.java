package com.accommodation.system.security;

import com.accommodation.system.exception.ApiServiceException;
import com.accommodation.system.define.Constant;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenProvider jwtTokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {

        String header = request.getHeader(Constant.SecurityConstant.TOKEN_HEADER);
        String username = null;
        String authToken = null;

        if (header != null && header.startsWith(Constant.SecurityConstant.TOKEN_PREFIX)) {
            authToken = header.replace(Constant.SecurityConstant.TOKEN_PREFIX, "");

            try {
                try {
                    username = jwtTokenProvider.getUsernameFromToken(authToken);
                } catch (ApiServiceException e) {
                    e.printStackTrace();
                }
            } catch (IllegalArgumentException e) {
                log.error("CUSTOM LOGGER ----------- An error occured during getting username and password", e);
            } catch (ExpiredJwtException e) {
                log.error("CUSTOM LOGGER ----------- The token is expired and not valid anymore", e);
            } catch (SignatureException e) {
                log.error("CUSTOM LOGGER ----------- Authentication failed. Username and password not valid", e);
            }
        } else {
            log.warn("CUSTOM LOGGER ----------- Couldn't find bearer string, will ignore the header");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            try {
                if (jwtTokenProvider.validateToken(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = jwtTokenProvider.getAuthentication(
                            authToken,
                            SecurityContextHolder.getContext().getAuthentication(),
                            userDetails
                    );

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (ApiServiceException e) {
                e.printStackTrace();
            }
        }
        filterChain.doFilter(request, response);
    }
}
