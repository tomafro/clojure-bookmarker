(ns bookmarks
  (:require
   [response]))

(defn new-bookmark
  []
  response/ok)

(defn create-bookmark
  []
  response/created)

(defn show-bookmark
  []
  response/ok)
