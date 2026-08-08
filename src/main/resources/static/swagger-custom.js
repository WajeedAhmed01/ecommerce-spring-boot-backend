window.onload = function () {
    window.ui = SwaggerUIBundle({
        url: "/v3/api-docs",
        dom_id: "#swagger-ui",

        requestInterceptor: function (request) {
            const token = localStorage.getItem("jwt_token");

            if (token) {
                request.headers["Authorization"] = "Bearer " + token;
            }

            if (
                request.method &&
                request.method.toLowerCase() === "post" &&
                request.url.includes("/api/users/login")
            ) {
                request._isLoginRequest = true;
            }

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
            if (
                response.url &&
                response.url.includes("/api/users/login") &&
                response.status === 200
            ) {
                try {
                    const data = JSON.parse(response.text);

                    if (data.token) {
                        localStorage.setItem("jwt_token", data.token);
                        console.log("JWT stored successfully");
                    }
                } catch (error) {
                    console.error("Could not store JWT:", error);
                }
            }

            return response;
        }
    });
};