(ns yow-server.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [clojure.string :as str]
            [compojure.core :refer [routes GET POST]]
            [compojure.route :as route]
            compojure.handler
            [clojure.pprint :refer [pprint]]))


(defn app-handler
  [req]

  (pprint req)

  {:status 200
   :body "Hello World"
   :headers {}})


(def hello-handler
  (GET "/" req "Hello World"))

;; /time?format=unix -> 123333
;; /time?format=human -> 7
(def time-handler
  (GET "/time" req
    (let [now (java.util.Date.)
          format (get-in req [:query-map :format])]

      (str
       (case format
         :unix (.getTime now)
         :human (.getHours now)
         "Sorry")))))


(defn parse-query-string
  "Converts a query string into a map of keywords to keywords."
  [query-str]

  (->> query-str
       (.split #"&|=")
       (vec)
       (map keyword)
       (apply hash-map)))


(defn qs-middleware
  "Middleware that parses querystring and adds :query-map"
  [handler]
  
  (fn [req]
    (handler (assoc req :query-map (parse-query-string (:query-string req))))))



;; jetty -> ring -> foo -> f -> foo -> ring -> jetty -> client


(def app-handler
  
  (-> (routes
       hello-handler
       ;;(get-in req [:query-map :format]) => :unix
       time-handler)
      qs-middleware))







(comment

  (def server
    (run-jetty #'app-handler
               {:join? false
                :port 8080}))


  (app-handler {:uri "/"})


  (app-handler {:uri "/foo"
                :server-port 8080
                :server-name "localhost"
                :remote-addr "localhost"
                :scheme :http
                :request-method :get
                :protocol "HTTP/1.1"})





  )
