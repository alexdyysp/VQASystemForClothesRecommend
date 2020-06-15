package com.graphSearch.domain.Entity;

import com.graphSearch.domain.Node;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = "Figure")
@Data
@NoArgsConstructor
public class Figure extends Node {



}
