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
package t3.tic.bw5;

import org.apache.maven.plugin.AbstractMojo;

import t3.MojosFactory;
import t3.tic.bw5.project.DesignerMojo;
import t3.tic.bw5.project.UpdateAliasLibMojo;
import t3.tic.bw5.project.ear.CompileEARMojo;
import t3.tic.bw5.project.ear.IncludeDependenciesInEARMojo;
import t3.tic.bw5.project.projlib.CompileProjlibMojo;
import t3.tic.bw5.project.properties.PropertiesMergeMojo;
import t3.tic.bw5.project.properties.PropertiesToXMLMojo;
import t3.tic.bw5.project.properties.XMLFromEARMojo;
import t3.tic.bw5.project.properties.XMLToPropertiesMojo;

public class BW5MojosFactory extends MojosFactory {

	@SuppressWarnings("unchecked")
	public <T extends AbstractMojo> T getMojo(Class<T> type) {
		if (type == null) {
			return null;
		}

		String typeName = type.getSimpleName();

		switch (typeName) {
		// project
		case "DesignerMojo":
			return (T) new DesignerMojo();
		case "UpdateAliasLibMojo":
			return (T) new UpdateAliasLibMojo();
			// ear
			case "CompileEARMojo":
				return (T) new CompileEARMojo();
			case "IncludeDependenciesInEARMojo":
				return (T) new IncludeDependenciesInEARMojo();
				// packaging
				case "PropertiesMergeMojo":
					return (T) new PropertiesMergeMojo();
				case "PropertiesToXMLMojo":
					return (T) new PropertiesToXMLMojo();
				case "XMLFromEARMojo":
					return (T) new XMLFromEARMojo();
				case "XMLToPropertiesMojo":
					return (T) new XMLToPropertiesMojo();
			// projlib
			case "CompileProjlibMojo":
				return (T) new CompileProjlibMojo();
		default:
			return super.getMojo(type);
		}

	}

}
