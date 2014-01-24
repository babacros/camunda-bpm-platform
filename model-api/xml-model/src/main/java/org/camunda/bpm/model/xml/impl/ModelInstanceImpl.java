/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.model.xml.impl;

import org.camunda.bpm.model.xml.Model;
import org.camunda.bpm.model.xml.ModelException;
import org.camunda.bpm.model.xml.ModelInstance;
import org.camunda.bpm.model.xml.impl.instance.ModelElementInstanceImpl;
import org.camunda.bpm.model.xml.impl.type.ModelElementTypeImpl;
import org.camunda.bpm.model.xml.impl.util.DomUtil;
import org.camunda.bpm.model.xml.impl.util.ModelUtil;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * An instance of a model
 *
 * @author Daniel Meyer
 * @author Sebastian Menski
 *
 */
public class ModelInstanceImpl implements ModelInstance {

  private final Document document;
  private final ModelImpl model;

  public ModelInstanceImpl(final ModelImpl model, final Document document) {
    this.model = model;
    this.document = document;
  }

  public Document getDocument() {
    return document;
  }

  public ModelElementInstance getDocumentElement() {
    final Element documentElement = DomUtil.getDocumentElement(document);
    if(documentElement != null) {
      return ModelUtil.getModelElement(documentElement, this);
    } else {
      return null;
    }
  }

  public void setDocumentElement(final ModelElementInstance modelElement) {
    ModelUtil.ensureInstanceOf(modelElement, ModelElementInstanceImpl.class);
    final Element domElement = ((ModelElementInstanceImpl)modelElement).getDomElement();
    DomUtil.setDocumentElement(document, domElement);
  }

  public <T extends ModelElementInstance> T newInstance(final Class<T> type) {
    final ModelElementType modelElementType = model.getType(type);
    if(modelElementType != null) {
      return newInstance(modelElementType);
    } else {
      throw new ModelException("Cannot create instance of ModelType "+type+": no such type registered.");
    }
  }

  @SuppressWarnings("unchecked")
  public <T extends ModelElementInstance> T newInstance(final ModelElementType type) {
    return (T) type.newInstance(this);
  }

  public Model getModel() {
    return model;
  }

  public ModelElementInstance getModelElementById(final String id) {
    if (id == null) {
      return null;
    }

    final Element element = DomUtil.findElementById(document, id);
    if(element != null) {
      return ModelUtil.getModelElement(element, this);
    } else {
      return null;
    }
  }

  public Collection<ModelElementInstance> getModelElementsByType(ModelElementType type) {
    final HashSet<ModelElementType> extendingTypes = new HashSet<ModelElementType>();
    extendingTypes.add(type);
    ((ModelElementTypeImpl)type).resolveExtendingTypes(extendingTypes);

    final List<ModelElementInstance> instances = new ArrayList<ModelElementInstance>();
    for (ModelElementType modelElementType : extendingTypes) {
      if(!modelElementType.isAbstract()) {
        instances.addAll(modelElementType.getInstances(this));
      }
    }
    return instances;
  }

  /**
   * Clones the model instance but not the model. So only the wrapped DOM document is cloned.
   * Changes of the model are persistent between multiple model instances.
   *
   * @return the new model instance
   */
  public Object clone() {
    return new ModelInstanceImpl(model, (Document) document.cloneNode(true));
  }
}
