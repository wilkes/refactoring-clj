(ns video-store.customer
  (:require [video-store.rental :as r]
            [video-store.movie :as m]))

(defrecord Customer [name rentals])

(defn add-rental [customer rental]
  (update customer :rentals conj rental))

(defn statement-data [customer]
  (let [state (transient
                {:total-amount (reduce + 0.0 (map r/rental-amount (:rentals customer)))
                 :frequent-renter-points (reduce + 0 (map r/frequent-renter-points (:rentals customer)))
                 :items (into [] (map (fn [rental] [(-> rental :movie :title)
                                                    (r/rental-amount rental)])
                                      (:rentals customer)))})]
    (persistent! state)))

(defn statement [customer]
  (let [state (transient {:result (str "Rental Record for " (:name customer) "\n")})
        data (statement-data customer)]
    (doseq [[title amount] (-> data :items)]
      (assoc! state :result (str (-> state :result) "\t" title "\t" amount "\n")))
    (assoc! state :result (str (-> state :result)
                               "Amount owed is " (-> data :total-amount) "\n"))
    (assoc! state :result (str (-> state :result)
                               "You earned " (-> data :frequent-renter-points)
                               " frequent renter points"))
    (-> state :result)))