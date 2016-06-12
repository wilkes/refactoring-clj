(ns video-store.rental
  (:require [video-store.movie :as m]))

(defrecord Rental [movie days-rented])


(defn rental-amount [rental]
  (m/amount (:movie rental) (:days-rented rental)))

(defn frequent-renter-points [rental]
  (if (and (= m/NEW-RELEASE (-> rental :movie :price-code))
           (> (-> rental :days-rented) 1))
    2
    1))