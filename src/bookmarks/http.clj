(ns bookmarks.http
  (:require
   [bookmarks.views :refer [view]]
   [database]
   [http.response :as response]
   [bookmarks.db]
   [io.pedestal.interceptor.helpers :as interceptor]))

(defn new-bookmark
  [_]
  (response/ok (view "bookmarks/new")))

(defn show-bookmark
  [request]
  (if-let [bookmark (get-in request [:bookmarker :bookmark])]
      (response/ok (view "bookmarks/show" bookmark))
      (response/not-found "Not found")))

(defn create-bookmark
  [request]
  (database/create database/db :bookmarks (select-keys (:params request) [:bookmarks/title :bookmarks/url]))
  (response/created "created"))

(defn index-bookmarks
  [_]
  (response/ok "LIST"))

(def find-bookmark
  (interceptor/on-request
   (fn [request]
     (let [bookmark-id (get-in request [:path-params :bookmark-id])]
       (assoc-in request [:bookmarker :bookmark]
                 (bookmarks.db/find (Integer/parseInt bookmark-id)))))))

(defn routes
  []
  [["/bookmarks" {:get [:bookmarks `index-bookmarks] :post [:bookmarks/create `create-bookmark]}
     ["/new" {:get [:bookmarks/new `new-bookmark]}]]
    ["/bookmark/:bookmark-id" ^:interceptors [find-bookmark] {:get [:bookmark `show-bookmark]}]])
