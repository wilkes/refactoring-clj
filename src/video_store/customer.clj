(ns video-store.customer
  (:require [video-store.rental :as r]
            [video-store.movie :as m]
            [clojure.string :as string]
            [clojure.spec :as s]))

(s/def ::name (s/and string? (complement empty?)))
(s/def ::rentals (s/coll-of ::r/rental []))
(s/def ::customer (s/keys :req [::name ::rentals]))

(s/def ::total-amount (s/and double? pos?))
(s/def ::frequent-renter-points (s/and integer? pos?))
(s/def ::statement (s/keys :req [::name ::rentals ::frequent-renter-points ::total-amount]))

(defn make-customer [name rentals]
  {::name name ::rentals rentals})

(s/fdef make-customer
        :args (s/cat :name ::name :rentals ::rentals)
        :ret ::customer)

(defn add-rental [customer rental]
  (update customer ::rentals conj rental))

(s/fdef add-rental
        :args (s/cat :customer ::customer :rental ::r/rental)
        :ret ::customer)

(defn statement-data [{:keys [::name ::rentals]}]
  {::total-amount (reduce + 0.0 (map ::r/amount rentals))
   ::frequent-renter-points (reduce + 0 (map ::r/frequent-renter-points rentals))
   ::rentals rentals
   ::name name})

(s/fdef statement-data
        :args (s/cat :customer ::customer)
        ::ret ::statement
        :fn #(and (= (-> % :args :customer ::name)
                     (-> % :ret ::name))
                  (= (-> % :args :customer ::rentals)
                     (-> % :ret ::rentals))))


(defn statement [customer]
  (let [data (statement-data customer)]
    (str "Rental Record for " (::name data) "\n"
         (string/join (map #(str "\t" (::r/title %) "\t" (::r/amount %) "\n")
                           (::rentals data)))
         "Amount owed is " (-> data ::total-amount) "\n"
         "You earned " (-> data ::frequent-renter-points) " frequent renter points")))