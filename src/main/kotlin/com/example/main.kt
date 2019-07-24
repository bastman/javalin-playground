package com.example

import io.javalin.Javalin

fun main(args: Array<String>) {
    val app = Javalin.create { config ->
        config.defaultContentType = "application/json"
        config.addStaticFiles("/public")
        config.enableCorsForAllOrigins()
    }
    app.get("/health")  { ctx -> ctx.json(HealthResponse()) }
    app.get("/")  { ctx -> ctx.json(IndexResponse()) }
    app.start(7000)
    /*
    app.get("/") { ctx -> ctx.result(HealthResponse()) }
    app.get("/health")  { ctx -> ctx. }

     */
}

data class HealthResponse(val status:String="UP")
data class IndexResponse(val message:String="It works ;)")
