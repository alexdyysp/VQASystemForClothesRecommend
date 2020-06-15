package com.graphSearch.domain.Relation;

import com.graphSearch.domain.RelationShip;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.RelationshipEntity;

@Data
@NoArgsConstructor
@RelationshipEntity(type = "Season_of")
public class Season_of extends RelationShip{
}
