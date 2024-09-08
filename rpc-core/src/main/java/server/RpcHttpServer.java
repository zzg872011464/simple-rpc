package server;

import handler.RpcHttpRequestHandler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;

// 启动vertx http服务器
public class RpcHttpServer {

    public void doStart(int port) {
        Vertx vertx = Vertx.vertx();
        HttpServer server = vertx.createHttpServer();

        server.requestHandler(new RpcHttpRequestHandler());

        server.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("Server is now listening on serverPort " + port);
            } else {
                System.err.println("Failed to start server: " + result.cause());
            }
        });
    }
}
