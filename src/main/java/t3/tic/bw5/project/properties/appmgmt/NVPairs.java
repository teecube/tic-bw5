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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import com.tibco.xmlns.applicationmanagement.NVPairType;
import com.tibco.xmlns.applicationmanagement.NameValuePair;

public class NVPairs extends com.tibco.xmlns.applicationmanagement.NVPairs {

	private com.tibco.xmlns.applicationmanagement.NVPairs nvPairs;

	public static List<NVPairs> convertList(List<com.tibco.xmlns.applicationmanagement.NVPairs> nvPairsList) {
		if (nvPairsList == null) {
			return null;
		}

		ArrayList<NVPairs> result = new ArrayList<NVPairs>();

		for (com.tibco.xmlns.applicationmanagement.NVPairs nvPairs : nvPairsList) {
			result.add(new NVPairs(nvPairs));
		}

		return result;
	}

	public NVPairs() {
		this(new com.tibco.xmlns.applicationmanagement.NVPairs());
	}

	public NVPairs(String name) {
		this(new com.tibco.xmlns.applicationmanagement.NVPairs());
		this.name = name;
	}

	public NVPairs(com.tibco.xmlns.applicationmanagement.NVPairs nvPairs) {
		this.nvPairs = nvPairs;
		if (nvPairs != null) {
			this.name = nvPairs.getName();
			this.nvPair = nvPairs.getNVPair();
		} else {
			this.name = "";
			this.nvPair = new ArrayList<JAXBElement<? extends NVPairType>>();
		}
	}

	public NameValuePair get(String name) {
		NameValuePair result = null;
		if (name != null) {
			for (JAXBElement<? extends NVPairType> j : this.nvPairs.getNVPair()) {
				if (name.equals(j.getValue().getName())) {
					result = (NameValuePair) j.getValue();
					break;
				}
			}
		}
		return result;
	}

	public void put(String name, String value) {
		put(name, value, "");
	}

	public void put(String name, String value, String description) {
		NameValuePair pair = get(name);
		if (pair == null) {
			pair = new NameValuePair();
			pair.setName(name);

			QName qName = new QName(ApplicationMarshaller.NAMESPACE, "NameValuePair");
			JAXBElement<NameValuePair> _pair = new JAXBElement<NameValuePair>(qName, NameValuePair.class, pair);

			this.nvPairs.getNVPair().add(_pair);
		}
		pair.setValue(value);
		pair.setDescription(description);
	}

}
