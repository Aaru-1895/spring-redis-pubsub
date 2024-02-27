package com.redis.springredispubsub.config;

import com.redis.springredispubsub.subscriber.Receiver;
import com.redis.springredispubsub.subscriber.Receiver2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public JedisConnectionFactory connectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName("localhost");
        configuration.setPort(6379);
        return new JedisConnectionFactory(configuration);
    }

    @Bean
    public RedisTemplate<String, Object> template() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory());
        template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
        return template;
    }

    @Bean
    public ChannelTopic topic()
    {
        return new ChannelTopic("pubsub:Aarti-channel");
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter()
    {
        return new MessageListenerAdapter(new Receiver());
    }
    @Bean
    public MessageListenerAdapter messageListenerAdapter2() {
        return new MessageListenerAdapter(new Receiver2());
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer()
    {
        RedisMessageListenerContainer container=new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.addMessageListener(messageListenerAdapter(),topic());
        container.addMessageListener(messageListenerAdapter2(), topic()); // Add more as needed
        return  container;
    }

}
