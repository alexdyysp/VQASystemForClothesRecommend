package com.userManager.repository;

import com.graphSearch.domain.Node;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface Repository extends Neo4jRepository {
    @Query("match(n) where id(n) ={id} return n")
    Node findNodeById(Long id);

    @Query("match(n{Name:{name}}) return n ")
    Node findByName(@Param("name") String name);

    @Query("MATCH(n) where n.name contains {name} return n")
    Collection<Node> findNodesByNameLike(@Param("name") String name);



}
