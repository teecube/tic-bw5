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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;

import t3.SortedProperties;
import t3.Utils;
import t3.plugin.annotations.Mojo;
import t3.plugin.annotations.Parameter;
import t3.tic.bw5.BW5MojoInformation;
import t3.tic.bw5.Messages;

/**
 * <p>
 * This goal merges properties files with different reference files (<i>main</i>
 * file for the current artifact, <i>common</i> file for all artifacts,
 * <i>custom</i> files for additional properties).
 * </p>
 * <p>
 * <b>NB</b>: by default, if nothing is configured, the files to merge all have
 * the same name and nothing is merged.
 * </p>
 * @author Mathieu Debove &lt;mad@teecu.be&gt;
 *
 */
@Mojo(name="properties-merge", defaultPhase=LifecyclePhase.PACKAGE)
public class PropertiesMergeMojo extends PackagingCommonMojo {

	/* Source files */

	/**
	 * <p>
	 * Path to the properties file for Global Variables (by default, the one
	 * <a href="./xml-to-properties-mojo.html#propertiesGlobalVariables">generated from XML</a>).
	 * </p>
	 */
	@Parameter (property=BW5MojoInformation.BW5EAR.Packaging.Properties.Extracted.propertiesExtractedGlobalVariables, defaultValue = BW5MojoInformation.BW5EAR.Packaging.Properties.Extracted.propertiesExtractedGlobalVariables_default)
	protected File propertiesGlobalVariables;

	/**
	 * <p>
	 * Path to the properties file for Services (by default, the one
	 * <a href="./xml-to-properties-mojo.html#propertiesServices">generated from XML</a>).
	 * </p>
	 */
	@Parameter (property=BW5MojoInformation.BW5EAR.Packaging.Properties.Extracted.propertiesExtractedServices, defaultValue = BW5MojoInformation.BW5EAR.Packaging.Properties.Extracted.propertiesExtractedServices_default)
	protected File propertiesServices;

	/* Merge files */

	/**
	 * <p>
	 * Path to the common properties file for Global Variables.
	 * </p>
	 */
	@Parameter (property=BW5MojoInformation.BW5EAR.Packaging.Properties.Merge.propertiesGlobalVariablesCommon, defaultValue = BW5MojoInformation.BW5EAR.Packaging.Properties.Merge.propertiesGlobalVariablesCommon_default)
	protected File propertiesGlobalVariablesCommon;

	/**
	 * <p>
	 * Path to the common properties file for Services.
	 * </p>
	 */
	@Parameter (property=BW5MojoInformation.BW5EAR.Packaging.Properties.Merge.propertiesServicesCommon, defaultValue = BW5MojoInformation.BW5EAR.Packaging.Properties.Merge.propertiesServicesCommon_default)
	protected File propertiesServicesCommon;

	/**
	 * <p>
	 * Path to the main properties file for Global Variables.
	 * </p>
	 */
	@Parameter (property=BW5MojoInformation.BW5EAR.Packaging.Properties.Merge.propertiesGlobalVariablesMain, defaultValue = BW5MojoInformation.BW5EAR.Packaging.Properties.Merge.propertiesGlobalVariablesMain_default)
	protected File propertiesGlobalVariablesMain;

	/**
	 * <p>
	 * Path to the main properties file for Services.
	 * </p>
	 */
	@Parameter (property=BW5MojoInformation.BW5EAR.Packaging.Properties.Merge.propertiesServicesMain, defaultValue = BW5MojoInformation.BW5EAR.Packaging.Properties.Merge.propertiesServicesMain_default)
	protected File propertiesServicesMain;

	/**
	 * <p>
	 * A list of additional properties files to merge for Global Variables.
	 * </p>
	 * <p>
	 * <b>NB</b>: additional properties files are always merged after common and
	 * main files.
	 * </p>
	 */
	@org.apache.maven.plugins.annotations.Parameter
	public List<File> propertiesGlobalVariablesFiles;

	/**
	 * <p>
	 * A list of additional properties files to merge for Services.
	 * </p>
	 * <p>
	 * <b>NB</b>: additional properties files are always merged after common and
	 * main files.
	 * </p>
	 */
	@org.apache.maven.plugins.annotations.Parameter
	public List<File> propertiesServicesFiles;

	/* Destination files */

	/**
	 * <p>
	 * Path to the merged properties file for Global Variables.
	 * </p>
	 */
	@Parameter (property=BW5MojoInformation.BW5EAR.Packaging.Properties.Merged.propertiesGlobalVariablesMerged, defaultValue = BW5MojoInformation.BW5EAR.Packaging.Properties.Merged.propertiesGlobalVariablesMerged_default)
	protected File propertiesGlobalVariablesMerged;

	/**
	 * <p>
	 * Path to the merged properties file for Services.
	 * </p>
	 */
	@Parameter (property=BW5MojoInformation.BW5EAR.Packaging.Properties.Merged.propertiesServicesMerged, defaultValue = BW5MojoInformation.BW5EAR.Packaging.Properties.Merged.propertiesServicesMerged_default)
	protected File propertiesServicesMerged;

	/* Merge tweaks */

	/**
	 * <p>
	 * By default, the empty binding ("bw[EXAMPLE.par]/bindings/binding[]) is
	 * removed if other named bindings exists
	 * ("bw[EXAMPLE.par]/bindings/binding[named]").
	 * </p>
	 * <p>
	 * It is possible to keep this empty binding by setting
     * <i>alwaysKeepEmptyBindings</i> to <i>true</i>. Default is <i>false</i>.
     * </p>
	 */
	@Parameter (property = BW5MojoInformation.BW5EAR.Packaging.Properties.propertiesServicesAlwaysKeepEmptyBinding, defaultValue = BW5MojoInformation.BW5EAR.Packaging.Properties.propertiesServicesAlwaysKeepEmptyBinding_default)
	boolean alwaysKeepEmptyBinding;

	/**
	 * <p>
	 * Whether to filter properties files or not.
	 * </p>
	 */
	@Parameter (property=BW5MojoInformation.BW5EAR.Packaging.Properties.propertiesFilter, defaultValue = BW5MojoInformation.BW5EAR.Packaging.Properties.propertiesFilter_default)
	protected boolean filterProperties;

	/**
	 * <p>
	 * It is possible to ignore any merging of properties files by setting
	 * <i>ignorePropertiesMerge</i> to <i>true</i>. Default is <i>false</i>
	 * (do merge).
	 * </p>
	 */
	@Parameter (property = BW5MojoInformation.BW5EAR.Packaging.Properties.propertiesMergeIgnore, defaultValue = BW5MojoInformation.BW5EAR.Packaging.Properties.propertiesMergeIgnore_default)
	boolean ignorePropertiesMerge;

	/**
	 * <p>
	 * It is possible to ignore merging of main properties files by setting
	 * <i>ignoreMainFiles</i> to <i>true</i>. Default is <i>false</i>
	 * (do merge).
	 * </p>
	 */
	@Parameter (property=BW5MojoInformation.BW5EAR.Packaging.Properties.propertiesMergeMainIgnore, defaultValue = BW5MojoInformation.BW5EAR.Packaging.Properties.propertiesMergeMainIgnore_default)
	protected boolean ignoreMainFiles;

	/**
	 * <p>
	 * It is possible to ignore merging of common properties files by setting
	 * <i>ignoreCommonFiles</i> to <i>true</i>. Default is <i>false</i>
	 * (do merge).
	 * </p>
	 */
	@Parameter (property=BW5MojoInformation.BW5EAR.Packaging.Properties.propertiesMergeCommonIgnore, defaultValue = BW5MojoInformation.BW5EAR.Packaging.Properties.propertiesMergeCommonIgnore_default)
	protected boolean ignoreCommonFiles;

	/**
	 * <p>
	 * By default the properties from common properties are merged first and the
	 * main properties specific to the project last. By setting
	 * <i>mergeCommonAfter</i> to true, the common properties are mergerd after.
	 * </p>
	 */
	@Parameter (property = BW5MojoInformation.BW5EAR.Packaging.Properties.propertiesMergeCommonAfter, defaultValue = BW5MojoInformation.BW5EAR.Packaging.Properties.propertiesMergeCommonAfter_default)
	boolean mergeCommonAfter;

	private List<ImmutablePair<String, String>> pairParInstance = new ArrayList<ImmutablePair<String, String>>(); // keep trace of dynamically created bindings (to generate them only once)
	private final static String regexNotEmptyBinding = "^bw\\[(.*)\\]/bindings/binding\\[(.+)\\]/(.*)$";
	private final static String regexEmptyBinding = "^bw\\[(.*)\\]/bindings/binding(\\[\\])/(.*)$";
	private final static Pattern pNotEmptyBinding = Pattern.compile(regexNotEmptyBinding);

	/**
	 * <p>
	 * This method adds {@code fileToAdd} to {@code list} only if
	 * {@code fileToAdd} != {@code originalFile}
	 * </p>
	 *
	 * @param list
	 * @param fileToAdd
	 * @param originalFile
	 */
	private void addPropertiesFile(List<File> list, File fileToAdd, File originalFile) {
		if (list == null || fileToAdd == null) return;

		try {
			if (fileToAdd.exists() && (originalFile == null || !fileToAdd.getCanonicalPath().equals(originalFile.getCanonicalPath()))) {
				list.add(fileToAdd);
			}
		} catch (IOException e) {
			// does not add but does not throw
		}
	}

	private List<File> initPropertiesFiles(List<File> propertiesFiles, boolean servicesFiles) {
		List<File> result = new ArrayList<File>();

		if (ignorePropertiesMerge) {
			return result; // return an empty list files
		}

		// add common and main properties files
		if (servicesFiles) {
			if (!mergeCommonAfter && !ignoreCommonFiles) {
				addPropertiesFile(result, propertiesServicesCommon, propertiesServices);
			}
			if (!ignoreMainFiles) {
				addPropertiesFile(result, propertiesServicesMain, propertiesServices);
			}
			if (mergeCommonAfter && !ignoreCommonFiles) {
				addPropertiesFile(result, propertiesServicesCommon, propertiesServices);
			}
		} else {
			if (!mergeCommonAfter && !ignoreCommonFiles) {
				addPropertiesFile(result, propertiesGlobalVariablesCommon, propertiesGlobalVariables);
			}
			if (!ignoreMainFiles) {
				addPropertiesFile(result, propertiesGlobalVariablesMain, propertiesGlobalVariables);
			}
			if (mergeCommonAfter && !ignoreCommonFiles) {
				addPropertiesFile(result, propertiesGlobalVariablesCommon, propertiesGlobalVariables);
			}
		}

		// adding additional properties files
		if (propertiesFiles != null && !propertiesFiles.isEmpty()) {
			result.addAll(propertiesFiles);
		}

		// remove non existing files
		for (ListIterator<File> it = result.listIterator(); it.hasNext();) {
			File f = it.next();
			if (!f.exists()) {
				it.remove();
			}
		}

		return result;
	}

	private void copyFileIfDifferent(boolean servicesFiles) throws MojoExecutionException {
		try {
			if (servicesFiles) {
					if (!propertiesServicesMerged.getCanonicalPath().equals(propertiesServices.getCanonicalPath())) {
						FileUtils.copyFile(propertiesServices, propertiesServicesMerged);
					}
			} else {
				if (!propertiesGlobalVariablesMerged.getCanonicalPath().equals(propertiesGlobalVariables.getCanonicalPath())) {
					FileUtils.copyFile(propertiesGlobalVariables, propertiesGlobalVariablesMerged);
				}
			}
		} catch (IOException e) {
			throw new MojoExecutionException(e.getLocalizedMessage(), e);
		}
	}

	private void doMerge(List<File> mergeGlobalVariables, List<File> mergeServices) throws MojoExecutionException {
		try {
			Properties earGlobalVariables = SortedProperties.loadPropertiesFile(propertiesGlobalVariables, this.sourceEncoding);

			if (!mergeGlobalVariables.isEmpty()) {
				getLog().info(Messages.MERGING_PROPERTIES_GVS + " ('" + propertiesGlobalVariables + "')");
				for (File mergeGlobalVariable : mergeGlobalVariables) {
					mergeGlobalVariable(earGlobalVariables, mergeGlobalVariable);
				}
			}

			Properties earBwServices = SortedProperties.loadPropertiesFile(propertiesServices, this.sourceEncoding);

			if (!mergeServices.isEmpty()) {
				if (!mergeGlobalVariables.isEmpty()) {
					getLog().info("");
				}
				getLog().info(Messages.MERGING_PROPERTIES_SERVICES + " ('" + propertiesServices + "')");
				for (File mergeService : mergeServices) {
					mergeService(earBwServices, mergeService);
				}
			}

		    earGlobalVariables = SortedProperties.sortProperties(earGlobalVariables);
		    earBwServices = SortedProperties.sortProperties(earBwServices);

		    earBwServices = removeWildCards(earBwServices);
		    if (!alwaysKeepEmptyBinding) {
				earBwServices = removeEmptyBindings(earBwServices);
		    }

		    getLog().info("");
			// Export Properties in a properties file
			//  Global Variables
			if (!mergeGlobalVariables.isEmpty()) {
				SortedProperties.savePropertiesToFile(propertiesGlobalVariablesMerged,
													  earGlobalVariables,
													  this.sourceEncoding,
													  "Global Variables",
													  Messages.PROPERTIES_SAVE_GVS_SUCCESS,
													  Messages.PROPERTIES_SAVE_GVS_FAILURE,
													  filterProperties,
													  this);
			} else {
				copyFileIfDifferent(false);
			}
			//  Services (=~ Process Archives)
			if (!mergeServices.isEmpty()) {
				SortedProperties.savePropertiesToFile(propertiesServicesMerged,
													  earBwServices,
													  this.sourceEncoding,
													  "Services (Bindings, Processes)",
													  Messages.PROPERTIES_SAVE_SERVICES_SUCCESS,
													  Messages.PROPERTIES_SAVE_SERVICES_FAILURE,
													  filterProperties,
													  this);
			} else if (!propertiesServicesMerged.getCanonicalPath().equals(propertiesServices.getCanonicalPath())) {
				copyFileIfDifferent(true);
			}
		} catch (IOException e) {
			throw new MojoExecutionException(e.getLocalizedMessage(), e);
		}
	}

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		if (super.skip()) {
			return;
		}

		List<File> propertiesGlobalVariablesFiles = initPropertiesFiles(this.propertiesGlobalVariablesFiles, false);
		List<File> propertiesServicesFiles = initPropertiesFiles(this.propertiesServicesFiles, true);

		// remove duplicates
		List<File> mergeGlobalVariables = new ArrayList<File>(new LinkedHashSet<>(propertiesGlobalVariablesFiles));
		List<File> mergeServices = new ArrayList<File>(new LinkedHashSet<>(propertiesServicesFiles));

		getLog().debug(mergeGlobalVariables.toString());
		getLog().debug(mergeServices.toString());

		getLog().info(Messages.MERGING_PROPERTIES);
		getLog().info("");

		if (mergeGlobalVariables.isEmpty() && mergeServices.isEmpty()) {
			getLog().debug("GV main: " + propertiesGlobalVariablesMain.getAbsolutePath());
			getLog().debug("SVC main: " + propertiesServicesMain.getAbsolutePath());
			getLog().debug("GV common: " + propertiesGlobalVariablesCommon.getAbsolutePath());
			getLog().debug("SVC common: " + propertiesServicesCommon.getAbsolutePath());
			getLog().info(Messages.NOTHING_TO_MERGE);

			// copy original properties files to merged ones (if different)
			copyFileIfDifferent(true);
			copyFileIfDifferent(false);
		} else {
			doMerge(mergeGlobalVariables, mergeServices);
		}
	}

	/* GVs merging */
	private boolean mergeGlobalVariable(Properties propertiesDominant, File fileToMerge) throws MojoExecutionException {
		if (propertiesDominant == null || fileToMerge == null || !fileToMerge.exists()) {
			return false;
		}

		getLog().info(Messages.MERGING_PROPERTIES_USING + "'" + fileToMerge.getAbsolutePath() + "'");

		Properties propertiesRecessive;
		try {
			propertiesRecessive = SortedProperties.loadPropertiesFile(fileToMerge, this.sourceEncoding);
		} catch (Exception e) {
			throw new MojoExecutionException(Messages.PROPERTIES_LOAD_FAILURE, e);
		}

		Enumeration<Object> e = propertiesRecessive.keys();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			String value = propertiesRecessive.getProperty(key);

			propertiesDominant.setProperty(key, value); // add or override value
		}

		return true;
	}

	/* Services merging */
	private boolean mergeService(Properties propertiesDominant, File fileToMerge) throws MojoExecutionException {
		if (propertiesDominant == null || fileToMerge == null || !fileToMerge.exists()) {
			return false;
		}

		getLog().info(Messages.MERGING_PROPERTIES_USING + fileToMerge.getAbsolutePath());
		Properties propertiesRecessive;
		try {
			propertiesRecessive = SortedProperties.loadPropertiesFile(fileToMerge, this.sourceEncoding);
		} catch (Exception e) {
			throw new MojoExecutionException(Messages.PROPERTIES_LOAD_FAILURE, e);
		}

		String par, binding;
		Enumeration<Object> e = propertiesRecessive.keys();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();

			Matcher mNotEmptyBinding = pNotEmptyBinding.matcher(key);

			if (mNotEmptyBinding.matches() && !isAWildCard(key)) {
				par = mNotEmptyBinding.group(1);
				binding = mNotEmptyBinding.group(2);
				ImmutablePair<String, String> pair = new ImmutablePair<String, String>(par, binding);
				if (!pairParInstance.contains(pair)) {
					propertiesDominant = duplicateEmptyBinding(propertiesDominant, par, binding);
					pairParInstance.add(pair);
				}
			}

			String value = propertiesRecessive.getProperty(key);

			propertiesDominant.setProperty(key, value); // add or override value
		}

		propertiesDominant = expandWildCards(propertiesDominant);

		return true;
	}

	/* Empty binding */
	private Properties duplicateEmptyBinding(Properties properties, String par, String binding) {
		String key, value;
		Pattern pEmptyBinding = Pattern.compile(regexEmptyBinding);

		Enumeration<Object> e = properties.keys();
		while (e.hasMoreElements()) {
			key = (String) e.nextElement();
			value = properties.getProperty(key);
			Matcher mEmptyBinding = pEmptyBinding.matcher(key);
			if (mEmptyBinding.matches() && mEmptyBinding.group(1).equals(par)) {
				key = "bw[" + mEmptyBinding.group(1) + "]/bindings/binding[" + binding + "]/" + mEmptyBinding.group(3);
			}
			properties.setProperty(key, value);
		}
		return properties;
	}

	private Properties removeEmptyBindings(Properties properties) {
		ArrayList<String> pars = new ArrayList<String>();

		Pattern pNotEmptyBinding = Pattern.compile(regexNotEmptyBinding);
		Pattern pEmptyBinding = Pattern.compile(regexEmptyBinding);
		String parName;

		Enumeration<Object> e = properties.keys();
		// first check if there is at least one non empty binding (non default)
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			Matcher mNotEmptyBinding = pNotEmptyBinding.matcher(key);
			if (mNotEmptyBinding.matches()) {
				parName = mNotEmptyBinding.group(1);
				if (pars.contains(parName))
					continue;
				pars.add(parName);
			}
		}
		// then delete
		e = properties.keys();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			Matcher mEmptyBinding = pEmptyBinding.matcher(key);
			if (mEmptyBinding.matches()) {
				parName = mEmptyBinding.group(1);
				if (pars.contains(parName)) {
					properties.remove(key);
				}
			}
		}

		return properties;
	}

	/* Wildcards */
    /**
     * <p>
     * This expands wild cards properties.
     * </p>
     * <p>
     * Both wildcard expressions and expressions to expand are present in the
     * same properties object.
     * </p>
     * <p>
     * <i>Example</i>
     *  <ul>
     * 	 <li><b>property with wildcard</b>: /root/element[*]/key=new_value</li>
     * 	 <li><b>property matching</b>: /root/element[my_name]/key=old_value</li>
     *  </ul>
     *  will expand to:
     *  <ul>
     * 	 <li><b>property after expansion</b>:
     * /root/element[my_name]/key=new_value</li>
     *  </ul>
     * </p>
     *
     * @param properties, the properties object with wildcard expressions and
     * expressions to expand
     * @return properties with expanded expressions, but without wildcard
     * expressions
     */
	protected Properties expandWildCards(Properties properties) {
		Properties propertiesWithWildCards = new Properties() { // sorted properties
			private static final long serialVersionUID = 7793482336210629858L;

			@Override
			public synchronized Enumeration<Object> keys() {
				return Collections.enumeration(new TreeSet<Object>(super
						.keySet()));
			}
		};
		String key;

		// retrieve the keys with WildCards
		Enumeration<Object> e = properties.keys();
		while (e.hasMoreElements()) {
			key = (String) e.nextElement();
			if (isAWildCard(key)) {
				propertiesWithWildCards.setProperty(key, properties.getProperty(key));
				properties.remove(key);
			}
		}

		// try to replace the values of other keys matching the keys with
		// WildCards
		Enumeration<Object> w = propertiesWithWildCards.keys();
		while (w.hasMoreElements()) {
			String keyWithWildCards = (String) w.nextElement();
			String regex = Utils.wildcardToRegex(keyWithWildCards);

			Boolean found = false;

			e = properties.keys();
			while (e.hasMoreElements()) {
				key = (String) e.nextElement();

				if (Pattern.matches(regex, key)) {
					found = true;
					String value = (String) propertiesWithWildCards.getProperty(keyWithWildCards);
					properties.setProperty(key, value);
				}
			}

			// not found, we put back the expression with wild cards in the
			// original list (false positive)
			// this way the wildcard can still be used in a next pass and will
			// be removed at the end by AbstractPackagingMojo.removeWildCards
			if (!found) {
				properties.setProperty(keyWithWildCards,
						propertiesWithWildCards.getProperty(keyWithWildCards));
			}
		}

		return properties;
	}

    protected boolean isAWildCard(String key) {
		String regexVariable = "bw\\[.*\\]/variables\\[.*\\]/variable\\[.*\\]"; // ignore variable because they can have '*' in their name

		return key.contains("*") && !Pattern.matches(regexVariable, key);
    }

	protected Properties removeWildCards(Properties properties) {
		String key;

		Enumeration<Object> e = properties.keys();
		while (e.hasMoreElements()) {
			key = (String) e.nextElement();

			if (isAWildCard(key)) {
				properties.remove(key);
			}
		}

		return properties;
	}

}
