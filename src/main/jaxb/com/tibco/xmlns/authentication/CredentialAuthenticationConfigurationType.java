//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.04.16 at 02:58:23 PM CEST 
//


package com.tibco.xmlns.authentication;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * Type that all CredentialAuthenticationConfigurations must be defined as.
 * 
 * <p>Java class for CredentialAuthenticationConfigurationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CredentialAuthenticationConfigurationType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.tibco.com/xmlns/authentication}AuthenticationConfigurationType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="requiresPasswordInCleartext" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CredentialAuthenticationConfigurationType", propOrder = {
    "requiresPasswordInCleartext"
})
@XmlSeeAlso({
    CookieAuthenticationConfiguration.class,
    HttpSessionAuthenticationConfiguration.class,
    CustomAuthenticationConfiguration.class,
    URLAuthenticationConfiguration.class
})
public class CredentialAuthenticationConfigurationType
    extends AuthenticationConfigurationType
{

    protected boolean requiresPasswordInCleartext;

    /**
     * Gets the value of the requiresPasswordInCleartext property.
     * 
     */
    public boolean isRequiresPasswordInCleartext() {
        return requiresPasswordInCleartext;
    }

    /**
     * Sets the value of the requiresPasswordInCleartext property.
     * 
     */
    public void setRequiresPasswordInCleartext(boolean value) {
        this.requiresPasswordInCleartext = value;
    }

}
