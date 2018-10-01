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
package t3.tic.bw5;

/**
 *
 * @author Mathieu Debove &lt;mad@teecu.be&gt;
 *
 */
public class Messages extends t3.Messages {

    public static final String MESSAGE_PREFIX = "~-> TIC BW5: ";

    public static final String LOADED = MESSAGE_PREFIX + "Loaded.";

    public static final String WARN_NO_ARTIFACT_ATTACHED = "Could not attach artifact.";

    public static final String SKIPPING = MESSAGE_PREFIX + t3.Messages.SKIPPING;

    public static final String TRANSLATING_DEPENDENCIES = "Translating TIBCO dependencies model to Maven dependencies model.";

    // Projlib
    public static final String PROJLIB_PREFIX = MESSAGE_PREFIX  + "Projlib : ";
    public static final String SKIPPING_PROJLIB = PROJLIB_PREFIX + SKIPPING;
    public static final String BUILDING_PROJLIB = PROJLIB_PREFIX + "Building...";
    public static final String BUILD_PROJLIB_FAILED = PROJLIB_PREFIX + "The build failed.";

    // EAR
    public static final String EAR_PREFIX = MESSAGE_PREFIX  + "EAR : ";
    public static final String SKIPPING_EAR = EAR_PREFIX + t3.Messages.SKIPPING;
    public static final String BUILDING_EAR = EAR_PREFIX + "Building...";
    public static final String EAR_CREATED = "The EAR has been successfully created in";
    public static final String BUILD_EAR_FAILED = EAR_PREFIX + "The build failed.";

    // Designer
    public final static String LAUNCH_DESIGNER_FAILED = "The launch of the TIBCO Designer has failed.";
    public final static String LAUNCHING_DESIGNER = "Launching TIBCO Designer...";
    public final static String PROJECT_LOCATION = "Project location: ";

    // Packaging
    public final static String TIBCO_XML_DD_BW5 = "TIBCO XML Deployment Descriptor (BW5)";
    public final static String EXTRACT_XML_FROM_EAR = "Extracting " + TIBCO_XML_DD_BW5 + " from";
    public final static String CREATE_XML_FROM_EAR_SUCCESS = "Successfully extracted the " + TIBCO_XML_DD_BW5 + " to";
    public final static String CREATE_XML_FROM_EAR_FAILED = "The extraction of the " + TIBCO_XML_DD_BW5 + " file from the EAR file has failed.";

    public final static String XML_LOAD_SUCCESS = "Successfully loaded properties from " + TIBCO_XML_DD_BW5 + " file";
    public final static String XML_LOAD_FAILURE = "Failed to load properties from " + TIBCO_XML_DD_BW5 + " file";

    public final static String PROPERTIES_SAVE_SERVICES_SUCCESS = "Successfully saved Services properties to " + TIBCO_XML_DD_BW5 + " file";
    public final static String PROPERTIES_SAVE_SERVICES_FAILURE = "Failed to save Services properties to " + TIBCO_XML_DD_BW5 + " file";

    public final static String PROPERTIES_SAVE_GVS_SUCCESS = "Successfully saved Global Variables properties to " + TIBCO_XML_DD_BW5 + " file";
    public final static String PROPERTIES_SAVE_GVS_FAILURE = "Failed to save Global Variables properties to " + TIBCO_XML_DD_BW5 + " file";

        // Properties merging
        public final static String MERGING_PREFIX = "   "; // indentation
        public final static String MERGING_PROPERTIES = "Merging properties...";
        public final static String MERGING_PROPERTIES_USING = MERGING_PREFIX + "-> merging ";
        public final static String MERGING_PROPERTIES_GVS = MERGING_PREFIX + "Global Variables properties";
        public final static String MERGING_PROPERTIES_SERVICES = MERGING_PREFIX + "Services properties";
        public final static String NOTHING_TO_MERGE = MERGING_PREFIX + "Nothing to merge.";
        public final static String MERGE_FAILURE = MERGING_PREFIX + "Failed to merge properties";
        public final static String PROPERTIES_LOAD_FAILURE = MERGING_PREFIX + "Failed to load properties";

    public final static String PROPERTIES_LOAD_SERVICES_SUCCESS = "Successfully loaded Services properties from";
    public final static String PROPERTIES_LOAD_SERVICES_FAILURE = "Failed to load Services properties from";

    public final static String PROPERTIES_LOAD_GVS_SUCCESS = "Successfully loaded Global Variables properties from";
    public final static String PROPERTIES_LOAD_GVS_FAILURE = "Failed to load Global Variables properties from";

    public final static String XML_SAVE_SUCCESS = "Successfully saved properties to " + TIBCO_XML_DD_BW5 + " file";
    public final static String XML_SAVE_FAILURE = "Failed to save properties to " + TIBCO_XML_DD_BW5 + " file";

    public static final String APPLICATION_MANAGEMENT_COPY_FAILURE = "Failed to copy the Application Management file from";
    public static final String APPLICATION_MANAGEMENT_LOAD_FAILURE = "Failed to load the Application Management file in";

}
