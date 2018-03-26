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
package t3.tic.bw5;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.tools.ant.taskdefs.optional.ReplaceRegExp;

import t3.AdvancedMavenLifecycleParticipant;
import t3.CommonTIBCOMojo;
import t3.plugin.annotations.GlobalParameter;

/**
 *
 * @author Mathieu Debove &lt;mad@teecu.be&gt;
 *
 */
public abstract class BW5CommonMojo extends CommonTIBCOMojo {

	/* BW */
	@GlobalParameter (property = BW5MojoInformation.BW5.bwVersion, category = BW5MojoInformation.BW5.category, valueGuessedByDefault = false)
	protected String tibcoBW5Version;

	@GlobalParameter (property = BW5MojoInformation.BW5.bwBin, defaultValue = BW5MojoInformation.BW5.bwBin_default, category = BW5MojoInformation.BW5.category)
	protected File tibcoBW5Bin;

	@GlobalParameter (property = BW5MojoInformation.BW5.bwEngine, defaultValue = BW5MojoInformation.BW5.bwEngine_default, category = BW5MojoInformation.BW5.category)
	protected File tibcoBW5Engine;

	@GlobalParameter (property = BW5MojoInformation.BW5.bwEngineTRA, defaultValue = BW5MojoInformation.BW5.bwEngineTRA_default, category = BW5MojoInformation.BW5.category)
	protected File tibcoBW5EngineTRA;

	/* Designer */
	@GlobalParameter (property = BW5MojoInformation.BW5.designerVersion, category = BW5MojoInformation.BW5.category, valueGuessedByDefault = false)
	protected String tibcoDesignerVersion;

	@GlobalParameter (property = BW5MojoInformation.BW5.designerBin, defaultValue = BW5MojoInformation.BW5.designerBin_default, category = BW5MojoInformation.BW5.category)
	protected File tibcoDesignerBin;

	@GlobalParameter (property = BW5MojoInformation.BW5.buildLibrary, defaultValue = BW5MojoInformation.BW5.buildLibrary_default, category = BW5MojoInformation.BW5.category)
	protected File tibcoBuildLibrary;

	@GlobalParameter (property = BW5MojoInformation.BW5.buildLibraryTRA, defaultValue = BW5MojoInformation.BW5.buildLibraryTRA_default, category = BW5MojoInformation.BW5.category)
	protected File tibcoBuildLibraryTRA;

	@GlobalParameter (property = BW5MojoInformation.BW5.designer, defaultValue = BW5MojoInformation.BW5.designer_default, category = BW5MojoInformation.BW5.category)
	protected File tibcoDesigner;

	@GlobalParameter (property = BW5MojoInformation.BW5.designerTRA, defaultValue = BW5MojoInformation.BW5.designerTRA_default, category = BW5MojoInformation.BW5.category)
	protected File tibcoDesignerTRA;

	/* TRA */
	@GlobalParameter (property = BW5MojoInformation.BW5.traVersion, category = BW5MojoInformation.BW5.category, valueGuessedByDefault = false)
	protected String tibcoTRAVersion;

	@GlobalParameter (property = BW5MojoInformation.BW5.traBin, defaultValue = BW5MojoInformation.BW5.traBin_default, category = BW5MojoInformation.BW5.category)
	protected File tibcoTRABin;

	@GlobalParameter(property = BW5MojoInformation.BW5.appManage, defaultValue = BW5MojoInformation.BW5.appManage_default, category = BW5MojoInformation.BW5.category)
	protected File tibcoAppManage;

	@GlobalParameter (property = BW5MojoInformation.BW5.appManageTRA, defaultValue = BW5MojoInformation.BW5.appManageTRA_default, category = BW5MojoInformation.BW5.category)
	protected File tibcoAppManageTRA;

	@GlobalParameter (property = BW5MojoInformation.BW5.buildEAR, defaultValue = BW5MojoInformation.BW5.buildEAR_default, category = BW5MojoInformation.BW5.category)
	protected File tibcoBuildEAR;

	@GlobalParameter (property = BW5MojoInformation.BW5.buildEARTRA, defaultValue = BW5MojoInformation.BW5.buildEARTRA_default, category = BW5MojoInformation.BW5.category)
	protected File tibcoBuildEARTRA;

	/**
	 * <p>
	 * Timeout for the execution of any TIBCO binary.
	 * This time is given in seconds.
	 * </p>
	 * <p>
	 * Default value is: 180 seconds (3 minutes)
	 * </p>
	 */
	@GlobalParameter (property = BW5MojoInformation.BW5.timeout, defaultValue = BW5MojoInformation.BW5.timeout_default, category = BW5MojoInformation.BW5.category)
	protected int timeOut;

	@Override
	protected int getTimeOut() {
		return timeOut;
	}

	@Override
	protected void prepareTRAs(List<File> tras, HashMap<File, File> trasMap) throws IOException {
		boolean tibcoBuildEARUseDesignerTRA = true;
		boolean tibcoBuildLibraryUseDesignerTRA = true;

		for (File tra : trasMap.keySet()) {
			if (trasMap.containsKey(tibcoDesignerTRA) &&
				((tibcoBuildEARUseDesignerTRA && tra == tibcoBuildEARTRA) ||
				 (tibcoBuildLibraryUseDesignerTRA && tra == tibcoBuildLibraryTRA))
			   ) {
				if (tras.size() > 1) {
					ReplaceRegExp replaceRegExp = new ReplaceRegExp();
					replaceRegExp.setFile(trasMap.get(tra));
					replaceRegExp.setMatch("tibco.include.tra (.*/designer.tra)");
					replaceRegExp.setReplace("tibco.include.tra " + trasMap.get(tibcoDesignerTRA).toString().replace('\\', '/'));
					replaceRegExp.setByLine(true);

					replaceRegExp.execute();
				}
			}

			if (tra == tibcoBuildEARTRA ||
				tra == tibcoDesignerTRA ||
				tra == tibcoBW5EngineTRA) { //TODO: really check file with their paths
				// append user.home at the end to force the use of custom Designer5.prefs
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(trasMap.get(tra), true)));
				out.println("");
				out.println("java.property.user.home=" + directory.getAbsolutePath().replace("\\", "/"));
				out.close();
			}
		}
	}

	@Override
	protected AdvancedMavenLifecycleParticipant getLifecycleParticipant() throws MojoExecutionException {
		return new BW5LifecycleParticipant();
	}

}
