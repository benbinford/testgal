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

import com.google.gwt.testgal.client.testing.TestGalTestCase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Tests GalleryRequest.
 *
 * @author Rod McChesney
 */
public class GalleryRequestTest extends TestGalTestCase {

  private HashMap<String, List<String>> paramMap;

  @Override
  public void gwtSetUp() {
    paramMap = new HashMap<String, List<String>>();
  }

  public void testIncludes_nonePresent() {
    GalleryRequest galleryRequest = new GalleryRequest(paramMap);
    assertTrue(galleryRequest.includes("a.b"));
    assertTrue(galleryRequest.includes("something.else"));
  }

  public void testIncludes_onePresent() {
    paramMap.put("run", Arrays.asList(new String[] { "foo.bar" }));
    GalleryRequest galleryRequest = new GalleryRequest(paramMap);
    assertTrue(galleryRequest.includes("foo.bar"));
    assertFalse(galleryRequest.includes("something.else"));
  }

  public void testIncludes_twoPresent() {
    paramMap.put("run", Arrays.asList(new String[] { "foo.bar", "baz.blat" }));
    GalleryRequest galleryRequest = new GalleryRequest(paramMap);
    assertTrue(galleryRequest.includes("foo.bar"));
    assertTrue(galleryRequest.includes("baz.blat"));
    assertFalse(galleryRequest.includes("something.else"));
  }
}
