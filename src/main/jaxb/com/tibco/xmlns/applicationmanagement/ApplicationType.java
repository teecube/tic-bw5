//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.04.16 at 02:58:23 PM CEST 
//


package com.tibco.xmlns.applicationmanagement;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ApplicationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ApplicationType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="repoInstanceName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="contact" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="maxdeploymentrevision" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.tibco.com/xmlns/ApplicationManagement}NVPairs" minOccurs="0"/&gt;
 *         &lt;element name="SymmetricKeyEnabled" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.tibco.com/xmlns/ApplicationManagement}repoInstances" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.tibco.com/xmlns/ApplicationManagement}services"/&gt;
 *         &lt;element ref="{http://www.tibco.com/xmlns/ApplicationManagement}plugins" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ApplicationType", propOrder = {
    "repoInstanceName",
    "description",
    "contact",
    "maxdeploymentrevision",
    "nvPairs",
    "symmetricKeyEnabled",
    "repoInstances",
    "services",
    "plugins"
})
public class ApplicationType {

    protected String repoInstanceName;
    protected String description;
    protected String contact;
    protected String maxdeploymentrevision;
    @XmlElement(name = "NVPairs")
    protected NVPairs nvPairs;
    @XmlElement(name = "SymmetricKeyEnabled")
    protected String symmetricKeyEnabled;
    protected RepoInstances repoInstances;
    @XmlElement(required = true)
    protected Services services;
    protected Plugins plugins;
    @XmlAttribute(name = "name", required = true)
    protected String name;

    /**
     * Gets the value of the repoInstanceName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRepoInstanceName() {
        return repoInstanceName;
    }

    /**
     * Sets the value of the repoInstanceName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRepoInstanceName(String value) {
        this.repoInstanceName = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the contact property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContact() {
        return contact;
    }

    /**
     * Sets the value of the contact property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContact(String value) {
        this.contact = value;
    }

    /**
     * Gets the value of the maxdeploymentrevision property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxdeploymentrevision() {
        return maxdeploymentrevision;
    }

    /**
     * Sets the value of the maxdeploymentrevision property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxdeploymentrevision(String value) {
        this.maxdeploymentrevision = value;
    }

    /**
     * Gets the value of the nvPairs property.
     * 
     * @return
     *     possible object is
     *     {@link NVPairs }
     *     
     */
    public NVPairs getNVPairs() {
        return nvPairs;
    }

    /**
     * Sets the value of the nvPairs property.
     * 
     * @param value
     *     allowed object is
     *     {@link NVPairs }
     *     
     */
    public void setNVPairs(NVPairs value) {
        this.nvPairs = value;
    }

    /**
     * Gets the value of the symmetricKeyEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSymmetricKeyEnabled() {
        return symmetricKeyEnabled;
    }

    /**
     * Sets the value of the symmetricKeyEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSymmetricKeyEnabled(String value) {
        this.symmetricKeyEnabled = value;
    }

    /**
     * Gets the value of the repoInstances property.
     * 
     * @return
     *     possible object is
     *     {@link RepoInstances }
     *     
     */
    public RepoInstances getRepoInstances() {
        return repoInstances;
    }

    /**
     * Sets the value of the repoInstances property.
     * 
     * @param value
     *     allowed object is
     *     {@link RepoInstances }
     *     
     */
    public void setRepoInstances(RepoInstances value) {
        this.repoInstances = value;
    }

    /**
     * Gets the value of the services property.
     * 
     * @return
     *     possible object is
     *     {@link Services }
     *     
     */
    public Services getServices() {
        return services;
    }

    /**
     * Sets the value of the services property.
     * 
     * @param value
     *     allowed object is
     *     {@link Services }
     *     
     */
    public void setServices(Services value) {
        this.services = value;
    }

    /**
     * Gets the value of the plugins property.
     * 
     * @return
     *     possible object is
     *     {@link Plugins }
     *     
     */
    public Plugins getPlugins() {
        return plugins;
    }

    /**
     * Sets the value of the plugins property.
     * 
     * @param value
     *     allowed object is
     *     {@link Plugins }
     *     
     */
    public void setPlugins(Plugins value) {
        this.plugins = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

}
