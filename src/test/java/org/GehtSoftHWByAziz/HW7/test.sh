#!/bin/bash
# Test CRUD
curl http://localhost:8080/api/v1/users  # GET all
curl -X POST -H "Content-Type: application/json" -d '{"id":"1","name":"John","email":"john@example.com"}' http://localhost:8080/api/v1/users  # POST create
curl http://localhost:8080/api/v1/users/1  # GET by ID
curl -X PUT -H "Content-Type: application/json" -d '{"id":"1","name":"John Updated","email":"john@new.com"}' http://localhost:8080/api/v1/users/1  # PUT update
curl -X DELETE http://localhost:8080/api/v1/users/1  # DELETE
