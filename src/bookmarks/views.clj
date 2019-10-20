(ns bookmarks.views
  (:require
   [hiccup.core :refer [html h]]))

(defn new-bookmark
  []
  (html [:div "hello world"]))


(defn show-bookmark
  []
  (html [:div "show-bookmark"]))
