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
package t3.tic.bw5;

import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.MavenExecutionException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.annotations.Component;
import t3.AdvancedMavenLifecycleParticipant;
import t3.CommonMavenLifecycleParticipant;
import t3.plugin.PluginConfigurator;
import t3.plugin.PluginManager;
import t3.plugin.PropertiesEnforcer;
import t3.site.GenerateGlobalParametersDocMojo;
import t3.tic.bw5.project.BW5ProjectCommonMojo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Mathieu Debove &lt;mad@teecu.be&gt;
 *
 */
@Component(role = AbstractMavenLifecycleParticipant.class, hint = "TICBW5LifecycleParticipant")
public class BW5LifecycleParticipant extends CommonMavenLifecycleParticipant implements AdvancedMavenLifecycleParticipant {

    public final static String pluginGroupId = "io.teecube.tic";
    public final static String pluginArtifactId = "tic-bw5";

    @Override
    protected String getPluginGroupId() {
        return pluginGroupId;
    }

    @Override
    protected String getPluginArtifactId() {
        return pluginArtifactId;
    }

    @Override
    protected String loadedMessage() {
        return Messages.LOADED;
    }

    @Override
    protected void initProjects(MavenSession session) throws MavenExecutionException {
        List<MavenProject> projects = prepareProjects(session.getProjects(), session);
        session.setProjects(projects);

        customizeGoalsExecutions(session);

        List<String> projectPackagings = new ArrayList<String>();
        projectPackagings.add(BW5ProjectCommonMojo.BWEAR_TYPE);
        projectPackagings.add(BW5ProjectCommonMojo.PROJLIB_TYPE);

        PropertiesEnforcer.setCustomProperty(session, "sampleProfileCommandLine", GenerateGlobalParametersDocMojo.standaloneGenerator(session.getCurrentProject(), this.getClass()).getFullSampleProfileForCommandLine("tic-bw5", "| ")); // TODO: retrieve artifactId with pluginDescriptor

        if (!ignoreRules(session)) {
            PropertiesEnforcer.enforceProperties(session, pluginManager, logger, projectPackagings, BW5LifecycleParticipant.class, getPluginKey()); // check that all mandatory properties are correct
        }

        PluginManager.registerCustomPluginManager(pluginManager, new BW5MojosFactory()); // to inject Global Parameters in Mojos
    }

    private boolean ignoreRules(MavenSession session) {
        for (String goal : session.getRequest().getGoals()) {
            if (goal.startsWith("toe:") || goal.startsWith("archetype:")) {
                return true;
            }
        }
        return false;
    }

    private void customizeGoalsExecutions(MavenSession session) {
        if (session.getRequest().getGoals().contains("bw5:designer")) {
            session.getRequest().getUserProperties().put("archiveBuilderSkip", "true");
        }
    }

    /**
     * <p>
     *
     * </p>
     *
     * @param session
     * @param projects
     * @throws MavenExecutionException
     */
    private List<MavenProject> prepareProjects(List<MavenProject> projects, MavenSession session) throws MavenExecutionException {
        List<MavenProject> result = new ArrayList<MavenProject>();

        if (projects == null) {
            logger.warn("No projects to prepare.");
            return result;
        }

        for (MavenProject mavenProject : projects) {
            mavenProject.getProperties().put("_d", "$");
            PluginConfigurator.addPluginsParameterInModel(mavenProject, BW5LifecycleParticipant.class, logger);
            try {
                switch (mavenProject.getPackaging()) {
                case BW5ProjectCommonMojo.PROJLIB_TYPE:
                case BW5ProjectCommonMojo.BWEAR_TYPE:
                    PluginConfigurator.updatePluginsConfiguration(mavenProject, session, true, BW5LifecycleParticipant.class, logger, getPluginKey());
                    if (session.getGoals().contains(BW5ProjectCommonMojo.DESIGNER_GOAL)) {
                        removeCompileGoal(mavenProject);
                    }
                    break;
                default:
                    logger.debug("No preparation for : " + mavenProject.getName());
                    break;
                }
            } catch (Exception e) {
                throw new MavenExecutionException(e.getLocalizedMessage(), e);
            }
            result.add(mavenProject);
        }

        return result;
    }

    private void removeCompileGoal(MavenProject mavenProject) {
        for (Plugin plugin : mavenProject.getBuildPlugins()) {
            if ("tic-bw5".equals(plugin.getArtifactId())) { // FIXME: use a pluginDescriptor to identify self
                for (Iterator<PluginExecution> iterator = plugin.getExecutions().iterator(); iterator.hasNext();) {
                    PluginExecution pluginExecution = iterator.next();
                    if (pluginExecution.getGoals().contains("compile-ear")) { // FIXME: externalize
                        iterator.remove();
                    }
                }
            }
        }
    }

}