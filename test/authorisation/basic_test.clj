(ns authorisation.basic-test
  (:require [clojure.test :refer :all]
            [io.pedestal.test :refer :all]
            [io.pedestal.http :as http]
            [main]
            [authorisation.basic :as basic]))

; (def service
;   (::http/service-fn (http/create-servlet main/service-map)))

; (def http-get
;   (partial response-for service :get))

; (def http-post
;   (partial response-for service :post))

(deftest extracting-username-password-from-authorization-header
  (are [expected header] (= expected (basic/parse-header header))
    {:user "Aladdin" :password "OpenSesame"} "Basic QWxhZGRpbjpPcGVuU2VzYW1l"
    {:user "Lightman" :password "Joshua"} "Basic TGlnaHRtYW46Sm9zaHVh"
    {:user "Wagstaff" :password "Swordfish"} "Basic V2Fnc3RhZmY6U3dvcmRmaXNo"))
