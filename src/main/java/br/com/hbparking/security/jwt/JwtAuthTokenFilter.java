package br.com.hbparking.security.jwt;

import br.com.hbparking.security.user.UserAuthServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthTokenFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthTokenFilter.class);

    private final JwtProvider jwtProvider;
    private final UserAuthServiceImpl userAuthService;

    @Autowired
    public JwtAuthTokenFilter(JwtProvider jwtProvider, UserAuthServiceImpl userAuthService) {
        this.jwtProvider = jwtProvider;
        this.userAuthService = userAuthService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        try {
            String jwt = jwtProvider.getJwt(req);
            if (jwt != null && !jwt.isEmpty() && jwtProvider.validateJwtToken(jwt)) {
                String username = jwtProvider.getEmailFromUserAuthenticated(jwt);

                UserDetails userDetails = userAuthService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            LOGGER.error("Erro durante o acesso ao controller - Mensagem: ", e);
        }

        chain.doFilter(req, res);
    }
}
