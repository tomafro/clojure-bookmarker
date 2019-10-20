(ns bookmarks-test
  (:require [clojure.test :refer :all]
            [io.pedestal.test :refer :all]
            [io.pedestal.http :as http]
            [main]
            [bookmarks]))

(def service
  (::http/service-fn (http/create-servlet main/service-map)))

(def http-get
  (partial response-for service :get))

(deftest new-bookmark-test
  (is (= "<div>hello world</div>" (:body (http-get "/bookmarks/new")))))

(deftest show-bookmark-test
  (is (= "<div>show-bookmark</div>" (:body (http-get "/bookmarks/abcd1234")))))
