/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.uberfire.client;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.uberfire.client.annotations.Perspective;
import org.uberfire.client.annotations.WorkbenchPerspective;
import org.uberfire.client.workbench.events.PerspectiveChange;
import org.uberfire.shared.EclipsePlaceManagerBridge;
import org.uberfire.workbench.model.PerspectiveDefinition;
import org.uberfire.workbench.model.impl.PerspectiveDefinitionImpl;

import com.google.gwt.user.client.Window;

@ApplicationScoped
@WorkbenchPerspective(identifier = "HomePerspective", isDefault = true)

public class HomePerspective {
    
    PerspectiveDefinition perspectiveDefinition;
    String pathParameter;
    
    @Perspective
    public PerspectiveDefinition buildPerspective() {
        perspectiveDefinition = new PerspectiveDefinitionImpl(SingleWorkbenchPanelPresenter.class.getName() );
        perspectiveDefinition.setName( "Eclipse Editor Perspective" );
        perspectiveDefinition.getRoot().addPart("BlankScreen");
        return perspectiveDefinition;
    }
    
    @PostConstruct
    public void onPostConstruct() {
        if ( Window.Location.getParameterMap().containsKey( "path" ) ) {
            if (!Window.Location.getParameterMap().get("path").isEmpty()) {
                pathParameter = Window.Location.getParameterMap().get("path").get(0);
            }
        }
    }

    public void loadEditor(@Observes PerspectiveChange e) {
//        Window.alert("HomePerspective.loadEditor() path="+pathParameter);
        EclipsePlaceManagerBridge ec = new EclipsePlaceManagerBridge();
        ec.goTo(pathParameter);
    }
}