(ns video-store.customer
  (:require [video-store.rental :as r]
            [video-store.movie :as m]))

(defrecord Customer [name rentals])

(defn add-rental [customer rental]
  (update customer :rentals conj rental))

(defn statement [customer]
  (let [state (transient
                {:total-amount 0.0
                 :frequent-renter-points 0
                 :result (str "Rental Record for " (:name customer) "\n")
                 :this-amount 0.0})]
    (doseq [rental (-> customer :rentals)]
      (assoc! state :this-amount 0.0)
      (condp = (-> rental :movie :price-code)
        m/REGULAR (do
                    (assoc! state :this-amount (+ 2 (-> state :this-amount)))
                    (when (> (-> rental :days-rented) 2)
                      (assoc! state :this-amount (+ (-> state :this-amount)
                                                    (* 1.5 (- (-> rental :days-rented) 2))))))
        m/NEW-RELEASE (do
                        (assoc! state :this-amount (+ (-> state :this-amount)
                                                      (* 3 (-> rental :days-rented)))))
        m/CHILDRENS (do
                      (assoc! state :this-amount (+ 1.5 (-> state :this-amount)))
                      (when (> (-> rental :days-rented) 3)
                        (assoc! state :this-amount (+ (-> state :this-amount)
                                                      (* 1.5 (- (-> rental :days-rented) 3)))))))
      (assoc! state :frequent-renter-points (inc (-> state :frequent-renter-points)))
      (when (and (= m/NEW-RELEASE (-> rental :movie :price-code))
                 (> (-> rental :days-rented) 1))
        (assoc! state :frequent-renter-points (inc (-> state :frequent-renter-points))))
      (assoc! state :result (str (-> state :result)
                                 "\t" (-> rental :movie :title)
                                 "\t" (-> state :this-amount) "\n"))
      (assoc! state :total-amount (+ (-> state :total-amount) (-> state :this-amount))))
    (assoc! state :result (str (-> state :result)
                           "Amount owed is " (-> state :total-amount) "\n"))
    (assoc! state :result (str (-> state :result)
                           "You earned " (-> state :frequent-renter-points)
                               " frequent renter points"))
    (-> state :result)))



