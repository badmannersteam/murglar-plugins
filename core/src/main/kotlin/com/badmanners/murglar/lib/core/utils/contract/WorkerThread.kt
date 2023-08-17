package com.badmanners.murglar.lib.core.utils.contract


/**
 * Denotes that the annotated method should only be called on a worker thread.
 *
 * If the annotated element is a class, then all methods in the class should be called
 * on a worker thread.
 */
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.CLASS,
    AnnotationTarget.VALUE_PARAMETER
)
annotation class WorkerThread 