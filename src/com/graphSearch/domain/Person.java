package com.graphSearch.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@Data
@NodeEntity
@NoArgsConstructor
public class Person {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

}
