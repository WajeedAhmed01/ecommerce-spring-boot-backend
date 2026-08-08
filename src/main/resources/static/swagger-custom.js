window.onload = function () {
    window.ui = SwaggerUIBundle({
        url: "/v3/api-docs",
        dom_id: "#swagger-ui",

        requestInterceptor: function (request) {

            if (
                request.method &&
                request.method.toLowerCase() === "post" &&
                request.url.includes("/api/orders")
            ) {
                request.headers["Idempotency-Key"] = crypto.randomUUID();
            }

            return request;
        }
    });
};