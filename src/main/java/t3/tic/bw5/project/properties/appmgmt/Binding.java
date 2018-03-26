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

import com.tibco.xmlns.applicationmanagement.Product;
import com.tibco.xmlns.applicationmanagement.Setting;
import com.tibco.xmlns.applicationmanagement.Shutdown;

import t3.xml.XMLVisitor;

public class Binding extends XMLVisitor<com.tibco.xmlns.applicationmanagement.Binding> {

	public Binding(com.tibco.xmlns.applicationmanagement.Binding visited) {
		super(visited);
	}

	@Override
	public Object addParameter(String key, String value) {
		if ("machine".equals(key)) {
			visited().setMachine(value);
		} else if ("product".equals(key)) {
			if (visited().getProduct() == null) {
				visited().setProduct(new Product());
			}
			return visited().getProduct();				
		} else if ("container".equals(key)) {
			visited().setContainer(value);
		} else if ("description".equals(key)) {
			visited().setDescription(value);
		} else if ("contact".equals(key)) {
			visited().setContact(value);
		} else if ("setting".equals(key)) {
			if (visited().getSetting() == null) {
				visited().setSetting(new Setting());
			}
			return visited().getSetting();
		} else if ("ftWeight".equals(key)) {
			visited().setFtWeight(BigInteger.valueOf(Long.parseLong(value)));
		} else if ("shutdown".equals(key)) {
			if (visited().getShutdown() == null) {
				visited().setShutdown(new Shutdown());
			}
			return visited().getShutdown();
		}

		return visited();
	}

}
