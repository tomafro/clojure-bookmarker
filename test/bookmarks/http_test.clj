(ns bookmarks.http-test
  (:require [clojure.test :refer [deftest testing is use-fixtures]]
            [io.pedestal.test :refer [response-for]]
            [io.pedestal.http :as http]
            [next.jdbc :as jdbc]
            [http :as server]
            [routes :refer [url-for]]
            [bookmarks.http]
            [database]
            [bookmarks.db]))

(defn run-within-transaction [test]
  (jdbc/with-transaction [tx database/db {:rollback-only true}]
    (binding [database/db tx]
      (test))))

(use-fixtures :each run-within-transaction)

(def service
  (::http/service-fn (http/create-servlet server/service-map)))

(def http-get
  (partial response-for service :get))

(def http-post
  (partial response-for service :post))

(deftest new-bookmark-test
  (is (= "<div>hello world</div>\n" (:body (http-get (url-for :bookmarks/new))))))

(deftest create-bookmark-test
  (testing "successful create"
    (let [response (http-post "/bookmarks")]
      (is (= "created" (:body response)))
      (is (= 1 (bookmarks.db/count))))))
    
(deftest show-bookmark-test
  (testing "missing bookmark"
    (let [response (http-get "/bookmark/1234")]
      (is (= 404 (:status response)))))
  (testing "existing bookmark"
    (let [bookmark (bookmarks.db/create #:bookmarks{:title "Hello" :url "https://www.example.cm/url"})
          response (http-get (url-for :bookmark :params {:bookmark-id (:bookmarks/id bookmark)}))]
      (is (= 200 (:status response)))
      (is (= "<a href=\"https://www.example.cm/url\">Hello</a>\n" (:body response))))))

(deftest index-bookmarks-test
  (is (= "LIST" (:body (http-get "/bookmarks")))))
