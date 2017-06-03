### pricecomparer

Parts:
- Get all data from AH (JsonPath, Gson)
- Get all data from Jumbo (JSoup)
- Create the layer that will turn AhProduct data into classes for Hibernate to persist
- Create a Database and add a Hibernate model for it (Spring-ORM + Hibernate) 
- Create the Repository Layer (Spring WebMVC or Spring Boot web starter + REST)
- Create the Front-end (Angular)


    The issue currently is that I have no idea how I will sort products
    from AH and Jumbo. They have different category structures.
    
    Taking the data and persisting it to db is not the problem.
