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
package t3.tic.bw5.project.ear;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.ResolutionScope;

import t3.plugin.annotations.Mojo;
import t3.plugin.annotations.Parameter;
import t3.tic.bw5.BW5Artifact;
import t3.tic.bw5.BW5MojoInformation;
import t3.tic.bw5.Messages;
import t3.tic.bw5.project.BW5ProjectCommonMojo;
import t3.tic.bw5.project.projlib.CompileProjlibMojo;

/**
 * <p>
 * This goal compiles the TIBCO BusinessWorks EAR.
 * </p>
 * <p>
 * The TIBCO <i>buildear</i> command line tool is used.
 * </p>
 *
 * @author Mathieu Debove &lt;mad@teecu.be&gt;
 *
 */
@Mojo(name="compile-ear", defaultPhase = LifecyclePhase.COMPILE, requiresProject = true, requiresDependencyResolution = ResolutionScope.TEST)
public class CompileEARMojo extends BW5ProjectCommonMojo implements BW5Artifact {

	/**
	 * <p>
	 * Path to the "ArchiveBuilder" relatively to the BusinessWorks project
	 * path.
	 * </p>
	 */
	@Parameter (property=BW5MojoInformation.BW5EAR.archiveBuilder, required=true, defaultValue = "")
	protected String archiveBuilder;

	/**
	 * <p>
	 * Allows to ignore any alias when building EAR or Projlib.
	 * This will mimic the "Hide Library Resources" behaviour of the TIBCO
	 * Designer.
	 * </p>
	 */
	@Parameter (property = BW5MojoInformation.BW5Project.hideLibraryResources, defaultValue = BW5MojoInformation.BW5Project.hideLibraryResources_default)
	protected boolean hideLibraryResources;

    /**
     * <p>
     * Whether to skip the compilation of EAR <b>and</b> Projlib.
     * </p>
     */
    @Parameter (property = BW5MojoInformation.BW5Project.skipCompile, required=false, defaultValue=BW5MojoInformation.BW5Project.skipCompile_default)
    protected Boolean skipCompile;

	/**
	 * <p>
	 * Whether to skip the compilation of EAR.
	 * </p>
	 */
    @Parameter (property=BW5MojoInformation.BW5EAR.skip, required=false, defaultValue=BW5MojoInformation.BW5EAR.skip_default)
    protected Boolean skipEARCompile;

	/**
	 * <p>
	 * Whether to "touch" the EAR file when EAR compilation is skipped.
	 * </p>
	 * <p>
	 * NB: must be used with 'tibco.bw5.compile.skip' or
	 * 'tibco.bw5.compile.ear.skip' set to true.
	 * </p>
	 */
    @Parameter (property=BW5MojoInformation.BW5EAR.skipTouch, required=false, defaultValue=BW5MojoInformation.BW5EAR.skipTouch_default)
    protected Boolean touchEARIfSkipped;

	/**
	 * <p>
	 * Directory where the EAR will be built (usually "target/package").
	 * </p>
	 */
	@Parameter (property = BW5MojoInformation.BW5EAR.directory, required = true, defaultValue = BW5MojoInformation.BW5EAR.directory_default )
	protected File earDirectory;

	@Override
    public File getArtifactFile(File basedir, String finalName, String classifier) {
		return super.getArtifactFile(earDirectory, finalName, classifier);
	}

	@Override
	public String getArtifactFileExtension() {
		return BWEAR_EXTENSION;
	}

	/**
	 * <p>
	 * Create the output directory (usually "target/package") if it doesn't
	 * exist yet.
	 * </p>
	 */
	@Override
	protected void createOutputDirectory() {
		if (earDirectory == null || earDirectory.exists()) {
			return;
		}

		earDirectory.mkdirs();
	}

	/**
	 * <p>
	 * This calls the "buildlibrary" binary found in TIBCO Designer to build
	 * a Projlib for the {@link AbstractBWMojo#project}, defined by the
	 * {@link CompileProjlibMojo#libraryBuilder}.
	 * </p>
	 *
	 * @param outputFile, the path where the Projlib output will be created
	 * @throws MojoExecutionException
	 * @throws IOException
	 */
    private void buildEAR(File outputFile) throws MojoExecutionException, IOException {
        assert (outputFile != null);

        ArrayList<String> arguments = new ArrayList<String>();
        arguments.add("-ear"); // path of the Enterprise Archive "builder" relative to the BW project path
        arguments.add(archiveBuilder);
        arguments.add("-p"); // BW project path
        arguments.add(targetProjectSource.getAbsolutePath());
        arguments.add("-o"); // output file
        arguments.add(outputFile.getAbsolutePath());
        arguments.add("-x"); // overwrite the output
        arguments.add("-v"); // validate the project
        File aliasesFile = new File(directory, ALIASES_FILE);
        if (aliasesFile.exists()) {
            arguments.add("-a");
            arguments.add(aliasesFile.getAbsolutePath());
        }
        getLog().info(Messages.BUILDING_EAR);

        ArrayList<File> tras = new ArrayList<File>();
        tras.add(tibcoBuildEARTRA);
//        if (tibcoBuildEARUseDesignerTRA) {
            tras.add(tibcoDesignerTRA);
//        }
		executeTIBCOBinary(tibcoBuildEAR, tras, arguments, directory, Messages.BUILD_EAR_FAILED);
	}

	@Override
    public void execute() throws MojoExecutionException, MojoFailureException {
		if (skipCompile || skipEARCompile) {
			getLog().info(Messages.SKIPPING_EAR);

			File outputFile = getOutputFile(false);
			if (outputFile != null && !outputFile.exists() && touchEARIfSkipped) {
				// EAR was not created because compilation is skipped
				// however we "touch" the EAR file so there is an empty EAR file
				// created
				try {
					outputFile.getParentFile().mkdirs();
					outputFile.createNewFile();
				} catch (IOException e) {
					throw new MojoExecutionException(e.getLocalizedMessage(), e);
				}
			}
			if (outputFile != null && outputFile.exists()) {
				attachArtifact(outputFile);
			} else {
				getLog().warn(Messages.WARN_NO_ARTIFACT_ATTACHED);
			}
			return;
		}

        if (isCurrentGoal(DESIGNER_GOAL)) {
            return; // ignore
        }

//        doCleanDefaultVars();

        super.execute();
        translateDependenciesModels(hideLibraryResources);

        File outputFile = getOutputFile(false);
        getLog().debug(outputFile.getAbsolutePath());

        try {
            buildEAR(outputFile);
            if (!outputFile.exists() || outputFile.length() == 0) {
				throw new MojoExecutionException(Messages.BUILD_EAR_FAILED); // TODO: specialize error
            }
    		getLog().info(Messages.EAR_CREATED + " '" + outputFile + "'");
        } catch (IOException e) {
            throw new MojoExecutionException(Messages.BUILD_EAR_FAILED, e);
        }

        attachArtifact(outputFile);
    }

}
