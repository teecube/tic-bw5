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
package t3.tic.bw5.project.properties;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.xml.sax.SAXException;
import t3.SortedProperties;
import t3.plugin.annotations.Mojo;
import t3.plugin.annotations.Parameter;
import t3.tic.bw5.BW5MojoInformation;
import t3.tic.bw5.Messages;
import t3.tic.bw5.project.properties.appmgmt.ApplicationMarshaller;
import t3.tic.bw5.project.properties.appmgmt.ApplicationType;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.Properties;

/**
 * <p>
 * This goal converts the TIBCO XML Deployment Descriptor to properties files.
 * </p>
 *
 * @author Mathieu Debove &lt;mad@teecu.be&gt;
 *
 */
@Mojo(name="xml-to-properties", defaultPhase=LifecyclePhase.PREPARE_PACKAGE )
public class XMLToPropertiesMojo extends PackagingCommonMojo {

    /**
     * <p>
     * Path to the base TIBCO XML Deployment Descriptor to use (by default the one
     * <a href="./xml-from-ear-mojo.html#deploymentDescriptor">extracted from a TIBCO BusinessWorks EAR</a>).
     * </p>
     */
    @Parameter (property = BW5MojoInformation.BW5EAR.Packaging.deploymentDescriptorBased, defaultValue = BW5MojoInformation.BW5EAR.Packaging.deploymentDescriptorBased_default)
    protected File deploymentDescriptor;

    /**
     * <p>
     * Whether to filter properties files or not.
     * </p>
     */
    @Parameter (property=BW5MojoInformation.BW5EAR.Packaging.Properties.propertiesFilter, defaultValue = BW5MojoInformation.BW5EAR.Packaging.Properties.propertiesFilter_default)
    protected boolean filterProperties;

    /**
     * <p>
     * Path to the properties file for Global Variables (the one generated from
     * XML).
     * </p>
     */
    @Parameter (property=BW5MojoInformation.BW5EAR.Packaging.Properties.Extracted.propertiesExtractedGlobalVariables, defaultValue = BW5MojoInformation.BW5EAR.Packaging.Properties.Extracted.propertiesExtractedGlobalVariables_default)
    protected File propertiesGlobalVariables;

    /**
     * <p>
     * Path to the properties file for Services (the one generated from XML).
     * </p>
     */
    @Parameter (property=BW5MojoInformation.BW5EAR.Packaging.Properties.Extracted.propertiesExtractedServices, defaultValue = BW5MojoInformation.BW5EAR.Packaging.Properties.Extracted.propertiesExtractedServices_default)
    protected File propertiesServices;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (super.skip()) {
            return;
        }

        Properties earGlobalVariables = new Properties();
        Properties earBwServices = new Properties();

        try {
            ApplicationMarshaller applicationManagement = new ApplicationMarshaller(deploymentDescriptor);
            ApplicationType application = applicationManagement.getObject();

            earGlobalVariables = application.getGlobalVariablesProperties();
            earBwServices = application.getBwServicesProperties();

            getLog().info(Messages.XML_LOAD_SUCCESS + " '" + deploymentDescriptor + "'");
        } catch (JAXBException | SAXException e) {
            throw new MojoExecutionException(e.getLocalizedMessage(), e);
        }

        // Export Properties in a properties file
        //  Global Variables
        SortedProperties.savePropertiesToFile(propertiesGlobalVariables,
                                              earGlobalVariables,
                                              this.sourceEncoding,
                                              "Global Variables",
                                              Messages.PROPERTIES_SAVE_GVS_SUCCESS,
                                              Messages.PROPERTIES_SAVE_GVS_FAILURE,
                                              filterProperties,
                                              this);

        //  Services (=~ Process Archives)
        SortedProperties.savePropertiesToFile(propertiesServices,
                                              earBwServices,
                                              this.sourceEncoding,
                                              "Services (Bindings, Processes)",
                                              Messages.PROPERTIES_SAVE_SERVICES_SUCCESS,
                                              Messages.PROPERTIES_SAVE_SERVICES_FAILURE,
                                              filterProperties,
                                              this);
    }

}
