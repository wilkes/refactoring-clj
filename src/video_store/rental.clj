(ns video-store.rental
  (:require [video-store.movie :as m]))

(defrecord Rental [movie days-rented])

(defn amount [rental]
  (m/amount (:movie rental) (:days-rented rental)))

(defn frequent-renter-points [rental]
  (m/frequent-renter-points (:movie rental) (:days-rented rental)))