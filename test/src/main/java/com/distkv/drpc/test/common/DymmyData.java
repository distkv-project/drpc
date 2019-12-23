package com.distkv.drpc.test.common;

import java.util.concurrent.ThreadLocalRandom;

public class DymmyData {

  /**
   * The threshold of determinant weather need fill content randomly.
   *
   * If size is greater than threshold, 36 bytes will be filled with UUID while left bytes filled
   * with an constance content. Be simple, only 36 bytes will be randomly.
   */
  private static final int THRESHOLD = 36;

  public static DymmyData INSTANCE_5_BYTES = new DymmyData(5);
  public static DymmyData INSTANCE_1K_BYTES = new DymmyData(1024);

  private final byte[] content;
  private final int size;

  public DymmyData(int size) {
    this.size = size;
    if (size <= THRESHOLD) {
      content = null;
    } else {
      content = new byte[size];
      ThreadLocalRandom random = ThreadLocalRandom.current();
      byte[] constanceContent = new byte[size - THRESHOLD];
      random.nextBytes(constanceContent);
      System.arraycopy(constanceContent, 0, content, 0, constanceContent.length);
    }
  }

  public byte[] getContent() {
    ThreadLocalRandom random = ThreadLocalRandom.current();
    if (size <= THRESHOLD) {
      byte[] randomContent = new byte[size];
      random.nextBytes(randomContent);
      return randomContent;
    } else {
      byte[] randomContent = new byte[THRESHOLD];
      random.nextBytes(randomContent);
      System.arraycopy(randomContent, 0, content, size - THRESHOLD, THRESHOLD);
      return content;
    }
  }
}
