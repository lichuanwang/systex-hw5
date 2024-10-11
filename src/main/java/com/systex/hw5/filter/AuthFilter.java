package com.systex.hw5.filter;

import com.systex.hw5.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import static com.systex.hw5.util.JsonUtil.parseJsonRequest;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class AuthFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Value("${app.auth.whitelist}")
    private String noAuthUrls;

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

        // Special case for static resources and h2-console
        if (requestURI.startsWith(contextPath + "/h2-console") ||
                requestURI.startsWith(contextPath + "/js") ||
                requestURI.startsWith(contextPath + "/style")) {
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
            handleUnauthorizedRequest(httpRequest, httpResponse);
            return;
        }

        // Proceed with the request if user is authenticated or accessing non-protected routes
        filterChain.doFilter(httpRequest, httpResponse);
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException, ServletException {
        // Check if the request is an AJAX request
        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

        // Parse the request body if it's an AJAX call
        String username = null;
        String password = null;
        if (isAjax) {
            // Parse JSON data from the request body
            Map<String, Object> requestBody = parseJsonRequest(request);
            username = (String) requestBody.get("username");
            password = (String) requestBody.get("password");
        } else {
            // For non-AJAX requests, get parameters directly
            username = request.getParameter("username");
            password = request.getParameter("password");
        }

        if (username == null || password == null) {
            respondWithError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Username or password is missing", isAjax);
            return;
        }

        // Authenticate user
        if (userService.authenticateUser(username, password)) {
            handleSuccessfulLogin(request, response, session, username, isAjax);
        } else {
            respondWithError(request, response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid username or password", isAjax);
        }
    }

    private void handleSuccessfulLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session, String username, boolean isAjax) throws IOException {
        if (session != null) {
            session.invalidate(); // Invalidate the old session
        }
        session = request.getSession(true); // Create a new session after successful login
        session.setAttribute("user", username);
        session.setMaxInactiveInterval(30 * 60); // Set session timeout (30 minutes)

        if (isAjax) {
            // Return success JSON response for AJAX requests
            respondWithJson(response, "{\"success\": true}");
        } else {
            // Redirect to home for normal form requests
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }

    private void handleUnauthorizedRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
        if (isAjax) {
            respondWithError(request, response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized access. Please log in.", true);
        } else {
            // Redirect to login page for non-AJAX requests
            response.sendRedirect(request.getContextPath() + "/auth/login");
        }
    }

    private void respondWithError(HttpServletRequest request, HttpServletResponse response, int statusCode, String message, boolean isAjax) throws IOException, ServletException {
        if (isAjax) {
            response.setContentType("application/json");
            response.setStatus(statusCode);
            PrintWriter out = response.getWriter();
            out.print("{\"success\": false, \"message\": \"" + message + "\"}");
            out.flush();
        } else {
            request.getSession().setAttribute("loginError", message);
            response.sendRedirect(request.getContextPath() + "/auth/login");
        }
    }

    private void respondWithJson(HttpServletResponse response, String jsonResponse) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }
}
