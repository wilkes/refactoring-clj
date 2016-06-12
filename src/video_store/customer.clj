(ns video-store.customer
  (:require [video-store.rental :as r]
            [video-store.movie :as m]
            [clojure.string :as string]))

(defrecord Customer [name rentals])

(defn add-rental [customer rental]
  (update customer :rentals conj rental))

(defn statement-data [{:keys [rentals]}]
  {:total-amount (reduce + 0.0 (map r/rental-amount rentals))
   :frequent-renter-points (reduce + 0 (map r/frequent-renter-points rentals))
   :items (mapv (fn [rental] {:title (-> rental :movie :title)
                              :amount (r/rental-amount rental)})
                rentals)})

(defn statement [customer]
  (let [data (statement-data customer)]
    (str "Rental Record for " (:name customer) "\n"
         (string/join (map #(str "\t" (:title %) "\t" (:amount %) "\n")
                           (:items data)))
         "Amount owed is " (-> data :total-amount) "\n"
         "You earned " (-> data :frequent-renter-points) " frequent renter points")))