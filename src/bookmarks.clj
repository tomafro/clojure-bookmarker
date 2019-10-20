(ns bookmarks
  (:require
   [hiccup.core :refer [html h]]
   [response]))

(defn new-bookmark-view
  []
  (html [:div "hello world"]))

(defn new-bookmark
  [request]
  (response/ok (new-bookmark-view)))

(defn show-bookmark-view
  []
  (html [:div "show-bookmark"]))

(defn show-bookmark
  [request]
  (response/ok (show-bookmark-view)))

(defn new-or-show-bookmark
  [request]
  (if (= "new" (get-in request [:path-params :bookmark-id]))
    (new-bookmark request)
    (show-bookmark request)))

(defn create-bookmark
  [request]
  (response/created "created"))
