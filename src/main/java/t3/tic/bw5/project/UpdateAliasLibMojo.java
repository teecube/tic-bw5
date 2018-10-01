/**
 * (C) Copyright 2016-2018 teecube
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
package t3.tic.bw5.project;

import com.tibco.xmlns.repo.types._2002.ObjectFactory;
import com.tibco.xmlns.repo.types._2002.Repository;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.FileSet;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.xml.sax.SAXException;
import t3.plugin.annotations.Mojo;
import t3.plugin.annotations.Parameter;
import t3.tic.bw5.BW5MojoInformation;
import t3.tic.bw5.Messages;
import t3.utils.Utils;
import t3.xml.XMLMarshall;

import javax.xml.bind.JAXBException;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * This goal updates the aliases in <i>.alisalib</i> files.
 * </p>
 *
 * @author Mathieu Debove &lt;mad@teecu.be&gt;
 *
 */
@Mojo(name = "update-aliaslib", defaultPhase = LifecyclePhase.COMPILE, requiresProject = true, requiresDependencyResolution = ResolutionScope.TEST)
public class UpdateAliasLibMojo extends BW5ProjectCommonMojo {

    /**
     * <p>
     * Whether to skip the compilation of EAR <b>and</b> Projlib (including this
     * goal).
     * </p>
     */
    @Parameter (property = BW5MojoInformation.BW5Project.skipCompile, required=false, defaultValue=BW5MojoInformation.BW5Project.skipCompile_default)
    protected Boolean skipCompile;

    /**
     * <p>
     * Whether to skip the update of ".aliaslib" files.
     * </p>
     */
    @Parameter (property = BW5MojoInformation.BW5Project.Aliases.skip, required=false, defaultValue=BW5MojoInformation.BW5Project.Aliases.skip_default)
    protected Boolean skipUpdateAliasLib;

    protected class RepositoryMarshaller extends XMLMarshall<Repository, ObjectFactory> {
        public RepositoryMarshaller(File xmlFile) throws JAXBException, SAXException {
            super(xmlFile);
        }
    }

    @Override
    public String getArtifactFileExtension() {
        return null;
    }

    protected final static String SRC_NOT_SET = "Source directory is not set. Modification of '.aliaslib' file might fail.";

    /**
     * <p>
     * Whether to keep original entries in '.aliaslib' files when updating.
     * </p>
     */
    @Parameter (property = BW5MojoInformation.BW5Project.Aliases.keepOriginalAliasLib, defaultValue = BW5MojoInformation.BW5Project.Aliases.keepOriginalAliasLib_default, required = false)
    public Boolean keepOriginalAliasLib;

    /**
     * <p>
     * This method retrieves a list of ".aliaslib" files to process in the
     * source directory (usually "target/src") which is a copy of the actual
     * source directory used to compile the TIBCO BusinessWorks EAR.
     * </p>
     */
    private List<File> initFiles() throws IOException {
        FileSet restriction = new FileSet();

        File directory = targetProjectSource;
        if (directory == null) {
            return Utils.toFileList(restriction);
        }

        getLog().debug(directory.getAbsolutePath());
        restriction.setDirectory(directory.getAbsolutePath());

        restriction.addInclude("**/*.aliaslib");

        List<File> result = Utils.toFileList(restriction);

        getLog().debug("List of '.aliaslib' files to update: " + result);
        return result;
    }

    /**
     * <p>
     * This method add an alias in the object used internally by TIBCO
     * BusinessWorks.
     * </p>
     *
     * @param list, an object used internally by TIBCO BusinessWorks.
     * @param aliasName, the name of the alias.
     */
    private void addAlias(ArrayList<HashMap<String, Object>> list, String aliasName) {
        for (HashMap<String, Object> h : list) {
            String name = (String) h.get("name");
            if (name != null && name.equals(aliasName)) {
                return; // avoid duplicates
            }
        }
        HashMap<String, Object> h = new HashMap<String, Object>();
        h.put("isClasspathFile", Boolean.TRUE);
        h.put("name", aliasName);
        h.put("includeInDeployment", Boolean.TRUE);

        list.add(h);
    }

    @SuppressWarnings("unchecked")
    // unchecked because we know it's the type used by TIBCO BusinessWorks
    private ArrayList<HashMap<String, Object>> readXMLBean(RepositoryMarshaller repositoryModel, File f) throws JAXBException, UnsupportedEncodingException {
        // retrieve the content of the XML Bean in the ".aliaslib" file
        String xmlBean = repositoryModel.getObject().getName().getFILEALIASESLIST();

        // this XML bean is decoded to a Java object with the XMLDecoder
        XMLDecoder d = new XMLDecoder(new ByteArrayInputStream(xmlBean.getBytes(this.sourceEncoding)));
        ArrayList<HashMap<String, Object>> result = null;
        try {
            result = (ArrayList<HashMap<String, Object>>) d.readObject();
        } finally {
            d.close();
        }

        return result;
    }

    private void writeXMLBean(RepositoryMarshaller repositoryModel, File f, ArrayList<HashMap<String, Object>> aliases) throws JAXBException, IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        XMLEncoder e = new XMLEncoder(os);
        e.writeObject(aliases);
        e.close();

        String xmlBean = os.toString(this.sourceEncoding);
        os.close();

        // put back the XML Bean in the ".aliaslib" file
        repositoryModel.getObject().getName().setFILEALIASESLIST(xmlBean);
        repositoryModel.saveWithoutFilter();
    }

    /**
     * This method adds the JAR aliases to a ".aliaslib" file
     *
     * @param f, the ".aliaslib" file to update
     * @throws MojoExecutionException
     */
    public void processFile(File f) throws MojoExecutionException {
        try {
            RepositoryMarshaller repositoryModel = new RepositoryMarshaller(f);

            ArrayList<HashMap<String, Object>> aliases = readXMLBean(repositoryModel, f);

            // reset old references
            if (!keepOriginalAliasLib) {
                aliases.clear();
            }

            // adding the JAR dependencies
            for (Dependency dependency : this.getJarDependencies()) {
                addAlias(aliases, getJarAlias(dependency, false));
            }

            writeXMLBean(repositoryModel, f, aliases);
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skipCompile    || isCurrentGoal(DESIGNER_GOAL)) {
            getLog().info(Messages.SKIPPING);
            return; // ignore
        }

        super.execute();

        try {
            List<File> aliaslibFiles = initFiles(); // look for ".aliaslib" files in "target/src" folder and optional directories in 'customAliasLibDirectories'
            for (File f : aliaslibFiles) {
                processFile(f);
            }
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }

    }

}
