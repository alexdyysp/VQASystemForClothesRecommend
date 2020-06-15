package com.graphSearch.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
@Data
@NoArgsConstructor
public class Node {
    @Id
    @GeneratedValue
    private Long id;
    private String Name;
    private String Sex;
    private String ImageId;
    private String Type;
    private String Describe;
    private String Recommend;
}
