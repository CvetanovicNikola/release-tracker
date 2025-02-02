
Release Tracker App with Gateway

The Release Tracker is a Spring Boot-based application designed to manage software releases efficiently. 
It features a gateway, basic authentication and authorization, caching, transaction handling, pagination, 
centralized exception handling, input validation, custom logging, and swagger documentation.

⛩️ Gateway (Enabled by Default)

    The application is secured behind a gateway, which can be disabled by setting:

	release-tracker.enable.gateway = false

    in the application properties file.

When the gateway is enabled, the Release Tracker app is accessible only through the gateway on port 8081.
All requests through the gateway require authentication except GET /releases/welcome.
Basic authentication is implemented on the gateway:

    User Credentials:
        Ordinary User: username: user, password: user (Can access GET /releases)
        Admin User: username: admin, password: admin (Can access all endpoints: POST, PUT, DELETE)



🎯 Release Tracker API

    If the gateway is enabled, access the app via:

	http://localhost:8081

If the gateway is disabled, access the app directly on port 8080.

📖 Swagger Documentation (Available without authentication):
	http://localhost:8080/swagger-ui/index.html


💥 Get All Releases GET /releases

    Returns a paginated list of releases.
    Default pagination: 5 results per page, sorted by release date (descending order).
    Custom pagination: Modify query parameters:
	http://localhost:8081/releases?size=4&page=1

Filtering options:

    Supports filtering by name, description, status, and release date.
    Flexible filtering:
        If only one parameter is provided (e.g., status), it filters releases matching that status.
        If multiple parameters are provided, results must match all specified criteria.
    Search behavior:
        name and description support partial matches (LIKE queries).
        status and release date require exact matches.

💥 Get a Single Release GET /releases/{id}

    Retrieves a release by ID.
    If the release exists, it is returned.
    If not found, a custom 400 Bad Request error is returned.


💥 Create a Release POST /releases

    Requires a JSON request body with the following parameters:
	{
 	 "name": "Release Name",
  	"description": "Detailed release description",
  	"status": "CREATED",
 	"releaseDate": "2025-02-15"
	}

Validation Rules:

    All fields are required.
    name and description must not exceed a predefined length.
    status must be one of the predefined values.
    releaseDate must be in the future.

If any validation fails, a custom 400 Bad Request error is returned, indicating missing or invalid parameters.


💥 Update a Release PUT /releases/{id}

    Updates an existing release by ID.
    Request body can include one or more fields to update:
	{
 	 	"status": "DONE"
	}
Behavior:

    Only provided fields are updated.
    If a release with the given ID does not exist, a custom 400 Bad Request error is returned.


💥 Delete a Release DELETE /releases/{id}

    Deletes a release by ID.
    If the release exists, it is deleted.
    If the release does not exist, a custom 400 Bad Request error is returned.


✅ Caching: Improves performance by caching query results.
✅ Transaction Handling: Ensures database integrity during updates.
✅ Custom Logging: Logs API calls and key actions.
✅ Custom Exception Handling: Provides consistent error responses.
✅ Custom App Banner: Displays a unique banner on startup.
✅ DTOs (Data Transfer Objects): Ensure clean and structured API responses.
