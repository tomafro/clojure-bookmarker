(ns bookmarks.http-test
  (:require [clojure.test :refer :all]
            [io.pedestal.test :refer :all]
            [io.pedestal.http :as http]
            [main]
            [bookmarks.http]))

(def service
  (::http/service-fn (http/create-servlet main/service-map)))

(def http-get
  (partial response-for service :get))

(def http-post
  (partial response-for service :post))

(deftest new-bookmark-test
  (is (= "<div>hello world</div>" (:body (http-get "/bookmarks/new")))))

(deftest create-bookmark-test
  (is (= "created" (:body (http-post "/bookmarks")))))

(deftest show-bookmark-test
  (testing "missing bookmark"
    (let [response (http-get "/bookmarks/1234")]
      (is (= 404 (:status response)))))
  (testing "existing bookmark"
    (let [bookmark (database/create-bookmark database/db {:bookmarks/title "Hello" :bookmarks/url "https://www.example.cm/url"})
          response (http-get (str "/bookmarks/" (:bookmarks/id bookmark)))]
      (is (= 200 (:status response)))
      (is (= "<a href=\"https://www.example.cm/url\">Hello</a>" (:body response))))))

(deftest index-bookmarks-test
  (is (= "LIST" (:body (http-get "/bookmarks")))))
