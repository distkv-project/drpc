package com.distkv.drpc.test.common.utils;

import com.distkv.drpc.utils.ReflectUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.lang.reflect.Method;
import java.util.List;

public class ReflectUtilsTest {

  @Test
  public void testParseMethod() {
    List<Method> methods = ReflectUtils.parseMethod(Runnable.class);
    Assert.assertEquals("public abstract void java.lang.Runnable.run()",
        methods.get(0).toString());
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
    Assert.assertEquals("run()",
        ReflectUtils.getMethodDesc(ReflectUtils.parseMethod(Runnable.class).get(0)));
  }
}
