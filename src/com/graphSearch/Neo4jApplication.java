package com.graphSearch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@EnableNeo4jRepositories
@ComponentScan
public class Neo4jApplication {
	private final static Logger log = LoggerFactory.getLogger(Neo4jApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(Neo4jApplication.class, args);
	}


}
