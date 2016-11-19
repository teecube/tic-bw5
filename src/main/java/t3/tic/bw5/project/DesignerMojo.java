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
package t3.tic.bw5.project;

import static org.apache.commons.io.FileUtils.copyFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.ResolutionScope;

import t3.SortedProperties;
import t3.plugin.annotations.Mojo;
import t3.plugin.annotations.Parameter;
import t3.tic.bw5.BW5MojoInformation;
import t3.tic.bw5.Messages;

/**
 * <p>
 * This goal launches the TIBCO Designer.
 * </p>
 *
 * @author Mathieu Debove &lt;mad@teecu.be&gt;
 *
 */
@Mojo(name="designer", defaultPhase = LifecyclePhase.PROCESS_TEST_RESOURCES, requiresDependencyResolution = ResolutionScope.TEST)
@Execute(phase=LifecyclePhase.PROCESS_TEST_RESOURCES)
public class DesignerMojo extends BW5ProjectCommonMojo {

	/**
	 * <p>
	 * If true, the project copied in "target/src" is used to launch
	 * TIBCO Designer.
	 * </p>
	 */
	@Parameter (property=BW5MojoInformation.BW5Project.Designer.useCopy, required=false, defaultValue=BW5MojoInformation.BW5Project.Designer.useCopy_default)
	protected Boolean useBuildSrcDirectory;

	/**
	 * <p>
	 * If true, ".aliaslib" files can use parameters such as ${project.version}.
	 * </p>
	 */
	@Parameter (property=BW5MojoInformation.BW5Project.Designer.adaptAliases, required=false, defaultValue=BW5MojoInformation.BW5Project.Designer.adaptAliases_default)
	protected Boolean adaptAliases;

	@Override
	public String getArtifactFileExtension() {
		return null;
	}

	/**
	 * <p>
	 * This method copies the '.designtimelibs' of the "target/src" directory
	 * to the actual project's source folder
	 * </p>
	 *
	 * @throws IOException
	 */
	private void copyDesignTimeLibs() throws IOException {
		copyFile(new File(targetProjectTestSource + "/" + DTL_FILE_NAME),
				 new File(projectSource + "/" + DTL_FILE_NAME));
	}

	private void updateAliasesFile() throws IOException {
		File aliasesFile = getAliasesFile();
		File designer5Prefs = getDesigner5Prefs();

		Properties prefs = new SortedProperties();
		FileInputStream fisPrefs = new FileInputStream(designer5Prefs);
		prefs.load(fisPrefs);
		fisPrefs.close();

		Integer maxFileAliasPref = 0;
		for (Object k : prefs.keySet()) {
			String key = (String) k;

			if (key.startsWith(FILE_ALIAS_PREFIX)) {
				maxFileAliasPref++;
			}
		}

		Properties aliases = new Properties();
		FileInputStream fis = new FileInputStream(aliasesFile);
		aliases.load(fis);
		fis.close();

		String projectVersion = this.project.getVersion();
		Properties duplicates = new Properties();

		for (Object k : aliases.keySet()) {
			String key = (String) k;
			String value = aliases.getProperty(key);
			if (key.contains(projectVersion) && key.endsWith(":jar")) {
				getLog().debug(key);
				key = key.replace(projectVersion, "${project.version}");
				duplicates.put(key, value);
			}
		}

		if (!duplicates.isEmpty()) {
			for (Object k : duplicates.keySet()) {
				String key = (String) k;
				String value = duplicates.getProperty(key);
				key = key.replace(TIBCO_ALIAS_PREFIX, "");

				prefs.put(FILE_ALIAS_PREFIX + maxFileAliasPref.toString(), key + "=" + value);
				maxFileAliasPref++;
			}

			FileOutputStream fosPrefs = new FileOutputStream(designer5Prefs);
			prefs.store(fosPrefs, "");
			fis.close();

			aliases.putAll(duplicates);

			FileOutputStream fos = new FileOutputStream(aliasesFile);
			aliases.store(fos, "");
			fis.close();
		}
	}

	/**
	 *
	 * @throws MojoExecutionException
	 * @throws IOException
	 */
	private void launchDesigner() throws MojoExecutionException, IOException {
		copyDesignTimeLibs();

		ArrayList<String> arguments = new ArrayList<String>();
		if (useBuildSrcDirectory) {
			arguments.add(targetProjectSource.getAbsolutePath()); // using the one in "target/src"
			getLog().info(Messages.PROJECT_LOCATION + targetProjectSource.getAbsolutePath());
		} else {
			arguments.add(projectSource.getAbsolutePath()); // real BW project path (not the one in "target/src")
			getLog().info(Messages.PROJECT_LOCATION + projectSource.getAbsolutePath());
		}

		getLog().info(Messages.LAUNCHING_DESIGNER);

		ArrayList<File> tras = new ArrayList<File>();
		tras.add(tibcoDesignerTRA);

		executeTIBCOBinary(tibcoDesigner, tras, arguments, directory, Messages.LAUNCH_DESIGNER_FAILED, true, false);
	}

	public void execute() throws MojoExecutionException, MojoFailureException {
		enableTestScope();

		super.execute();
		translateDependenciesModels(false);

		if (adaptAliases) {
			try {
				updateAliasesFile();
			} catch (IOException e) {
				throw new MojoExecutionException(e.getLocalizedMessage(), e);
			}
		}

		try {
			launchDesigner();
		} catch (IOException e) {
			throw new MojoExecutionException(Messages.LAUNCH_DESIGNER_FAILED, e);
		}
	}

}
