package net.test;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

public class KeyExpiredListener  extends RedisMessageListenerContainer{

	@Bean
	RedisMessageListenerContainer keyExpirationListenerContainer(RedisConnectionFactory connectionFactory) {

	    RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer();
	    listenerContainer.setConnectionFactory(connectionFactory);

	    listenerContainer.addMessageListener((message, pattern) -> {

	        // event handling comes here

	    }, new PatternTopic("__keyevent@*__:expired"));

	    return listenerContainer;
	}
}