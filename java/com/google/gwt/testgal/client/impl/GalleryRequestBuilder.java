/*
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.testgal.client.impl;

import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.ui.Anchor;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder for links to run one or all TestGal tests.
 *
 * @author Rod McChesney
 */
public class GalleryRequestBuilder {

  private final UrlBuilder urlBuilder;
  private final List<String> styleNames = new ArrayList<String>();
  private String anchorText = "Rerun";

  /**
   * Constructs an instance with the current location's URL builder.
   * Modifies the builder by setting or removing the "run" parameter.
   */
  public GalleryRequestBuilder(UrlBuilder urlBuilder) {
    this.urlBuilder = urlBuilder;
  }

  /**
   * Sets up the request to run all tests.
   */
  public GalleryRequestBuilder setRunAll() {
    urlBuilder.removeParameter(GalleryRequest.RUN_PARAM);
    return this;
  }

  /**
   * Sets up the request to run a single test.
   */
  public GalleryRequestBuilder setRunOne(String testClassName) {
    urlBuilder.setParameter(GalleryRequest.RUN_PARAM, testClassName);
    return this;
  }

  /**
   * Sets the text for the link being built.
   */
  public GalleryRequestBuilder setAnchorText(String newAnchorText) {
    anchorText = newAnchorText;
    return this;
  }

  /**
   * Adds a style to the link being built.
   */
  public GalleryRequestBuilder addStyleName(String styleName) {
    styleNames.add(styleName);
    return this;
  }

  /**
   * Returns an Anchor for the request being built.
   */
  public Anchor build() {
    Anchor anchor = new Anchor(anchorText, urlBuilder.buildString());
    for (String styleName : styleNames) {
      anchor.addStyleName(styleName);
    }
    styleNames.clear();
    return anchor;
  }
}
