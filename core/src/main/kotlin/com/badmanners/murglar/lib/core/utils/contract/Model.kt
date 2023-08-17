package com.badmanners.murglar.lib.core.utils.contract

/**
 * Annotation for kotlin-noarg plugin, that generates no-arg constructors.
 */
//https://youtrack.jetbrains.com/issue/KT-53122
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS)
annotation class Model
