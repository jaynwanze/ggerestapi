package com.example.ggerestapi.parser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import com.example.ggerestapi.entity.Emission;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EmissionParser {
    public static List<Emission> parseEmissions() {
        ArrayList<Emission> emissions = parseXml();
        ArrayList<HashMap<String, List<String>>> categoriesJson = parseJson();
        ArrayList<HashMap<String, String>> categoryDescriptions = htmlParser();

        for (Emission emission : emissions) {
            // Loop through categories and update value if found
            for (HashMap<String, List<String>> category : categoriesJson) {
                if (category.containsKey(emission.getCategory())) {
                    List<String> categoryMap = category.get(emission.getCategory());
                    String gasUnit = categoryMap.get(1);
                    if (gasUnit.equalsIgnoreCase(emission.getGasUnits())) {
                        System.out.println("Matched Category: " + emission.getCategory());
                        System.out.println("Matched Gas Unit: " + emission.getGasUnits());

                        Float value = Float.parseFloat(categoryMap.get(0));
                        emission.setValue(value);
                    }
                }
            }
            // Loop through category descriptions and update category if found
            for (HashMap<String, String> categoryDesc : categoryDescriptions) {
                if (categoryDesc.containsKey(emission.getCategory())) {
                    String description = categoryDesc.get(emission.getCategory());
                    emission.setCategoryDescription(description);
                }
            }
            System.out.println(emission.toString());
        }

        System.out.println("Emissions Size: " + emissions.size());
        return emissions;

    }

    private static ArrayList<HashMap<String, List<String>>> parseJson() {
        ArrayList<HashMap<String, List<String>>> jsonEmissions = new ArrayList<>();
        try {
            // Read JSON file
            File jsonFile = new File("src/main/resources/json/GreenhouseGasEmissions.json");
            ObjectMapper objectMapper = new ObjectMapper();
            // Read JSON tree
            JsonNode root = objectMapper.readTree(jsonFile);

            JsonNode emissionsNode = root.path("Emissions");
            for (JsonNode emissionNode : emissionsNode) {
                double value = emissionNode.path("Value").asDouble();
                String gasUnit = emissionNode.path("Gas Units").asText().trim();
                if (value > 0) {
                    String category = emissionNode.path("Category").asText();
                    HashMap<String, List<String>> emission = new HashMap<>();
                    List<String> values = new ArrayList<>();
                    values.add(String.valueOf(value));
                    values.add(gasUnit);
                    emission.put(category, values);
                    jsonEmissions.add(emission);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Categories Json Size: " + jsonEmissions.size());
        return jsonEmissions;
    }

    private static ArrayList<Emission> parseXml() {
        ArrayList<Emission> categories = new ArrayList<>();
        File xmlFile = new File("src/main/resources/xml/MMR_IRArticle23T1_IE_2016v2.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            String country = null;

            // Get all row and ms elements
            NodeList rowList = doc.getElementsByTagName("Row");
            System.out.println("Number of 'Row' elements: " + rowList.getLength());

            NodeList msList = doc.getElementsByTagName("MS");
            System.out.println("Number of 'MS' elements: " + msList.getLength());

            // Extract country
            for (int index = 0; index < msList.getLength(); index++) {
                Node ms = msList.item(index);
                if (ms != null && ms.getNodeType() == Node.ELEMENT_NODE) {
                    Element elem = (Element) ms;
                    String countryString = elem.getTextContent().trim();
                    if (countryString.equals("IE")) {
                        System.out.println("Country: " + countryString);
                        country = "Ireland";
                        break;
                    }
                }
            }

            // Extract emmsions data
            for (int i = 0; i < rowList.getLength(); i++) {
                Node row = rowList.item(i);

                if (row != null && row.getNodeType() == Node.ELEMENT_NODE) {
                    Element elem = (Element) row;
                    // Extract and validate data
                    String category = getElementValue(elem, "Category__1_3");
                    String year = getElementValue(elem, "Year");
                    String scenario = getElementValue(elem, "Scenario");
                    String gasUnits = getElementValue(elem, "Gas___Units");
                    String nk = getElementValue(elem, "NK");
                    String predictedValue = getElementValue(elem, "Value");

                    if (year.isEmpty() || scenario.isEmpty() || predictedValue.isEmpty()) {
                        continue;
                    }

                    int yearValue = Integer.parseInt(year);
                    float value = Float.parseFloat(predictedValue);

                    if (yearValue != 2023 || !scenario.equalsIgnoreCase("WEM") || value <= 0) {
                        continue;
                    }
                    Emission emission = new Emission();

                    System.out.println("Category: " + category);
                    System.out.println("Year: " + year);
                    System.out.println("Scenario: " + scenario);
                    System.out.println("Gas Units: " + gasUnits);
                    System.out.println("NK: " + nk);
                    System.out.println("Value: " + predictedValue);

                    emission.setCategory(category);
                    emission.setYear(yearValue);
                    emission.setScenario(scenario);
                    emission.setGasUnits(gasUnits);
                    if (!nk.isEmpty())
                        emission.setNk(nk);
                    emission.setCountry(country);
                    emission.setPredictedValue(value);
                    categories.add(emission);
                }
            }

        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }

        System.out.println("Categories XML Size: " + categories.size());
        return categories;
    }

    private static ArrayList<HashMap<String, String>> htmlParser() {
        ArrayList<HashMap<String, String>> categoryDescriptions = new ArrayList<>();
        try {
            // Connect to the webpage
            org.jsoup.nodes.Document doc = Jsoup.connect("https://www.ipcc-nggip.iges.or.jp/EFDB/find_ef.php").get();

            // Extract all script tags
            Elements scriptTags = doc.select("script");

            // Extract category descriptions
            for (org.jsoup.nodes.Element script : scriptTags) {
                if (script.data().contains("ipccTree.add")) {
                    String scriptContent = script.data();

                    String[] lines = scriptContent.split(";");
                    for (String line : lines) {
                        if (line.contains("ipccTree.add")) {
                            // Extract category description
                            String[] parts = line.split(",");
                            if (parts.length >= 3) {
                                // remove unwanted characters
                                String categoryDescription = parts[2].trim();
                                categoryDescription = categoryDescription.replaceAll("'", "").replaceAll("<[^>]*>", "")
                                        .trim();
                                if (categoryDescription.contains(" - ")) {
                                    String[] mainParts = categoryDescription.split(" - ");
                                    if (mainParts.length >= 2) {
                                        String category = mainParts[0].trim() + "."; 
                                        String categoryDesc = mainParts[1].trim();
                                        // add to map
                                        HashMap<String, String> map = new HashMap<>();
                                        map.put(category, categoryDesc);
                                        categoryDescriptions.add(map);
                                        System.out.println("Category: " + category + " Description: " + categoryDesc);
                                    }
                                }
                            }
                        }

                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return categoryDescriptions;
    }

    // Utility method to get element value safely
    private static String getElementValue(Element elem, String tagName) {
        Node node = elem.getElementsByTagName(tagName).item(0);
        return node.getTextContent().trim();
    }
}