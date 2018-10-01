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
package t3.tic.bw5.project.properties;

import t3.plugin.annotations.GlobalParameter;
import t3.plugin.annotations.Parameter;
import t3.tic.bw5.BW5MojoInformation;
import t3.tic.bw5.Messages;
import t3.tic.bw5.project.BW5ProjectCommonMojo;

import java.io.File;

/**
 *
 * @author Mathieu Debove &lt;mad@teecu.be&gt;
 *
 */
public abstract class PackagingCommonMojo extends BW5ProjectCommonMojo {

    /**
     * <p>
     * Path to the packaging directory.
     * </p>
     */
    @GlobalParameter (property = BW5MojoInformation.BW5EAR.Packaging.directory, defaultValue = BW5MojoInformation.BW5EAR.Packaging.directory_default, description = BW5MojoInformation.BW5EAR.Packaging.directory_description, category = BW5MojoInformation.BW5EAR.category)
    protected File targetProjectPacakge;

    public boolean skip() {
        if (skipPackage) {
            getLog().info(Messages.SKIPPING);
        }
        return skipPackage;
    }

    /**
     * <p>
     * Whether to skip the packaging goals (including this goal).
     * </p>
     */
    @Parameter (property=BW5MojoInformation.BW5EAR.Packaging.skip, required=false, defaultValue=BW5MojoInformation.BW5EAR.Packaging.skip_default)
    protected Boolean skipPackage;

    @Override
    public String getArtifactFileExtension() {
        return null;
    }

}
