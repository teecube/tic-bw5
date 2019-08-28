/**
 * (C) Copyright 2016-2019 teecube
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

import com.tibco.xmlns.applicationmanagement.Binding;
import com.tibco.xmlns.applicationmanagement.*;
import com.tibco.xmlns.applicationmanagement.Bwprocess;
import com.tibco.xmlns.applicationmanagement.Product;
import com.tibco.xmlns.applicationmanagement.Shutdown;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.math.BigInteger;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
public class Bw extends com.tibco.xmlns.applicationmanagement.Bw {

    public NVPairs getNVPairs(String name) {
        NVPairs result = null;
        
        for (com.tibco.xmlns.applicationmanagement.NVPairs nvPairs : this.getNVPairs()) {
            if (nvPairs.getName().equals(name)) {
                result = new NVPairs(nvPairs);
                break;
            }
        }

        return result;
    }

    public Binding getBinding(String nameAttribute) {
        Bindings bindings = this.getBindings();

        if (bindings != null) {
            for (Binding binding : bindings.getBinding()) {
                if (binding.getName().equals(nameAttribute)) {
                    return binding;
                }
            }
        } else {
            bindings = new Bindings();
            this.setBindings(bindings);
        }

        // create new Binding
        Binding binding = new Binding();
        binding.setName(nameAttribute);
        Product p = new Product();
        p.setType("bwengine");
        p.setLocation("");
        p.setVersion("");
        binding.setProduct(p);
        binding.setDescription("");
        binding.setContact("");
        Shutdown s = new Shutdown();
        s.setCheckpoint(false);
        s.setTimeout(new BigInteger("0"));
        binding.setShutdown(s);

        bindings.getBinding().add(binding);

        return binding;
    }

    public Bwprocess getBWProcess(String nameAttribute) {
        Bwprocesses bwProcesses = this.getBwprocesses();

        if (bwProcesses != null) {
            for (Bwprocess bwProcess : bwProcesses.getBwprocess()) {
                if (bwProcess.getName().equals(nameAttribute)) {
                    return bwProcess;
                }
            }
        } else {
            bwProcesses = new Bwprocesses();
            this.setBwprocesses(bwProcesses);
        }

        Bwprocess bwProcess = new Bwprocess();
        bwProcess.setName(nameAttribute);
        bwProcesses.getBwprocess().add(bwProcess);

        return bwProcess;
    }

    public Object addParameter(String elementName, String value) {
        if ("enabled".equals(elementName)) {
            this.setEnabled(Boolean.parseBoolean(value));
        } else if ("failureCount".equals(elementName)) {
            this.setFailureCount(BigInteger.valueOf(Long.parseLong(value)));
        } else if ("failureInterval".equals(elementName)) {
            this.setFailureInterval(BigInteger.valueOf(Long.parseLong(value)));
        } else if ("isFt".equals(elementName)) {
            this.setIsFt(Boolean.parseBoolean(value));
        } else if ("faultTolerant".equals(elementName)) {
            return this.getFaultTolerant();
        }

        return this;
    }

}
