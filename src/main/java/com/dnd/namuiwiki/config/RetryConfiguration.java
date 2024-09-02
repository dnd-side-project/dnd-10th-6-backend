package com.dnd.namuiwiki.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EnableRetry(proxyTargetClass=true)
public class RetryConfiguration {
}
