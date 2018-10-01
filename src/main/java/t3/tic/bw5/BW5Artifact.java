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

import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;

/**
 *
 * @author Mathieu Debove &lt;mad@teecu.be&gt;
 *
 */
public interface BW5Artifact {

    public String getArtifactFileExtension();
    public File getArtifactFile(File basedir, String finalName, String classifier);
    public File getOutputFile(boolean failIfNotFound) throws MojoExecutionException;

}
