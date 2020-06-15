package com.graphSearch.domain.Relation;

import com.graphSearch.domain.RelationShip;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.RelationshipEntity;

@Data
@NoArgsConstructor
@RelationshipEntity(type = "Color_of")//服装所属的颜色系；
public class Color_of extends RelationShip{
}
