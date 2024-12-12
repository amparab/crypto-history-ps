# crypto-history

This is a project to fetch the bitcoin prices history for a specified date range.
This project consists of a UI and a backend to server the request.

## Run the service locally.

You can run the service locally in your favourite IDE.
Just add the following VM arg. ```-Dapi_key=<API_KEY>```
and run the Spring boot application.
The UI should be accessible on http://localhost:8080/

To run using Docker,
1. Navigate to ``` cd E:\PS\crypto-history ```
2. Run ``` docker build -t crypto-history-service . ```
3. Optional steps to push to local registry. (Or skip to step 4)
  Run ``` docker tag crypto-history-service localhost:5000/crypto-history-service ```
  Run ``` docker push localhost:5000/crypto-history-service```
  Run ``` docker pull localhost:5000/crypto-history-service ```
  Run ```docker run -d -e JAVA_OPTS="-Dapi_key=482ca6e1dfab2be61f618712" -p 8080:8080 localhost:5000/crypto-history-service```
4. Check if image is created using ``` docker images``` command.
5. Run the service in detached mode ``` docker run -d -e JAVA_OPTS="-Dapi_key=<API_KEY>" -p 8080:8080 crypto-history-service ```

The UI should be accessible on http://localhost:8080/

## UI Features
There are 3 required inputs that the user is expected to give.
From Date : The date from which user needs the bitcoin prices.
To Date : The date to which the user needs to bitcoin prices.
Currency : The default currency to display the prices is USD, but the user can request the history in a desired currency from dropdown.

The highest and lowest values in the date range are displayed with markers "(high)" and "(low)".
Offline mode : The user can switch to offline mode in case of poor or no internet connectivity. In offline mode, the user is served requests based on cached data.

Techstack : ReactJs, Material UI

## Backend features
The backend exposes 2 endpoints to the users which are accessible via Swagger http://localhost:8080/swagger-ui/index.html#/
1. /bitcoin/history/dateRange : This is the main endpoint responsible for returning the historical prices based on date range
2. /currency : This returns the list of currencies supported by the application.

Techstack : Java, SpringBoot

Application flow diagram:

<img width="412" alt="crypto-history" src="https://github.com/user-attachments/assets/f75685f1-b58f-4269-8f09-3f19a3ce8052" />

## Design Approach

The communication between the UI and the Java backend is carried out via REST APIs.

Types of Responses:

The user can expect one of the following responses:

Success: The history data is successfully fetched.

No Data Found: No historical data is available for the given inputs.

Invalid Input: The input provided by the user is not valid.
Could Not Process Request: The request could not be processed due to an error.

Dependencies on External Services:
The application relies on external services for the following:

1. Bitcoin History Data:

Provides the historical price of Bitcoin for each day.
Since this data doesn’t change frequently during the day, it is cached in-memory and refreshed every 12 hours by fetching it from the external service.

2. Conversion Rates:

Provides exchange rates for different currencies.
As these rates can change frequently, this data is cached in-memory and updated every 5 minutes.
An alternative approach was to fetch the data on every request for near real-time values. However, the external API returns the exchange rates for all currencies in a single large response (USD value of all currencies). Fetching this data on every request would have caused unnecessary network overhead, so in-memory caching was introduced.

3. Currency Data:

Used to populate the dropdown on the UI.
This data is rarely updated, so it is fetched once during application startup and cached in-memory for future use.
This data is designed to depend on the conversion rates data, with the rationale that only currencies for which conversion rates are available should be exposed to the user.

In the event of a failure of any external service, the system will continue to serve requests using the cached data.

### Design patterns used

**Template Design Pattern** :

The application's flow primarily consists of three steps:

Create the Request: Prepare the request with the necessary parameters.

Fetch Data for the Date Range: Retrieve data for the specified date range from external services or the cache.

Create the Response: Convert values to the target currency, structure the response, and include other necessary details.

These steps can be encapsulated in a standard template, ensuring extensibility. Subclasses can provide specific implementations for the abstract methods while relying on the overall structure. This improves code reusability and ensures a consistent flow.

**Builder Pattern** :

The Builder Pattern is used to construct the final response, as it is built incrementally in different parts of the code. This pattern ensures that the construction logic is separated from the representation, making the code easier to maintain and extend.

## Future Scope:

API Key Management: Currently, the API key required to start the application is supplied as a VM argument, as committing the API key in code or storing it directly in the repository is not a best practice.
In a live environment, the API key can be securely stored as secrets (e.g., Kubernetes Secrets) and accessed at runtime.
Online vs. Offline Mode:

Currently, there is no difference in how the application serves requests in online and offline modes.
In the future, the application could:
Fetch historical and conversion rate data from the external service for every API request in online mode.
Serve requests from the cache in offline mode.
Cache Monitoring and Recovery:

A background thread can be introduced to monitor the last cache update time.
If the cache is not updated as expected (e.g., due to an external service failure):
The system can make an appropriate decision, such as serving stale data and displaying a relevant message to the user.



