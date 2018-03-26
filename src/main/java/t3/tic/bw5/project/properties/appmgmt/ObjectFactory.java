/**
 * (C) Copyright 2016-2018 teecube
 * (http://teecu.be) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package t3.tic.bw5.project.properties.appmgmt;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import com.tibco.xmlns.applicationmanagement.ServiceType;

@XmlRegistry
public class ObjectFactory extends com.tibco.xmlns.applicationmanagement.ObjectFactory {

	protected final static QName _Application_QNAME = new QName("http://www.tibco.com/xmlns/ApplicationManagement", "application");
	protected final static QName _BaseService_QNAME = new QName("http://www.tibco.com/xmlns/ApplicationManagement", "baseService");
	protected final static QName _Bw_QNAME = new QName("http://www.tibco.com/xmlns/ApplicationManagement", "bw");

    /**
     * Create an instance of {@link ApplicationType }
     */
    public ApplicationType createApplicationType() {
        return new ApplicationType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ApplicationType }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.tibco.com/xmlns/ApplicationManagement", name = "application")
    public JAXBElement<ApplicationType> createApplication(ApplicationType value) {
        return new JAXBElement<ApplicationType>(_Application_QNAME, ApplicationType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tibco.com/xmlns/ApplicationManagement", name = "baseService")
    public JAXBElement<ServiceType> createBaseService(ServiceType value) {
        return new JAXBElement<ServiceType>(_BaseService_QNAME, ServiceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Bw }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.tibco.com/xmlns/ApplicationManagement", name = "bw", substitutionHeadNamespace = "http://www.tibco.com/xmlns/ApplicationManagement", substitutionHeadName = "baseService")
    public JAXBElement<Bw> createBw(Bw value) {
        return new JAXBElement<Bw>(_Bw_QNAME, Bw.class, null, value);
    }

}
