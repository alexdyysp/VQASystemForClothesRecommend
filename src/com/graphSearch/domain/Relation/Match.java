package com.graphSearch.domain.Relation;

import com.graphSearch.domain.RelationShip;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.RelationshipEntity;

@Data
@NoArgsConstructor
@RelationshipEntity(type = "Match")//服装与服装之间的搭配
public class Match extends RelationShip {


}