package org.unreal.starter.sensitive.resolver;

import org.springframework.web.method.HandlerMethod;

public interface HandlerMethodResolver {
    public HandlerMethod resolve();
}
