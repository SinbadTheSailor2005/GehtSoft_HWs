import http from "k6/http";
import { check, sleep } from "k6";

export const options = {
  vus: 5,
  duration: "10s",
};

const url = "http://localhost:8080/";

export default function () {
  const res = http.get(url);
  check(res, {
    "Response status = 200": (r) => r.status === 200,
    "Did receive response body": (r) => r.body && r.body.length > 0,
  });

  sleep(1);
}
