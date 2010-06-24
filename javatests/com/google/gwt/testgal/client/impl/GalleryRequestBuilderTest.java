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
import com.google.gwt.testgal.client.testing.TestGalTestCase;
import com.google.gwt.user.client.ui.Anchor;

/**
 * Tests GalleryRequestBuilder.
 *
 * @author Rod McChesney
 */
public class GalleryRequestBuilderTest extends TestGalTestCase {

  private UrlBuilder urlBuilder;

  @Override
  public void gwtSetUp() {
    urlBuilder = new UrlBuilder();
    urlBuilder.setHost("example.com");
  }

  public void testRunOne() {
    urlBuilder.setParameter("run", "abc.def");
    GalleryRequestBuilder galleryRequestBuilder = new GalleryRequestBuilder(urlBuilder);
    Anchor anchor = galleryRequestBuilder
        .setRunOne("foo.bar")
        .build();
    assertEquals("http://example.com?run=foo.bar", anchor.getHref());
  }

  public void testRunAll() {
    urlBuilder.setParameter("run", "abc.def");
    GalleryRequestBuilder galleryRequestBuilder = new GalleryRequestBuilder(urlBuilder);
    Anchor anchor = galleryRequestBuilder
        .setRunAll()
        .build();
    assertEquals("http://example.com", anchor.getHref());
  }

  public void testAnchorText() {
    GalleryRequestBuilder galleryRequestBuilder = new GalleryRequestBuilder(urlBuilder);
    Anchor anchor = galleryRequestBuilder
        .setRunAll()
        .setAnchorText("Wahoo")
        .build();
    assertEquals("http://example.com", anchor.getHref());
    assertEquals("Wahoo", anchor.getText());
  }

  public void testAddStyleName_one() {
    GalleryRequestBuilder galleryRequestBuilder = new GalleryRequestBuilder(urlBuilder);
    Anchor anchor = galleryRequestBuilder
        .setRunAll()
        .addStyleName("bar")
        .addStyleName("baz")
        .build();
    assertEquals("http://example.com", anchor.getHref());
    assertEquals("bar baz", anchor.getStyleName());
  }
}
