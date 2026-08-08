window.onload = function () {

    window.ui = SwaggerUIBundle({
        url: "/v3/api-docs",
        dom_id: "#swagger-ui",

        requestInterceptor: function (request) {

            // Get JWT automatically from browser storage
            const token = localStorage.getItem("jwt_token");

            if (token) {
                request.headers["Authorization"] = "Bearer " + token;
            }

            // Generate Idempotency-Key automatically
            // for placing an order
            if (
                request.method &&
                request.method.toLowerCase() === "post" &&
                request.url.includes("/api/orders")
            ) {
                request.headers["Idempotency-Key"] = crypto.randomUUID();
            }

            return request;
        },

        responseInterceptor: function (response) {

            // After successful login, automatically store JWT
            if (
                response.url &&
                response.url.includes("/api/users/login") &&
                response.status === 200
            ) {
                try {
                    const body = JSON.parse(response.text);

                    if (body.token) {
                        localStorage.setItem("jwt_token", body.token);
                        console.log("JWT stored automatically");
                    }

                } catch (error) {
                    console.error("Could not store JWT:", error);
                }
            }

            return response;
        }
    });
};