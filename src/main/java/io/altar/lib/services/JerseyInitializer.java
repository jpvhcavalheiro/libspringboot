package io.altar.lib.services;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyInitializer extends ResourceConfig {
	public JerseyInitializer() {
		registerEndpoints();
	}
	
	private void registerEndpoints() {
		register(BookServices.class);
		register(HistoryServices.class);
		register(UserServices.class);
	}
}
