# ridtracker
Tracks record id in a Loan IQ database.
This project was originally a legacy c++ app now modernised in Java.
If you can't find what table the rid belongs or where else it exists, this app will find it.
For example, a Loan drawdown transaction may own a cashflow, consequently the rid will appear in both locations.
Example 2 - find all references to a customer within the DB.

This project is largely a playground for my software development albeit useful within my domain.
Technical :
Added standard logging using Log4j (refactor)
Added Dependency Injection using Guice. (refactor)
TODO: implement jpa using Hibernate.
TODO: scale up with akka.

Functional : 
TODO: given enough computational power...
An entire model of business data within the DB can be graphed and used for business intelligence.
Embed this as a web app providing analytics on the business.
