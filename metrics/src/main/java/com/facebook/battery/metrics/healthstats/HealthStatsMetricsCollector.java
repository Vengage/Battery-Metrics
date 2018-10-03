/*
 * Copyright (c) 2017-present, Facebook, Inc. All rights reserved.
 *
 * <p>This source code is licensed under the BSD-style license found in the LICENSE file in the root
 * directory of this source tree. An additional grant of patent rights can be found in the PATENTS
 * file in the same directory.
 */
package com.facebook.battery.metrics.healthstats;

import android.content.Context;
import android.os.Build;
import android.os.health.SystemHealthManager;
import android.support.annotation.RequiresApi;
import com.facebook.battery.metrics.core.SystemMetricsCollector;
import com.facebook.battery.metrics.core.SystemMetricsLogger;
import com.facebook.infer.annotation.ThreadSafe;

@RequiresApi(api = Build.VERSION_CODES.N)
@ThreadSafe(enableChecks = false)
public class HealthStatsMetricsCollector extends SystemMetricsCollector<HealthStatsMetrics> {

  private static final String TAG = "HealthStatsMetricsCollector";
  private final SystemHealthManager mSystemHealthManager;

  public HealthStatsMetricsCollector(Context context) {
    mSystemHealthManager =
        (SystemHealthManager) context.getSystemService(Context.SYSTEM_HEALTH_SERVICE);
  }

  @Override
  @SuppressWarnings("CatchGeneralException")
  // because takeMyUidSnapshot wraps RemoteException in a RuntimeException
  public boolean getSnapshot(HealthStatsMetrics snapshot) {
    try {
      snapshot.set(mSystemHealthManager.takeMyUidSnapshot());
      return true;
    } catch (RuntimeException e) {
      SystemMetricsLogger.wtf(TAG, "Unable to snapshot healthstats", e);
    }

    return false;
  }

  @Override
  public HealthStatsMetrics createMetrics() {
    return new HealthStatsMetrics();
  }
}
