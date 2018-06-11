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

import t3.CommonMojoInformation;
import t3.plugin.annotations.Categories;
import t3.plugin.annotations.Category;

/**
* <p>
* Centralization of all Mojo parameters.
* </p>
*
* @author Mathieu Debove &lt;mad@teecu.be&gt;
*
*/
@Categories({
    @Category(title = BW5MojoInformation.BW5.category, description = BW5MojoInformation.BW5.categoryDescription),
    @Category(title = BW5MojoInformation.BW5Project.category, description = BW5MojoInformation.BW5Project.categoryDescription),
    @Category(title = BW5MojoInformation.BW5EAR.category, description = BW5MojoInformation.BW5EAR.categoryDescription),
    @Category(title = BW5MojoInformation.BW5Projlib.category, description = BW5MojoInformation.BW5Projlib.categoryDescription)
})
public class BW5MojoInformation extends CommonMojoInformation {

    public static class BW5 {
        public static final String category = "TIBCO BusinessWorks 5";
        public static final String categoryDescription = "Properties concerning TIBCO BusinessWorks 5 binaries & environment";

        /* BW */
        public static final String bwVersion = "tibco.bw5.version";

        public static final String bwBin = "tibco.bw5.bw.bin";
        public static final String bwBin_default = "${tibco.home}/bw/${tibco.bw5.version}/bin";

        public static final String bwEngine = "tibco.bw5.bw.bwengine";
        public static final String bwEngine_default = "${tibco.bw5.bw.bin}/bwengine${executables.extension}";

        public static final String bwEngineTRA = "tibco.bw5.bw.bwengine.tra";
        public static final String bwEngineTRA_default = "${tibco.bw5.bw.bin}/bwengine.tra";

        /* Designer */
        public static final String designerVersion = "tibco.bw5.designer.version";

        public static final String designerBin = "tibco.bw5.designer.bin";
        public static final String designerBin_default = "${tibco.home}/designer/${tibco.bw5.designer.version}/bin";

        public static final String buildLibrary = "tibco.bw5.designer.buildlibrary";
        public static final String buildLibrary_default = "${tibco.bw5.designer.bin}/buildlibrary${executables.extension}";

        public static final String buildLibraryTRA = "tibco.bw5.designer.buildlibrary.tra";
        public static final String buildLibraryTRA_default = "${tibco.bw5.designer.bin}/buildlibrary.tra";

        public static final String designer = "tibco.bw5.designer.designer";
        public static final String designer_default = "${tibco.bw5.designer.bin}/designer${executables.extension}";

        public static final String designerTRA = "tibco.bw5.designer.designer.tra";
        public static final String designerTRA_default = "${tibco.bw5.designer.bin}/designer.tra";

        /* TRA */
        public static final String traVersion = "tibco.bw5.tra.version";

        public static final String traBin = "tibco.bw5.tra.bin";
        public static final String traBin_default = "${tibco.home}/tra/${tibco.bw5.tra.version}/bin";

        public static final String appManage = "tibco.bw5.tra.AppManage";
        public static final String appManage_default = "${tibco.bw5.tra.bin}/AppManage${executables.extension}";

        public static final String appManageTRA = "tibco.bw5.tra.AppManage.tra";
        public static final String appManageTRA_default = "${tibco.bw5.tra.bin}/AppManage.tra";

        public static final String buildEAR = "tibco.bw5.tra.buildear";
        public static final String buildEAR_default = "${tibco.bw5.tra.bin}/buildear${executables.extension}";

        public static final String buildEARTRA = "tibco.bw5.tra.buildear.tra";
        public static final String buildEARTRA_default = "${tibco.bw5.tra.bin}/buildear.tra";

        public static final String timeout = "tibco.bw5.timeout";
        public static final String timeout_default = "180";
    }

    public static class BW5Project {
        public static final String category = "TIBCO BusinessWorks 5 project";
        public static final String categoryDescription = "Properties concerning a TIBCO BusinessWorks 5 project";

        public static final String source = "tibco.bw5.project.source";
        public static final String source_default = "${basedir}/${tibco.bw5.project.relativeSource}";

        public static final String relativeSource = "tibco.bw5.project.relativeSource";
        public static final String relativeSource_default = "src/main/tibco/bw5/${tibco.bw5.project.name}";

        public static final String targetSource = "tibco.bw5.target.project.source";
        public static final String targetSource_default = "${project.build.directory}/src";

        public static final String targetLib = "tibco.bw5.target.project.lib";
        public static final String targetLib_default = "${project.build.directory}/lib";

        public static final String targetTestSource = "tibco.bw5.target.project.testSource";
        public static final String targetTestSource_default = "${project.test.directory}/src";

        public static final String targetTestLib = "tibco.bw5.target.project.testLib";
        public static final String targetTestLib_default = "${project.test.directory}/lib";

        public static final String hideLibraryResources = "tibco.bw5.hideLibraryResources";
        public static final String hideLibraryResources_default = "false";

        public static final String skipCompile = "tibco.bw5.compile.skip";
        public static final String skipCompile_default = "false";

        public static class Designer {
            public static final String useCopy = "tibco.bw5.project.useCopy";
            public static final String useCopy_default = "false";

            public static final String adaptAliases = "tibco.bw5.project.adaptAliases";
            public static final String adaptAliases_default = "true";
        }

        public static class Aliases {
            public static final String keepOriginalAliasLib = "tibco.bw5.project.aliases.keepOriginalAliasLib";
            public static final String keepOriginalAliasLib_default = "true";

            public static final String skip = "tibco.bw5.project.aliases.skip";
            public static final String skip_default = "false";
        }
    }

    public static class BW5EAR {
        public static final String category = "TIBCO BusinessWorks 5 EAR";
        public static final String categoryDescription = "Properties concerning a TIBCO BusinessWorks 5 EAR";

        public static final String archiveBuilder = "tibco.bw5.project.archiveBuilder";

        public static final String directory = "tibco.bw5.project.ear";
        public static final String directory_default = "${tibco.bw5.project.packaging}";

        public static final String skip = "tibco.bw5.compile.ear.skip";
        public static final String skip_default = "false";

        public static final String skipTouch = "tibco.bw5.compile.ear.skip.touch";
        public static final String skipTouch_default = "false";

        public static class Dependencies {
            public static final String includeTransitiveJARsInEAR = "tibco.bw5.project.dependencies.includeTransitiveJARsInEAR";
            public static final String includeTransitiveJARsInEAR_default = "true";

            public static final String removeVersionFromFileNames = "tibco.bw5.project.dependencies.removeVersionFromFileNames";
            public static final String removeVersionFromFileNames_default = "false";
        }

        public static class Packaging {
            public static final String skip = "tibco.bw5.project.packaging.skip";
            public static final String skip_default = "false";

            public static final String directory = "tibco.bw5.project.packaging";
            public static final String directory_default = "${project.build.directory}/package";
            public static final String directory_description = "The output directory for package-related files.<br />This is a <a href=\"${siteURL}/index.html\">link</a>!";

            public static final String deploymentDescriptorBased = "tibco.bw5.project.packaging.deploymentDescriptor";
            public static final String deploymentDescriptorBased_default = "${tibco.bw5.project.packaging}/${project.build.finalName}-base.xml";

            public static final String deploymentDescriptorMerged = "tibco.bw5.project.packaging.deploymentDescriptor.final";
            public static final String deploymentDescriptorMerged_default = "${tibco.bw5.project.packaging}/${project.build.finalName}-merged.xml";

            /* Properties */ // by default extracted = merged = generated
            public static class Properties {
                public static class Extracted {
                    public static final String propertiesExtractedGlobalVariables = "tibco.bw5.project.packaging.properties.extracted.globalVariables";
                    public static final String propertiesExtractedGlobalVariables_default = "${tibco.bw5.project.packaging}/${project.build.finalName}.gv.properties";

                    public static final String propertiesExtractedServices = "tibco.bw5.project.packaging.properties.extracted.services";
                    public static final String propertiesExtractedServices_default = "${tibco.bw5.project.packaging}/${project.build.finalName}.services.properties";
                }

                public static class Merged {
                    public static final String propertiesGlobalVariablesMerged = "tibco.bw5.project.packaging.properties.merged.globalVariables";
                    public static final String propertiesGlobalVariablesMerged_default = "${tibco.bw5.project.packaging}/${project.build.finalName}.gv.merged.properties";

                    public static final String propertiesServicesMerged = "tibco.bw5.project.packaging.properties.merged.services";
                    public static final String propertiesServicesMerged_default = "${tibco.bw5.project.packaging}/${project.build.finalName}.services.merged.properties";
                }

                public static class Merge {
                    /* * Common */
                    public static final String propertiesGlobalVariablesCommon = "tibco.bw5.project.packaging.properties.common.globalVariables";
                    public static final String propertiesGlobalVariablesCommon_default = "${tibco.bw5.project.packaging}/${project.build.finalName}.gv.properties";

                    public static final String propertiesServicesCommon = "tibco.bw5.project.packaging.properties.common.services";
                    public static final String propertiesServicesCommon_default = "${tibco.bw5.project.packaging}/${project.build.finalName}.services.properties";
                    /* * Main */
                    public static final String propertiesGlobalVariablesMain = "tibco.bw5.project.packaging.properties.main.globalVariables";
                    public static final String propertiesGlobalVariablesMain_default = "${tibco.bw5.project.packaging}/${project.build.finalName}.gv.properties";

                    public static final String propertiesServicesMain = "tibco.bw5.project.packaging.properties.main.services";
                    public static final String propertiesServicesMain_default = "${tibco.bw5.project.packaging}/${project.build.finalName}.services.properties";
                }

                public static class Generated {
                    public static final String propertiesGlobalVariables = "tibco.bw5.project.packaging.properties.globalVariables";
                    public static final String propertiesGlobalVariables_default = "${tibco.bw5.project.packaging}/${project.build.finalName}.gv.properties";

                    public static final String propertiesServices = "tibco.bw5.project.packaging.properties.services";
                    public static final String propertiesServices_default = "${tibco.bw5.project.packaging}/${project.build.finalName}.services.properties";
                }

                public static final String propertiesServicesAlwaysKeepEmptyBinding = "tibco.bw5.project.packaging.properties.alwaysKeepEmptyBinding";
                public static final String propertiesServicesAlwaysKeepEmptyBinding_default = "false";

                public static final String propertiesMergeIgnore = "tibco.bw5.project.packaging.properties.mergeIgnore";
                public static final String propertiesMergeIgnore_default = "false";

                public static final String propertiesMergeCommonIgnore = "tibco.bw5.project.packaging.properties.common.mergeIgnore";
                public static final String propertiesMergeCommonIgnore_default = "false";

                public static final String propertiesMergeMainIgnore = "tibco.bw5.project.packaging.properties.main.mergeIgnore";
                public static final String propertiesMergeMainIgnore_default = "false";

                public static final String propertiesMergeCommonAfter = "tibco.bw5.project.packaging.properties.mergeCommonAfter";
                public static final String propertiesMergeCommonAfter_default = "false";

                public static final String propertiesFilter = "tibco.bw5.project.packaging.properties.filter";
                public static final String propertiesFilter_default = "false";
            }
        }
    }

    public static class BW5Projlib {
        public static final String category = "TIBCO BusinessWorks 5 Projlib";
        public static final String categoryDescription = "Properties concerning a TIBCO BusinessWorks 5 Projlib";

        public static final String libraryBuilder = "tibco.bw5.project.libraryBuilder";

        public static final String skip = "tibco.bw5.compile.projlib.skip";
        public static final String skip_default = "false";

        public static final String skipTouch = "tibco.bw5.compile.projlib.skip.touch";
        public static final String skipTouch_default = "false";
    }

}
