package com.assignment.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import smile.clustering.XMeans;
import smile.plot.swing.Canvas;
import smile.plot.swing.ScatterPlot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
public class ClusterService {

    ObjectMapper mapper = new ObjectMapper();
    int maxClusterNumber = 12;
    final static Logger LOGGER = LoggerFactory.getLogger("ClusterService");

    @Setter
    @Getter
    private BufferedImage chart;

    public ArrayNode convertFileToArrayNode(MultipartFile multipartFile) throws IOException {
        return mapper.readValue(multipartFile.getBytes(), ArrayNode.class);
    }

    public ArrayNode convertFileToArrayNode(InputStream file) throws IOException {
        return mapper.readValue(file, ArrayNode.class);
    }

    public double[][] convertArrayNodeToDouble(ArrayNode arrayNode) {
        double[][] locations = new double[arrayNode.size()][2];
        for (int i = 0; i < arrayNode.size(); i++) {
            double lat = (arrayNode.get(i).get("lat") != null) ? arrayNode.get(i).get("lat").asDouble() : 0;
            double lng = (arrayNode.get(i).get("lng") != null) ? arrayNode.get(i).get("lng").asDouble() : 0;
            locations[i] = new double[]{lat, lng};
        }
        return locations;
    }

    public BufferedImage createClusterGraph(double[][] dataset) {
        XMeans xmeans = XMeans.fit(dataset, maxClusterNumber);
        Canvas plot = ScatterPlot.of(dataset, xmeans.y, '.').canvas();
        plot.add(ScatterPlot.of(xmeans.centroids, '@'));
        setChart(plot.toBufferedImage(1400, 1200));
        return plot.toBufferedImage(1400, 1200);
    }

}
