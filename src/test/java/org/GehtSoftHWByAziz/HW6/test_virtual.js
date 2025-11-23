import http from "k6/http";
import { check, sleep } from "k6";

export const options = {
  vus: 5000,
  duration: "10s",
};

const baseUrl = "http://localhost:8080/api/v1/users";

export default function () {
  let res = http.get(baseUrl);
  check(res, {
    "GET status 200": (r) => r.status === 200,
  });
  sleep(1);

  res = http.post(baseUrl, JSON.stringify({
    id: 123,
    name: "Alice",
    email: "alice@example.com"
  }), { headers: { "Content-Type": "application/json" }});
  check(res, {
    "POST status 201 or 200": (r) => r.status === 201 || r.status === 200,
  });
  sleep(1);

  res = http.put(`${baseUrl}/123`, JSON.stringify({
    id: 123,
    name: "Alice Updated",
    email: "alice.updated@example.com"
  }), { headers: { "Content-Type": "application/json" }});
  check(res, {
    "PUT status 200": (r) => r.status === 200,
  });
  sleep(1);


  res = http.delete(`${baseUrl}/123`);
  check(res, {
    "DELETE status 204 or 200": (r) => r.status === 204 || r.status === 200,
  });
  sleep(1);
}
