package com.example.ggerestapi.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@JacksonXmlRootElement(localName = "emmision")
@Entity
public class Emission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String category;
    private String categoryDescription;
    private String scenario;
    private int year;
    private String gasUnits;
    private String nk;
    private float value; 
    private float predictedValue; 
    private String  country;

    public Emission() {
    }

    public Emission(String category, String categoryDescription, String scenario, int year, String gasUnits, String nk, float value, float predictedValue, String country) {
        this.category = category;
        this.categoryDescription = categoryDescription;
        this.scenario = scenario;
        this.year = year;
        this.gasUnits = gasUnits;
        this.nk = nk;
        this.value = value;
        this.predictedValue = predictedValue;
        this.country = country;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getGasUnits() {
        return gasUnits;
    }

    public void setGasUnits(String gasUnits) {
        this.gasUnits = gasUnits;
    }

    public String getNk() {
        return nk;
    }

    public void setNk(String nk) {
        this.nk = nk;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getPredictedValue() {
        return predictedValue;
    }

    public void setPredictedValue(float predictedValue) {
        this.predictedValue = predictedValue;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    

    @Override
    public String toString() {
        return "Emission{" +
                ", category='" + category + '\'' +
                ", categoryDescription='" + categoryDescription + '\'' +
                ", scenario='" + scenario + '\'' +
                ", year=" + year +
                ", gasUnits='" + gasUnits + '\'' +
                ", nk='" + nk + '\'' +
                ", value=" + value +
                ", predictedValue=" + predictedValue +
                ", country='" + country + '\'' +
                '}';
    }
}
