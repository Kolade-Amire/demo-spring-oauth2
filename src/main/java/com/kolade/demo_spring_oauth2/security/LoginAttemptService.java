package com.kolade.demo_spring_oauth2.security;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


@Service
public class LoginAttemptService {

    private static final Logger logger = LoggerFactory.getLogger(LoginAttemptService.class);

    private static final int MAX_ATTEMPTS = 3;
    private static final int BLOCK_DURATION_HOURS = 12;

    private final LoadingCache<String, Integer> loginAttemptCache;

    public LoginAttemptService() {
        super();
        loginAttemptCache = CacheBuilder.newBuilder()
                .expireAfterWrite(BLOCK_DURATION_HOURS, TimeUnit.HOURS)
                .build(new CacheLoader<>() {
                    @Override
                    @NonNull
                    public Integer load(@NonNull String key) {
                        return 0;
                    }
                });
    }

    public void loginSucceeded(String key) {loginAttemptCache.invalidate(key);}

    public void loginFailed(String key) {
        int attempts = 0;
        try {
            attempts = loginAttemptCache.get(key);
        } catch (ExecutionException e) {
            logger.warn("Failed to get login attempts for key: {}", key, e);
        }
        attempts++;
        loginAttemptCache.put(key, attempts);
    }

    public boolean isBlocked(String key) {
        try {
            return (loginAttemptCache.get(key)) >= MAX_ATTEMPTS;

        }catch(ExecutionException e){
            return false;
        }
    }


}
