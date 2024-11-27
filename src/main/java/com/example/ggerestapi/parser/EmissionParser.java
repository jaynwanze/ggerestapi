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
import com.example.ggerestapi.repository.EmissionRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;

public class EmissionParser {
    public static List<Emission> parseEmissions() {

        ArrayList<Emission> emissions = parseXml();
        ArrayList<HashMap<String, List<String>>> categoriesJson = parseJson();

        for (Emission emission : emissions) {
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
            System.out.println(emission.toString());
        }

        System.out.println("Emissions Size: " + emissions.size());
        return emissions;

        // htmlParser("4.A - Solid Waste Disposal");
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

            NodeList rowList = doc.getElementsByTagName("Row");
            System.out.println("Number of 'Row' elements: " + rowList.getLength());

            NodeList msList = doc.getElementsByTagName("MS");
            System.out.println("Number of 'MS' elements: " + msList.getLength());

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

    private static String htmlParser(String category) {
        String description = null;
        try {
            org.jsoup.nodes.Document doc = Jsoup.connect("https://www.ipcc-nggip.iges.or.jp/EFDB/find_ef.php").get();
            // Get all th attrs
            Elements table = doc.getElementsByTag("table"); // Adjust the selector to target the correct table
            System.out.println("Table Size: " + table.size());
            int categoryIndex = -1;
            int descriptionIndex = -1;

            for (org.jsoup.nodes.Element tableElem : table) {
                if (tableElem.attr("class").equalsIgnoreCase("list")) {

                    Elements tableTrs = tableElem.getElementsByTag("tr");
                    for (org.jsoup.nodes.Element tr : tableTrs) {
                        Elements allThs = tr.getElementsByTag("th");
                        int index = 0;
                        for (org.jsoup.nodes.Element th : allThs) {
                            if (th.text().equalsIgnoreCase("IPCC 2006")) {
                                categoryIndex = index;
                                System.out.println("Category Index: " + categoryIndex);
                            } else if (th.text().equalsIgnoreCase("Description")) {
                                descriptionIndex = index;
                            }
                            index++;
                        }
                        index++;
                    }

                    Elements allTrs = tableElem.getElementsByTag("tr");

                    for (org.jsoup.nodes.Element tr : allTrs) {
                        Elements tds = tr.getElementsByTag("td");
                        if (categoryIndex < tds.size() && descriptionIndex < tds.size()) {
                            String categoryText = tds.get(categoryIndex).text();
                            if (categoryText.equalsIgnoreCase(category)) {
                                System.out.println("Category: " + categoryText);
                                description = tds.get(descriptionIndex).text();
                                System.out.println("Description: " + description);
                            }
                            if (description != null) {
                                break;
                            }
                        }
                    }
                }
            }

        } catch (

        IOException e) {
            e.printStackTrace();
        }
        return description;
    }

    // Utility method to get element value safely
    private static String getElementValue(Element elem, String tagName) {
        Node node = elem.getElementsByTagName(tagName).item(0);
        return node.getTextContent().trim();
    }
}