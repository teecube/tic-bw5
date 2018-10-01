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
package t3.tic.bw5.project.properties.appmgmt;

import t3.xml.XMLVisitor;

import java.math.BigInteger;

public class Bwprocess extends XMLVisitor<com.tibco.xmlns.applicationmanagement.Bwprocess> {

    public Bwprocess(com.tibco.xmlns.applicationmanagement.Bwprocess visited) {
        super(visited);
    }

    @Override
    public Object addParameter(String key, String value) {
        if ("starter".equals(key)) {
            visited().setStarter(value);
        } else if ("enabled".equals(key)) {
            visited().setEnabled(Boolean.parseBoolean(value));
        } else if ("maxJob".equals(key)) {
            visited().setMaxJob(BigInteger.valueOf(Long.parseLong(value)));
        } else if ("activation".equals(key)) {
            visited().setActivation(Boolean.parseBoolean(value));
        } else if ("flowLimit".equals(key)) {
            visited().setFlowLimit(BigInteger.valueOf(Long.parseLong(value)));
        }

        return visited();
    }

}
