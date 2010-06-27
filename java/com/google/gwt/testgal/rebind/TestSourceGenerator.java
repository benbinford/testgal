/*
 * Copyright 2009 Google Inc.
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
package com.google.gwt.testgal.rebind;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Given a subclass of junit.framework.TestCase, generates a class that implements
 * the {@link com.google.gwt.testgal.client.impl.TestClass} interface.
 *
 * <p>Note: this generator is incompatible with the normal way of
 * running a GWTTestCase and should be used only in a test gallery module.</p>
 *
 * @author Brian Slesinsky
 */
public class TestSourceGenerator extends Generator {

  @Override
  public String generate(TreeLogger logger, GeneratorContext context, String requestedTypeName)
      throws UnableToCompleteException {

    TypeOracle typeOracle = context.getTypeOracle();
    JClassType requestedType = findType(logger, typeOracle, requestedTypeName);
    JClassType expectedSuperClass = findType(logger, typeOracle, "junit.framework.TestCase");
    checkHasSuperClass(logger, requestedType, expectedSuperClass);

    JClassType gwtTestCaseClass =
        findType(logger, typeOracle, "com.google.gwt.junit.client.GWTTestCase");
    boolean isGWTTestCase = requestedType.isAssignableTo(gwtTestCaseClass);

    String genPackageName = requestedType.getPackage().getName();
    String genTypeName = requestedType.getSimpleSourceName() + "_GEN";
    String genFullName = genPackageName + "." + genTypeName;

    SourceWriter out =
        getSourceWriter(logger, context, genPackageName, genTypeName);
    if (out == null) {
      // already generated
      return genFullName;
    }

    List<String> testMethods = findTestMethods(requestedType);

    out.println("public String __getName() {");
    out.println("  return \"" + requestedTypeName + "\";");
    out.println("}");
    out.println();
    out.println("public List<TestMethod> __getTestMethods() {");
    out.indent();
    out.println("List<TestMethod> result = new ArrayList<TestMethod>();");
    for (String testMethod : testMethods) {
      out.println("result.add(new TestMethod() {");
      out.println("  public String getName() { return \"" + testMethod + "\"; }");
      out.println("  public SingleTest makeTest() {");
      out.println("    return new " + testMethod + "_GEN();");
      out.println("  }");
      out.println("});");
    }
    out.println("return result;");
    out.outdent();
    out.println("}");

    for (String testMethod : testMethods) {
      out.println("static class " + testMethod + "_GEN");
      out.println("  extends " + requestedTypeName + " implements SingleTest {");
      out.println("  public void __runSetUp() throws Exception {");
      out.println("    setName(\"" + testMethod + "\");");
      out.println("    super.setUp();");
      out.println("  }");
      out.println("  public void __runTestMethod() throws Exception {");
      out.println("    " + testMethod + "();");
      out.println("  }");
      out.println("  public void __runTearDown() throws Exception { super.tearDown(); }");
      if (isGWTTestCase) {

        out.println("  private SingleTest.Callback __callback;");
        out.println("  public void __setCallback(SingleTest.Callback newCallback) {");
        out.println("    __callback = newCallback;");
        out.println("  }");
        out.println("  public void delayTestFinish(int timeoutMillis) {");
        out.println("    if (__callback != null) { __callback.delayTestFinish(timeoutMillis); }");
        out.println("  }");
        out.println("  public void finishTest() {");
        out.println("    if (__callback != null) { __callback.finishTest(); }");
        out.println("  }");
      } else {
        out.println("  public void __setCallback(SingleTest.Callback newCallback) {");
        out.println("  }");
      }
      out.println("}");
    }

    out.commit(logger);
    
    return genFullName;
  }

  private List<String> findTestMethods(JClassType classType) {
    List<String> result = new ArrayList<String>();

    // TODO(skybrian): handle inheritance
    for (JMethod method : classType.getMethods()) {
      if (method.getName().startsWith("test") &&
          method.isPublic() &&
          method.getParameters().length == 0) {
        result.add(method.getName());
      }
    }

    return result;
  }

  private static JClassType findType(TreeLogger logger, TypeOracle typeOracle, String fullClassName)
      throws UnableToCompleteException {
    JClassType classType = typeOracle.findType(fullClassName);
    if (classType == null) {
      logger.log(TreeLogger.Type.ERROR, "cannot load type: " + fullClassName);
      throw new UnableToCompleteException();
    }
    return classType;
  }

  private static SourceWriter getSourceWriter(TreeLogger logger, GeneratorContext context,
      String genPackageName, String genTypeName) {

    PrintWriter printWriter = context.tryCreate(logger, genPackageName, genTypeName);
    if (printWriter == null) {
      // The compiler called us even though we have already generated this class, so
      // we don't need to generate it again.
      return null;
    }

    ClassSourceFileComposerFactory factory =
        new ClassSourceFileComposerFactory(genPackageName, genTypeName);

    factory.addImport("com.google.gwt.testgal.client.impl.TestClass");
    factory.addImport("com.google.gwt.testgal.client.impl.TestMethod");
    factory.addImport("com.google.gwt.testgal.client.impl.SingleTest");
    factory.addImport("java.util.List");
    factory.addImport("java.util.ArrayList");
    factory.addImport("junit.framework.TestCase");
    factory.addImplementedInterface("TestClass");

    return factory.createSourceWriter(context, printWriter);
  }


  private static void checkHasSuperClass(TreeLogger logger, JClassType requestedType,
      JClassType testCaseClass)
      throws UnableToCompleteException {

    if (!requestedType.isAssignableTo(testCaseClass)) {
      logger.log(TreeLogger.Type.ERROR,
          "can't generate subclass because " + requestedType.getQualifiedSourceName() +
          " is not a subclass of" + testCaseClass.getQualifiedSourceName());
      throw new UnableToCompleteException();
    }
  }
}
