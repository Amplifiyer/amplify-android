/*
 * Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.amplifyframework.analytics;

import android.support.annotation.NonNull;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.plugin.Category;
import com.amplifyframework.core.plugin.CategoryPlugin;

/**
 * Defines the Client API consumed by the application.
 * Internally routes the calls to the Analytics Category
 * plugins registered.
 */
public class AnalyticsCategory extends Amplify implements AnalyticsCategoryClientBehavior {

    /**
     * Mark that this is Analytics category.
     */
    private static Category category = Category.ANALYTICS;

    /**
     * By default collection and sending of Analytics events
     * are enabled.
     */
    private static boolean enabled = true;

    /**
     * Protect enabling and disabling of Analytics event
     * collection and sending.
     */
    private static final Object LOCK = new Object();

    @Override
    public void disable() {
        synchronized (LOCK) {
            enabled = false;
        }
    }

    @Override
    public void enable() {
        synchronized (LOCK) {
            enabled = true;
        }
    }

    @Override
    public void recordEvent(@NonNull String eventName) throws AnalyticsException {
        if (enabled) {
            CategoryPlugin analyticsCategoryPlugin = Amplify.getPluginForCategory(category);
            if (analyticsCategoryPlugin instanceof AnalyticsCategoryPlugin) {
                AnalyticsEvent analyticsEvent = new AnalyticsEvent(eventName);
                ((AnalyticsCategoryPlugin) analyticsCategoryPlugin).recordEvent(analyticsEvent);
            } else {
                throw new AnalyticsException("Failed to record analyticsEvent. " +
                        "Please check if a valid storage plugin is registered.");
            }
        }
    }

    @Override
    public void recordEvent(@NonNull final AnalyticsEvent analyticsEvent) throws AnalyticsException {
        if (enabled) {
            CategoryPlugin analyticsPlugin = Amplify.getPluginForCategory(category);
            if (analyticsPlugin instanceof AnalyticsCategoryPlugin) {
                ((AnalyticsCategoryPlugin) analyticsPlugin).recordEvent(analyticsEvent);
            } else {
                throw new AnalyticsException("Failed to record analyticsEvent. " +
                        "Please check if a valid storage plugin is registered.");
            }
        }
    }

    @Override
    public void updateProfile(@NonNull AnalyticsProfile analyticsProfile) throws AnalyticsException {
        if (enabled) {
            CategoryPlugin analyticsPlugin = Amplify.getPluginForCategory(category);
            if (analyticsPlugin instanceof AnalyticsCategoryPlugin) {
                ((AnalyticsCategoryPlugin) analyticsPlugin).updateProfile(analyticsProfile);
            } else {
                throw new AnalyticsException("Failed to record analyticsEvent. " +
                        "Please check if a valid storage plugin is registered.");
            }
        }
    }
}