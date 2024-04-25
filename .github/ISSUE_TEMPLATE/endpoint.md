---
name: 'Endpoint'
about: 'A template to easy add a Spring-Boot endpoint to an issue.'
title: 'E. Method EndpointName'
labels: ''
assignees: ''

---

**Details**
- **Path:** `/api/transactions/create`
- **Method:** POST
- **Description:** Creates a new transaction in the database.
- **DTO:**
```java
public class ExampleDTO {
  private int identifier;
  private String denomination;
}
```
