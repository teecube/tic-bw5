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
package t3.tic.bw5.project;

import com.google.common.base.Predicate;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.tools.ant.taskdefs.optional.ReplaceRegExp;
import t3.plugin.annotations.GlobalParameter;
import t3.tic.bw5.BW5CommonMojo;
import t3.tic.bw5.BW5MojoInformation;
import t3.tic.bw5.Messages;

import java.io.*;
import java.util.List;

import static org.apache.commons.io.FileUtils.copyFile;

/**
 * <p>
 *  There are two kinds of BW5 projects:
 * </p>
 *  <ul>
 *   <li>BW5 EAR</li>
 *   <li>BW5 Projlib</li>
 *  </ul>
 * <p>
 *  They all have in common:
 * </p>
 *  <ul>
 *   <li>a main folder holding the source (with a defaultVars subfolder, a
 *   .folder file and a vcrepo.dat file</li>
 *  </ul>
 * <p>
 * This abstract Mojo (which can be inherited by concrete ones) defines all
 * these common objects as Mojo parameters.
 * </p>
 * <p>
 * This abstract Mojo also translates the Maven dependency model into the
 * TIBCO dependency model (.designtimelibs + aliases file + aliases in Designer
 * configuration).
 * </p>
 *
 * @author Mathieu Debove &lt;mad@teecu.be&gt;
 *
 */
public abstract class BW5ProjectCommonMojo extends BW5CommonMojo {

    /**
     * Name of the generated artifact (without file extension).
     */
    @GlobalParameter(property = "project.build.finalName", required = true, category = BW5MojoInformation.mavenCategory)
    protected String finalName;

    @GlobalParameter(property = "project.build.classifier", category = BW5MojoInformation.mavenCategory)
    protected String classifier;

    @GlobalParameter (property = BW5MojoInformation.BW5Project.source, defaultValue = BW5MojoInformation.BW5Project.source_default, category = BW5MojoInformation.BW5Project.category)
    protected File projectSource;

    @GlobalParameter (property = BW5MojoInformation.BW5Project.relativeSource, defaultValue = BW5MojoInformation.BW5Project.relativeSource_default, category = BW5MojoInformation.BW5Project.category)
    protected File projectRelativeSource;

    @GlobalParameter (property = BW5MojoInformation.BW5Project.targetSource, defaultValue = BW5MojoInformation.BW5Project.targetSource_default, category = BW5MojoInformation.BW5Project.category)
    protected File targetProjectSource;

    @GlobalParameter (property = BW5MojoInformation.BW5Project.targetLib, defaultValue = BW5MojoInformation.BW5Project.targetLib_default, category = BW5MojoInformation.BW5Project.category)
    protected File targetProjectLibs;

    @GlobalParameter (property = BW5MojoInformation.BW5Project.targetTestSource, defaultValue = BW5MojoInformation.BW5Project.targetTestSource_default, category = BW5MojoInformation.BW5Project.category)
    protected File targetProjectTestSource;

    @GlobalParameter (property = BW5MojoInformation.BW5Project.targetTestLib, defaultValue = BW5MojoInformation.BW5Project.targetTestLib_default, category = BW5MojoInformation.BW5Project.category)
    protected File targetProjectTestLibs;

    protected List<Dependency> resolvedDependenciesBWEAR;

    protected List<Dependency> resolvedDependenciesJAR;

    protected List<Dependency> resolvedDependenciesProjlib;

    protected final static String ALIASES_FILE = "aliases.properties";
    protected final static String DESIGNER5_PREFS = "Designer5.prefs";
    protected final static String DTL_FILE_NAME = ".designtimelibs";
    protected final static String FILE_ALIAS_PREFIX = "filealias.pref.";
    protected final static String FILE_ALIAS_PREFIX_ESCAPED = "filealias\\.pref\\.";
    protected final static String TIBCO_HOME_DIR = ".TIBCO";
    protected final static String TIBCO_ALIAS_PREFIX = "tibco.alias.";

    public final static String DESIGNER_GOAL = "bw5:designer";

    public final static String BWEAR_EXTENSION = ".ear";
    public final static String BWEAR_TYPE = "bw5-ear";
    public final static String PROJLIB_EXTENSION = ".projlib";
    public final static String PROJLIB_TYPE = "bw5-projlib";
    public final static String PROPERTIES_EXTENSION = ".properties";
    public final static String JAR_EXTENSION = ".jar";
    public final static String JAR_TYPE = "jar";
    public final static String XML_EXTENSION = ".xml";

    protected void attachArtifact(File outputFile) {
        project.getArtifact().setFile(outputFile);
    }

    protected void enableTestScope() {
        super.enableTestScope();
        targetProjectSource = targetProjectTestSource; // set buildSrcDirectory to "target/test/src" instead of "target/src"
        targetProjectLibs = targetProjectTestLibs; // set buildLibDirectory to "target/test/lib" instead of "target/lib"
    }

    public abstract String getArtifactFileExtension(); // to be overridden only if child class implements BW5ArtifactMojo, can return null otherwise

    /**
     * <p>
     * Retrieves the full path of the artifact that will be created.
     * </p>
     *
     * @param basedir, the directory where the artifact will be created
     * @param finalName, the name of the artifact, without file extension
     * @param classifier
     * @return a {@link File} object with the path of the artifact
     */
    public File getArtifactFile(File basedir, String finalName, String classifier) {
        if (classifier == null) {
            classifier = "";
        } else if (classifier.trim().length() > 0 && !classifier.startsWith("-")) {
            classifier = "-" + classifier;
        }

        return new File(basedir, finalName + classifier + getArtifactFileExtension());
    }

    public File getOutputFile() throws MojoExecutionException {
        return getOutputFile(true);
    }
    /**
     * @return the Maven artefact as a {@link File}
     * @throws MojoExecutionException
     */
    public File getOutputFile(boolean failIfNotFound) throws MojoExecutionException {
        File result = getArtifactFile(directory, finalName, classifier);
        if (result == null) {
            throw new MojoExecutionException(Messages.ARTIFACT_NOT_FOUND, new FileNotFoundException());
        } else if (!result.exists() && failIfNotFound) {
            throw new MojoExecutionException(Messages.ARTIFACT_NOT_FOUND, new FileNotFoundException(result.getAbsolutePath()));
        }
        return result;
    }

    /* -------------------- Dependency management --------------------------- */

    /**
     * @return the list of BW EARs Dependencies from the POM project
     * @throws MojoExecutionException
     */
    protected List<Dependency> getBWEARsDependencies() throws MojoExecutionException {
        if (resolvedDependenciesBWEAR != null) {
            return resolvedDependenciesBWEAR;
        }

        resolvedDependenciesBWEAR = getDependencies(
            new Predicate<Dependency>() {
                public boolean apply(Dependency d) {
                    return BWEAR_TYPE.equals(d.getType());
                }
                public boolean test(Dependency d) {
                    return BWEAR_TYPE.equals(d.getType());
                }
            }
        );

        return resolvedDependenciesBWEAR;
    }

    /**
     * @return the list of Projlib Dependencies from the POM project
     * @throws MojoExecutionException
     */
    protected List<Dependency> getProjlibsDependencies() throws MojoExecutionException {
        if (resolvedDependenciesProjlib != null) {
            return resolvedDependenciesProjlib;
        }

        resolvedDependenciesProjlib =  getDependencies(
                new Predicate<Dependency>() {
                    public boolean apply(Dependency d) {
                        return PROJLIB_TYPE.equals(d.getType());
                    }
                    public boolean test(Dependency d) {
                        return PROJLIB_TYPE.equals(d.getType());
                    }
                }
            );

        return resolvedDependenciesProjlib;
    }

    /**
     * @return the list of Jar Dependencies from the POM project
     * @throws MojoExecutionException
     */
    protected List<Dependency> getJarDependencies() throws MojoExecutionException {
        if (resolvedDependenciesJAR != null) {
            return resolvedDependenciesJAR;
        }

        resolvedDependenciesJAR = getDependencies(
                new Predicate<Dependency>() {
                    public boolean apply(Dependency d) {
                        return JAR_TYPE.equals(d.getType());
                    }
                    public boolean test(Dependency d) {
                        return JAR_TYPE.equals(d.getType());
                    }
                }
            );

        return resolvedDependenciesJAR;
    }

    /**
     * @param bw5EARDependency, a BW5 EAR dependency from Maven point-of-view
     * @return the name of the BW5 EAR artifact (without path but with
     * extension) : artifactId-version.ear
     */
    protected String getBWEARName(Dependency bw5EARDependency) {
        assert (bw5EARDependency != null);

        return bw5EARDependency.getArtifactId() + "-" + bw5EARDependency.getVersion() + BWEAR_EXTENSION;
    }

    /**
     * @param bw5EARDependency, a BW5 EAR dependency from Maven point-of-view
     * @return groupId:artifactId:version:bw-ear of the BW EAR dependency
     */
    protected String getBWEARAlias(Dependency bw5EARDependency) {
        assert (bw5EARDependency != null);

        return bw5EARDependency.getGroupId() + ":" + bw5EARDependency.getArtifactId() + ":" + bw5EARDependency.getVersion() + ":" + BWEAR_TYPE;
    }

    /**
     * @param jarDependency, a JAR dependency from Maven point-of-view
     * @param replaceDot, allows to replace dots in the version of the artifact
     * by underscores. This is because Maven will do so on the generated
     * artifact.
     *
     * @return the name of the Jar artifact (without path but with extension) :
     * artifactId-version.jar
     */
    protected String getJarName(Dependency jarDependency, boolean replaceDot) {
        assert (jarDependency != null);
        String version = jarDependency.getVersion();
        if (replaceDot) {
            version = version.replace('.', '_');
        }

        return jarDependency.getArtifactId() + "-" + version + JAR_EXTENSION;
    }

    /**
     * @param jarDependency, a JAR dependency from Maven point-of-view
     * @param replaceDot, allows to replace dots in the version of the artifact
     * by underscores. This is because Maven will do so on the generated
     * artifact.
     *
     * @return groupId:artifactId:version:jar of the JAR dependency
     */
    protected String getJarAlias(Dependency jarDependency, boolean replaceDot) {
        assert (jarDependency != null);
        String version = jarDependency.getVersion();
        if (replaceDot) {
            version = version.replace('.', '_');
        }

        return jarDependency.getGroupId() + ":" + jarDependency.getArtifactId() + ":" + version + ":" + JAR_TYPE;
    }

    /**
     * @param projlibDependency, a Projlib dependency from Maven point-of-view
     * @return the name of the Projlib artifact (without path but with
     * extension) : artifactId-version.projlib
     */
    protected String getProjlibName(Dependency projlibDependency) {
        assert (projlibDependency != null);

        return projlibDependency.getArtifactId() + "-" + projlibDependency.getVersion() + PROJLIB_EXTENSION;
    }

    /**
     * @param projlibDependency, a Projlib dependency from Maven point-of-view
     * @return groupId:artifactId:version:projlib of the Projlib dependency
     */
    protected String getProjlibAlias(Dependency projlibDependency) {
        assert (projlibDependency != null);

        return projlibDependency.getGroupId() + ":" + projlibDependency.getArtifactId() + ":" + projlibDependency.getVersion() + ":" + PROJLIB_TYPE;
    }

    /**
     * @param dependencyName, a dependency from Maven point-of-view, retrieved
     * with getJarName or getProjlibName
     * @return the absolute path of the dependency file (usually in "target/lib")
     */
    protected String getDependencyPath(String dependencyName) {
        return targetProjectLibs.getAbsolutePath().replace('\\', '/') + "/" + dependencyName;
    }

    // 1) aliases file

    /**
     * <p>
     * Aliases files must escape the ':' character of Maven dependency
     * coordinates syntax.
     * </p>
     *
     * @param alias
     * @return alias but with '\\:' replaced by ':'
     */
    private String formatAlias(String alias) {
        if (alias != null) {
            return alias.replace(":", "\\:");
        }
        return null;
    }

    /**
     *
     * @return the 'aliases.properties' file in the target folder
     */
    public File getAliasesFile() {
        return new File(directory, ALIASES_FILE);
    }

    /**
     * <p>
     * This will create an aliases file ('aliases.properties') that can be
     * provided to 'buildear' for instance to specify the JAR aliases.
     * It seems that the JAR aliases are not recognized by 'buildear' from
     * 'Designer5.prefs' whereas they are by TIBCO Designer.
     * </p>
     *
     * @throws IOException
     * @throws MojoExecutionException
     */
    private void copyAliasesFile() throws IOException, MojoExecutionException {
        // Create the aliases.properties in the target folder
        File aliasesFile = getAliasesFile();
        FileWriter file = null;
        BufferedWriter buffer = null;
        PrintWriter aliasesFileOut = null;
        try {
            file = new FileWriter(aliasesFile);
            buffer = new BufferedWriter(file);
            aliasesFileOut = new PrintWriter(buffer);

            // Copy all of the required aliases for the project, and map them with their path in Designer5.prefs
            for (Dependency dependency : getJarDependencies()) {
                String jarName = getJarName(dependency, false);
                String jarAlias = formatAlias(getJarAlias(dependency, false));
                String jarPath = getDependencyPath(jarName);

                if (new File(jarPath).exists()) {
                    aliasesFileOut.println(TIBCO_ALIAS_PREFIX + jarAlias + "=" + jarPath);
                }
            }

            for (Dependency dependency : getProjlibsDependencies()) {
                String projlibName = getProjlibName(dependency);
                String projlibAlias = formatAlias(getProjlibAlias(dependency));
                String projlibPath = getDependencyPath(projlibName);

                aliasesFileOut.println(TIBCO_ALIAS_PREFIX + projlibAlias + "=" + projlibPath);
            }
        } finally {
            aliasesFileOut.close();
            buffer.close();
            file.close();
        }
    }

    // 2) Designer preference file (Designer5.prefs)

    /**
     *
     * @return the 'Designer5.prefs' file in the target folder
     */
    protected File getDesigner5Prefs() throws IOException {
        // create a ".TIBCO" user_dir-like in build directory
        File homeTIBCO = new File(directory, TIBCO_HOME_DIR);
        if (!homeTIBCO.exists()) {
            homeTIBCO.mkdir();
        }

        File designer5Prefs = new File(homeTIBCO, DESIGNER5_PREFS);

        return designer5Prefs;
    }

    /**
     * <p>
     * This will create the 'Designer5.prefs' file in 'target/.TIBCO' which will
     * override the platform TIBCO_HOME directory.
     * </p>
     * <p>
     * The content of the 'Designer5.prefs' will be a copy of the original
     * 'Designer5.prefs' file found on the current system (in the user home dir)
     * However all the references to alias files will be removed and replaced by
     * the actual alias files referencing Maven artifacts.
     * </p>
     *
     * @throws IOException
     * @throws MojoExecutionException
     */
    private void copyDesigner5Prefs(boolean hideLibraryResources) throws IOException, MojoExecutionException {
        File designer5Prefs = getDesigner5Prefs();

        // copy system 'Designer5.prefs' to this ".TIBCO" directory
        File systemDesigner5Prefs = new File(System.getProperty("user.home") + "/" + TIBCO_HOME_DIR + "/" + DESIGNER5_PREFS);
        getLog().debug(DESIGNER5_PREFS + " : " + systemDesigner5Prefs.getAbsolutePath());
        if (systemDesigner5Prefs.exists()) {
            copyFile(systemDesigner5Prefs, designer5Prefs);
        } else {
            designer5Prefs.createNewFile();
        }

        // remove file aliases
        ReplaceRegExp replaceRegExp = new ReplaceRegExp();
        replaceRegExp.setFile(designer5Prefs);
        replaceRegExp.setMatch(FILE_ALIAS_PREFIX_ESCAPED + "([0-9]*)=(.*)");
        replaceRegExp.setReplace("");
        replaceRegExp.setByLine(true);
        replaceRegExp.execute();

        // replace with actual file aliases (which are Maven artifacts)
        FileWriter file = null;
        BufferedWriter buffer = null;
        PrintWriter designer5PrefsOut = null;
        try {
            file = new FileWriter(designer5Prefs, true);
            buffer = new BufferedWriter(file);
            designer5PrefsOut = new PrintWriter(buffer);
            designer5PrefsOut.println("");

            // first the Projlibs aliases
            int i = 0;
            if (!hideLibraryResources) { // implements the "Hide Library Resources" of TIBCO Designer
                for (Dependency dependency : getProjlibsDependencies()) {
                    String projlibName = getProjlibName(dependency);
                    String projlibAlias = getProjlibAlias(dependency);
                    String projlibPath = getDependencyPath(projlibName);
                    designer5PrefsOut.println(FILE_ALIAS_PREFIX + (i++) + "=" + projlibAlias + "\\=" + projlibPath);
                }
            }

            // then the Jar aliases
            for (Dependency dependency : getJarDependencies()) {
                String jarName = getJarName(dependency, false);
                String jarAlias = getJarAlias(dependency, false);
                String jarPath = getDependencyPath(jarName);
                // String jarNameUnderscored = getJarName(dependency, true);
                designer5PrefsOut.println(FILE_ALIAS_PREFIX + (i++) + "=" + jarAlias + "\\=" + jarPath);
            }
        } finally {
            designer5PrefsOut.close();
            buffer.close();
            file.close();
        }
    }

    /**
     * <p>
     * This will create the '.designtimelibs' file in {@link BW5ProjectCommonMojo#targetProjectSource}
     * which is basically the path to the temporary BusinessWorks project being
     * built.
     * </p>
     * <p>
     * The content of the '.designtimelibs' will be the Projlib dependencies of
     * the project, defined in the POM project and resolved with classic Maven
     * mechanism.
     * </p>
     *
     * @throws IOException
     * @throws MojoExecutionException
     */
    private void copyDesignTimeLibs(boolean hideLibraryResources) throws IOException, MojoExecutionException {
        File designTimeLibs = new File(targetProjectSource + "/" + DTL_FILE_NAME);
        getLog().debug(DTL_FILE_NAME + " : " + targetProjectSource + "/" + DTL_FILE_NAME);

        if (!designTimeLibs.exists()) {
            if (!targetProjectSource.exists()) {
                targetProjectSource.mkdirs();
            }
            designTimeLibs.createNewFile();
        }

        FileWriter file = null;
        BufferedWriter buffer = null;
        PrintWriter designTimeLibsOut = null;
        try {
            file = new FileWriter(designTimeLibs, false);
            buffer = new BufferedWriter(file);
            designTimeLibsOut = new PrintWriter(buffer);

            int i = getProjlibsDependencies().size();
            if (!hideLibraryResources) {
                for (Dependency dependency : getProjlibsDependencies()) {
                    //String projlibName = getProjlibName(dependency);
                    String projlibAlias = getProjlibAlias(dependency);
                    designTimeLibsOut.println((--i) + "=" + projlibAlias + "\\=");
                }
            }
        } finally {
            designTimeLibsOut.close();
            buffer.close();
            file.close();
        }
    }

    public void translateDependenciesModels(boolean hideLibraryResources) throws MojoFailureException {
        getLog().debug(Messages.TRANSLATING_DEPENDENCIES);
        try {
            copyAliasesFile();
            copyDesigner5Prefs(hideLibraryResources);
            copyDesignTimeLibs(hideLibraryResources);
        } catch (IOException | MojoExecutionException e) {
            throw new MojoFailureException(e.getLocalizedMessage(), e);
        }
    }
    /* ---------------------------------------------------------------------- */

}
