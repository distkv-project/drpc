package com.distkv.drpc.test.common;

import java.util.Arrays;
import java.util.Random;

public class DymmyData {

  private static ThreadLocal<Long> SEED = new ThreadLocal<>();
  private static final int SIZE_MB = 1024 * 1024; // 1MB
  private static final int SEED_SIZE = Long.BYTES;
  private static final byte[] BIG_SIZE_DEFAULT_VALUE = new byte[SIZE_MB];

  static {
    Random random = new Random();
    random.nextBytes(BIG_SIZE_DEFAULT_VALUE);
  }

  private final byte[] content;

  public DymmyData(int size) {
    if (size > SIZE_MB) {
      throw new IllegalArgumentException("Size could not larger than 1MB(1024 * 1024)");
    }
    this.content = new byte[size];
    Long seed = SEED.get();
    if (seed == null) {
      SEED.set(0L);
      seed = SEED.get();
    }
    longToBytes(content, Math.max(0, size - SEED_SIZE), seed);
    seed++;
    SEED.set(seed);
  }

  public byte[] getContent() {
    return content;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof DymmyData)) {
      return false;
    }
    return Arrays.equals(content, ((DymmyData) obj).getContent());
  }

  private void longToBytes(byte[] dst, int pos, long val) {
    for (int i = 0; i < Math.min(dst.length, Long.BYTES); i++) {
      dst[pos + i] = (byte) ((val >> (8 * i)) & 0xff);
    }
  }
}
