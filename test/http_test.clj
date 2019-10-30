(ns http-test
  (:refer-clojure :exclude [uuid?])
  (:require [clojure.test :refer :all]
            [io.pedestal.test :refer :all]
            [io.pedestal.http :as http]
            [http :as server]
            [response]))

(defn request-id
  [request]
  (response/ok (get-in request [:headers "x-request-id"])))

(def service-routes
  [[["/request-id" ^:interceptors [server/x-request-id] {:get [:id `request-id]}]]])

(def service
  (::http/service-fn (http/create-servlet {::http/routes service-routes
                                           ::http/type   :jetty
                                           ::http/port   8890
                                           ::http/join?  false})))

(def http-get
  (partial response-for service :get))

(def http-post
  (partial response-for service :post))

(defn uuid?
  [uuid]
  (re-find #"\A[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}" uuid))

(deftest x-request-id-test
  (testing "sets header in request"
    (is (uuid? (:body (http-get "/request-id")))))
  (testing "keeps existing header if set"
    (let [existing-id "abcdefg1-2345-6789-0abc-defg01234567"]
      (is (= existing-id (:body (http-get "/request-id" :headers {"X-Request-Id" existing-id}))))))
  (testing "passes same header to response"
    (let [response (http-get "/request-id")]
      (is (uuid? (get-in response [:headers "X-Request-Id"] "")))
      (is (= (get-in response [:headers "X-Request-Id"]) (:body response))))))