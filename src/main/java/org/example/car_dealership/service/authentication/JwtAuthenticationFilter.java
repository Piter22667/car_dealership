package org.example.car_dealership.service.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.car_dealership.service.authentication.config.JwtService;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtService jwtService; //Сервіс для роботи з JWT токенами
    private final UserDetailsService userDetailsService; //Сервіс для завантаження деталей користувача


    @Override
    protected void doFilterInternal
            ( @NonNull HttpServletRequest request,
              @NonNull HttpServletResponse response,
              @NonNull FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();
        String method = request.getMethod();
        log.debug("JWT Filter: Processing {} request to {}", method, path);

        final String authorizationHeader = request.getHeader("Authorization"); //Отримуємо заголовок авторизації
        final String jwt;
        final String userEmail;

        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.debug("JWT Filter: No Bearer token found, skipping authentication");
            filterChain.doFilter(request, response); //Якщо заголовок не існує або не починається з Bearer, пропускаємо фільтр
            return;
        }

        //Пропускаємо фільтри для ендпоінтів для тестування і аутентифікації
        if (path.startsWith("/api/auth") ||
                path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") ||
                path.equals("/error")) {
            log.debug("JWT Filter: Public endpoint, skipping authentication");
            filterChain.doFilter(request, response);
            return;
        } //TODO очистити

        //Якщо заголовок існує і починається з Bearer
        jwt = authorizationHeader.substring(7); //Витягуємо токен з заголовка
        userEmail = jwtService.extractUsername(jwt); //Отримуємо ім'я користувача з токена
        log.debug("JWT Filter: Extracted userEmail={} from token", userEmail);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //Якщо ім'я користувача не null і аутентифікація ще не виконана
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail); //Завантажуємо деталі користувача з бази даних
            if (jwtService.isTokenValid(jwt, userDetails)){
                //Якщо токен валідний, створюємо аутентифікацію
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); //Додаємо деталі аутентифікації (
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                log.debug("JWT Filter: Authentication successful for user={}, roles={}", userEmail, userDetails.getAuthorities());
            } else {
                log.warn("JWT Filter: Invalid token for user={}", userEmail);
            }
        }
        filterChain.doFilter(request, response); //Продовжуємо фільтрацію
    }
}
