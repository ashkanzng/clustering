package com.assignment.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = ClusterService.class)
class ClusterServiceTest {

    @Autowired
    ClusterService clusterService;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    void is_convert_arrayNode_to_double() throws JsonProcessingException {
        String stringJson = "[{ \"id\": \"1001\", \"city\": \"Amsterdam\", \"zip\": \"1012 RR\", \"lat\": \"52.372992\", \"lng\": \"4.890011\" }]";
        ArrayNode arrayNode = mapper.readValue(stringJson, ArrayNode.class);
        double[][] doubles = {{52.372992,4.890011}};
        assertArrayEquals(clusterService.convertArrayNodeToDouble(arrayNode)[0],doubles[0]);
    }
}