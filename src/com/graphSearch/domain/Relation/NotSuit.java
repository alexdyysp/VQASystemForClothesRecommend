package com.graphSearch.domain.Relation;

import com.graphSearch.domain.RelationShip;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.RelationshipEntity;

@Data
@NoArgsConstructor
@RelationshipEntity(type = "NotSuit")
public class NotSuit extends RelationShip{
}
