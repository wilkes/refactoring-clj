(ns video-store.rental
  (:require [video-store.movie :as m]
            [clojure.spec :as s]))

(s/def ::amount (s/and double? #(> % 0.0)))
(s/def ::frequent-renter-points #{1 2})
(s/def ::title (s/and string? (complement empty?)))
(s/def ::days-rented (s/and integer? #(> % 0)))
(s/def ::rental (s/keys :req [::title ::frequent-renter-points ::amount ::days-rented]))

(defn make-rental [movie days-rented]
  {::title (::m/title movie)
   ::amount (m/amount movie days-rented)
   ::frequent-renter-points (m/frequent-renter-points movie days-rented)
   ::days-rented days-rented})

(s/fdef make-rental
        :args (s/cat :movie ::m/movie :days-rented ::days-rented)
        :ret ::rental
        :fn #(= (-> % :args :movie ::m/title)
                (-> % :ret ::title)))