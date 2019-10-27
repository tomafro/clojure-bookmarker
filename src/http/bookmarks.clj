(ns http.bookmarks
  (:require
   [views.bookmarks :as views]
   [database]
   [response]
   [io.pedestal.interceptor.helpers :as interceptor]))

(def find-bookmark
  (interceptor/on-request
   (fn [request]
     (let [bookmark-id (get-in request [:path-params :bookmark-id])]
       (assoc-in request [:bookmarker :bookmark]
                 (database/find-bookmark (Integer/parseInt bookmark-id)))))))

(defn new-bookmark
  [request]
  (response/ok (views/new-bookmark)))

(defn show-bookmark
  [request]
  (if-let [bookmark (get-in request [:bookmarker :bookmark])]
      (response/ok (str "<a href=\"" (:bookmarks/url bookmark) "\">" (:bookmarks/title bookmark) "</a>"))
      (response/not-found "Not found")))

(defn create-bookmark
  [request]
  (response/created "created"))

(defn index-bookmarks
  [request]
  (response/ok "LIST"))

(defn routes
  []
  [["/bookmarks" {:get [:bookmarks `index-bookmarks] :post [:bookmarks/create `create-bookmark]}
     ["/new" {:get [:bookmarks/new `new-bookmark]}]]
    ["/bookmark/:bookmark-id" ^:interceptors [find-bookmark] {:get [:bookmark `show-bookmark]}]])
