package com.graphSearch.domain.Entity;

import com.graphSearch.domain.Node;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.NodeEntity;

@Data
@NoArgsConstructor
@NodeEntity(label = "Face")
public class Face extends Node {

}
