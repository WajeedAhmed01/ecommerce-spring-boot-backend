window.onload = function () {

    window.ui = SwaggerUIBundle({
        url: "/v3/api-docs",
        dom_id: "#swagger-ui",

        requestInterceptor: function (request) {

            // Get JWT token from browser storage
            const token = localStorage.getItem("jwt_token");

            if (token) {
                request.headers["Authorization"] = "Bearer " + token;
            }

            // Generate Idempotency-Key automatically for place-order
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

            // When login succeeds, save JWT token
            if (
                response.url.includes("/login") &&
                response.status === 200
            ) {
                try {
                    const data = JSON.parse(response.data);

                    if (data.token) {
                        localStorage.setItem("jwt_token", data.token);
                        console.log("JWT token saved automatically");
                    }
                } catch (e) {
                    console.error("Could not save JWT token", e);
                }
            }

            return response;
        }
    });
};