package com.polymeric.utils;
import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

/**
 * 自定义 HttpServletRequest 实现类，用于单元测试或内部调用
 * 支持链式调用添加参数和请求头
 * 
 * @author YourName
 * @date 2026-06-25
 */
public class TestHttpServletRequest implements HttpServletRequest {
    
    // 存储参数
    private Map<String, String> parameters = new HashMap<>();
    
    // 存储请求头
    private Map<String, String> headers = new HashMap<>();
    
    // 存储属性
    private Map<String, Object> attributes = new HashMap<>();
    
    /**
     * 添加请求参数（链式调用）
     */
    public TestHttpServletRequest addParameter(String key, String value) {
        parameters.put(key, value);
        return this;
    }
    
    /**
     * 批量添加请求参数（链式调用）
     */
    public TestHttpServletRequest addParameters(Map<String, String> params) {
        if (params != null) {
            parameters.putAll(params);
        }
        return this;
    }
    
    /**
     * 添加请求头（链式调用）
     */
    public TestHttpServletRequest addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }
    
    /**
     * 批量添加请求头（链式调用）
     */
    public TestHttpServletRequest addHeaders(Map<String, String> headerMap) {
        if (headerMap != null) {
            headers.putAll(headerMap);
        }
        return this;
    }
    
    /**
     * 添加属性（链式调用）
     */
    public TestHttpServletRequest addAttribute(String key, Object value) {
        attributes.put(key, value);
        return this;
    }
    
    // ==================== HttpServletRequest 接口方法实现 ====================
    
    @Override
    public String getAuthType() {
        return null;
    }
    
    @Override
    public Cookie[] getCookies() {
        return new Cookie[0];
    }
    
    @Override
    public long getDateHeader(String name) {
        return 0;
    }
    
    @Override
    public String getHeader(String name) {
        return headers.get(name);
    }
    
    @Override
    public Enumeration<String> getHeaders(String name) {
        Vector<String> vector = new Vector<>();
        String value = headers.get(name);
        if (value != null) {
            vector.add(value);
        }
        return vector.elements();
    }
    
    @Override
    public Enumeration<String> getHeaderNames() {
        Vector<String> vector = new Vector<>(headers.keySet());
        return vector.elements();
    }
    
    @Override
    public int getIntHeader(String name) {
        try {
            return Integer.parseInt(headers.get(name));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    @Override
    public String getMethod() {
        return "POST";
    }
    
    @Override
    public String getPathInfo() {
        return null;
    }
    
    @Override
    public String getPathTranslated() {
        return null;
    }
    
    @Override
    public String getContextPath() {
        return "";
    }
    
    @Override
    public String getQueryString() {
        return null;
    }
    
    @Override
    public String getRemoteUser() {
        return null;
    }
    
    @Override
    public boolean isUserInRole(String role) {
        return false;
    }
    
    @Override
    public Principal getUserPrincipal() {
        return null;
    }
    
    @Override
    public String getRequestedSessionId() {
        return null;
    }
    
    @Override
    public String getRequestURI() {
        return "/api/test";
    }
    
    @Override
    public StringBuffer getRequestURL() {
        return new StringBuffer("http://localhost:8080/api/test");
    }
    
    @Override
    public String getServletPath() {
        return "";
    }
    
    @Override
    public HttpSession getSession(boolean create) {
        return null;
    }
    
    @Override
    public HttpSession getSession() {
        return null;
    }
    
    @Override
    public String changeSessionId() {
        return null;
    }
    
    @Override
    public boolean isRequestedSessionIdValid() {
        return false;
    }
    
    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }
    
    @Override
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }
    
    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }
    
    @Override
    public boolean authenticate(HttpServletResponse response) {
        return false;
    }
    
    @Override
    public void login(String username, String password) throws ServletException {
        // 空实现
    }
    
    @Override
    public void logout() throws ServletException {
        // 空实现
    }
    
    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        return null;
    }
    
    @Override
    public Part getPart(String name) throws IOException, ServletException {
        return null;
    }
    
    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {
        return null;
    }
    
    // ==================== ServletRequest 接口方法实现 ====================
    
    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }
    
    @Override
    public Enumeration<String> getAttributeNames() {
        Vector<String> vector = new Vector<>(attributes.keySet());
        return vector.elements();
    }
    
    @Override
    public String getCharacterEncoding() {
        return "UTF-8";
    }
    
    @Override
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
        // 空实现
    }
    
    @Override
    public int getContentLength() {
        return 0;
    }
    
    @Override
    public long getContentLengthLong() {
        return 0;
    }
    
    @Override
    public String getContentType() {
        return "application/json";
    }
    
    @Override
    public ServletInputStream getInputStream() throws IOException {
        return null;
    }
    
    @Override
    public String getParameter(String name) {
        return parameters.get(name);
    }
    
    @Override
    public Enumeration<String> getParameterNames() {
        Vector<String> vector = new Vector<>(parameters.keySet());
        return vector.elements();
    }
    
    @Override
    public String[] getParameterValues(String name) {
        String value = parameters.get(name);
        if (value != null) {
            return new String[]{value};
        }
        return new String[0];
    }
    
    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> map = new HashMap<>();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            map.put(entry.getKey(), new String[]{entry.getValue()});
        }
        return map;
    }
    
    @Override
    public String getProtocol() {
        return "HTTP/1.1";
    }
    
    @Override
    public String getScheme() {
        return "http";
    }
    
    @Override
    public String getServerName() {
        return "localhost";
    }
    
    @Override
    public int getServerPort() {
        return 8080;
    }
    
    @Override
    public BufferedReader getReader() throws IOException {
        return null;
    }
    
    @Override
    public String getRemoteAddr() {
        return "127.0.0.1";
    }
    
    @Override
    public String getRemoteHost() {
        return "localhost";
    }
    
    @Override
    public void setAttribute(String name, Object o) {
        attributes.put(name, o);
    }
    
    @Override
    public void removeAttribute(String name) {
        attributes.remove(name);
    }
    
    @Override
    public Locale getLocale() {
        return Locale.CHINA;
    }
    
    @Override
    public Enumeration<Locale> getLocales() {
        Vector<Locale> vector = new Vector<>();
        vector.add(Locale.CHINA);
        return vector.elements();
    }
    
    @Override
    public boolean isSecure() {
        return false;
    }
    
    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return null;
    }
    
    @Override
    public String getRealPath(String path) {
        return null;
    }
    
    @Override
    public int getRemotePort() {
        return 0;
    }
    
    @Override
    public String getLocalName() {
        return "localhost";
    }
    
    @Override
    public String getLocalAddr() {
        return "127.0.0.1";
    }
    
    @Override
    public int getLocalPort() {
        return 8080;
    }
    
    @Override
    public ServletContext getServletContext() {
        return null;
    }
    
    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return null;
    }
    
    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) 
            throws IllegalStateException {
        return null;
    }
    
    @Override
    public boolean isAsyncStarted() {
        return false;
    }
    
    @Override
    public boolean isAsyncSupported() {
        return false;
    }
    
    @Override
    public AsyncContext getAsyncContext() {
        return null;
    }
    
    @Override
    public DispatcherType getDispatcherType() {
        return null;
    }
}