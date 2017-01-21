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
package t3.tic.bw5;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.MavenExecutionException;
import org.apache.maven.artifact.repository.ArtifactRepositoryFactory;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

import t3.AdvancedMavenLifecycleParticipant;
import t3.CommonMojo;
import t3.plugin.PluginConfigurator;
import t3.plugin.PluginManager;
import t3.plugin.PropertiesEnforcer;
import t3.site.GenerateGlobalParametersDocMojo;
import t3.tic.bw5.project.BW5ProjectCommonMojo;

/**
 *
 * @author Mathieu Debove &lt;mad@teecu.be&gt;
 *
 */
@Component(role = AbstractMavenLifecycleParticipant.class, hint = "TICBW5LifecycleParticipant")
public class BW5LifecycleParticipant extends AbstractMavenLifecycleParticipant implements AdvancedMavenLifecycleParticipant {

    @Requirement
    private PlexusContainer plexus;

	@Requirement
	private Logger logger;

	@Requirement
	private ArtifactRepositoryFactory artifactRepositoryFactory;

	@Requirement
	protected BuildPluginManager pluginManager;

	@Requirement
	protected ProjectBuilder projectBuilder;

	@org.apache.maven.plugins.annotations.Component
	protected PluginDescriptor pluginDescriptor; // plugin descriptor of this plugin

	public final static String pluginGroupId = "io.teecube.tic";
	public final static String pluginArtifactId = "tic-bw5";
	public final static String pluginKey = BW5LifecycleParticipant.pluginGroupId + ":" + BW5LifecycleParticipant.pluginArtifactId;

	private CommonMojo propertiesManager;

	@Override
	public void afterProjectsRead(MavenSession session)	throws MavenExecutionException {
		fixStandalonePOM(session.getCurrentProject(), new File(session.getRequest().getBaseDirectory()));

		propertiesManager = CommonMojo.propertiesManager(session, session.getCurrentProject());
		PluginConfigurator.propertiesManager = propertiesManager;

		List<MavenProject> projects = prepareProjects(session.getProjects(), session);
		session.setProjects(projects);

		customizeGoalsExecutions(session);

		List<String> projectPackagings = new ArrayList<String>();
		projectPackagings.add(BW5ProjectCommonMojo.BWEAR_TYPE);
		projectPackagings.add(BW5ProjectCommonMojo.PROJLIB_TYPE);

		PropertiesEnforcer.setCustomProperty(session, "sampleProfileCommandLine", GenerateGlobalParametersDocMojo.standaloneGenerator(session.getCurrentProject(), this.getClass()).getFullSampleProfileForCommandLine("tic-bw5", "| ")); // TODO: retrieve artifactId with pluginDescriptor

		if (!ignoreRules(session)) {
			PropertiesEnforcer.enforceProperties(session, pluginManager, logger, projectPackagings, BW5LifecycleParticipant.class, pluginKey); // check that all mandatory properties are correct
		}

		PluginManager.registerCustomPluginManager(pluginManager, new BW5MojosFactory()); // to inject Global Parameters in Mojos

		logger.info(Messages.LOADED);
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

	private void fixStandalonePOM(MavenProject mavenProject, File requestBaseDirectory) {
		if (mavenProject == null) return;

		if ("standalone-pom".equals(mavenProject.getArtifactId()) && requestBaseDirectory != null) {
			mavenProject.setFile(new File(requestBaseDirectory, "pom.xml"));
		}
	}

	/**
	 * <p>
	 *
	 * </p>
	 *
	 * @param session
	 * @param projects
	 * @param projectBuildingRequest
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
					PluginConfigurator.updatePluginsConfiguration(mavenProject, session, true, BW5LifecycleParticipant.class, logger, pluginKey);
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

	public void setPlexus(PlexusContainer plexus) {
		this.plexus = plexus;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public void setArtifactRepositoryFactory(ArtifactRepositoryFactory artifactRepositoryFactory) {
		this.artifactRepositoryFactory = artifactRepositoryFactory;
	}

	public void setPluginManager(BuildPluginManager pluginManager) {
		this.pluginManager = pluginManager;
	}

	public void setProjectBuilder(ProjectBuilder projectBuilder) {
		this.projectBuilder = projectBuilder;
	}

	public void setPluginDescriptor(PluginDescriptor pluginDescriptor) {
		this.pluginDescriptor = pluginDescriptor;
	}

}