package org.unreal.starter.sensitive.resolver;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Optional;

public class RequestMappingResolver implements HandlerMethodResolver {

    private final Logger log = LoggerFactory.getLogger(RequestMappingResolver.class);
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    public RequestMappingResolver(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    @Override
    public HandlerMethod resolve() {
        HttpServletRequest request = getCurrentHttpServletRequest();
        if (request == null) {
            log.warn("HttpServletRequest is null, unable to resolve HandlerMethod.");
            return null;
        }else{
            
        }

        return getHandlerFromRequest(request);
    }

    private HttpServletRequest getCurrentHttpServletRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance)
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getRequest)
                .orElse(null);
    }

    @org.jetbrains.annotations.Nullable
    private HandlerMethod getHandlerFromRequest(HttpServletRequest request) {
        try {
            HandlerExecutionChain handlerExecutionChain = requestMappingHandlerMapping.getHandler(request);
            return (handlerExecutionChain != null && handlerExecutionChain.getHandler() instanceof HandlerMethod)
                    ? (HandlerMethod) handlerExecutionChain.getHandler()
                    : null;
        } catch (Exception e) {
            log.error("Cannot get handler from current HttpServletRequest: {}", e.getMessage(), e);
            return null;
        }
    }
}
