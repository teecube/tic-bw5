/**
 * (C) Copyright 2016-2019 teecube
 * (https://teecu.be) and others.
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
package t3.tic.bw5.project.projlib;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import t3.plugin.annotations.Mojo;
import t3.plugin.annotations.Parameter;
import t3.tic.bw5.BW5Artifact;
import t3.tic.bw5.BW5MojoInformation;
import t3.tic.bw5.Messages;
import t3.tic.bw5.project.BW5ProjectCommonMojo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * <p>
 * This goal compiles the TIBCO BusinessWorks Projlib.
 * </p>
 * <p>
 * The TIBCO <i>buildlibrary</i> command line tool is used.
 * </p>
 *
 * @author Mathieu Debove &lt;mad@teecu.be&gt;
 *
 */
@Mojo(name="compile-projlib", defaultPhase = LifecyclePhase.COMPILE, requiresProject = true)
public class CompileProjlibMojo extends BW5ProjectCommonMojo implements BW5Artifact {

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
     * Path to the "LibBuilder" relatively to the BusinessWorks project path.
     * </p>
     */
    @Parameter (property=BW5MojoInformation.BW5Projlib.libraryBuilder, required = true, requiredForPackagings = {"bw5-projlib"}, defaultValue = "")
    protected String libraryBuilder;

    /**
     * <p>
     * Whether to skip the compilation of EAR <b>and</b> Projlib.
     * </p>
     */
    @Parameter (property = BW5MojoInformation.BW5Project.skipCompile, required=false, defaultValue=BW5MojoInformation.BW5Project.skipCompile_default)
    protected Boolean skipCompile;

    /**
     * <p>
     * Whether to skip the compilation of Projlib.
     * </p>
     */
    @Parameter (property=BW5MojoInformation.BW5Projlib.skip, required=false, defaultValue=BW5MojoInformation.BW5Projlib.skip_default)
    protected Boolean skipProjlibCompile;

    /**
     * <p>
     * Whether to "touch" the Projlib file when Projlib compilation is skipped.
     * </p>
     * <p>
     * NB: must be used with 'tibco.bw5.compile.skip' or
     * 'tibco.bw5.compile.projlib.skip' set to true.
     * </p>
     */
    @Parameter (property=BW5MojoInformation.BW5Projlib.skipTouch, required=false, defaultValue=BW5MojoInformation.BW5Projlib.skipTouch_default)
    protected Boolean touchProjlibIfSkipped;

    @Override
    public String getArtifactFileExtension() {
        return ".projlib"; // TODO : externalize
    }

    /**
     * <P>
     * This calls the "buildlibrary" binary found in TIBCO Designer to build
     * a Projlib for the current project, defined by the
     * {@link CompileProjlibMojo#libraryBuilder}.
     * </p>
     *
     * @param outputFile, the path where the Projlib output will be created
     * @throws MojoExecutionException
     * @throws IOException
     */
    private void buildProjlib(File outputFile) throws MojoExecutionException, IOException {
        assert(outputFile != null);

        ArrayList<String> arguments = new ArrayList<String>();
        arguments.add("-lib"); // path of the libbuilder relative to the BW project path
        arguments.add(libraryBuilder);
        arguments.add("-p"); // BW project path
        arguments.add(targetProjectSource.getAbsolutePath());
        arguments.add("-o"); // output file
        arguments.add(outputFile.getAbsolutePath());
        arguments.add("-x"); // overwrite the output
        if (!hideLibraryResources) {
            arguments.add("-v"); // validate the project
        }

        getLog().info(Messages.BUILDING_PROJLIB);

        ArrayList<File> tras = new ArrayList<File>();
        tras.add(tibcoBuildLibraryTRA);
//        if (tibcoBuildLibraryUseDesignerTRA) {
            tras.add(tibcoDesignerTRA);
//        }
        executeTIBCOBinary(tibcoBuildLibrary, tras, arguments, directory, Messages.BUILD_PROJLIB_FAILED);
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skipCompile || skipProjlibCompile) {
            getLog().info(Messages.SKIPPING_PROJLIB);

            File outputFile = getOutputFile(false);
            if (outputFile != null && !outputFile.exists()
                    && touchProjlibIfSkipped) {
                // Projlib was not created because compilation is skipped
                // however we "touch" the Projlib file so there is an empty
                // Projlib file created
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

        super.execute();
        translateDependenciesModels(hideLibraryResources);

        File outputFile = getOutputFile(false);

        try {
            buildProjlib(outputFile);
        } catch (IOException e) {
            throw new MojoExecutionException(Messages.BUILD_PROJLIB_FAILED, e);
        }

        attachArtifact(outputFile);
    }

}
