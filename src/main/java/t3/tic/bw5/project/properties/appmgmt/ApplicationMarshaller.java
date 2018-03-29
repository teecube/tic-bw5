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

import com.tibco.xmlns.applicationmanagement.Binding;
import com.tibco.xmlns.applicationmanagement.Bwprocess;
import com.tibco.xmlns.applicationmanagement.Checkpoints;
import com.tibco.xmlns.applicationmanagement.FaultTolerant;
import com.tibco.xmlns.applicationmanagement.*;
import com.tibco.xmlns.applicationmanagement.Product;
import com.tibco.xmlns.applicationmanagement.Setting;
import com.tibco.xmlns.applicationmanagement.Setting.Java;
import com.tibco.xmlns.applicationmanagement.Setting.NTService;
import com.tibco.xmlns.applicationmanagement.Shutdown;
import org.xml.sax.SAXException;
import t3.xml.RootElementNamespaceFilter.NamespaceDeclaration;
import t3.xml.XMLMarshall;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import java.io.File;
import java.util.ArrayList;

public class ApplicationMarshaller extends XMLMarshall<ApplicationType, ObjectFactory> {
    public static final String NAMESPACE = "http://www.tibco.com/xmlns/ApplicationManagement";

    private ApplicationType application;

    public ApplicationMarshaller(File xmlFile) throws JAXBException, SAXException {
        super(xmlFile, com.tibco.xmlns.applicationmanagement.bw.ObjectFactory.class,
                       com.tibco.xmlns.authentication.ObjectFactory.class,
                       com.tibco.xmlns.dd.ObjectFactory.class);

        this.rootElementLocalName = "application";

        this.namespaceDeclarationsToRemove = new ArrayList<NamespaceDeclaration>();
        this.namespaceDeclarationsToRemove.add(new NamespaceDeclaration("xmlns:dd", "http://www.tibco.com/xmlns/dd"));
        this.namespaceDeclarationsToRemove.add(new NamespaceDeclaration("xmlns:bw", "http://www.tibco.com/xmlns/ApplicationManagement/BW"));
        this.namespaceDeclarationsToRemove.add(new NamespaceDeclaration("xmlns:auth", "http://www.tibco.com/xmlns/authentication"));
        
        this.application = this.getObject();
    }

    @Override
    protected Object createElement(String path, String elementName, String nameAttribute, String value, Object parent) {
        if (nameAttribute != null) {

            if (elementName.equals("variables")) {
                NVPairs gvs = null;
                if (parent.getClass().equals(Bw.class)) {
                    Bw bw = (Bw) parent;
                    gvs = bw.getNVPairs(nameAttribute);
                    // TODO : create if not exists (?)
                } else if (parent.getClass().equals(Binding.class)) {
                    gvs = new NVPairs(nameAttribute);
                    ((Binding) parent).setNVPairs(gvs);
                }
                return gvs;
            } else if (elementName.equals("variable")) {
                NameValuePair simpleGV = new NameValuePair();
                simpleGV.setName(nameAttribute);
                simpleGV.setValue(value);

                QName name = new QName(NAMESPACE, "NameValuePair");
                JAXBElement<NameValuePair> _simpleGV = new JAXBElement<NameValuePair>(name, NameValuePair.class, simpleGV);
                boolean found = false;
                for (JAXBElement<? extends NVPairType> nvPair : ((NVPairs) parent).getNVPair()) {
                    if (nameAttribute.equals(nvPair.getValue().getName())) {
                        nvPair.getValue().setValue(value);
                        found = true;
                    }
                }
                if (!found) {
                    ((NVPairs) parent).getNVPair().add(_simpleGV);
                }

                return simpleGV;
            } else if (elementName.equals("bw")) {
                Bw service = application.getBw(nameAttribute);
                return service;
            } else if (elementName.equals("binding")) {
                Binding binding = ((Bw) parent).getBinding(nameAttribute);
                return binding;
            } else if (elementName.equals("bwprocess")) {
                Bwprocess bwProcess = ((Bw) parent).getBWProcess(nameAttribute);
                return bwProcess;
            } else if (elementName.equals("checkpoint")) {
                Checkpoints checkpoints = (Checkpoints) parent;
                if (!checkpoints.getCheckpoint().contains(nameAttribute)) {
                    checkpoints.getCheckpoint().add(nameAttribute);
                }
                if ("true".equals(value)) {
                    checkpoints.setSelected(nameAttribute);
                }
            }
        } else {
            if (elementName.equals("variables")) {
                NVPairs gvs = null;
                if (parent.getClass().equals(Bw.class)) {
                    Bw bw = (Bw) parent;
                    gvs = bw.getNVPairs("Runtime Variables");
                    // TODO : create if not exists (?)
                } else if (parent.getClass().equals(Binding.class)) {
                    gvs = new NVPairs();
                    gvs.setName("Runtime Variables");
                    ((Binding) parent).setNVPairs(gvs);
                }
                return gvs;
            } else if (elementName.equals("checkpoints")) {
                Checkpoints checkpoints = ((com.tibco.xmlns.applicationmanagement.Bw) parent).getCheckpoints();
                return checkpoints;
            } else if (elementName.equals("tablePrefix")) {
                Checkpoints checkpoints = (Checkpoints) parent;
                checkpoints.setTablePrefix(value);
            // Binding children
            } else if (parent.getClass().equals(Binding.class)) {
                return new t3.tic.bw5.project.properties.appmgmt.Binding((Binding) parent).addParameter(elementName, value);
//                return addBindingParameter((Binding) parent, elementName, value);
            } else if (parent.getClass().equals(Product.class)) {
                return new t3.tic.bw5.project.properties.appmgmt.Product((Product) parent).addParameter(elementName, value);
//                return addProductParameter((Product) parent, elementName, value);
            } else if (parent.getClass().equals(Setting.class)) {
                return new t3.tic.bw5.project.properties.appmgmt.Setting((Setting) parent).addParameter(elementName, value);
//                return addSettingParameter((Setting) parent, elementName, value);
            } else if (parent.getClass().equals(NTService.class)) {
                return new t3.tic.bw5.project.properties.appmgmt.NTService((NTService) parent).addParameter(elementName, value);
//                return addNTServiceParameter((NTService) parent, elementName, value);
            } else if (parent.getClass().equals(Java.class)) {
                return new t3.tic.bw5.project.properties.appmgmt.Java((Java) parent).addParameter(elementName, value);
//                return addJavaParameter((Java) parent, elementName, value);
            } else if (parent.getClass().equals(Shutdown.class)) {
                return new t3.tic.bw5.project.properties.appmgmt.Shutdown((Shutdown) parent).addParameter(elementName, value);
//                return addShutdownParameter((Shutdown) parent, elementName, value);
            //
            // Bwprocess children
            } else if (parent.getClass().equals(Bwprocess.class)) {
                return new t3.tic.bw5.project.properties.appmgmt.Bwprocess((Bwprocess) parent).addParameter(elementName, value);
//                addBWProcessParameter((Bwprocess) parent, elementName, value);
            } else if (parent.getClass().equals(FaultTolerant.class)) {
                return new t3.tic.bw5.project.properties.appmgmt.FaultTolerant((FaultTolerant) parent).addParameter(elementName, value);
//                addFaultTolerantParameter((FaultTolerant) parent, elementName, value);
            } else if (parent.getClass().equals(Checkpoints.class)) {
                return new t3.tic.bw5.project.properties.appmgmt.Checkpoints((Checkpoints) parent).addParameter(elementName, value);
//                addCheckpointsParameter((Checkpoints) parent, elementName, value);
            // Bw chidren (direct children)
            } else if (parent.getClass().equals(Bw.class)) {
                return ((Bw) parent).addParameter(elementName, value);
    //                return addBwParameter((Bw) parent, elementName, value);
            }
        }

        return parent;
    }

}
