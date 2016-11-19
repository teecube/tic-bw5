/**
 * (C) Copyright 2014-2016 teecube
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;

import t3.SortedProperties;
import t3.plugin.annotations.Mojo;
import t3.plugin.annotations.Parameter;
import t3.tic.bw5.BW5MojoInformation;
import t3.tic.bw5.Messages;
import t3.tic.bw5.project.properties.appmgmt.ApplicationMarshaller;
import t3.tic.bw5.project.properties.appmgmt.ApplicationType;

/**
 * <p>
 * This goal generates a TIBCO XML Deployment Descriptor from properties files.
 * </p>
 *
 * @author Mathieu Debove &lt;mad@teecu.be&gt;
 *
 */
@Mojo(name="properties-to-xml", defaultPhase=LifecyclePhase.PACKAGE)
public class PropertiesToXMLMojo extends PackagingCommonMojo {

	/**
	 * <p>
	 * Path to the base TIBCO XML Deployment Descriptor to use (usually the one
	 * extracted from a TIBCO BusinessWorks EAR).
	 * </p>
	 */
	@Parameter (property = BW5MojoInformation.BW5EAR.Packaging.deploymentDescriptorBased, defaultValue = BW5MojoInformation.BW5EAR.Packaging.deploymentDescriptorBased_default, required = true)
	protected File deploymentDescriptorBase;

	/**
	 * <p>
	 * Path to the base TIBCO XML Deployment Descriptor to use (usually the one
	 * extracted from a TIBCO BusinessWorks EAR).
	 * </p>
	 */
	@Parameter (property = BW5MojoInformation.BW5EAR.Packaging.deploymentDescriptorMerged, defaultValue = BW5MojoInformation.BW5EAR.Packaging.deploymentDescriptorMerged_default, required = true)
	protected File deploymentDescriptorMerged;

	/**
	 * <p>
	 * Path to the properties file for Global Variables used to generate a TIBCO
	 * XML Deployment Descriptor file.
	 * </p>
	 */
	@Parameter (property=BW5MojoInformation.BW5EAR.Packaging.Properties.Merged.propertiesGlobalVariablesMerged, defaultValue = BW5MojoInformation.BW5EAR.Packaging.Properties.Merged.propertiesGlobalVariablesMerged_default, required = true)
	protected File propertiesGlobalVariables;

	/**
	 * <p>
	 * Path to the properties file for Services used to generate a TIBCO XML
	 * Deployment Descriptor file.
	 * </p>
	 */
	@Parameter (property=BW5MojoInformation.BW5EAR.Packaging.Properties.Merged.propertiesServicesMerged, defaultValue = BW5MojoInformation.BW5EAR.Packaging.Properties.Merged.propertiesServicesMerged_default, required = true)
	protected File propertiesServices;

	protected static final String FLAT_PATH_SEPARATOR = "/";

	private ApplicationMarshaller applicationManager;
	private ApplicationType application;

	/**
	 * <p>
	 * First we will copy the XML file extracted from the EAR to the final file
	 * to be produced after merging all the properties files.
	 * </p>
	 * 
	 * <p>
	 * Then we will initialize the {@link ApplicationType} by unmarshalling
	 * this final file.
	 * </p>
	 * 
	 * @throws MojoExecutionException
	 */
	private void init() throws MojoExecutionException {
		try {
			FileUtils.copyFile(deploymentDescriptorBase, deploymentDescriptorMerged);
		} catch (IOException e) {
			throw new MojoExecutionException(Messages.APPLICATION_MANAGEMENT_COPY_FAILURE + " '" + deploymentDescriptorBase + "' to '" + deploymentDescriptorMerged + "'", e);
		}
		try {
			applicationManager = new ApplicationMarshaller(deploymentDescriptorMerged);
			application = applicationManager.getObject();
		} catch (JAXBException e) {
			throw new MojoExecutionException(Messages.APPLICATION_MANAGEMENT_LOAD_FAILURE + " '" + deploymentDescriptorMerged + "'", e);
		}
	}

	/**
	 * <p>
	 * This will merge the Global Variables properties file into the
	 * {@link ApplicationType} object.
	 * </p>
	 * 
	 * @throws MojoExecutionException
	 */
	private void mergeGlobalVariables() throws MojoExecutionException {
		Properties globalVariables;
		try {
			globalVariables = SortedProperties.loadPropertiesFile(propertiesGlobalVariables, this.sourceEncoding);
		} catch (Exception e) {
			throw new MojoExecutionException(Messages.PROPERTIES_LOAD_GVS_FAILURE + " '" + propertiesGlobalVariables + "'", e);
		}
		
		Enumeration<Object> e = globalVariables.keys();
   		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			String value = globalVariables.getProperty(key);

			application.setGlobalVariable(key, value);
		}
	}

	private void mergeServices() throws MojoExecutionException {
		Properties services;
		try {
			services = SortedProperties.loadPropertiesFile(propertiesServices, this.sourceEncoding);
		} catch (Exception e) {
			throw new MojoExecutionException(Messages.PROPERTIES_LOAD_SERVICES_FAILURE + " '" + propertiesServices + "'", e);
		}
		
		Pattern patternElements = Pattern.compile("(\\w+(\\[[\\w -\\.\\/?]*\\])?)+");

		Enumeration<Object> e = services.keys();
   		while (e.hasMoreElements()) {
			String currentPath = "";
			
			String key = (String) e.nextElement();
			String value = services.getProperty(key);
			
			Matcher matcherElements;
			Object parent = null;
			while ((matcherElements = patternElements.matcher(key)).find()) {
				String element = matcherElements.group();
				currentPath = currentPath + FLAT_PATH_SEPARATOR + element;
				
				parent = applicationManager.getElement(currentPath, element, value, parent);
				
				if (key.equals(element)) {
					break;
				}
				key = key.substring(element.length()+1);
			}
		}
		
		try {
			application.removeDefaultBindingIfNotExists(SortedProperties.loadPropertiesFile(propertiesServices, this.sourceEncoding));
		} catch (Exception ex) {
			throw new MojoExecutionException(Messages.PROPERTIES_LOAD_SERVICES_FAILURE + " '" + propertiesServices + "'", ex);
		}
		application.removeDuplicateDefaultBinding();
	}

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		if (super.skip()) {
			return;
		}

		getLog().info(Messages.PROPERTIES_LOAD_GVS_SUCCESS + " '" + propertiesGlobalVariables.getAbsolutePath() + "'");
		getLog().info(Messages.PROPERTIES_LOAD_SERVICES_SUCCESS + " '" + propertiesServices.getAbsolutePath() + "'");
		
		init();

		mergeGlobalVariables();
		mergeServices();
		
		try {
			applicationManager.save();
		} catch (UnsupportedEncodingException | FileNotFoundException | JAXBException e) {
			throw new MojoExecutionException(e.getLocalizedMessage(), e);
		}
	}

}
