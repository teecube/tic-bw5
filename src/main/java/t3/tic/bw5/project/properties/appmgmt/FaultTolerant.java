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
package t3.tic.bw5.project.properties.appmgmt;

import java.math.BigInteger;

import t3.xml.XMLVisitor;

public class FaultTolerant extends XMLVisitor<com.tibco.xmlns.applicationmanagement.FaultTolerant> {

	public FaultTolerant(com.tibco.xmlns.applicationmanagement.FaultTolerant visited) {
		super(visited);
	}

	@Override
	public Object addParameter(String key, String value) {
		if ("activationInterval".equals(key)) {
			visited().setActivationInterval(BigInteger.valueOf(Long.parseLong(value)));
		} else if ("hbInterval".equals(key)) {
			visited().setHbInterval(BigInteger.valueOf(Long.parseLong(value)));
		} else if ("preparationDelay".equals(key)) {
			visited().setPreparationDelay(BigInteger.valueOf(Long.parseLong(value)));
		}

		return visited();
	}
	
}
