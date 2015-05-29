(ns yow-workshop.state-machine-2
  (:require [datascript :as d]))

(def conn
  (d/create-conn {:state/name {:db/unique :db.unique/identity}
                  :transition/name {:db/unique :db.unique/identity}

                  :transition/to {:db/valueType :db.type/ref}
                  :transition/from {:db/valueType :db.type/ref}}))


(d/transact! conn [
                   ;;states
                   {:state/name "Login"}
                   {:state/name "Logging in"}
                   {:state/name "App"}

                   ;;transitions
                   {:transition/name :submit-credentials
                    :transition/from [:state/name "Login"]
                    :transition/to   [:state/name "Logging in"]}

                   {:transition/name :valid-credentials
                    :transition/from [:state/name "Logging in"]
                    :transition/to   [:state/name "App"]}

                   {:transition/name :invalid-credentials
                    :transition/from [:state/name "Logging in"]
                    :transition/to   [:state/name "Login"]}])





(comment
  ;;Finding all of the state names
  (d/q '{:find [?name]
         :where [[?state :state/name ?name]]}
       (d/db conn))


  ;;Traversing the graph
  (->> (d/entity (d/db conn)
                 [:state/name "Login"])
       :transition/_from
       first
       :transition/to
       :state/name)

  )


(defn step
  [state transition-name]
  (if-let [next-state (->> (:transition/_from state)
                           (filter (fn [t] (= transition-name (:transition/name t))))
                           first
                           :transition/to)]
    next-state
    state))


(comment
  (-> (d/entity (d/db conn) [:state/name "Login"])
      (step :submit-credentials)
      :state/name)


  (-> (reduce step
              (d/entity (d/db conn) [:state/name "Login"])
              [:submit-credentials :valid-credentials])
      :state/name)

  )
