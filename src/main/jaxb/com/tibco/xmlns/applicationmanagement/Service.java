//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.04.16 at 02:58:23 PM CEST 
//


package com.tibco.xmlns.applicationmanagement;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.tibco.com/xmlns/ApplicationManagement}ServiceType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.tibco.com/xmlns/ApplicationManagement}authentications" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.tibco.com/xmlns/ApplicationManagement}isFt" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.tibco.com/xmlns/ApplicationManagement}faultTolerant" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.tibco.com/xmlns/ApplicationManagement}plugins" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "authentications",
    "isFt",
    "faultTolerant",
    "plugins"
})
public class Service
    extends ServiceType
{

    protected Authentications authentications;
    protected Boolean isFt;
    protected FaultTolerant faultTolerant;
    protected Plugins plugins;

    /**
     * Gets the value of the authentications property.
     * 
     * @return
     *     possible object is
     *     {@link Authentications }
     *     
     */
    public Authentications getAuthentications() {
        return authentications;
    }

    /**
     * Sets the value of the authentications property.
     * 
     * @param value
     *     allowed object is
     *     {@link Authentications }
     *     
     */
    public void setAuthentications(Authentications value) {
        this.authentications = value;
    }

    /**
     * Gets the value of the isFt property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsFt() {
        return isFt;
    }

    /**
     * Sets the value of the isFt property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsFt(Boolean value) {
        this.isFt = value;
    }

    /**
     * Gets the value of the faultTolerant property.
     * 
     * @return
     *     possible object is
     *     {@link FaultTolerant }
     *     
     */
    public FaultTolerant getFaultTolerant() {
        return faultTolerant;
    }

    /**
     * Sets the value of the faultTolerant property.
     * 
     * @param value
     *     allowed object is
     *     {@link FaultTolerant }
     *     
     */
    public void setFaultTolerant(FaultTolerant value) {
        this.faultTolerant = value;
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

}
