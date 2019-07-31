package com.example

import io.javalin.Javalin
import io.javalin.http.Context
import io.javalin.plugin.openapi.OpenApiOptions
import io.javalin.plugin.openapi.OpenApiPlugin
import io.javalin.plugin.openapi.annotations.OpenApi
import io.javalin.plugin.openapi.ui.ReDocOptions
import io.javalin.plugin.openapi.ui.SwaggerOptions
import io.swagger.v3.oas.models.info.Info


fun main(args: Array<String>) {
    val app = Javalin.create { config ->
        config.defaultContentType = "application/json"
        config.addStaticFiles("/public")
        config.enableCorsForAllOrigins()
        config.registerPlugin(OpenApiPlugin(getOpenApiOptions()));
    }
    app.get("/health") { ctx -> ctx.json(HealthResponse()) }
    app.get("/") { ctx -> ctx.json(IndexResponse()) }



    app.post("/users", UserController::createUser);

    //OpenApiB

    /*
    val createUserDocumentation = OpenApiBuilder.document()
            .body(User::class.java)
            .json("200", User::class.java)

    app.post("/users", OpenApiBuilder.documented(createUserDocumentation, { ctx ->
        // ...
    }))

     */



    app.start(7000)
    /*
    app.get("/") { ctx -> ctx.result(HealthResponse()) }
    app.get("/health")  { ctx -> ctx. }

     */
}

data class HealthResponse(val status: String = "UP")
data class IndexResponse(val message: String = "It works ;)")

private fun getOpenApiOptions(): OpenApiOptions {
    val applicationInfo = Info()
            .version("1.0")
            .description("My Application")

    return OpenApiOptions(applicationInfo)
            .path("/swagger-docs")
            .swagger(SwaggerOptions("/swagger").title("My Swagger Documentation"))
            .activateAnnotationScanningFor("com.example")
            .reDoc(ReDocOptions("/redoc").title("My ReDoc Documentation"))
}


object UserController {
    @OpenApi(
            path = "/users",
            method = io.javalin.plugin.openapi.annotations.HttpMethod.POST
    )
    fun createUser(ctx: Context) {
        // ...
    }
}
