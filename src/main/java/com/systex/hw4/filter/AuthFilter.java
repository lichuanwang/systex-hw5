package com.systex.hw4.filter;

import com.systex.hw4.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

@Component
public class AuthFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Value("${app.auth.whitelist}")
    private String noAuthUrls;  // Inject URLs from application.properties

    private List<String> noAuthRequiredUrls;

    @Override
    protected void doFilterInternal(HttpServletRequest httpRequest, HttpServletResponse httpResponse, FilterChain filterChain)
            throws ServletException, IOException {

        httpRequest.setCharacterEncoding("UTF-8");
        httpResponse.setCharacterEncoding("UTF-8");

        // Load the whitelist URLs from the property file (done once on first request)
        if (noAuthRequiredUrls == null) {
            noAuthRequiredUrls = Arrays.asList(noAuthUrls.split(","));
        }

        HttpSession session = httpRequest.getSession(false); // Don't create a session unless needed
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();

        // Special case for h2-console (use startsWith)
        if (requestURI.startsWith(contextPath + "/h2-console")) {
            filterChain.doFilter(httpRequest, httpResponse);
            return;
        }

        // Check if the request URI matches any of the whitelisted URLs
        boolean noAuthRequired = noAuthRequiredUrls.stream()
                .anyMatch(url -> requestURI.equals(contextPath + url.trim()));

        // Handle POST requests to /auth/login for authentication
        if (requestURI.equals(contextPath + "/auth/login") && httpRequest.getMethod().equalsIgnoreCase("POST")) {
            handleLogin(httpRequest, httpResponse, session);
            return;
        }

        // Check if user is logged in or trying to access a protected resource
        if (!noAuthRequired && (session == null || session.getAttribute("user") == null)) {
            // Check if the request is an AJAX request
            boolean isAjax = "XMLHttpRequest".equals(httpRequest.getHeader("X-Requested-With"));

            if (isAjax) {
                // Respond with a JSON error if the request is AJAX and user is not authenticated
                httpResponse.setContentType("application/json");
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                PrintWriter out = httpResponse.getWriter();
                out.print("{\"error\": \"Unauthorized access. Please log in.\"}");
                out.flush();
            } else {
                // Redirect to login page if user is not authenticated and it's a normal request
                httpResponse.sendRedirect(contextPath + "/auth/login");
            }
            return;
        }

        // Proceed with the request if user is authenticated or accessing non-protected routes
        filterChain.doFilter(httpRequest, httpResponse);
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Check if the request is an AJAX request
        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

        // Authenticate user
        if (userService.authenticateUser(username, password)) {
            if (session != null) {
                session.invalidate(); // Invalidate the old session
            }
            session = request.getSession(true); // Create a new session after successful login
            session.setAttribute("user", username);
            session.setMaxInactiveInterval(30 * 60); // Set session timeout (30 minutes)

            if (isAjax) {
                // Return a JSON response for AJAX requests
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.print("{\"success\": true}");
                out.flush();
            } else {
                // Redirect to home for normal form requests
                response.sendRedirect(request.getContextPath() + "/home");
            }
        } else {
            if (isAjax) {
                // Respond with JSON error if login fails during an AJAX request
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                PrintWriter out = response.getWriter();
                out.print("{\"success\": false, \"message\": \"Invalid username or password\"}");
                out.flush();
            } else {
                // Reload login page with error message for normal requests
                request.setAttribute("error", "Invalid username or password");
                request.getRequestDispatcher("/auth/login").forward(request, response);
            }
        }
    }
}