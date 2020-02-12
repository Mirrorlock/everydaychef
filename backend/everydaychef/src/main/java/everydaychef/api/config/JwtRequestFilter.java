package everydaychef.api.config;

import everydaychef.api.service.JwtUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    private final JwtToken jwtTokenUtil;

    public JwtRequestFilter(JwtToken jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");
        final String authenticationMethod = request.getHeader("AuthenticationMethod");
        String username = null;
        String jwtToken;
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            if(authenticationMethod != null && authenticationMethod.equals("Google")){
                jwtTokenUtil.validateGoogleToken(jwtToken);
            }else if(authenticationMethod != null && authenticationMethod.equals("Facebook")){
                jwtTokenUtil.validateFacebookToken(jwtToken);
            }else{
                try {
                    username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                } catch (IllegalArgumentException e) {
                    logger.error("Unable to get JWT Token");
                } catch (ExpiredJwtException e) {
                    logger.error("JWT Token has expired");
                }
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
                    System.out.println(userDetails.toString());
                    if (jwtTokenUtil.validateManualToken(jwtToken, userDetails)) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken
                                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }
        chain.doFilter(request, response);
    }
}
