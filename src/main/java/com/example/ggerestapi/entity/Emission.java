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
    private String gas;
    private String nk;
    private double value; 
    private Double predictedValue; 

    public Emission() {
    }

    public Emission(String category, String categoryDescription, String scenario, int year, String gasUnits, String gas, String nk, double value, Double predictedValue) {
        this.category = category;
        this.categoryDescription = categoryDescription;
        this.scenario = scenario;
        this.year = year;
        this.gasUnits = gasUnits;
        this.gas = gas;
        this.nk = nk;
        this.value = value;
        this.predictedValue = predictedValue;
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

    public String getGas() {
        return gas;
    }

    public void setGas(String gas) {
        this.gas = gas;
    }

    public String getNk() {
        return nk;
    }

    public void setNk(String nk) {
        this.nk = nk;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Double getPredictedValue() {
        return predictedValue;
    }

    public void setPredictedValue(Double predictedValue) {
        this.predictedValue = predictedValue;
    }
    

    @Override
    public String toString() {
        return "Emission{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", categoryDescription='" + categoryDescription + '\'' +
                ", scenario='" + scenario + '\'' +
                ", year=" + year +
                ", gasUnits='" + gasUnits + '\'' +
                ", gas='" + gas + '\'' +
                ", nk='" + nk + '\'' +
                ", value=" + value +
                ", predictedValue=" + predictedValue +
                '}';
    }
}
