# **Project Overview**

The Reward Application is a backend service designed to calculate reward points for customers based on their transaction history. Customers earn points based on the amount spent in each transaction. Points are calculated monthly and totaled for each customer.

### **Features**

Calculate reward points for customers based on their transaction history.
Points are calculated differently for transactions over $50 and $100.
Monthly points calculation for each customer.
Total points calculation for each customer across all transactions.

### **Technologies Used**

* Java 11 (or higher)
* Spring Boot (for building the REST API)
* JUnit 5 (for unit testing)
* Mockito (for mocking dependencies in tests)
* Maven (for project management)

### **Usage**

#### API Endpoints

##### GET /rewards

This endpoint returns a list of reward points for each customer based on their transaction history.

Example Request:
GET http://localhost:8080/rewards

Example Response:
[
{
"customerName": "Albert",
"monthlyPoints": {
"NOVEMBER": 140
},
"totalPoints": 140
},
{
"customerName": "Dani",
"monthlyPoints": {
"DECEMBER": 180
},
"totalPoints": 180
}
]

### **Reward Points Calculation**

For transactions above $100, points are calculated as (amount - 100) * 2 in addition to points earned from amounts above $50.
For transactions above $50, points are calculated as (amount - 50).
Example:

A $120 transaction would earn 40 points for the amount above $100 and 50 points for the amount between $50 and $100, for a total of 90 points.
A $50 transaction would earn 0 points (since it's below the $50 threshold).