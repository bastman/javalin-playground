package com.example

import io.javalin.Javalin
import io.javalin.http.Context
import io.javalin.plugin.openapi.OpenApiOptions
import io.javalin.plugin.openapi.OpenApiPlugin
import io.javalin.plugin.openapi.annotations.OpenApi
import io.javalin.plugin.openapi.annotations.OpenApiRequestBody
import io.javalin.plugin.openapi.annotations.OpenApiResponse
import io.javalin.plugin.openapi.dsl.document
import io.javalin.plugin.openapi.dsl.documented
import io.javalin.plugin.openapi.ui.ReDocOptions
import io.javalin.plugin.openapi.ui.SwaggerOptions
import io.swagger.v3.oas.models.info.Info
import java.nio.charset.Charset

data class User(val firstname:String)

fun main(args: Array<String>) {


    println(Charset.defaultCharset())
    val app = Javalin.create { config ->
       // config.defaultContentType = "application/json"
        config.enableWebjars()
        config.addStaticFiles("/public")
        config.enableCorsForAllOrigins()
        config.registerPlugin(OpenApiPlugin(getOpenApiOptions()));
        config.enableDevLogging()
        //config.compressionStrategy(null,null)
        config.autogenerateEtags = true
        config.asyncRequestTimeout = 10_000L
        //config.dynamicGzip = true
        config.enforceSsl = false
       // config.


    }
    app.get("/health") { ctx -> ctx.json(HealthResponse()) }
    app.get("/") { ctx -> ctx.json(IndexResponse()) }


    val addUserDocs = document()
            .body<User>()
            .result<Unit>("400")
            .result<Unit>("204")

    app.post("/users", UserController::createUser);
    app.post("/users/v2", documented(addUserDocs, ::addUserHandlerV2))

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

fun addUserHandlerV2(ctx: Context) {
    val user = ctx.body<User>()
    //UserRepository.addUser(user)
    ctx.status(204)
}

/*
@OpenApi(
        requestBody = OpenApiRequestBody(User::class),
        responses = [
            OpenApiResponse("400", Unit::class),
            OpenApiResponse("201", Unit::class)
        ]
)
fun addUserHandlerV3(ctx: Context) {
    val user = ctx.body<User>()
   // UserRepository.createUser(user)
    ctx.status(201)
}

 */
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
