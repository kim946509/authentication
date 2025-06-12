package store.agong.authentication.global.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import store.agong.authentication.domain.forceReLogin.service.ForcedReLoginChecker;
import store.agong.authentication.domain.forceReLogin.service.ForcedReLoginService;
import store.agong.authentication.domain.user.enums.Role;
import java.io.IOException;
import java.util.Set;
import store.agong.authentication.global.jwt.dto.UserAuthentication;
import store.agong.authentication.global.jwt.provider.JwtTokenProvider;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final ForcedReLoginChecker forcedReLoginChecker;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = resolveToken(request);

        if (token != null && jwtTokenProvider.validateToken(token) && jwtTokenProvider.isAccessToken(token)) {
            String username = jwtTokenProvider.getUsername(token);

            if (forcedReLoginChecker.isForced(username)) {
                forcedReLoginChecker.handleForced(response);
                return;
            }

            Set<Role> roles = jwtTokenProvider.getRoles(token);

            // 사용자 인증 객체 생성
            UserAuthentication authentication = new UserAuthentication(username, roles);

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
