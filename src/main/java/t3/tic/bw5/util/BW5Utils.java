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
package t3.tic.bw5.util;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;
import t3.tic.bw5.project.BW5ProjectCommonMojo;

/**
 *
 * @author Mathieu Debove &lt;mad@teecu.be&gt;
 *
 */
public class BW5Utils {

    public static Boolean isBW5(Dependency dependency) {
        if (dependency == null) return false;
        return isBW5(dependency.getType());
    }

    public static  Boolean isBW5(Artifact artifact) {
        if (artifact == null) return false;
        return isBW5(artifact.getType());
    }

    private static Boolean isBW5(String type) {
        if (type == null) {
            return false;
        }

        switch (type) {
        case BW5ProjectCommonMojo.PROJLIB_TYPE:
        case BW5ProjectCommonMojo.BWEAR_TYPE:
            return true;
        default:
            return false;
        }
    }

}
