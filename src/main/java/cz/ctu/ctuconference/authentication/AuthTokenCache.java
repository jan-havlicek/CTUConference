package cz.ctu.ctuconference.authentication;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.MapMaker;
import com.sun.corba.se.impl.orbutil.graph.Graph;
import cz.ctu.ctuconference.user.AppUser;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Nick nemame on 08.12.2016.
 */
@Service
@Scope(value = BeanDefinition.SCOPE_SINGLETON)
public class AuthTokenCache {
	private Cache<UUID, AppUser> authTokens;
	private ConcurrentMap<UUID, AppUser> authTokensMap;
	public AuthTokenCache() {
		authTokens = CacheBuilder.newBuilder()
				.maximumSize(10000)
				.expireAfterWrite(100, TimeUnit.MINUTES)
				.build();
		authTokensMap = new ConcurrentHashMap<>();
	}

	public void addAuthToken(UUID uuid, AppUser user) {
		authTokens.put(uuid, user);
		authTokensMap.put(uuid, user);
	}

	public AppUser getAuthUserByToken(UUID uuid) {
		AppUser user = authTokens.getIfPresent(uuid);
		if(user != null) {
			authTokens.invalidate(uuid);
		}
		return user;
	}
}
