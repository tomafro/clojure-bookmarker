(ns bookmarks.http-test
  (:require [clojure.test :refer [deftest testing is use-fixtures]]
            [bookmarks.http]
            [test.http :as http]
            [test.html :as html]
            [database]
            [bookmarks.db]))

(use-fixtures :each http/run-within-transaction)

(deftest new-bookmark-test
  (let [response (http/get-url :bookmarks/new)]
    (is (html/select response [:form]))
    (is (= "a" (:body response)))))

(deftest create-bookmark-test
  (testing "successful create"
    (let [response (http/post-url :bookmarks/create :form #:bookmarks{:title "Blog" :url "https://tomafro.net"})]
      (is (= 201 (:status response)))
      (is (= "created" (:body response)))
      (is (= #:bookmarks{:title "Blog" :url "https://tomafro.net"}
             (select-keys (bookmarks.db/find-first) [:bookmarks/title :bookmarks/url]))))))
    
(deftest show-bookmark-test
  (testing "missing bookmark"
    (let [response (http/get "/bookmark/1234")]
      (is (= 404 (:status response)))))
  (testing "existing bookmark"
    (let [bookmark (bookmarks.db/create #:bookmarks{:title "Hello" :url "https://www.example.cm/url"})
          response (http/get-url [:bookmark :params {:bookmark-id (:bookmarks/id bookmark)}])]
      (is (= 200 (:status response)))
      (is (= "<a href=\"https://www.example.cm/url\">Hello</a>\n" (:body response))))))

(deftest index-bookmarks-test
  (is (= "LIST" (:body (http/get "/bookmarks")))))
