//package com.user.security.config;
//
//import com.user.error.ErrorCode;
//import com.user.security.config.authorization.Authority;
//import com.user.security.config.authorization.JwtAuthentication;
//import com.user.security.dto.JwtDto;
//import com.user.security.dto.ValidatedAuthorityDto;
//import com.user.security.service.JwtService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.hibernate.service.spi.ServiceException;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.stream.Collectors;
//
//@RequiredArgsConstructor
//@Component
//public class JwtRequestFilter extends OncePerRequestFilter {
//
//    private static final String AUTH_HEADER = "Authorization";
//
//    private final JwtService jwtService;
//
//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain filterChain
//    ) throws ServletException, IOException {
//        String token = request.getHeader(AUTH_HEADER);
//        if (token == null) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//        ValidatedAuthorityDto authorities = getAuthorities(token);
//        setAuthorities(authorities);
//        filterChain.doFilter(request, response);
//    }
//
//    private ValidatedAuthorityDto getAuthorities(String token) {
//        try {
//            return jwtService.validateAndGetAuthorities(new JwtDto(token));
//        } catch (ServiceException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new ServiceException(ErrorCode.SERVICE_UNAVAILABLE.getCode());
//        }
//    }
//
//    private void setAuthorities(ValidatedAuthorityDto authorities) {
//        JwtAuthentication jwtInfoToken = new JwtAuthentication(
//                authorities.userId(),
//                authorities.authorities().stream()
//                        .map(Authority::new)
//                        .collect(Collectors.toList()),
//                true
//
//        );
//        SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);
//    }
//}
