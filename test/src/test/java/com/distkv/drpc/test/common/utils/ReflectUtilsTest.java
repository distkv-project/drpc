package com.distkv.drpc.test.common.utils;

import com.distkv.drpc.utils.ReflectUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.List;

public class ReflectUtilsTest {

  @Test
  public void testParseMethod() {
    List<Method> methods = ReflectUtils.parseMethod(ReflectUtils.class);
    Assert.assertEquals(
        "public static java.lang.Class " +
            "com.distkv.drpc.utils.ReflectUtils.forName(java.lang.String) " +
            "throws java.lang.ClassNotFoundException", methods.get(0).toString());
    Assert.assertEquals(
        "public static java.util.List " +
            "com.distkv.drpc.utils.ReflectUtils.parseMethod(java.lang.Class)",
        methods.get(1).toString());

    Assert.assertEquals(
        "public static java.lang.String " +
            "com.distkv.drpc.utils.ReflectUtils.getMethodDesc(java.lang.reflect.Method)",
        methods.get(2).toString());
  }

  @Test
  public void testForName() throws ClassNotFoundException {
    Class<?> reflectUtils = ReflectUtils.forName("com.distkv.drpc.utils.ReflectUtils");
    Assert.assertEquals(reflectUtils, ReflectUtils.class);
  }

  @Test(expectedExceptions = ClassNotFoundException.class)
  public void testForNameException() throws ClassNotFoundException {
    ReflectUtils.forName("com.distkv.drpc.utils.XXXXX");
  }

  @Test
  public void testGetMethodDesc() {
    Assert.assertEquals("forName(java.lang.String)",
        ReflectUtils.getMethodDesc(ReflectUtils.parseMethod(ReflectUtils.class).get(0)));
  }
}
