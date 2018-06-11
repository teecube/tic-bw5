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

import com.tibco.xmlns.applicationmanagement.Binding;
import com.tibco.xmlns.applicationmanagement.*;
import com.tibco.xmlns.applicationmanagement.Bwprocess;
import com.tibco.xmlns.applicationmanagement.Checkpoints;
import com.tibco.xmlns.applicationmanagement.FaultTolerant;
import com.tibco.xmlns.applicationmanagement.Setting;
import com.tibco.xmlns.applicationmanagement.Setting.Java;
import com.tibco.xmlns.applicationmanagement.Setting.NTService;
import com.tibco.xmlns.applicationmanagement.Shutdown;
import t3.utils.SortedProperties;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

@XmlRootElement(name="application", namespace="http://www.tibco.com/xmlns/ApplicationManagement")
public class ApplicationType extends com.tibco.xmlns.applicationmanagement.ApplicationType {
    public static final String NAMESPACE = "http://www.tibco.com/xmlns/ApplicationManagement";

    /* Services (<bw> elements) */
    public List<Bw> getBwServices() {
        return ApplicationMarshaller.convertList(this.getServices().getBaseService(), new Bw());
    }

    public Bw getBw(String name) {
        for (Bw bw : getBwServices()) {
            if (bw.getName().equals(name)) {
                return bw;
            }
        }

        Bw result = new Bw();
        result.setName(name);
//        services.add(result);

        // TODO: create default <bw> with minimalistic objects

        QName qName = new QName(NAMESPACE, "bw");
        JAXBElement<Bw> j = new JAXBElement<Bw>(qName, Bw.class, result);
        this.getServices().getBaseService().add(j);
        
        return result;
    }

    public Properties getBwServicesProperties() {
        SortedProperties result = new SortedProperties();

        List<Bw> bwServices = getBwServices();

        String key;

        for (Bw bwService : bwServices) {
            String serviceKey = "bw[" + bwService.getName() + "]";

            /// "ServiceType" complexType
            // enabled
            key = serviceKey + "/enabled";
            result.setProperty(key, bwService.isEnabled());

            // bindings
            result.putAll(getBindings(bwService));
            // NVPairs
            result.putAll(getNVPairs(bwService));

            // failureCount
            key = serviceKey + "/failureCount";
            result.setProperty(key, bwService.getFailureCount());

            // failureInterval
            key = serviceKey + "/failureInterval";
            result.setProperty(key, bwService.getFailureInterval());

            // monitor

            /// "BWServiceType" complexType
            // bwprocesses
            result.putAll(getProcesses(bwService));

            // checkpoints
            result.putAll(getCheckpoints(bwService));

            // isFt
            key = serviceKey + "/isFt";
            result.setProperty(key, bwService.isIsFt());

            // faultTolerant
            result.putAll(getFaultTolerant(bwService));

            // plugins
            // not supported (too complex)
        }

        return result;
    }

    /**
     * <p>
     * This method focuses on bindings found in this path :
     * "/application/services/bw/bindings/binding"
     * </p>
     */
    protected Properties getBindings(Bw bwService) {
        SortedProperties result = new SortedProperties();

        String serviceKey = "bw[" + bwService.getName() + "]";

        Bindings bindings = bwService.getBindings();
        if (bindings != null) {
            for (Binding binding : bindings.getBinding()) {
                String processKey = serviceKey + "/bindings/binding[" + binding.getName() + "]";
                String key;

                // machine
                key = processKey + "/machine";
                result.setProperty(key, binding.getMachine());

                if (binding.getProduct() != null) {
                    key = processKey + "/product/type";
                    result.setProperty(key, binding.getProduct().getType());
                    key = processKey + "/product/version";
                    result.setProperty(key, binding.getProduct().getVersion());
                    key = processKey + "/product/location";
                    result.setProperty(key, binding.getProduct().getLocation());
                }

                // container
                key = processKey + "/container";
                result.setProperty(key, binding.getContainer());

                // description
                key = processKey + "/description";
                result.setProperty(key, binding.getDescription());

                // contact
                key = processKey + "/contact";
                result.setProperty(key, binding.getContact());

                // setting
                Setting setting = binding.getSetting();
                if (setting != null) {
                    key = processKey + "/setting/startOnBoot";
                    result.setProperty(key,  setting.isStartOnBoot());
                    key = processKey + "/setting/enableVerbose";
                    result.setProperty(key, setting.isEnableVerbose());
                    key = processKey + "/setting/maxLogFileSize";
                    result.setProperty(key, setting.getMaxLogFileSize());
                    key = processKey + "/setting/maxLogFileCount";
                    result.setProperty(key, setting.getMaxLogFileCount());
                    key = processKey + "/setting/threadCount";
                    result.setProperty(key, setting.getThreadCount());
                    NTService ntService = setting.getNTService();
                    if (ntService != null) {
                        key = processKey + "/setting/NTService/runAsNT";
                        result.setProperty(key, ntService.isRunAsNT());
                        key = processKey + "/setting/NTService/startupType";
                        result.setProperty(key,  ntService.getStartupType());
                        key = processKey + "/setting/NTService/loginAs";
                        result.setProperty(key, ntService.getLoginAs());
                        key = processKey + "/setting/NTService/password";
                        result.setProperty(key, ntService.getPassword());
                    }
                    Java java = setting.getJava();
                    if (java != null) {
                        key = processKey + "/setting/java/prepandClassPath";
                        result.setProperty(key, java.getPrepandClassPath());
                        key = processKey + "/setting/java/appendClassPath";
                        result.setProperty(key, java.getAppendClassPath());
                        key = processKey + "/setting/java/initHeapSize";
                        result.setProperty(key, java.getInitHeapSize());
                        key = processKey + "/setting/java/maxHeapSize";
                        result.setProperty(key, java.getMaxHeapSize());
                        key = processKey + "/setting/java/threadStackSize";
                        result.setProperty(key, java.getThreadStackSize());
                    }
                }

                // ftWeight
                key = processKey + "/ftWeight";
                result.setProperty(key, binding.getFtWeight());

                // shutdown
                Shutdown shutdown = binding.getShutdown();
                if (shutdown != null) {
                    key = processKey + "/shutdown/checkpoint";
                    result.setProperty(key, shutdown.isCheckpoint());
                    key = processKey + "/shutdown/timeout";
                    result.setProperty(key, shutdown.getTimeout());
                }

                // plugins
                // not supported (too complex)

                // NVPairs
                if (binding.getNVPairs() != null) {
                    for (JAXBElement<? extends NVPairType> nvPair : binding.getNVPairs().getNVPair()) {
                        key = processKey + "/variables/variable[" + nvPair.getValue().getName() + "]";
                        result.setProperty(key, nvPair.getValue().getValue());
                    }
                }
            }
        }

        return result;
    }

    /**
     * <p>
     * This method focuses on Global Variables found in this path :
     * "/application/services/bw/NVPairs"
     * </p>
     */
    private Properties getNVPairs(Bw bwService) {
        SortedProperties result = new SortedProperties();

        String serviceKey = "bw[" + bwService.getName() + "]";

        List<NVPairs> nvPairsList = new ArrayList<NVPairs>();
        nvPairsList = NVPairs.convertList(bwService.getNVPairs());

        String variablesKey;

        for (NVPairs nvPairs : nvPairsList) {
            variablesKey = serviceKey + "/variables[" + nvPairs.getName() + "]";
            String key;
            for (JAXBElement<? extends NVPairType> nvPair : nvPairs.getNVPair()) {
                key = variablesKey + "/variable[" + nvPair.getValue().getName() + "]";
                result.setProperty(key, nvPair.getValue().getValue());
            }
        }

        return result;
    }

    /**
     * <p>
     * This method focuses on processes found in this path :
     * "/application/services/bw/bwprocesses/bwprocess"
     * </p>
     */
    protected Properties getProcesses(Bw bwService) {
        SortedProperties result = new SortedProperties();

        String serviceKey = "bw[" + bwService.getName() + "]";

        Bwprocesses bwProcesses = bwService.getBwprocesses();
        if (bwProcesses != null) {
            for (Bwprocess process : bwProcesses.getBwprocess()) {
                String processKey = serviceKey + "/bwprocesses/bwprocess[" + process.getName() + "]";
                String key;

                // starter
                key = processKey + "/starter";
                result.setProperty(key, process.getStarter());

                // enabled
                key = processKey + "/enabled";
                result.setProperty(key, process.isEnabled());

                // maxJob
                key = processKey + "/maxJob";
                result.setProperty(key, process.getMaxJob());

                // activation
                key = processKey + "/activation";
                result.setProperty(key, process.isActivation());

                // flowLimit
                key = processKey + "/flowLimit";
                result.setProperty(key, process.getFlowLimit());
            }
        }

        return result;
    }

    /**
     * <p>
     * This method focuses on checkpoints found in this path :
     * "/application/services/bw/checkpoints/checkpoint"
     * </p>
     */
    protected Properties getCheckpoints(Bw bwService) {
        SortedProperties result = new SortedProperties();

        String serviceKey = "bw[" + bwService.getName() + "]";

        Checkpoints checkpoints = bwService.getCheckpoints();
        if (checkpoints != null) {
            String key;

            // tablePrefix
            key = serviceKey + "/checkpoints/tablePrefix";
            result.setProperty(key, checkpoints.getTablePrefix());

            for (String checkpoint : checkpoints.getCheckpoint()) {
                if (checkpoint == null) continue;

                // checkpoint
                key = serviceKey + "/checkpoints/checkpoint["+checkpoint+"]";
                result.setProperty(key, checkpoint.equals(checkpoints.getSelected()));
            }
        }

        return result;
    }

    /**
     * <p>
     * This method focuses on faultTolerant object found in this path :
     * "/application/services/bw/faultTolerant"
     * </p>
     */
    protected Properties getFaultTolerant(Bw bwService) {
        SortedProperties result = new SortedProperties();

        String serviceKey = "bw[" + bwService.getName() + "]";

        FaultTolerant faultTolerant = bwService.getFaultTolerant();
        if (faultTolerant != null) {
            String key;
            // hbInterval
            key = serviceKey + "/faultTolerant/hbInterval";
            result.setProperty(key, faultTolerant.getHbInterval());

            // activationInterval
            key = serviceKey + "/faultTolerant/activationInterval";
            result.setProperty(key, faultTolerant.getActivationInterval());

            // preparationDelay
            key = serviceKey + "/faultTolerant/preparationDelay";
            result.setProperty(key, faultTolerant.getPreparationDelay());
        }

        return result;
    }

    /* Global Variables (application level) */

    public NVPairs getNVPairs() {
        com.tibco.xmlns.applicationmanagement.NVPairs _super = super.getNVPairs();
        if (_super != null) {
            return new NVPairs(_super);
        } else {
            return null;
        }
    }

    /**
    * <p>
    * This method retrieves the Global Variables at application level
    * </p>
    */
    public NVPairs getGlobalVariablesPairs() {
        if (this.getNVPairs() == null) {
            this.setNVPairs(new NVPairs("Global Variables"));
        }
        if (this.getNVPairs().getName().equals("Global Variables")) {
            return this.getNVPairs();
        } else {
            return null;
        }
    }

    /**
     * <p>
     * The Global Variables are inside the NVPairs element with @name attribute
     * equals to "Global Variables" at the root level.
     * </p>
     *
     * @return The Global Variables of the xmlFile in a {@link SortedProperties}
     * object.
     */
    public Properties getGlobalVariablesProperties() {
        SortedProperties result = new SortedProperties();

        NVPairs globalVariablesPairs = this.getGlobalVariablesPairs();

        if (globalVariablesPairs != null) {
            for (JAXBElement<? extends NVPairType> nvPair : globalVariablesPairs.getNVPair()) {
                String key = nvPair.getValue().getName();
                String value = nvPair.getValue().getValue();
                result.setProperty(key, value);
            }
        }

        return result;
    }

    public void setGlobalVariable(String key, String value) {
        setGlobalVariable(key, value, false);
    }

    public void setGlobalVariable(String key, String value, boolean createIfNotExists) {
        if (key == null) return;
        
        NVPairs globalVariablesPairs = this.getGlobalVariablesPairs();

        if (globalVariablesPairs != null) {
            for (JAXBElement<? extends NVPairType> nvPair : globalVariablesPairs.getNVPair()) {
                if (key.equals(nvPair.getValue().getName())) {
                    nvPair.getValue().setValue(value);
                    return;
                }
            }
            if (createIfNotExists) {
                NameValuePair simpleGV = new NameValuePair();
                simpleGV.setName(key);
                simpleGV.setValue(value);
                globalVariablesPairs.getNVPair().add(new JAXBElement<NameValuePair>( new QName(NAMESPACE, "NameValuePair"), NameValuePair.class, simpleGV));
            }
        }
    }

    /**
     * <p>
     * This will remove from the XML the second duplicate default binding (with
     * empty name attribute) to keep only the first one.
     * This is is because "AppManage -export -max" exports two empty bindings
     * to prepare fault tolerance configuration.
     * </p>
     */
    public void removeDuplicateDefaultBinding() {
        List<Bw> bwServices = this.getBwServices();
        
        for (Bw bw : bwServices) {
            boolean first = true;

            if (bw.getBindings() != null && bw.getBindings().getBinding() != null) {
                List<Binding> bindings = bw.getBindings().getBinding();
                for (Iterator<Binding> iterator = bindings.iterator(); iterator.hasNext();) {
                    Binding binding = (Binding) iterator.next();
    
                    if (!first && binding.getName().equals("")) {
                        iterator.remove();
                    }
                    first = false;
                }
            }
        }
    }

    /**
     * <p>
     * This will remove from the XML the default binding (with empty name
     * attribute) if it is not found in the properties.
     * </p>
     */
    public void removeDefaultBindingIfNotExists(Properties properties) {
        List<Bw> bwServices = this.getBwServices();
        
        for (Bw bw : bwServices) {
            String path = "bw[" + bw.getName() + "]/bindings/binding[]/machine";
            
            if (bw.getBindings() != null && bw.getBindings().getBinding() != null) {
                List<Binding> bindings = bw.getBindings().getBinding();
                for (Iterator<Binding> iterator = bindings.iterator(); iterator.hasNext();) {
                    Binding binding = (Binding) iterator.next();
                    if (binding.getName().equals("") && !properties.containsKey(path)) {
                        iterator.remove();
                    }
                }
            }
        }
    }

}
