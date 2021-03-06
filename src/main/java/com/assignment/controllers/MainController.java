package com.assignment.controllers;

import com.assignment.services.ClusterService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.jfree.chart.ChartUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;


@CrossOrigin(allowCredentials = "false", origins = "*", allowedHeaders = "*", maxAge = 3600,
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT}
)
@RestController
public class MainController {

    final static Logger LOGGER = LoggerFactory.getLogger("MainController ");

    @Autowired
    ClusterService clusterService;

    @Autowired
    private ResourceLoader resourceLoader;

    /**
     * This method read json file from post request
     */
    @PostMapping(value = "/cluster", consumes = {"multipart/form-data"})
    public void clusterLocationsFromRequest(HttpServletResponse response, @RequestParam(name = "file") MultipartFile file) throws IOException {
        long clock = System.currentTimeMillis();
        ArrayNode arrayNode = clusterService.convertFileToArrayNode(file);
        double[][] dataset = clusterService.convertArrayNodeToDouble(arrayNode);
        BufferedImage scatterPlot = clusterService.createClusterGraph(dataset);
        LOGGER.info(String.valueOf(System.currentTimeMillis() - clock));
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        ChartUtilities.writeBufferedImageAsPNG(response.getOutputStream(), scatterPlot);
    }

    /**
     * This method read json data from post request
     */
    @PostMapping(value = "/jcluster", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public String clusterLocationsFromJsonRequest(HttpServletResponse response, @RequestBody ArrayNode jsonData) throws IOException {
        double[][] dataset = clusterService.convertArrayNodeToDouble(jsonData);
        clusterService.createClusterGraph(dataset);
        return "http://localhost:8080/chart";
    }

    @GetMapping(value = "/chart")
    public void showClusterChart(HttpServletResponse response) throws IOException {
        BufferedImage chart = clusterService.getChart();
        ChartUtilities.writeBufferedImageAsPNG(response.getOutputStream(), chart);
    }

    /**
     * This method read json file from local machine
     */
    @GetMapping(value = "/cluster")
    public void clusterLocationsFromLocal(HttpServletResponse response) throws IOException {
        long clock = System.currentTimeMillis();
        Resource resource = resourceLoader.getResource("classpath:static/locations.json");
        ArrayNode arrayNode = clusterService.convertFileToArrayNode(resource.getInputStream());
        double[][] dataset = clusterService.convertArrayNodeToDouble(arrayNode);
        BufferedImage scatterPlot = clusterService.createClusterGraph(dataset);
        LOGGER.info(String.valueOf(System.currentTimeMillis() - clock));
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        ChartUtilities.writeBufferedImageAsPNG(response.getOutputStream(), scatterPlot);
    }
}