(ns response)

(def status-codes
  {100 :continue
   101 :switching-protocols
   102 :processing
   103 :early-hints
   200 :ok
   201 :created
   202 :accepted
   203 :non-authoritative-information
   204 :no-content
   205 :reset-content
   206 :partial-content
   207 :multi-status
   208 :already-reported
   226 :im-used
   300 :multiple-choices
   301 :moved-permanently
   302 :found
   303 :see-other
   304 :not-modified
   305 :use-proxy
   307 :temporary-redirect
   308 :permanent-redirect
   400 :bad-request
   401 :unauthorized
   402 :payment-required
   403 :forbidden
   404 :not-found
   405 :method-not-allowed
   406 :not-acceptable
   407 :proxy-authentication-required
   408 :request-timeout
   409 :conflict
   410 :gone
   411 :length-required
   412 :precondition-failed
   413 :payload-too-large
   414 :uri-too-long
   415 :unsupported-media-type
   416 :range-not-satisfiable
   417 :expectation-failed
   421 :misdirected-request
   422 :unprocessable-entity
   423 :docked
   424 :failed-dependency
   425 :too-early
   426 :upgrade-required
   428 :precondition-required
   429 :too-many-requests
   431 :request-header-fields-too-large
   451 :unavailablefor-legal-reasons
   500 :internal-server-error
   501 :not-implemented
   502 :bad-gateway
   503 :service-unavailable
   504 :gateway-timeout
   505 :http-version-not-supported
   506 :variant-also-negotiates
   507 :insufficient-storage
   508 :loop-detected
   509 :bandwidth-limit-exceeded
   510 :not-extended
   511 :network-authentication-required})

(defn response
  ([status] (response status ""))
  ([status body & {:as headers}] {:status status :body body :headers headers}))

(def continue                        (partial response 100))
(def switching-protocols             (partial response 101))
(def processing                      (partial response 102))
(def early-hints                     (partial response 103))
(def ok                              (partial response 200))
(def created                         (partial response 201))
(def accepted                        (partial response 202))
(def non-authoritative-information   (partial response 203))
(def no-content                      (partial response 204))
(def reset-content                   (partial response 205))
(def partial-content                 (partial response 206))
(def multi-status                    (partial response 207))
(def already-reported                (partial response 208))
(def im-used                         (partial response 226))
(def multiple-choices                (partial response 300))
(def moved-permanently               (partial response 301))
(def found                           (partial response 302))
(def see-other                       (partial response 303))
(def not-modified                    (partial response 304))
(def use-proxy                       (partial response 305))
(def temporary-redirect              (partial response 307))
(def permanent-redirect              (partial response 308))
(def bad-request                     (partial response 400))
(def unauthorized                    (partial response 401))
(def payment-required                (partial response 402))
(def forbidden                       (partial response 403))
(def not-found                       (partial response 404))
(def method-not-allowed              (partial response 405))
(def not-acceptable                  (partial response 406))
(def proxy-authentication-required   (partial response 407))
(def request-timeout                 (partial response 408))
(def conflict                        (partial response 409))
(def gone                            (partial response 410))
(def length-required                 (partial response 411))
(def precondition-failed             (partial response 412))
(def payload-too-large               (partial response 413))
(def uri-too-long                    (partial response 414))
(def unsupported-media-type          (partial response 415))
(def range-not-satisfiable           (partial response 416))
(def expectation-failed              (partial response 417))
(def misdirected-request             (partial response 421))
(def unprocessable-entity            (partial response 422))
(def docked                          (partial response 423))
(def failed-dependency               (partial response 424))
(def too-early                       (partial response 425))
(def upgrade-required                (partial response 426))
(def precondition-required           (partial response 428))
(def too-many-requests               (partial response 429))
(def request-header-fields-too-large (partial response 431))
(def unavailable-for-legal-reasons   (partial response 451))
(def internal-server-error           (partial response 500))
(def not-implemented                 (partial response 501))
(def bad-gateway                     (partial response 502))
(def service-unavailable             (partial response 503))
(def gateway-timeout                 (partial response 504))
(def http-version-not-supported      (partial response 505))
(def variant-also-negotiates         (partial response 506))
(def insufficient-storage            (partial response 507))
(def loop-detected                   (partial response 508))
(def bandwidth-limit-exceeded        (partial response 509))
(def not-extended                    (partial response 510))
(def network-authentication-required (partial response 511))

(defn redirect-to
  [url]
  (found nil "Location" url))

(defn redirect-permanently-to
  [url]
  (moved-permanently nil "Location" url))
