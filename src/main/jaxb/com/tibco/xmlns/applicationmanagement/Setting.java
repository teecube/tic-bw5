//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.04.16 at 02:58:23 PM CEST 
//


package com.tibco.xmlns.applicationmanagement;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="startOnBoot" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="enableVerbose" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="maxLogFileSize" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/&gt;
 *         &lt;element name="maxLogFileCount" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/&gt;
 *         &lt;element name="threadCount" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/&gt;
 *         &lt;element name="NTService" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="runAsNT" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *                   &lt;element ref="{http://www.tibco.com/xmlns/ApplicationManagement}startupType"/&gt;
 *                   &lt;element name="loginAs" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="java" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="prepandClassPath" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="appendClassPath" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="initHeapSize" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/&gt;
 *                   &lt;element name="maxHeapSize" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/&gt;
 *                   &lt;element name="threadStackSize" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "startOnBoot",
    "enableVerbose",
    "maxLogFileSize",
    "maxLogFileCount",
    "threadCount",
    "ntService",
    "java"
})
@XmlRootElement(name = "setting")
public class Setting {

    @XmlElement(defaultValue = "false")
    protected Boolean startOnBoot;
    @XmlElement(defaultValue = "false")
    protected Boolean enableVerbose;
    @XmlElement(defaultValue = "20000")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger maxLogFileSize;
    @XmlElement(defaultValue = "5")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger maxLogFileCount;
    @XmlElement(defaultValue = "8")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger threadCount;
    @XmlElement(name = "NTService")
    protected Setting.NTService ntService;
    protected Setting.Java java;

    /**
     * Gets the value of the startOnBoot property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isStartOnBoot() {
        return startOnBoot;
    }

    /**
     * Sets the value of the startOnBoot property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setStartOnBoot(Boolean value) {
        this.startOnBoot = value;
    }

    /**
     * Gets the value of the enableVerbose property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEnableVerbose() {
        return enableVerbose;
    }

    /**
     * Sets the value of the enableVerbose property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEnableVerbose(Boolean value) {
        this.enableVerbose = value;
    }

    /**
     * Gets the value of the maxLogFileSize property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMaxLogFileSize() {
        return maxLogFileSize;
    }

    /**
     * Sets the value of the maxLogFileSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMaxLogFileSize(BigInteger value) {
        this.maxLogFileSize = value;
    }

    /**
     * Gets the value of the maxLogFileCount property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMaxLogFileCount() {
        return maxLogFileCount;
    }

    /**
     * Sets the value of the maxLogFileCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMaxLogFileCount(BigInteger value) {
        this.maxLogFileCount = value;
    }

    /**
     * Gets the value of the threadCount property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getThreadCount() {
        return threadCount;
    }

    /**
     * Sets the value of the threadCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setThreadCount(BigInteger value) {
        this.threadCount = value;
    }

    /**
     * Gets the value of the ntService property.
     * 
     * @return
     *     possible object is
     *     {@link Setting.NTService }
     *     
     */
    public Setting.NTService getNTService() {
        return ntService;
    }

    /**
     * Sets the value of the ntService property.
     * 
     * @param value
     *     allowed object is
     *     {@link Setting.NTService }
     *     
     */
    public void setNTService(Setting.NTService value) {
        this.ntService = value;
    }

    /**
     * Gets the value of the java property.
     * 
     * @return
     *     possible object is
     *     {@link Setting.Java }
     *     
     */
    public Setting.Java getJava() {
        return java;
    }

    /**
     * Sets the value of the java property.
     * 
     * @param value
     *     allowed object is
     *     {@link Setting.Java }
     *     
     */
    public void setJava(Setting.Java value) {
        this.java = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="prepandClassPath" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="appendClassPath" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="initHeapSize" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/&gt;
     *         &lt;element name="maxHeapSize" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/&gt;
     *         &lt;element name="threadStackSize" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "prepandClassPath",
        "appendClassPath",
        "initHeapSize",
        "maxHeapSize",
        "threadStackSize"
    })
    public static class Java {

        protected String prepandClassPath;
        protected String appendClassPath;
        @XmlElement(defaultValue = "32")
        @XmlSchemaType(name = "nonNegativeInteger")
        protected BigInteger initHeapSize;
        @XmlElement(defaultValue = "128")
        @XmlSchemaType(name = "nonNegativeInteger")
        protected BigInteger maxHeapSize;
        @XmlElement(defaultValue = "128")
        @XmlSchemaType(name = "nonNegativeInteger")
        protected BigInteger threadStackSize;

        /**
         * Gets the value of the prepandClassPath property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPrepandClassPath() {
            return prepandClassPath;
        }

        /**
         * Sets the value of the prepandClassPath property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPrepandClassPath(String value) {
            this.prepandClassPath = value;
        }

        /**
         * Gets the value of the appendClassPath property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAppendClassPath() {
            return appendClassPath;
        }

        /**
         * Sets the value of the appendClassPath property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAppendClassPath(String value) {
            this.appendClassPath = value;
        }

        /**
         * Gets the value of the initHeapSize property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getInitHeapSize() {
            return initHeapSize;
        }

        /**
         * Sets the value of the initHeapSize property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setInitHeapSize(BigInteger value) {
            this.initHeapSize = value;
        }

        /**
         * Gets the value of the maxHeapSize property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getMaxHeapSize() {
            return maxHeapSize;
        }

        /**
         * Sets the value of the maxHeapSize property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setMaxHeapSize(BigInteger value) {
            this.maxHeapSize = value;
        }

        /**
         * Gets the value of the threadStackSize property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getThreadStackSize() {
            return threadStackSize;
        }

        /**
         * Sets the value of the threadStackSize property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setThreadStackSize(BigInteger value) {
            this.threadStackSize = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="runAsNT" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
     *         &lt;element ref="{http://www.tibco.com/xmlns/ApplicationManagement}startupType"/&gt;
     *         &lt;element name="loginAs" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "runAsNT",
        "startupType",
        "loginAs",
        "password"
    })
    public static class NTService {

        @XmlElement(defaultValue = "false")
        protected boolean runAsNT;
        @XmlElement(required = true, defaultValue = "automatic")
        protected String startupType;
        protected String loginAs;
        protected String password;

        /**
         * Gets the value of the runAsNT property.
         * 
         */
        public boolean isRunAsNT() {
            return runAsNT;
        }

        /**
         * Sets the value of the runAsNT property.
         * 
         */
        public void setRunAsNT(boolean value) {
            this.runAsNT = value;
        }

        /**
         * Gets the value of the startupType property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getStartupType() {
            return startupType;
        }

        /**
         * Sets the value of the startupType property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setStartupType(String value) {
            this.startupType = value;
        }

        /**
         * Gets the value of the loginAs property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLoginAs() {
            return loginAs;
        }

        /**
         * Sets the value of the loginAs property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLoginAs(String value) {
            this.loginAs = value;
        }

        /**
         * Gets the value of the password property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPassword() {
            return password;
        }

        /**
         * Sets the value of the password property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPassword(String value) {
            this.password = value;
        }

    }

}