package com.hanbat.guessmebackend.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = "com.hanbat.guessmebackend.domain.chat.repository.mongo")
public class MongoDbConfig {

	private final MongoMappingContext mongoMappingContext;

	// 해당 설정 없이 실행하면, _class가 자동 생성
	@Bean
	public MappingMongoConverter mappingMongoConverter(MongoDatabaseFactory dbFactory, MongoMappingContext mongoMappingContext) {
		DbRefResolver dbRefResolver = new DefaultDbRefResolver(dbFactory);
		MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);

		mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
		return mappingConverter;
	}
}
