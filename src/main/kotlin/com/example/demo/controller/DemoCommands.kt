package com.example.demo.controller

import com.example.demo.service.demo.DemoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.actuate.autoconfigure.wavefront.WavefrontProperties
import org.springframework.context.ApplicationContext
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod

/**
 * Optional Service Integration Implementation (internal CLI)
 */

@ShellComponent
class DemoCommands @Autowired constructor(val context: ApplicationContext, private val demoController: DemoController) {

    /**
     * Convenience Methods for SDET Systems
     */
    @ShellMethod("getVenueForecast")
    fun getVenueForecast(id: String): String {
        return demoController.test1(id).toString()
    }

    @ShellMethod("getGamesByDate")
    fun getGamesByDate(id: String, date: String, daysInFuture: Int = 7): String {
        return demoController.test2(id, date, daysInFuture).toString()
    }

    /**
     * Convenience Methods for DevOps Management
     */
    @ShellMethod("shutdown")
    fun shutdown(){
        SpringApplication.exit(context)
    }

    @ShellMethod("restart")
    fun restart(){
        shutdown()
        SpringApplication.run(WavefrontProperties.Application::class.java)
    }
}