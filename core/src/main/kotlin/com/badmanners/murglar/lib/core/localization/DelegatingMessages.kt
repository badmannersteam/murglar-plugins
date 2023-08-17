package com.badmanners.murglar.lib.core.localization

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.Locale

/**
 * [InvocationHandler] for [Messages], that delegates calls to the messages with current selected locale.
 */
class DelegatingMessages<Msg : Messages> : InvocationHandler {

    val messages: Map<Locale, Msg>

    var currentLocale: Locale
        set(value) {
            check(messages.containsKey(value)) { "Mapping doesn't contain locale '$value'!" }
            field = value
        }

    @Suppress("ConvertSecondaryConstructorToPrimary")
    private constructor(messages: Map<Locale, Msg>) {
        this.messages = messages
        this.currentLocale = Locale.ENGLISH
    }

    override fun invoke(proxy: Any, method: Method, args: Array<Any>?): Any {
        return method.invoke(messages[currentLocale], *(args ?: arrayOfNulls<Any>(0)))
    }

    companion object {

        @Suppress("UNCHECKED_CAST")
        fun <Msg : Messages> from(messagesInterface: Class<out Msg>, mapping: Map<Locale, Msg>): Msg {
            val classLoader = messagesInterface.classLoader
            val interfaces = arrayOf<Class<*>>(messagesInterface)
            val handler = DelegatingMessages(mapping)
            return Proxy.newProxyInstance(classLoader, interfaces, handler) as Msg
        }
    }
}