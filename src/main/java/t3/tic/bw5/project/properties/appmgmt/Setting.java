/**
 * (C) Copyright 2016-2017 teecube
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
package t3.tic.bw5.project.properties.appmgmt;

import java.math.BigInteger;

import com.tibco.xmlns.applicationmanagement.Setting.Java;
import com.tibco.xmlns.applicationmanagement.Setting.NTService;

import t3.xml.XMLVisitor;

public class Setting extends XMLVisitor<com.tibco.xmlns.applicationmanagement.Setting> {

	public Setting(com.tibco.xmlns.applicationmanagement.Setting visited) {
		super(visited);
	}

	@Override
	public Object addParameter(String key, String value) {
		if ("startOnBoot".equals(key)) {
			visited().setStartOnBoot(Boolean.parseBoolean(value));
		} else if ("enableVerbose".equals(key)) {
			visited().setEnableVerbose(Boolean.parseBoolean(value));
		} else if ("maxLogFileSize".equals(key)) {
			visited().setMaxLogFileSize(BigInteger.valueOf(Long.parseLong(value)));
		} else if ("maxLogFileCount".equals(key)) {
			visited().setMaxLogFileCount(BigInteger.valueOf(Long.parseLong(value)));
		} else if ("threadCount".equals(key)) {
			visited().setThreadCount(BigInteger.valueOf(Long.parseLong(value)));
		} else if ("NTService".equals(key)) {
			if (visited().getNTService() == null) {
				visited().setNTService(new NTService());
			}
			return visited().getNTService();
		} else if ("java".equals(key)) {
			if (visited().getJava() == null) {
				visited().setJava(new Java());
			}
			return visited().getJava();
		}

		return visited();
	}

}
