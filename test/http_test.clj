(ns http-test
  (:refer-clojure :exclude [uuid?])
  (:require [clojure.test :refer [deftest testing is]]
            [test.http :as http]
            [http :as server]
            [http.response :as response]))

(defn uuid?
  [uuid]
  (re-find #"\A[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}" uuid))

(deftest x-request-id-test
  (testing "sets header in request"
    (is (uuid? (:body (http/get "/echo/header/x-request-id")))))
  (testing "keeps existing header if set"
    (let [existing-id "abcdefg1-2345-6789-0abc-defg01234567"]
      (is (= existing-id (:body (http/get "/echo/header/x-request-id" :headers {"X-Request-Id" existing-id}))))))
  (testing "passes same header to response"
    (let [response (http/get "/echo/header/x-request-id")]
      (is (uuid? (get-in response [:headers "X-Request-Id"] "")))
      (is (= (get-in response [:headers "X-Request-Id"]) (:body response))))))
