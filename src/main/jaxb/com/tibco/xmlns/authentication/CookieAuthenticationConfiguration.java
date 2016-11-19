//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.04.16 at 02:58:23 PM CEST 
//


package com.tibco.xmlns.authentication;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.tibco.com/xmlns/authentication}CredentialAuthenticationConfigurationType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="cookieDomain" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="cookiePath" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="cookieKeepExpire" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="signaturePassword" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
    "cookieDomain",
    "cookiePath",
    "cookieKeepExpire",
    "signaturePassword"
})
public class CookieAuthenticationConfiguration
    extends CredentialAuthenticationConfigurationType
{

    protected String cookieDomain;
    @XmlElement(required = true)
    protected String cookiePath;
    protected int cookieKeepExpire;
    @XmlElement(required = true)
    protected String signaturePassword;

    /**
     * Gets the value of the cookieDomain property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCookieDomain() {
        return cookieDomain;
    }

    /**
     * Sets the value of the cookieDomain property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCookieDomain(String value) {
        this.cookieDomain = value;
    }

    /**
     * Gets the value of the cookiePath property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCookiePath() {
        return cookiePath;
    }

    /**
     * Sets the value of the cookiePath property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCookiePath(String value) {
        this.cookiePath = value;
    }

    /**
     * Gets the value of the cookieKeepExpire property.
     * 
     */
    public int getCookieKeepExpire() {
        return cookieKeepExpire;
    }

    /**
     * Sets the value of the cookieKeepExpire property.
     * 
     */
    public void setCookieKeepExpire(int value) {
        this.cookieKeepExpire = value;
    }

    /**
     * Gets the value of the signaturePassword property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignaturePassword() {
        return signaturePassword;
    }

    /**
     * Sets the value of the signaturePassword property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignaturePassword(String value) {
        this.signaturePassword = value;
    }

}
