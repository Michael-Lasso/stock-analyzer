package com.bugalu.orchestrator.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.bugalu.domain.stock.StockDto;

@Configuration
public class KakfaConfiguration {

	// private String host = "192.168.1.8";
	private String host = "127.0.0.1";
	private String port = "9092";

	@Bean
	public ProducerFactory<String, StockDto> producerFactory() {
		Map<String, Object> config = new HashMap<>();

		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, host + ":" + port);
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

		return new DefaultKafkaProducerFactory<>(config);
	}

	@Bean
	public KafkaTemplate<String, StockDto> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

	@Bean
	public ConsumerFactory<String, StockDto> consumerFactory() {
		Map<String, Object> config = new HashMap<>();

		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, host + ":" + port);
		config.put(ConsumerConfig.GROUP_ID_CONFIG, "group_json");
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

		return new DefaultKafkaConsumerFactory<>(config);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, StockDto> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, StockDto> factory = new ConcurrentKafkaListenerContainerFactory();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}

	@Bean
	public ConsumerFactory<String, StockDto> messageConsumerFactory() {
		Map<String, Object> config = new HashMap<>();

		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, host + ":" + port);
		config.put(ConsumerConfig.GROUP_ID_CONFIG, "group_json");
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		
		return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(),
				new JsonDeserializer<>(StockDto.class));
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, StockDto> messageKafkaListenerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, StockDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(messageConsumerFactory());
		return factory;
	}
}
