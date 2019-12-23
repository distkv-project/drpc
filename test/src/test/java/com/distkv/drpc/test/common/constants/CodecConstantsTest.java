package com.distkv.drpc.test.common.constants;

import com.distkv.drpc.constants.CodecConstants;
import com.distkv.drpc.exception.CodecException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CodecConstantsTest {

  @Test(expectedExceptions = CodecException.class)
  public void testGetVersionByValue() {
    Assert.assertEquals(CodecConstants.Version.getVersionByValue((byte) 1),
        CodecConstants.Version.VERSION_1);

    //exception
    CodecConstants.Version.getVersionByValue((byte) 0);
  }

  @Test(expectedExceptions = CodecException.class)
  public void testGetDataTypeByValue() {
    Assert.assertEquals(CodecConstants.DataType.getDataTypeByValue((byte) 0),
        CodecConstants.DataType.NONE);
    Assert.assertEquals(CodecConstants.DataType.getDataTypeByValue((byte) 1),
        CodecConstants.DataType.REQUEST);
    Assert.assertEquals(CodecConstants.DataType.getDataTypeByValue((byte) 2),
        CodecConstants.DataType.RESPONSE);
    Assert.assertEquals(CodecConstants.DataType.getDataTypeByValue((byte) 3),
        CodecConstants.DataType.EXCEPTION);

    //exception
    CodecConstants.DataType.getDataTypeByValue((byte) 4);
  }
}
