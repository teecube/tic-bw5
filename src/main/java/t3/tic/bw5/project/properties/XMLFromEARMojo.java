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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;

import t3.plugin.annotations.Mojo;
import t3.plugin.annotations.Parameter;
import t3.tic.bw5.BW5Artifact;
import t3.tic.bw5.BW5MojoInformation;
import t3.tic.bw5.Messages;

/**
 * <p>
 * This goal extracts the TIBCO XML Deployment Descriptor from a TIBCO
 * BusinessWorks EAR.
 * </p>
 *
 * @author Mathieu Debove &lt;mad@teecu.be&gt;
 *
 */
@Mojo(name="xml-from-ear", defaultPhase=LifecyclePhase.PREPARE_PACKAGE)
public class XMLFromEARMojo extends PackagingCommonMojo implements BW5Artifact {

	/**
	 * <p>
	 * Directory of the EAR to use for XML extraction
	 * (usually in "target/package").
	 * </p>
	 */
	@Parameter (property = BW5MojoInformation.BW5EAR.directory, required = true, defaultValue = BW5MojoInformation.BW5EAR.directory_default )
	protected File earDirectory;

	/**
	 * <p>
	 * Path where to extract the TIBCO XML Deployment Descriptor from the EAR.
	 * </p>
	 */
	@Parameter (property = BW5MojoInformation.BW5EAR.Packaging.deploymentDescriptorBased, defaultValue = BW5MojoInformation.BW5EAR.Packaging.deploymentDescriptorBased_default, required = true)
	protected File deploymentDescriptor;

	@Override
    public File getArtifactFile(File basedir, String finalName, String classifier) {
		return super.getArtifactFile(earDirectory, finalName, classifier);
	}

	@Override
	public String getArtifactFileExtension() {
		return BWEAR_EXTENSION;
	}

	private void createXML() throws MojoExecutionException, IOException {
		String earPath = getOutputFile().getAbsolutePath();
		getLog().info(Messages.EXTRACT_XML_FROM_EAR + " '" + earPath + "'");

		String xmlOutputFile = deploymentDescriptor.getAbsolutePath();

		ArrayList<String> arguments = new ArrayList<String>();
		arguments.add("-export");
		arguments.add("-max");
		arguments.add("-ear");
		arguments.add(earPath);
		arguments.add("-out");
		arguments.add(xmlOutputFile);

		ArrayList<File> tras = new ArrayList<File>();
		tras.add(tibcoAppManageTRA);

		executeTIBCOBinary(tibcoAppManage, tras, arguments, directory, Messages.CREATE_XML_FROM_EAR_FAILED);
	}

	public void execute() throws MojoExecutionException, MojoFailureException {
		if (super.skip()) {
			return;
		}

		try {
			createXML();
			getLog().info(Messages.CREATE_XML_FROM_EAR_SUCCESS + " '" + deploymentDescriptor + "'");
		} catch (IOException e) {
			throw new MojoExecutionException(Messages.CREATE_XML_FROM_EAR_FAILED, e);
		}
	}

}
