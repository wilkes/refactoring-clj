(ns video-store.customer
  (:require [video-store.rental :as r]
            [video-store.movie :as m]))

(defrecord Customer [name rentals])

(defn add-rental [customer rental]
  (update customer :rentals conj rental))

(defn statement-data [customer]
  (let [state (transient
                {:total-amount 0.0
                 :frequent-renter-points 0
                 :items []})]
    (doseq [rental (-> customer :rentals)]
      (assoc! state :frequent-renter-points (+ (-> state :frequent-renter-points)
                                               (r/frequent-renter-points rental)))
      (assoc! state :items (conj (-> state :items) [(-> rental :movie :title)
                                                    (r/rental-amount rental)]))
      (assoc! state :total-amount (+ (-> state :total-amount) (r/rental-amount rental))))
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