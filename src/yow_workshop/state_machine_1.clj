(ns yow-workshop.state-machine-1)


(def login-flow
  {:state "Login"
   :transitions {"Login" {:submit-credentials "Logging in"}
                 "Logging in" {:valid-credentials "App"
                               :invalid-credentials "Login"}
                 "App" {}}})


(defn step
  [state-machine transition]
  (let [{:keys [state transitions]} state-machine]

    (if-let [next-state (get-in transitions [state transition])]
      (assoc state-machine :state next-state)
      state-machine)))


(comment ;;inline tests!

  (= (:state (step login-flow :submit-credentials))
     "Logging in")

  (-> login-flow
      (step :submit-credentials)
      :state
      (= "Logging in"))

  (-> (reduce step login-flow [:submit-credentials :valid-credentials])
      :state
      (= "App"))

  )
