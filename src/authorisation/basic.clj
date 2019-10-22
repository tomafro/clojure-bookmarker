(ns authorisation.basic
  (:require
   [clojure.string :as string])
  (:import java.util.Base64))

(defn encode-b64 [to-encode]
  (.encodeToString (Base64/getEncoder) (.getBytes to-encode)))

(defn decode-b64 [to-decode]
  (String. (.decode (Base64/getDecoder) to-decode)))

(defn basic-header-token
  [header]
  (if (string/starts-with? header "Basic ")
    (subs header 6)))

(defn parse-header [header]
  (if-let [token (basic-header-token header)] 
    (let [[user password] (string/split (decode-b64 token) #":")]
      {:user user :password password})))

(defn authorized?
  [user password request]
  (if-let [header (get-in request [:headers :Authorization])]
    (= {:user user :password password} (parse-header [header]))))

(defn interceptor
  [user password]
  {:name ::basic-authentication
   :enter (fn [context]
            (if (not (authorized? user password (:request context)))
              (assoc context :response (response/unauthorized "" {:WWW-Authenticate "Basic realm=\"Admin\", charset=\"UTF-8\""}))))})
