//package com.user.security.config;
//
//import com.user.security.config.authorization.Authority;
//import com.user.security.config.authorization.JwtAuthentication;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final RequestAttributeSecurityContextRepository repository = new RequestAttributeSecurityContextRepository();
//
//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain filterChain
//    ) throws ServletException, IOException {
//        SecurityContextHolder.clearContext();
//        SecurityContextHolder.createEmptyContext();
//        JwtAuthentication jwtUser = new JwtAuthentication(UUID.randomUUID(), new ArrayList<>(List.of(Authority.USER)), true);
//                                                          SecurityContextHolder.getContext().setAuthentication(jwtUser);
//        this.repository.saveContext(SecurityContextHolder.getContext(), request, response);
//        filterChain.doFilter(request, response);
//    }
//}