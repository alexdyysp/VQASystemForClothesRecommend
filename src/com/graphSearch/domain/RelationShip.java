package com.graphSearch.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.*;

@RelationshipEntity
@Data
@NoArgsConstructor
public class RelationShip {
    @Id
    @GeneratedValue
    private Long id;
    @StartNode
    private Node StartNode;

    @EndNode
    private Node EndNode;


}
